package com.activedge.usermgt.security;

import com.activedge.usermgt.config.JwtConfig;
import com.activedge.usermgt.model.*;
import com.activedge.usermgt.model.enumeration.MakerChecker;
import com.activedge.usermgt.repository.StaffRepository;
import com.activedge.usermgt.service.LdapUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private BCryptPasswordEncoder encoder;

    private AuthenticationManager authManager;

    private final JwtConfig jwtConfig;

    private LdapUserService ldapUserService;

    private StaffRepository staffRepository;

    public JwtUsernameAndPasswordAuthenticationFilter(StaffRepository staffRepository, AuthenticationManager authManager, JwtConfig jwtConfig, LdapUserService ldapUserService, BCryptPasswordEncoder encoder) {
        this.staffRepository = staffRepository;
        this.authManager = authManager;
        this.jwtConfig = jwtConfig;
        this.ldapUserService = ldapUserService;
        this.encoder = encoder;
        // By default, UsernamePasswordAuthenticationFilter listens to "/login" path.
        // In our case, we use "/auth". So, we need to override the defaults.
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtConfig.getUri(), "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            // Get credentials from request
            UserCredentials creds = new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);

            String encryptedPassword = encoder.encode(creds.getPassword());

            // Create auth object (contains credentials) which will be used by auth manager
            UsernamePasswordAuthenticationToken daoAuthToken = new UsernamePasswordAuthenticationToken(
                    creds.getUsername(), creds.getPassword(), Collections.emptyList());

            return authManager.authenticate(daoAuthToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Upon successful authentication, generate a token.
    // The 'auth' passed to successfulAuthentication() is the current authenticated user.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        log.info("Authentication object {}", auth);

        if(auth.getPrincipal() instanceof LdapUserDetailsImpl) {

            // if the user exist and activated on local-store and get its permission.
            // Else create the user locally without activation pending makerchecker.
            staffRepository.findOneWithAuthoritiesByEmail(((LdapUserDetailsImpl) auth.getPrincipal()).getUsername().toLowerCase())
                    .map(existingUser -> {
                        // this means that a user can use his AD account or Introspec account to login
                        if(existingUser.isActivated()) {
                            this.displayToken(this.generateToken(auth, request, existingUser), "old", response);
                        } else {
                            this.displayToken("Empty", "old", response);
                        }
                        return null;
                    })
                    .orElse(this.createNewUser(auth, request, response));

//            System.out.println("authenticating john and secret in ldap >>> " + ldapUserService.authenticate("john", "{SHA}5en6G6MezRroT3XKqkdPOmY/BfQ="));

//            log.info("Authentication successful from LDAP - Authorities:{} --- Dn:{} --- Username:{} --- Password:{} --- Enabled:{}",
//                    ((LdapUserDetailsImpl) auth.getPrincipal()).getAuthorities(),
//                    ((LdapUserDetailsImpl) auth.getPrincipal()).getDn(),
//                    ((LdapUserDetailsImpl) auth.getPrincipal()).getUsername(),
//                    ((LdapUserDetailsImpl) auth.getPrincipal()).getPassword(),
//                    ((LdapUserDetailsImpl) auth.getPrincipal()).isEnabled());
        } else {
            Optional<Staff> authUser = staffRepository.findOneWithAuthoritiesByEmail(((User) auth.getPrincipal()).getUsername());

            this.displayToken(this.generateToken(auth, request, authUser.get()), "old", response);

//            log.info("Authentication successful from JPA Authorities:{} --- Username:{} --- Password:{}",
//                    ((User) auth.getPrincipal()).getAuthorities(),
//                    ((User) auth.getPrincipal()).getUsername(),
//                    ((User) auth.getPrincipal()).getPassword());
        }
//        log.info("Authentication successful from {}", auth.getPrincipal().getClass());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        this.displayToken(failed.getMessage(), "401", response);
    }

    public String generateToken(Authentication auth, HttpServletRequest request, Staff staff) {
        Long now = System.currentTimeMillis();

        System.out.println("Staff permissions: --- " + staff.getGroup().getPermissions()
                .stream()
                .map(permission -> permission.getAction())
                .collect(Collectors.joining(",")));

        String module = request.getHeader(jwtConfig.getModule());

        List<String> allowedModules = staff.getAuthorities().stream().map(authority -> authority.getModule().getCode()).collect(Collectors.toList());;

        if(!allowedModules.contains(module) && !allowedModules.contains("ADMIN")) return "N/A";

        return Jwts.builder()
                .setSubject(auth.getName())
                // Convert to list of strings.
                // This is important because it affects the way we get them back in the Gateway.
                .claim("authorities", auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList())) //.collect(Collectors.joining(",")
                .claim("permissions", staff.getGroup().getPermissions()
                        .stream()
                        .map(permission -> permission.getAction())
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))  // in milliseconds
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                .compact();

    }

    private Staff createNewUser(Authentication auth, HttpServletRequest request, HttpServletResponse response) {
        Staff newUser = new Staff();

        LdapUser ldapUser = ldapUserService.getByUserid(((LdapUserDetailsImpl) auth.getPrincipal()).getUsername());

        String encryptedPassword = encoder.encode(ldapUser.getUsername() + "secret");
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(ldapUser.getUsername().split(" ")[0]);
        newUser.setLastName(ldapUser.getUsername().split(" ")[1]);
        newUser.setEmail(ldapUser.getUserid().toLowerCase() + "@default.com");
        newUser.setMakerChecker(MakerChecker.NONE);
        // new user is active
        newUser.setActivated(true);
        // new user gets registration key
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        // assign new user group
        Group group = new Group();
        group.setId(0L);
        newUser.setGroup(group);

        staffRepository.save(newUser);

        log.debug("Created Information for Staff: {}", newUser);

        this.displayToken(this.generateToken(auth, request, newUser), "new", response);

        return newUser;
    }

    private void displayToken(String token, String whois, HttpServletResponse response) {
        // Add token to header
        response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);

        Map<String, String> res = new HashMap<>();
        res.put("token", jwtConfig.getPrefix() + token);
        res.put("status", whois);

        String json = new Gson().toJson(res);

        PrintWriter out = null;
        try {
            out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, Authorization, Origin, Accept, Access-Control-Request-Method, Access-Control-Request-Headers");
            out.print(json);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // A (temporary) class just to represent the user credentials
    @Data
    private static class UserCredentials {
        private String username, password;
    }
}


package com.activedge.usermgt.service;

import com.activedge.usermgt.model.*;
import com.activedge.usermgt.model.enumeration.Type;
import com.activedge.usermgt.repository.GroupRepository;
import com.activedge.usermgt.repository.StaffRepository;
import com.activedge.usermgt.security.AuthoritiesConstants;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.expiration}")
    private int jwtExpirationInMs;

    public String getJwtToken(Authentication authentication, String module) {
        Set<String> staffPermissions = new HashSet<>();
        String token = "";

        if(authentication.getPrincipal() instanceof LdapUserDetailsImpl) {
            // do LDAP
            LdapUserDetailsImpl userPrincipal = (LdapUserDetailsImpl) authentication.getPrincipal();
            token = staffRepository.findOneWithAuthoritiesByUsername(userPrincipal.getUsername().toLowerCase())
                    .map(staff -> {
                        // --- Fetch all groups this staff belongs to and populate its permission
                        List<Group> groups = groupRepository.findAllByModule_IdAndStaffsContains(module.toUpperCase(), staff);
                        for(Group group: groups) {
                            for(Permission permission: group.getPermissions()) {
                                staffPermissions.add(module.toLowerCase() + "." + permission.getId());
                            }
                        }
//                        System.out.println("ldap: >>" + staffPermissions);
                        return generateToken(authentication, staffPermissions, staff.getAuthorities());
                    })
                    .orElse(null);
        } else {
            // do JDBC
            User user = (User) authentication.getPrincipal();
            token = staffRepository.findOneWithAuthoritiesByUsername(user.getUsername().toLowerCase())
                    .map(staff -> {
                        // --- Fetch all groups this staff belongs to and populate its permission
                        List<Group> groups = groupRepository.findAllByModule_IdAndStaffsContains(module.toUpperCase(), staff);
                        for(Group group: groups) {
                            for(Permission permission: group.getPermissions()) {
                                staffPermissions.add(module.toLowerCase() + "." + permission.getId());
                            }
                        }
//                         System.out.println("jdbc: >>" + staffPermissions);
                        return generateToken(authentication, staffPermissions, staff.getAuthorities());
                    }).orElse("null");
        }

        return token;
    }

    private String generateToken(Authentication authentication, Set<String> staffPermissions, Set<Authority> staffAuthorities) {
        Date now = new Date(System.currentTimeMillis());

        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        log.debug("Token set to expire @{} {}", expiryDate);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorities", staffAuthorities.stream().map(Authority::getId).collect(Collectors.toList()))
                .claim("permissions", staffPermissions)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes())
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public List getAuthoritiesFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.get("authorities", List.class);
    }

    public List getPermissionFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.get("permissions", List.class);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    private String createNewUserToken(Authentication auth, Set<String> staffPermissions) {
        Staff newUser = new Staff();

        String usr = auth.getPrincipal().toString();

        String encryptedPassword = encoder.encode(auth.getPrincipal().toString());
        newUser.setPassword(encryptedPassword);
        newUser.setFirst_name(auth.getPrincipal().toString());
        newUser.setLast_name(auth.getPrincipal().toString());
        newUser.setEmail(auth.getPrincipal().toString().toLowerCase() + "@default.com");
        newUser.setType(Type.USER);
        // new user is active
        newUser.setActivated(true);
        // new user gets registration key
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setId(AuthoritiesConstants.USER);
        authority.setName(AuthoritiesConstants.USER);
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        // assign new user group
        Group group = new Group();
        group.setId(new GroupPK());
        newUser.setGroups(new HashSet<Group>());

        log.debug("about to create new staff - {}", newUser);

        Staff staff = staffRepository.save(newUser);

        log.debug("Created new staff - {}", staff);

        return generateToken(auth, staffPermissions, staff.getAuthorities());
    }

}

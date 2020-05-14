package com.activedge.usermgt.controller;

import com.activedge.usermgt.model.CustomHttpTrace;
import com.activedge.usermgt.service.JwtTokenProvider;
import com.activedge.usermgt.service.MapValidationErrorService;
import com.activedge.usermgt.service.StaffModuleService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DefaultDirObjectFactory;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.jms.Queue;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.NotSupportedException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;

import static com.activedge.usermgt.config.Constants.TOKEN_PREFIX;

/**
 * Controller to authenticate users.
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class UserJWTController {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Queue queue;

    private static long starttime;
    private static long endtime;

    private AuthenticationManager authenticationManager;

    private StaffModuleService staffModuleService;

    public UserJWTController(JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager, StaffModuleService staffModuleService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.staffModuleService = staffModuleService;
    }

    @PostMapping
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest loginRequest, @RequestHeader(value = "Module", required = false) String module, HttpServletRequest req, BindingResult result) throws NotSupportedException {
        starttime = System.currentTimeMillis();
        String jwt;

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null) return errorMap;

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // check if user belongs to the specified App before generating token
        if(staffModuleService.matchModuleAndEmail(module, loginRequest.username)) {
            jwt = TOKEN_PREFIX + tokenProvider.getJwtToken(authentication, module);
            // log successful login
            audit(req, authentication);
        } else {
            throw new NotSupportedException("User account not supported in the specified App: " + module);
        }

        return ResponseEntity.ok(new JWTResponse(true, jwt));
    }

    @PostMapping("/test-ldap")
    public Map<String, String> testLdap(@RequestBody LdapRequest request) throws Exception {
        Map<String, String> response = new HashMap<>();
        log.info("Connecting to LDAP " + request.getSourceBase() + "...");
        LdapContextSource sourceLdapCtx = new LdapContextSource();
        sourceLdapCtx.setUrl("ldap://" + request.getSourceHost() + ":389/");
        sourceLdapCtx.setUserDn(request.getSourceBindAccount());
        sourceLdapCtx.setBase(request.getSourceBase());
        sourceLdapCtx.setPassword(request.getSourcePassword());
        sourceLdapCtx.setDirObjectFactory(DefaultDirObjectFactory.class);
        sourceLdapCtx.afterPropertiesSet();
        LdapTemplate ldapTemplate = new LdapTemplate(sourceLdapCtx);

        // Authenticate:
        ldapTemplate.getContextSource().getContext(request.getSourceBindAccount(), request.getSourcePassword());
        log.info("....Authenticated !");

        response.put("message", "Authentication Passed !");

        return response;
    }

    @PostMapping("/test-ldap-search")
    public Map<String, String> testLdapSearch(@RequestBody LdapRequest request) {
        Map<String, String> response = new HashMap<>();
        log.info("Connecting to LDAP " + request.getSourceBase() + "...");
        LdapContextSource sourceLdapCtx = new LdapContextSource();
        sourceLdapCtx.setUrl("ldap://" + request.getSourceHost() + ":389/");
        sourceLdapCtx.setUserDn(request.getSourceBindAccount());
        sourceLdapCtx.setBase(request.getSourceBase());
        sourceLdapCtx.setPassword(request.getSourcePassword());
        sourceLdapCtx.setDirObjectFactory(DefaultDirObjectFactory.class);
        sourceLdapCtx.afterPropertiesSet();
        LdapTemplate ldapTemplate = new LdapTemplate(sourceLdapCtx);

        List<String> res = ldapTemplate.search(
                request.getBase(),
                request.getFilter(),
                (AttributesMapper<String>) attrs -> (String) attrs.get("DistinguishedName").get());

        if(res.isEmpty()) {
            throw new NoSuchElementException("No such user in AD.");
        } else {
            response.put("message", res.get(0));
            return response;
        }
    }

    private void audit(HttpServletRequest req, Authentication authentication) {
        endtime = System.currentTimeMillis();
        CustomHttpTrace cTrace = new CustomHttpTrace.CustomHttpTraceBuilder()
                .timestamp(new Date())
                .status(0)
                .username(req.getRemoteUser())
                .sourceIp(req.getRemoteAddr())
                .path(req.getRequestURL().toString())
                .queryParams(req.getQueryString())
                .method(req.getMethod())
                .timeTaken((endtime-starttime))
                .payload(authentication.getPrincipal().toString())
                .rawBody("n/a")
                .build();

        // async log
        this.jmsMessagingTemplate.convertAndSend(this.queue, cTrace);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private class JWTResponse {
        private boolean status;
        private String token;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class LoginRequest {
        @NotBlank(message = "Username cannot be blank")
        private String username;
        @NotBlank(message = "Password cannot be blank")
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class LdapRequest {
        private String sourceHost;
        private String sourceBase;
        private String sourceBindAccount;
        private String sourcePassword;
        private String base;
        private String filter;
    }

}

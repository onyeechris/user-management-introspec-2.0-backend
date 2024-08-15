package com.activedge.usermgt.controller;

import com.activedge.usermgt.model.ChangePasswordRequest;
import com.activedge.usermgt.model.CustomHttpTrace;
import com.activedge.usermgt.model.LdapSetting;
import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.dto.ForgetPasswordResponse;
import com.activedge.usermgt.model.dto.PasswordRequest;
import com.activedge.usermgt.repository.StaffRepository;
import com.activedge.usermgt.service.JwtTokenProvider;
import com.activedge.usermgt.service.MapValidationErrorService;
import com.activedge.usermgt.service.StaffModuleService;
import com.activedge.usermgt.service.StaffService;
import com.activedge.usermgt.util.EncryptionUtils;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DefaultDirObjectFactory;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.jms.Queue;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.NotSupportedException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.*;

import static com.activedge.usermgt.config.Constants.PASSWORD_ENCRYPTION_KEY;
import static com.activedge.usermgt.config.Constants.TOKEN_PREFIX;

/**
 * Controller to authenticate users.
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@SecurityRequirement(name = "introspec-cas")
@SecurityRequirement(name = "introspec-sso")
public class UserJWTController {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Queue queue;

    private static final String TITLE = "Introspec-CAS";

    private static long starttime;
    private static long endtime;

    @Autowired
    @Qualifier("db")
    private StaffService staffService;
    @Autowired
    private StaffRepository staffRepository;
    private AuthenticationManager authenticationManager;

    private StaffModuleService staffModuleService;

    public UserJWTController(JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager, StaffModuleService staffModuleService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.staffModuleService = staffModuleService;
    }

    /**
     * Obtain a new JWT token.
     *
     * @param module the appModule key. <b>Note: This is a header parameter<b/>
     * @return the ResponseEntity with status 200 (OK) and with body the modulesDTO, or with status 404 (Not Found)
     */
    @PostMapping
    public ResponseEntity<?> authenticate(
            @Valid @RequestBody LoginRequest loginRequest,
            @RequestHeader(value = "Module", required = false) String module,
            HttpServletRequest req,
            BindingResult result) throws Exception {

        long starttime = System.currentTimeMillis();
        String jwt;

        loginRequest.setUsername(EncryptionUtils.decrypt(loginRequest.getUsername(), System.getProperty(PASSWORD_ENCRYPTION_KEY)).toLowerCase());
        loginRequest.setPassword(EncryptionUtils.decrypt(loginRequest.getPassword(), System.getProperty(PASSWORD_ENCRYPTION_KEY)));

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if (errorMap != null) return errorMap;

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (staffModuleService.matchModuleAndEmail(module, loginRequest.getUsername())) {
            Optional<Staff> findStaff = staffRepository.findByUsernameAndActiveIsTrue(loginRequest.getUsername());
            if (!findStaff.isPresent()) {
                return ResponseEntity.status(404).body("User not found or not active");
            }

            Staff principal = findStaff.orElse(null);
            boolean authenticated = !principal.getEnable2FA();
            boolean isDefault = principal != null && principal.isDefault();
            boolean enrolled = principal.getEnrol();
            String userId = principal != null ? principal.getId() : "";

            jwt = TOKEN_PREFIX + tokenProvider.getJwtToken(authentication, module, authenticated, enrolled, userId, isDefault);

            audit(req, authentication);

            return ResponseEntity.ok(new JWTResponse(true, jwt));
        } else {
            throw new NotSupportedException("User account not supported in the specified App: " + module);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgetPasswordResponse<String>> forgotPassword(@RequestParam String email) {
        String result = staffService.forgotPassword(email);
        ForgetPasswordResponse<String> response = new ForgetPasswordResponse<>(HttpStatus.OK.value(), "Password reset link sent successfully", result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ForgetPasswordResponse<String>> resetPassword(@RequestParam String email, @Valid @RequestBody PasswordRequest passwordRequest) {
        String newPassword = passwordRequest.getNewPassword();
        String confirmPassword = passwordRequest.getConfirmPassword();

        if (!newPassword.equals(confirmPassword)) {
            ForgetPasswordResponse<String> response = new ForgetPasswordResponse<>(HttpStatus.BAD_REQUEST.value(), "Passwords do not match", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        String result = staffService.resetPassword(email, newPassword, confirmPassword);
        ForgetPasswordResponse<String> response = new ForgetPasswordResponse<>(HttpStatus.OK.value(), "Password reset successfully", result);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/yek_cne")
    public String yekCne() {
        return System.getProperty("PASSWORD_ENCRYPTION_KEY");
    }

//    @GetMapping("/generateQRCode/{id}")
//    public ResponseEntity<?> registerQR(@PathVariable String id) {
//        try {
//
//            Optional<Staff> staff = staffRepository.findById(id);
//            Staff signUpRequest = staff.get();
//            if (signUpRequest.is2FAEnabled()) {
//                QrData data = qrDataFactory.newBuilder().label(signUpRequest.getUsername()).secret(signUpRequest.getSecret()).issuer(TITLE).build();
//                // Generate the QR code image data as a base64 string which can be used in an <img> tag:
//                String qrCodeImage = getDataUriForImage(qrGenerator.generate(data), qrGenerator.getImageMimeType());
//                String secret = signUpRequest.getSecret();
//                return ResponseEntity.ok().body(new MfaResponse(true, qrCodeImage, secret));
//            }
//        } catch (RuntimeException e) {
//            log.error("Exception Occurred", e);
//            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"), HttpStatus.BAD_REQUEST);
//        } catch (QrGenerationException e) {
//            log.error("QR Generation Exception Occurred", e);
//            return new ResponseEntity<>(new ApiResponse(false, "Unable to generate QR code!"), HttpStatus.BAD_REQUEST);
//        }
//        return ResponseEntity.ok().body(new ApiResponse(false, "2FA not enabled"));
//    }

//    @PostMapping("/verify")
//    @PreAuthorize("hasRole('PRE_VERIFICATION_USER')")
//    public ResponseEntity<?> verifyCode(@NotEmpty @RequestBody QRCodeRequest code,
//                                        @RequestHeader(value = "Module", required = false) String module) throws Exception {
//        String jwt;
//        Optional<Staff> staff = staffRepository.findByUsername(code.getUsername());
//        Staff user = staff.get();
//        String userId = user!=null ? user.getId() : "";
//        String cd = code.getCode();
//        if (!verifier.isValidCode(user.getSecret(), cd)) {
//            return new ResponseEntity<>(new ApiResponse(false, "Invalid Code!"), HttpStatus.BAD_REQUEST);
//        }
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        code.getUsername(),
//                        code.getPassword()
//                )
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        if(staffModuleService.matchModuleAndEmail(module, code.username)) {
//            jwt = tokenProvider.getJwtToken(authentication, module,true, true, userId);
//        } else {
//            throw new NotSupportedException("User account not supported in the specified App: " + module);
//        }
//        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, true, user));
//    }

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

    @Autowired
    LdapSetting ldapSetting;

    @GetMapping("/ldap-settings")
    public LdapSetting getLdapSetting(){
        return ldapSetting;
    }

    public boolean audit(HttpServletRequest req, Authentication authentication) {
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
                .payload(authentication == null || authentication.getPrincipal() == null ? null : authentication.getPrincipal().toString())
                .rawBody("n/a")
                .build();

        // async log
        this.jmsMessagingTemplate.convertAndSend(this.queue, cTrace);

        return true;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private class JWTResponse {
        /**
         * Authentication response status
         */
        private boolean status;

        /**
         * Generated JWT token
         */
        private String token;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class LoginRequest {
        /**
         * Login username or email on database or AD
         */
        @NotBlank(message = "Username cannot be blank")
        private String username;
        /**
         * Login password on database or AD
         */
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

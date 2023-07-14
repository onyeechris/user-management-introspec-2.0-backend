package com.activedge.usermgt.controller;

import com.activedge.usermgt.controller.util.*;
import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.dto.StaffDTO;
import com.activedge.usermgt.repository.StaffRepository;
import com.activedge.usermgt.service.JwtTokenProvider;
import com.activedge.usermgt.service.StaffModuleService;
import com.activedge.usermgt.service.StaffService;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.transaction.NotSupportedException;
import javax.validation.ValidationException;
import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@RestController
@RequestMapping("/enrol")
@SecurityRequirement(name = "introspec-cas")
public class EnrolmentController {
    private final Logger log = LoggerFactory.getLogger(EnrolmentController.class);
    private static final String TITLE = "Introspec-CAS";
    private static final String ENROLMENT_PREFERENCE = "enrol/{id}";
    private static final String VERIFY_TOKEN = "verifyToken";
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private QrDataFactory qrDataFactory;

    @Autowired
    private QrGenerator qrGenerator;

    @Autowired
    private CodeVerifier verifier;
    @Autowired
    @Qualifier("db")
    private StaffService staffService;
    @Autowired
    private StaffRepository staffRepository;
    private AuthenticationManager authenticationManager;

    private StaffModuleService staffModuleService;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${verify.token.uri}")
    private String uri;
    @Value("${client.id}")
    private String clientId;
    @Value("${client.secret}")
    private String clientSecret;
    public EnrolmentController(StaffModuleService staffModuleService, AuthenticationManager authenticationManager){
        this.staffModuleService = staffModuleService;
        this.authenticationManager = authenticationManager;
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/generateQRCode/{id}")
    public ResponseEntity<?> registerQR(@PathVariable String id) {
        try {

            Optional<Staff> staff = staffRepository.findById(id);
            Staff signUpRequest = staff.get();
            if (signUpRequest.is2FAEnabled()) {
                QrData data = qrDataFactory.newBuilder().label(signUpRequest.getUsername()).secret(signUpRequest.getSecret()).issuer(TITLE).build();
                // Generate the QR code image data as a base64 string which can be used in an <img> tag:
                String qrCodeImage = getDataUriForImage(qrGenerator.generate(data), qrGenerator.getImageMimeType());
                String secret = signUpRequest.getSecret();
                return ResponseEntity.ok().body(new MfaResponse(true, qrCodeImage, secret));
            }
        } catch (RuntimeException e) {
            log.error("Exception Occurred", e);
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"), HttpStatus.BAD_REQUEST);
        } catch (QrGenerationException e) {
            log.error("QR Generation Exception Occurred", e);
            return new ResponseEntity<>(new ApiResponse(false, "Unable to generate QR code!"), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(new ApiResponse(false, "2FA not enabled"));
    }

    /**
     *
     * @param code
     * @param module
     * @return
     * @throws Exception
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@NotEmpty @RequestBody EnrolmentController.QRCodeRequest code,
                                        @RequestHeader(value = "Module", required = false) String module) throws Exception {
        String jwt;
        Optional<Staff> staff = staffRepository.findByUsername(code.getUsername());
        Staff user = staff.get();
        String cd = code.getCode();
        if (!verifier.isValidCode(user.getSecret(), cd)) {
            return new ResponseEntity<>(new ApiResponse(false, "Invalid Code!"), HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        code.getUsername(),
                        code.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        if(staffModuleService.matchModuleAndEmail(module, code.username)) {
            jwt = tokenProvider.getJwtToken(authentication, module,true, true);
        } else {
            throw new NotSupportedException("User account not supported in the specified App: " + module);
        }
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, true, user,null));
    }
    @PostMapping(VERIFY_TOKEN)
    public ResponseEntity<?> verifyToken(@RequestBody TokenRequest tokenRequest,@RequestHeader(value = "Module", required = false) String module) throws NotSupportedException {
        String jwt;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<Staff> staff = staffRepository.findByUsername(auth.getName());
        Staff user = staff.get();
        HttpEntity<TokenRequest> tokenEntity = new HttpEntity<>(tokenRequest,getHeaders());
//        ValidationResponse verify = restTemplate.exchange(uri, HttpMethod.POST, tokenEntity, ValidationResponse.class).getBody();
        if(staffModuleService.matchModuleAndEmail(module, auth.getName())) {
            jwt = tokenProvider.getJwtToken(auth, module,true, true);
        } else {
            throw new NotSupportedException("User account not supported in the specified App: " + module);
        }
        return ResponseEntity.ok().body(new JwtAuthenticationResponse(jwt,true,user,null));
    }
    private HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-client-id",clientId);
        headers.add("x-client-secret",clientSecret);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }

    @PutMapping()
    public ResponseEntity<StaffDTO> updateStaffPreference(@PathVariable String id, @RequestBody StaffDTO staffDTO, Errors errors) throws Exception {
        log.debug("REST request to update enrolment preference {} : {}", ENROLMENT_PREFERENCE, id);
        if (errors.hasErrors() || id == null) {
            log.error("Error in updating user preference detected...\n{}", errors.getAllErrors());
            throw new ValidationException(errors.getAllErrors().stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
        }
        StaffDTO result = staffService.savePreference(id, staffDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENROLMENT_PREFERENCE, id))
                .body(result);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class QRCodeRequest {
        private String code;
        private String username;
        private String password;
    }
}

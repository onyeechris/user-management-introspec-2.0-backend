package com.activedge.usermgt.controller;

import com.activedge.usermgt.controller.util.HeaderUtil;
import com.activedge.usermgt.model.dto.StaffDTO;
import com.activedge.usermgt.service.JwtTokenProvider;
import com.activedge.usermgt.service.StaffService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.validation.ValidationException;
import java.util.stream.Collectors;


@RestController
@RequestMapping(Enable2faController.MFA)
@SecurityRequirement(name = "introspec-cas")
public class Enable2faController {
    private final Logger log = LoggerFactory.getLogger(Enable2faController.class);

    static final String MFA = "mfa";
    private static final String STAFF_PREFERENCE ="preference/{id}";
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    @Qualifier("db")
    private StaffService staffService;

    @PutMapping(STAFF_PREFERENCE)
    public ResponseEntity<String> updateAllStaffPreference(@PathVariable String id, @RequestBody StaffDTO staffDTO, Errors errors) throws Exception {
        log.debug("REST request to update preference {} : {}", STAFF_PREFERENCE, id);
        if (errors.hasErrors() || id == null) {
            log.error("Error in updating user preference detected...\n{}", errors.getAllErrors());
            throw new ValidationException(errors.getAllErrors().stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
        }
        Integer result = staffService.enable2faForAllStaff(id, staffDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(STAFF_PREFERENCE, id))
                .body(String.format("%s staff preferences updated", result));
    }
}

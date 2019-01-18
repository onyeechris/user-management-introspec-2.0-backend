package com.activedge.usermgt.controller;

import com.activedge.usermgt.model.dto.NewStaffDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * Controller to authenticate users.
 * This class is only added for documentation purposes
 * @See com.activedge.usermgt.security.JwtUsernameAndPasswordAuthenticationFilter#61
 */
@RestController
@RequestMapping("/auth")
public class UserJWTController {

//    private final TokenProviderer tokenProvider;

    private final AuthenticationManager authenticationManager = null;

//    publicic UserJWTController(TokenProvider tokenProvider, AuthenticationManager authenticationManager) {
//        this.tokenProvider = tokenProvider;
//        this.authenticationManager = authenticationManager;
//    }

    @PostMapping
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody JWTTokenRequest jwtTokenRequest) {
        return null;
//        UsernamePasswordAuthenticationToken authenticationToken =
//            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
//
//        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
//        String jwt = tokenProvider.createToken(authentication, rememberMe);
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
//        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);

    }

    /**
     * Object to return as body in JWT Authentication.
     */
    @Data
    static class JWTToken {

        private String status;
        private String token;

    }
    /**
     * Object to return as body in JWT Authentication.
     */
    @Data
    static class JWTTokenRequest {

        @ApiModelProperty(notes = "Staff login username/email from ldap or local store", required = true)
        private String username;

        @ApiModelProperty(notes = "Staff login password from ldap or local store", required = true)
        private String password;

    }
}

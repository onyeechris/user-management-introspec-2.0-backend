package com.activedge.usermgt.controller.util;

import com.activedge.usermgt.model.Staff;
import lombok.Value;

@Value
public class JwtAuthenticationResponse {
        private String accessToken;
        private boolean authenticated;
        private Staff user;
        private ValidationResponse tokenResponse;


//        public JwtAuthenticationResponse(String accessToken, boolean authenticated, Staff staff, ValidationResponse tokenResponse){
//                this.accessToken = accessToken;
//                this.authenticated = authenticated;
//                this.user = staff;
//                this.tokenResponse = tokenResponse;
//        }
//        public JwtAuthenticationResponse(String accessToken, boolean authenticated, Staff user){
//                this.accessToken = accessToken;
//                this.authenticated = authenticated;
//                this.user = user;
//        }

}

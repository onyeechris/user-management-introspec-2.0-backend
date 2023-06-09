package com.activedge.usermgt.controller.util;

import com.activedge.usermgt.model.Staff;
import lombok.Value;

@Value
public class JwtAuthenticationResponse {
        private String accessToken;
        private boolean authenticated;
        private Staff user;

}

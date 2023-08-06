package com.activedge.usermgt.controller.util;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TokenRequest {
    private String user;
    private String token;
}

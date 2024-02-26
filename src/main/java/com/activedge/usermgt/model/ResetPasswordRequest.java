package com.activedge.usermgt.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {

    private String adminUsername;
    private String username;
    private String newPassword;
}

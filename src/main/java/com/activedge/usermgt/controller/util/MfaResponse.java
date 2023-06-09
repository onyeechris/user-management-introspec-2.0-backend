package com.activedge.usermgt.controller.util;

import lombok.Value;

@Value
public class MfaResponse {
    private boolean using2FA;
    private String qrCodeImage;
    private String secret;
}

package com.activedge.usermgt.controller.util;

import lombok.Value;

@Value
public class ApiResponse {
    private Boolean success;
    private String message;
}

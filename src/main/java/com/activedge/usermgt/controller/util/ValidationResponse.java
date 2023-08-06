package com.activedge.usermgt.controller.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ValidationResponse {
    private String responseCode;
    private String responseMessage;
}

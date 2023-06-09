package com.activedge.usermgt.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationCodeDTO {
    private Integer code;
    private String username;
}

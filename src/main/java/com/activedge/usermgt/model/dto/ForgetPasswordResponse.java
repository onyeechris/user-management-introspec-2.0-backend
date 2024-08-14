package com.activedge.usermgt.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ForgetPasswordResponse <T>{
    private int status;
    private String message;
    private T data;
    public ForgetPasswordResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}

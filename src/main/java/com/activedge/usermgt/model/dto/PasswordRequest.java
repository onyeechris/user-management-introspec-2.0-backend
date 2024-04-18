package com.activedge.usermgt.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.activedge.usermgt.config.Constants.PASSWORD_MAX_LENGTH;
import static com.activedge.usermgt.config.Constants.PASSWORD_MIN_LENGTH;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PasswordRequest {
    @NotNull(message = "Staff password is required.")
    @JsonProperty( value = "newPassword", access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "Password must contain at least one uppercase and lowercase letter," +
                    "one special character,one digit and be 8 characters or longer.")
    private String newPassword;

}

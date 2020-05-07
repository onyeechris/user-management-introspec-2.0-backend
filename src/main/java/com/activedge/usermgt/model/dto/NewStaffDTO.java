package com.activedge.usermgt.model.dto;

import com.activedge.usermgt.model.enumeration.Type;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.UUID;

import static com.activedge.usermgt.config.Constants.PASSWORD_MAX_LENGTH;
import static com.activedge.usermgt.config.Constants.PASSWORD_MIN_LENGTH;

@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "The New Staff Transfer Entity")
public class NewStaffDTO extends StaffDTO {

    @ApiModelProperty(notes = "The staff password", required = true)
    @NotNull(message = "Staff password is required.")
    @JsonProperty( value = "password", access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // LdapUser Creation Constructor
    public NewStaffDTO(String password, @NotNull(message = "Staff firstname is required") @Size(max = 50) String first_name, String last_name, @Size(min = 9, max = 13, message = "phone number length too short or long.") String phone, @NotNull(message = "Staff email address is required.") @Email(message = "Please enter a correct email address") String email, Type type) {
        super(first_name, last_name, phone, email, type);
        this.password = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "NewUserVM{" +
                "} " + super.toString();
    }

}

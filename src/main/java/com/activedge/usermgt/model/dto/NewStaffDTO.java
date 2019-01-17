package com.activedge.usermgt.model.dto;

import com.activedge.usermgt.model.Staff;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.activedge.usermgt.config.Constants.PASSWORD_MAX_LENGTH;
import static com.activedge.usermgt.config.Constants.PASSWORD_MIN_LENGTH;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "The New Staff Transfer Entity")
public class NewStaffDTO extends StaffDTO {

    @ApiModelProperty(notes = "The staff password", required = true)
    @NotNull(message = "Staff password is required.")
    @JsonProperty( value = "password", access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    public NewStaffDTO() {
        // Empty constructor needed for Jackson.
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "NewUserVM{" +
                "} " + super.toString();
    }

}

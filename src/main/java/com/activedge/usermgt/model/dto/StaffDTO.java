package com.activedge.usermgt.model.dto;

import com.activedge.usermgt.model.enumeration.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the Staff entity.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "The Staff Transfer Entity")
public class StaffDTO implements Serializable {

    @ApiModelProperty(notes = "The database generated staff ID")
    private String id;

    @ApiModelProperty(notes = "The staff first name with maximum of 50 characters", required = true)
    @NotNull(message = "Staff firstname is required")
    @Size(max = 50)
    private String first_name;

    @ApiModelProperty(notes = "The staff last name")
    private String last_name;

    @ApiModelProperty(notes = "The staff phone number")
    @Size(min = 9, max = 13, message = "phone number length too short or long.")
    private String phone;

    @ApiModelProperty(notes = "The staff email", required = true)
    @NotNull(message = "Staff email address is required.")
    @Email(message = "Please enter a correct email address")
    private String email;

    @ApiModelProperty(notes = "The staff account type", required = true, example = "USER")
    @NotNull(message = "Staff ADMIN or USER type is required.")
    private Type user_type;

    @ApiModelProperty(notes = "The staff hired date", example = "MM/dd/yyyy")
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate hire_date;

    @ApiModelProperty(notes = "The staff permission groups")
    @NotNull(message = "Staff access group is required.")
    private Set<GroupDTO> groups = new HashSet<>();

    @ApiModelProperty(notes = "The staff active status")
    private Boolean activated;

    public StaffDTO(@NotNull(message = "Staff firstname is required") @Size(max = 50) String first_name, String last_name, @Size(min = 9, max = 13, message = "phone number length too short or long.") String phone, @NotNull(message = "Staff email address is required.") @Email(message = "Please enter a correct email address") String email) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public String toString() {
        return "StaffDTO{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", user_type=" + user_type +
                ", hire_date=" + hire_date +
                ", activated=" + activated +
                '}';
    }

}

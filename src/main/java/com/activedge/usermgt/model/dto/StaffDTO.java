package com.activedge.usermgt.model.dto;

import com.activedge.usermgt.model.enumeration.MakerChecker;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.Instant;
import javax.persistence.Version;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the Staff entity.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "The Staff Transfer Entity")
public class StaffDTO implements Serializable {

    @ApiModelProperty(notes = "The database generated staff ID")
    private Long id;

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

    @ApiModelProperty(notes = "The staff account type", required = true, example = "MAKER")
    @NotNull(message = "Staff Maker or Checker role is required.")
    private MakerChecker maker_checker;

    @ApiModelProperty(notes = "The staff hired date", example = "MM/dd/yyyy")
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate hire_date;

    @ApiModelProperty(notes = "The staff permission group id", required = true)
    @NotNull(message = "Staff access group is required.")
    private Long group_id;

    private Boolean activated;

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MakerChecker getMaker_checker() {
        return maker_checker;
    }

    public void setMaker_checker(MakerChecker maker_checker) {
        this.maker_checker = maker_checker;
    }

    public LocalDate getHire_date() {
        return hire_date;
    }

    public void setHire_date(LocalDate hire_date) {
        this.hire_date = hire_date;
    }

    public Long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StaffDTO staffDTO = (StaffDTO) o;
        if (staffDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), staffDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StaffDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirst_name() + "'" +
            ", lastName='" + getLast_name() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", makerChecker='" + getMaker_checker() + "'" +
            ", hireDate='" + getHire_date() + "'" +
            ", group=" + getGroup_id() +
            "}";
    }
}

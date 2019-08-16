package com.activedge.usermgt.model.dto;

import com.activedge.usermgt.model.enumeration.MakerChecker;
import com.activedge.usermgt.model.log.MakerItem;
import com.activedge.usermgt.repository.redis.MakerItemRepository;
import com.activedge.usermgt.service.SpringUtil;
import com.activedge.usermgt.service.StaffService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import javax.persistence.Version;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * A DTO for the Staff entity.
 */
@Data
@NoArgsConstructor
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

    @ApiModelProperty(notes = "The staff permission groups")
    @NotNull(message = "Staff access group is required.")
    private Set<GroupDTO> groups = new HashSet<>();

    @ApiModelProperty(notes = "The staff active status")
    private Boolean activated;

    @ApiModelProperty(notes = "The redis reference number if available")
    private String redis_key;

}

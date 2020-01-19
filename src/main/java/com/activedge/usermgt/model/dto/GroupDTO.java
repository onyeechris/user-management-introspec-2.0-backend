package com.activedge.usermgt.model.dto;

import com.activedge.usermgt.model.GroupPK;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.*;

/**
 * A DTO for the Group entity.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "The Group Transfer Entity")
public class GroupDTO implements Serializable {

    @ApiModelProperty(notes = "The database generated group ID")
    private String id;

//    @NotNull(message = "Group name is required")
    @ApiModelProperty(notes = "The group name with minimum of 3 characters", required = true)
    @Size(min = 3, message = "group character length too short. Should be atleast 3 charaters")
    private String name;

    @ApiModelProperty(notes = "The group description text")
    @Size(min = 10, message = "group description length too short. Should be atleast 10 charaters")
    private String description;

    @ApiModelProperty(notes = "The group module/app")
    @Size(min = 3, message = "group module length too short. Should be atleast 3 charaters")
    private String mod;

//    @Valid
//    @JsonBackReference
    private Set<PermissionDTO> permissions = new HashSet<>();

//    @Valid
//    @JsonBackReference
    private Set<StaffDTO> staffs = new HashSet<>();

    @Override
    public String toString() {
        return "GroupDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", module='" + mod + '\'' +
                '}';
    }
}

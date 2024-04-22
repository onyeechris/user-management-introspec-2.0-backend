package com.activedge.usermgt.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the Group entity.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupDTO implements Serializable {

//    @ApiModelProperty(notes = "The database generated group ID")
    private String id;
//    @ApiModelProperty(notes = "The group name with minimum of 3 characters", required = true)
    @Size(min = 3, message = "group character length too short. Should be atleast 3 charaters")
    private String name;
//    @ApiModelProperty(notes = "The group description text")
    @Size(min = 10, message = "group description length too short. Should be atleast 10 charaters")
    private String description;
//    @ApiModelProperty(notes = "The group module/app")
    @Size(min = 3, message = "group module length too short. Should be atleast 3 charaters")
    private String mod;
    private Set<PermissionDTO> permissions = new HashSet<>();
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

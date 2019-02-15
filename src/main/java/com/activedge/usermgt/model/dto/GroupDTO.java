package com.activedge.usermgt.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Group entity.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "The Group Transfer Entity")
public class GroupDTO implements Serializable {

    @ApiModelProperty(notes = "The database generated group ID")
    private Long id;

    @ApiModelProperty(notes = "The group name with minimum of 3 characters", required = true)
    @NotNull
    @Size(min = 3, message = "group character length too short. Should be atleast 3 charaters")
    private String name;

    @ApiModelProperty(notes = "The group description text")
    @Size(min = 10, message = "group description length too short. Should be atleast 10 charaters")
    private String description;

    @Valid
    private Set<PermissionDTO> permissions = new HashSet<>();

    @ApiModelProperty(notes = "The redis reference number if available")
    private String redis_key;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<PermissionDTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionDTO> permissions) {
        this.permissions = permissions;
    }

    public String getRedis_key() {
        return redis_key;
    }

    public void setRedis_key(String redis_key) {
        this.redis_key = redis_key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GroupDTO groupDTO = (GroupDTO) o;
        if (groupDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), groupDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GroupDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", permissions='" + getPermissions() + "'" +
            "}";
    }
}

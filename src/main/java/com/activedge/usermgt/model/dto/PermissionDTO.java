package com.activedge.usermgt.model.dto;

import com.activedge.usermgt.model.Authority;
import com.activedge.usermgt.model.AuthorityPK;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Permission entity.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "The Permission Transfer Entity")
public class PermissionDTO implements Serializable {

    @ApiModelProperty(notes = "The database generated permission ID")
    private Long id;

    @ApiModelProperty(notes = "Permission action", required = true)
    @NotNull(message = "Permission action is required")
    @Size(min = 3, message = "The permission action length is too short. Should be at least 3 charater")
    private String action;

    private String description;

    @JsonIgnore
    private Authority authority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PermissionDTO permissionDTO = (PermissionDTO) o;
        if (permissionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), permissionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PermissionDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", authority='" + getAuthority() + "'" +
            "}";
    }
}

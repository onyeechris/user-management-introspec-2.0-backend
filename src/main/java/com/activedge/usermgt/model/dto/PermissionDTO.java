package com.activedge.usermgt.model.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Permission entity.
 */
public class PermissionDTO implements Serializable {

    private Long id;

    private String action;

    private String description;

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
            ", action='" + getAction() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}

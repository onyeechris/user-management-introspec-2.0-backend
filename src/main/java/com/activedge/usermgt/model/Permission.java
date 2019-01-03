package com.activedge.usermgt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "action")
    private String action;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "permissions")
    @JsonIgnore
    private Set<Groups> grps = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public Permission action(String action) {
        this.action = action;
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public Permission description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Groups> getGrps() {
        return grps;
    }

    public Permission grps(Set<Groups> groups) {
        this.grps = groups;
        return this;
    }

    public Permission addGrp(Groups groups) {
        this.grps.add(groups);
        groups.getPermissions().add(this);
        return this;
    }

    public Permission removeGrp(Groups groups) {
        this.grps.remove(groups);
        groups.getPermissions().remove(this);
        return this;
    }

    public void setGrps(Set<Groups> groups) {
        this.grps = groups;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Permission permission = (Permission) o;
        if (permission.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), permission.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + getId() +
                ", action='" + getAction() + "'" +
                ", description='" + getDescription() + "'" +
                "}";
    }
}


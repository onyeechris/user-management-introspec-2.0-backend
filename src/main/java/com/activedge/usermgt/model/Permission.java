package com.activedge.usermgt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "permissions")
@SequenceGenerator(name = "permGenerator", initialValue = 40, allocationSize = 50)
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permGenerator")
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "action", unique = true)
    private String action;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "permissions", cascade = CascadeType.MERGE)
    @JsonIgnore
    private Set<Group> grps = new HashSet<>();

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

    public Set<Group> getGrps() {
        return grps;
    }

    public Permission grps(Set<Group> groups) {
        this.grps = groups;
        return this;
    }

    public Permission addGrp(Group group) {
        this.grps.add(group);
        group.getPermissions().add(this);
        return this;
    }

    public Permission removeGrp(Group group) {
        this.grps.remove(group);
        group.getPermissions().remove(this);
        return this;
    }

    public void setGrps(Set<Group> groups) {
        this.grps = groups;
    }

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


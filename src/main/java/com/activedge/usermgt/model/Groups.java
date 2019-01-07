package com.activedge.usermgt.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "groups")
public class Groups implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "groups")
    private Set<Staff> staff = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "groups_permission",
            joinColumns = @JoinColumn(name = "groups_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permissions_id", referencedColumnName = "id"))
    private Set<Permission> permissions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Groups name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Groups description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Staff> getStaff() {
        return staff;
    }

    public Groups staff(Set<Staff> staff) {
        this.staff = staff;
        return this;
    }

    public Groups addStaff(Staff staff) {
        this.staff.add(staff);
        staff.setGroups(this);
        return this;
    }

    public Groups removeStaff(Staff staff) {
        this.staff.remove(staff);
        staff.setGroups(null);
        return this;
    }

    public void setStaff(Set<Staff> staff) {
        this.staff = staff;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Groups permissions(Set<Permission> permissions) {
        this.permissions = permissions;
        return this;
    }

    public Groups addPermission(Permission permission) {
        this.permissions.add(permission);
        permission.getGrps().add(this);
        return this;
    }

    public Groups removePermission(Permission permission) {
        this.permissions.remove(permission);
        permission.getGrps().remove(this);
        return this;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
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
        Groups groups = (Groups) o;
        if (groups.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), groups.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Groups{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                ", description='" + getDescription() + "'" +
                "}";
    }
}

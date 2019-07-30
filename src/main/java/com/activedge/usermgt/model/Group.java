package com.activedge.usermgt.model;


import com.activedge.usermgt.model.event.GroupEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "groups")
@EntityListeners(GroupEntityListener.class)
public class Group extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
//    @TableGenerator(name = "tableGenerator", initialValue = 3)
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Size(min = 10)
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "group")
    private Set<Staff> staff = new HashSet<>();

    @ManyToOne
    private Module module;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "groups_permission",
            joinColumns = @JoinColumn(name = "groups_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permissions_id", referencedColumnName = "id"))
    private Set<Permission> permissions = new HashSet<>();

    @Transient
    private String redisKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Group name(String name) {
        this.name = name;
        return this;
    }

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Group description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Staff> getStaff() {
        return staff;
    }

    public Group staff(Set<Staff> staff) {
        this.staff = staff;
        return this;
    }

    public Group addStaff(Staff staff) {
        this.staff.add(staff);
//        staff.setGroups(this);
        return this;
    }

    public Group removeStaff(Staff staff) {
        this.staff.remove(staff);
//        staff.setGroups(null);
        return this;
    }

    public void setStaff(Set<Staff> staff) {
        this.staff = staff;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Group permissions(Set<Permission> permissions) {
        this.permissions = permissions;
        return this;
    }

    public Group addPermission(Permission permission) {
        this.permissions.add(permission);
        permission.getGrps().add(this);
        return this;
    }

    public Group removePermission(Permission permission) {
        this.permissions.remove(permission);
        permission.getGrps().remove(this);
        return this;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Group group = (Group) o;
        if (group.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), group.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                ", description='" + getDescription() + "'" +
                ", permissions='" + getPermissions() + "'" +
                "}";
    }
}

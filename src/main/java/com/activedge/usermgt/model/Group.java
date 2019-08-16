package com.activedge.usermgt.model;


import com.activedge.usermgt.model.event.GroupEntityListener;
import org.hibernate.annotations.BatchSize;

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

    /*
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableGenerator")
//    @TableGenerator(name = "tableGenerator", initialValue = 3)
    private Long id;
    */

    @EmbeddedId
    private GroupPK id;

    @NotNull
    @Size(min = 3)
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Size(min = 10)
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "module", insertable = false, updatable = false)
    private Module module;

//    @OneToMany(mappedBy = "group")
//    private Set<Staff> staff = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "staff_group",
            joinColumns = {
                @JoinColumn(name = "group_id", referencedColumnName = "id"),
                @JoinColumn(name = "module", referencedColumnName = "module")},
            inverseJoinColumns = {
                @JoinColumn(name = "staff_id", referencedColumnName = "id") })
    @BatchSize(size = 10)
    private Set<Staff> staffs = new HashSet<>();

//    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @ManyToMany
    @JoinTable(name = "groups_permission",
            joinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id"), @JoinColumn(name = "module", referencedColumnName = "module")},
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
    private Set<Permission> permissions = new HashSet<>();

    @Transient
    private String redisKey;

    public GroupPK getId() {
        return id;
    }

    public void setId(GroupPK id) {
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

    public Set<Staff> getStaffs() {
        return staffs;
    }

    public Group staff(Set<Staff> staff) {
        this.staffs = staff;
        return this;
    }

    public Group addStaff(Staff staff) {
        this.staffs.add(staff);
//        staff.setGroups(this);
        return this;
    }

    public Group removeStaff(Staff staff) {
        this.staffs.remove(staff);
//        staff.setGroups(null);
        return this;
    }

    public void setStaffs(Set<Staff> staff) {
        this.staffs = staff;
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
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        if (!super.equals(o)) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id) &&
                Objects.equals(name, group.name) &&
                Objects.equals(description, group.description) &&
                Objects.equals(staffs, group.staffs) &&
                Objects.equals(permissions, group.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, description, staffs, permissions);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", staffs=" + getStaffs() +
                ", permissions=" + getPermissions() +
                '}';
    }
}

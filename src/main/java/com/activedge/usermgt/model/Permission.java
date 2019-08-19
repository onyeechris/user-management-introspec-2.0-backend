package com.activedge.usermgt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "permissions",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"module", "action"})
)
@SequenceGenerator(name = "permGenerator", initialValue = 200, allocationSize = 1)
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permGenerator")
    private Long id;

    @NotNull(message = "Permission action is required")
    @Size(min = 3)
    @Column(name = "action")
    private String action;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "module", referencedColumnName = "code")
    private Module module;

//    @ManyToOne
//    @JoinColumns({
//            @JoinColumn(
//                    name = "module",
//                    referencedColumnName = "module"),
//            @JoinColumn(
//                    name = "authority",
//                    referencedColumnName = "code")
//    })
//    private Authority authority;

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

//    public Authority getAuthority() {
//        return authority;
//    }

//    public void setAuthority(Authority authority) {
//        this.authority = authority;
//    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", action='" + action + '\'' +
                ", description='" + description + '\'' +
//                ", authority=" + authority +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission)) return false;
        Permission that = (Permission) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(action, that.action) &&
                Objects.equals(description, that.description);
//                Objects.equals(authority, that.authority) &&
//                Objects.equals(grps, that.grps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, action, description /*, authority, grps*/);
    }

}


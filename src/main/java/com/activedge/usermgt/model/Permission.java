package com.activedge.usermgt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "permissions")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @NotNull(message = "Permission action is required")
    @Size(min = 3)
    @Column(name = "action")
    private String action;

    @Column(name = "description")
    private String description;

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @ManyToOne
    @JoinColumn(name = "module", referencedColumnName = "id")
    @DBRef
    private Module module;

    @ManyToMany(mappedBy = "permissions", cascade = CascadeType.MERGE)
    @JsonIgnore
    private Set<Group> grps = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", action='" + action + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission)) return false;
        Permission that = (Permission) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}


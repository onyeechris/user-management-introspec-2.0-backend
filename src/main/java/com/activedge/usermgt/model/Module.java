package com.activedge.usermgt.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "introspec_module")
public class Module implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 50)
    @Id
    @Column(length = 50, unique = true)
    private String code;

    @NotNull
    @Size(max = 50)
    @Column(length = 50, unique = true)
    private String name;

//    @OneToMany(mappedBy = "module")
//    private Set<Permission> permissions = new HashSet<>();

//    @OneToMany(mappedBy = "module")
//    private Set<Group> groups = new HashSet<>();

//    @OneToMany(mappedBy = "module")
//    private Set<Authority> authority = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Module authority = (Module) o;

        return !(name != null ? !name.equals(authority.name) : authority.name != null);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Module{" +
                "name='" + name + '\'' +
                "}";
    }
}


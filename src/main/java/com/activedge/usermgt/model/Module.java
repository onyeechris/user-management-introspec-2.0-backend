package com.activedge.usermgt.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "introspec_module")
@NoArgsConstructor
@AllArgsConstructor
@Data
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

    @Column(length = 255)
    private String description;

    @OneToMany(mappedBy = "module", fetch = FetchType.EAGER)
    @JsonBackReference
    Set<StaffModule> staffModules;

//    @OneToMany(mappedBy = "module")
//    private Set<Permission> permissions = new HashSet<>();

//    @OneToMany(mappedBy = "module")
//    private Set<Group> groups = new HashSet<>();

//    @OneToMany(mappedBy = "module")
//    private Set<Authority> authority = new HashSet<>();

}


package com.activedge.usermgt.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Table(name = "introspec_module")
@Document(collection = "introspec_module")
public class Module implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Size(max = 50)
    @Column(length = 50, unique = true)
    private String code;

    @NotNull
    @Size(max = 50)
    @Column(length = 50, unique = true)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(length = 255)
    private String key;

    @OneToMany(mappedBy = "module", fetch = FetchType.LAZY)
//    @JsonBackReference
    Set<StaffModule> staffModules;

//    @OneToMany(mappedBy = "module")
//    private Set<Permission> permissions = new HashSet<>();

//    @OneToMany(mappedBy = "module")
//    private Set<Group> groups = new HashSet<>();

//    @OneToMany(mappedBy = "module")
//    private Set<Authority> authority = new HashSet<>();

}


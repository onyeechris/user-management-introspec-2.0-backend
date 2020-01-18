package com.activedge.usermgt.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
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
@Table(name = "introspec_modules")
@Document(collection = "introspec_modules")
public class Module implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Size(max = 50)
    @Column(length = 50, unique = true)
    private String id;

    @NotNull
    @Size(max = 50)
    @Column(length = 50, unique = true)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(length = 255)
    private String key;

    @OneToMany(mappedBy = "module", fetch = FetchType.LAZY)
    @DBRef
    Set<StaffModule> staffModules;
}


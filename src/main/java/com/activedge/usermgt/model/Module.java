package com.activedge.usermgt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
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
    @Column(unique = true)
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

    private boolean active=true;
}


package com.activedge.usermgt.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@ToString
@Entity
@Getter
@Setter
@Table(name = "introspec_authority")
public class Authority implements Serializable {

    private static final long serialVersionUID = 1L;

//    @EmbeddedId
//    private AuthorityPK id;
    @Id
    @NotNull
    @Size(min = 3)
    @Column(name = "code")
    private String code;

//    @ManyToOne
//    @JoinColumn(name = "module",insertable = false, updatable = false)
//    private Module module;

    @NotNull
    @Size(max = 50)
    @Column(length = 50/*, unique = true*/)
    private String name;

}


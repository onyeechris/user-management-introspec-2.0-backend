package com.activedge.usermgt.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@ToString
@Entity
@Getter
@Setter
@Table(name = "introspec_authorities")
@Document(collection = "introspec_authorities")
public class Authority implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Size(min = 3)
    @Column(name = "id")
    private String id;

    @NotNull
    @Size(max = 50)
    @Column(length = 50/*, unique = true*/)
    private String name;

}


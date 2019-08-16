package com.activedge.usermgt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class GroupPK implements Serializable {

    @ManyToOne
    @JoinColumn(name = "module")
    private Module module;

    @Column(name = "id")
    private String id;

}

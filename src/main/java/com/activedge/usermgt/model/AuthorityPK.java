package com.activedge.usermgt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityPK implements Serializable {

    @Column(name = "module")
    private String module;

    @NotNull
    @Size(min = 3)
    @Column(name = "id")
    private String code;

}

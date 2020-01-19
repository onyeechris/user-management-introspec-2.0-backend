package com.activedge.usermgt.model.dto;

import com.activedge.usermgt.model.Authority;
import com.activedge.usermgt.model.AuthorityPK;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Permission entity.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "The Permission Transfer Entity")
public class PermissionDTO implements Serializable {

    @ApiModelProperty(notes = "The database generated permission ID")
    private String id;

    @ApiModelProperty(notes = "Permission action", required = true)
    @NotNull(message = "PermissionDTO action is required")
    @Size(min = 3, message = "The permission action length is too short. Should be at least 3 charater")
    private String action;

    private String description;

    private String modul;

//    @JsonIgnore
//    private Authority authority;

}

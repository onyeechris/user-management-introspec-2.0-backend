package com.activedge.usermgt.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the Module entity.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "The AppModule Entity")
@Data
public class ModuleDTO implements Serializable {

    @ApiModelProperty(notes = "The unique app module Id")
    @NotNull
    private String code;

    @ApiModelProperty(notes = "The module name with minimum of 3 characters", required = true)
    @NotNull
    @Size(min = 3, message = "module character length too short. Should be atleast 3 charaters")
    private String name;

    @ApiModelProperty(notes = "The module description text")
    @Size(min = 10, message = "module description length too short. Should be atleast 10 charaters")
    private String description;

}

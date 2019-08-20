package com.activedge.usermgt.model.dto;

import com.activedge.usermgt.model.Module;
import com.activedge.usermgt.model.Staff;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A DTO for the StaffModule entity.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "The StaffModule Transfer Entity")
public class StaffModuleDTO implements Serializable {

    @ApiModelProperty(notes = "The database generated staffModule ID")
    private Long id;

    @ApiModelProperty(notes = "The staff assignment date", example = "yyyy-MM-dd HH:mm:ss", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime assign_at;

    @ApiModelProperty(notes = "Staff grade level")
    private Long grade;

//    @JsonIgnore
    @ApiModelProperty(notes = "The module staff belong to", required = true)
    private String module;

//    @JsonIgnore
    @ApiModelProperty(notes = "The staff in the module", required = true)
    private StaffDTO staff;

}

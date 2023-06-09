package com.activedge.usermgt.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the StaffModule entity.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
//@ApiModel(description = "The StaffModule Transfer Entity")
public class StaffModuleDTO implements Serializable {

//    @ApiModelProperty(notes = "The database generated staffModule ID")
    private String id;

//    @ApiModelProperty(notes = "The staff assignment date", example = "yyyy-MM-dd HH:mm:ss", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime assign_at;

//    @ApiModelProperty(notes = "Staff grade level")
    private Long grade;

//    @ApiModelProperty(notes = "The module staff belong to", required = true)
    private String module;

//    @ApiModelProperty(notes = "The staff in the module", required = true)
    private StaffDTO staff;

}

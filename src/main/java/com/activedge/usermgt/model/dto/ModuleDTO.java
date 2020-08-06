package com.activedge.usermgt.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the AppModule entity.
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModuleDTO implements Serializable {

    /**
     * The unique app module Id
     */
    @NotNull
    private String id;

    /**
     * The module name with minimum of 3 characters
     */
    @NotNull
    @Size(min = 3, message = "module character length too short. Should be atleast 3 charaters")
    private String name;

    /**
     * The module description text
     */
    @Size(min = 10, message = "module description length too short. Should be atleast 10 charaters")
    private String description;

    /**
     * The module key text
     */
    private String key;

}

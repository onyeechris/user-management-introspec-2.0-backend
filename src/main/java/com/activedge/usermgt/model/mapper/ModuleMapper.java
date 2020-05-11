package com.activedge.usermgt.model.mapper;


import com.activedge.usermgt.model.Module;
import com.activedge.usermgt.model.dto.ModuleDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Module and its DTO ModuleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ModuleMapper extends EntityMapper<ModuleDTO, Module> {

    ModuleDTO toDto(Module module);

    Module toEntity(ModuleDTO moduleDTO);

    default Module fromId(String id) {
        if (id == null) {
            return null;
        }
        Module module = new Module();
        module.setId(id);
        return module;
    }

}

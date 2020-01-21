package com.activedge.usermgt.model.mapper;


import com.activedge.usermgt.model.Permission;
import com.activedge.usermgt.model.dto.PermissionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

/**
 * Mapper for the entity Permission and its DTO PermissionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PermissionMapper extends EntityMapper<PermissionDTO, Permission> {

    @Mapping(source = "module.id", target = "modul")
    PermissionDTO toDto(Permission permission);

    @Mapping(target = "grps", ignore = true)
    @Mapping(source = "modul", target = "module.id")
    Permission toEntity(PermissionDTO permissionDTO);

    Set<PermissionDTO> toDtoSet(Set<Permission> permissions);

    default Permission fromId(String id) {
        if (id == null) {
            return null;
        }
        Permission permission = new Permission();
        permission.setId(id);
        return permission;
    }
}

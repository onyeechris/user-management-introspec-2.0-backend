package com.activedge.usermgt.model.mapper;


import com.activedge.usermgt.model.AuthorityPK;
import com.activedge.usermgt.model.Permission;
import com.activedge.usermgt.model.dto.PermissionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity Permission and its DTO PermissionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PermissionMapper extends EntityMapper<PermissionDTO, Permission> {

    @Mapping(target = "grps", ignore = true)
    Permission toEntity(PermissionDTO permissionDTO);

    default Permission fromId(Long id) {
        if (id == null) {
            return null;
        }
        Permission permission = new Permission();
        permission.setId(id);
        return permission;
    }
}

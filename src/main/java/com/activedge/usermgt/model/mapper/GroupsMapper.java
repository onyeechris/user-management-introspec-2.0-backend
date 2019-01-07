package com.activedge.usermgt.model.mapper;


import com.activedge.usermgt.model.Groups;
import com.activedge.usermgt.model.dto.GroupsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity Groups and its DTO GroupsDTO.
 */
@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface GroupsMapper extends EntityMapper<GroupsDTO, Groups> {


    @Mapping(target = "staff", ignore = true)
    Groups toEntity(GroupsDTO groupsDTO);

    default Groups fromId(Long id) {
        if (id == null) {
            return null;
        }
        Groups groups = new Groups();
        groups.setId(id);
        return groups;
    }
}

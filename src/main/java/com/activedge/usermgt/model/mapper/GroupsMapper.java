package com.activedge.usermgt.model.mapper;


import com.activedge.usermgt.model.Group;
import com.activedge.usermgt.model.dto.GroupsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity Group and its DTO GroupsDTO.
 */
@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface GroupsMapper extends EntityMapper<GroupsDTO, Group> {


    @Mapping(target = "staff", ignore = true)
    Group toEntity(GroupsDTO groupsDTO);

    default Group fromId(Long id) {
        if (id == null) {
            return null;
        }
        Group group = new Group();
        group.setId(id);
        return group;
    }
}

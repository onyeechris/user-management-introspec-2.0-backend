package com.activedge.usermgt.model.mapper;


import com.activedge.usermgt.model.Group;
import com.activedge.usermgt.model.GroupPK;
import com.activedge.usermgt.model.dto.GroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity Group and its DTO GroupDTO.
 */
@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface GroupMapper extends EntityMapper<GroupDTO, Group> {

    @Mapping(source = "redisKey", target = "redis_key")
    GroupDTO toDto(Group group);

//    @Mapping(target = "staff", ignore = true)
    @Mapping(source = "redis_key", target = "redisKey")
    Group toEntity(GroupDTO groupDTO);

    default Group fromId(GroupPK id) {
        if (id == null) {
            return null;
        }
        Group group = new Group();
        group.setId(id);
        return group;
    }
}

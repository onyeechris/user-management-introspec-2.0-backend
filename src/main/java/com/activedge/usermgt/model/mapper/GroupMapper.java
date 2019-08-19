package com.activedge.usermgt.model.mapper;


import com.activedge.usermgt.model.Group;
import com.activedge.usermgt.model.GroupPK;
import com.activedge.usermgt.model.Module;
import com.activedge.usermgt.model.dto.GroupDTO;
import com.activedge.usermgt.repository.ModuleRepository;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * Mapper for the entity Group and its DTO GroupDTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ModuleMapper.class})
public interface GroupMapper extends EntityMapper<GroupDTO, Group> {

    ModuleMapper mapper = Mappers.getMapper( ModuleMapper.class );

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "id.module.code", target = "module")
    @Mapping(target = "staffs", ignore = true)
    GroupDTO toDto(Group group);

//    @Mapping(target = "staff", ignore = true)
//    @Mapping(source = "redis_key", target = "redisKey")
    @Mapping(target = "id", expression = "java( new GroupPK(fromCode(groupDTO.getModule()), groupDTO.getId()) )")
    Group toEntity(GroupDTO groupDTO);

//    @Mappings({
////         @Mapping(target = "father", expression = "java(null)"),
//            @Mapping(target = "father", qualifiedByName = "fatherToFatherDto")})
//    ChildDto childToChildDto(Child child);
//
//    @Named("fatherToFatherDto")
//    @Mappings({
//            @Mapping(target = "children", expression = "java(null)")})
//    FatherDto fatherToFatherDto(Father father);

//    @ObjectFactory
//    default GroupPK createId(GroupDTO dto) {
//        return dto == null ? null : new GroupPK(fromCode(dto.getModule()), dto.getId());
//    }

    default Group fromId(GroupPK id) {
        if (id == null) {
            return null;
        }
        Group group = new Group();
        group.setId(id);
        return group;
    }

    default Module fromCode(String code) {
        if (code == null) {
            return null;
        }
        Module module = mapper.fromId(code);

        return module;
    }

}

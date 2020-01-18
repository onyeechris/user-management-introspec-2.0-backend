package com.activedge.usermgt.model.mapper;


import com.activedge.usermgt.model.Group;
import com.activedge.usermgt.model.GroupPK;
import com.activedge.usermgt.model.Module;
import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.dto.GroupDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;

/**
 * Mapper for the entity Group and its DTO GroupDTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ModuleMapper.class})
public abstract class GroupMapper {

    ModuleMapper mapper = Mappers.getMapper( ModuleMapper.class );

    @BeforeMapping
    void enrichDTOWithFuelType(Group group, @MappingTarget GroupDTO groupDto) {
        Set<Staff> stf = group.getStaffs();
        group.setStaffs(new HashSet<>());

        for(Staff staff: stf) {
            staff.setGroups(null);
            group.addStaff(staff);
        }

    }

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "id.module.id", target = "mod")
//    @Mapping(target = "staffs", ignore = true)
    public abstract GroupDTO toDto(Group group);

    @Mapping(target = "id", expression = "java( new GroupPK(fromCode(groupDTO.getMod()), groupDTO.getId()) )")
    @Mapping(source = "mod", target = "module.id")
    public abstract Group toEntity(GroupDTO groupDTO);

    Group fromId(GroupPK id) {
        if (id == null) {
            return null;
        }
        Group group = new Group();
        group.setId(id);
        return group;
    }

    Module fromCode(String code) {
        if (code == null) {
            return null;
        }
        Module module = mapper.fromId(code);

        return module;
    }

}

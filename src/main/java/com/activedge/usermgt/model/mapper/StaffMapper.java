package com.activedge.usermgt.model.mapper;


import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.dto.StaffDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity Staff and its DTO StaffDTO.
 */
@Mapper(componentModel = "spring", uses = {GroupsMapper.class})
public interface StaffMapper extends EntityMapper<StaffDTO, Staff> {

    @Mapping(source = "group.id", target = "groupId")
//    @Mapping(source = "groups.id", target = "groupsId")
    StaffDTO toDto(Staff staff);

    @Mapping(source = "groupId", target = "group")
//    @Mapping(source = "groupsId", target = "groups")
    Staff toEntity(StaffDTO staffDTO);

    default Staff fromId(Long id) {
        if (id == null) {
            return null;
        }
        Staff staff = new Staff();
        staff.setId(id);
        return staff;
    }
}

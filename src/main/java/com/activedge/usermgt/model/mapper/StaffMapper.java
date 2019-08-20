package com.activedge.usermgt.model.mapper;


import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.dto.StaffDTO;
import org.mapstruct.*;

import java.util.Set;

/**
 * Mapper for the entity Staff and its DTO StaffDTO.
 */
@Mapper(componentModel = "spring", uses = {GroupMapper.class})
public interface StaffMapper extends EntityMapper<StaffDTO, Staff> {

//    @Mapping(source = "group.id", target = "group_id")
//    @Mapping(source = "first_name", target = "first_name")
//    @Mapping(source = "last_name", target = "last_name")
    @Mapping(source = "hireDate", target = "hire_date")
    @Mapping(source = "type", target = "user_type")
    @Mapping(target = "groups", ignore = true)
    StaffDTO toDto(Staff staff);

    Set<StaffDTO> toDtoSet(Set<Staff> staffs);

//    @Mapping(source = "group_id", target = "group")
//    @Mapping(source = "first_name", target = "first_name")
//    @Mapping(source = "last_name", target = "last_name")
    @Mapping(source = "hire_date", target = "hireDate")
    @Mapping(source = "user_type", target = "type")
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

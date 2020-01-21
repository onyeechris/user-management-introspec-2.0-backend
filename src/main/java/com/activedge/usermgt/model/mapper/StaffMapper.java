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

    @Mapping(source = "hireDate", target = "hire_date")
    @Mapping(source = "type", target = "user_type")
    StaffDTO toDto(Staff staff);

    Set<StaffDTO> toDtoSet(Set<Staff> staffs);

    @Mapping(source = "hire_date", target = "hireDate")
    @Mapping(source = "user_type", target = "type")
    Staff toEntity(StaffDTO staffDTO);

    default Staff fromId(String id) {
        if (id == null) {
            return null;
        }
        Staff staff = new Staff();
        staff.setId(id);
        return staff;
    }
}

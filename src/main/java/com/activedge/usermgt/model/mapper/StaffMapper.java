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
    @Mapping(source = "firstName", target = "first_name")
    @Mapping(source = "lastName", target = "last_name")
    @Mapping(source = "hireDate", target = "hire_date")
    @Mapping(source = "makerChecker", target = "maker_checker")
    @Mapping(source = "redisKey", target = "redis_key")
    StaffDTO toDto(Staff staff);

    Set<StaffDTO> toDtoSet(Set<Staff> staffs);

//    @Mapping(source = "group_id", target = "group")
    @Mapping(source = "first_name", target = "firstName")
    @Mapping(source = "last_name", target = "lastName")
    @Mapping(source = "hire_date", target = "hireDate")
    @Mapping(source = "maker_checker", target = "makerChecker")
    @Mapping(source = "redis_key", target = "redisKey")
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

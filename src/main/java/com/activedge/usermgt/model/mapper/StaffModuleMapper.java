package com.activedge.usermgt.model.mapper;


import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.StaffModule;
import com.activedge.usermgt.model.dto.StaffModuleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity StaffModule and its DTO StaffModuleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StaffModuleMapper extends EntityMapper<StaffModuleDTO, StaffModule> {

    @Mapping(source = "module.code", target = "module")
//    @Mapping(source = "staff.id", target = "staff")
    @Mapping(ignore = true, target = "staff.groups")
    @Mapping(source = "assignAt", target = "assign_at")
    StaffModuleDTO toDto(StaffModule staffModule);

    @Mapping(source = "module", target = "module.code")
//    @Mapping(source = "staff", target = "staff.id")
    @Mapping(ignore = true, target = "staff.groups")
    @Mapping(source = "assign_at", target = "assignAt")
    StaffModule toEntity(StaffModuleDTO staffModuleDTO);

    default StaffModule fromId(Long id) {
        if (id == null) {
            return null;
        }
        StaffModule staffModule = new StaffModule();
        staffModule.setId(id);
        return staffModule;
    }

}

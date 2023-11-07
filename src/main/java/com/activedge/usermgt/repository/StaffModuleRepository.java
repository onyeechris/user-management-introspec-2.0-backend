package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.Module;
import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.StaffModule;
import com.activedge.usermgt.model.dto.StaffModuleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface StaffModuleRepository extends PagingAndSortingRepository<StaffModule, String> {

    Page<StaffModule> findAllByModule_Id(Pageable pageable, String module);

    // Associations can only be pointed to directly or via their id property!
    StaffModule findByModule_IdAndStaff_Email(String module, String email);

    Set<StaffModule> findByModule_Id(String module);

    Optional<StaffModule> findByModuleAndStaff(Module module, Staff staff);

    Page<StaffModule> findByStaff(Staff staff, Pageable pageable);
}

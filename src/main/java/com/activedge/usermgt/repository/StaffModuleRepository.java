package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.StaffModule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface StaffModuleRepository extends PagingAndSortingRepository<StaffModule, Long> {

    Page<StaffModule> findAllByModule_Id(Pageable pageable, String module);

    // Associations can only be pointed to directly or via their id property!
    StaffModule findByModule_IdAndStaff_Email(String module, String email);

    Set<StaffModule> findByModule_Id(String module);

}

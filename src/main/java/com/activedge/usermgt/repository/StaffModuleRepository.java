package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.StaffModule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface StaffModuleRepository extends PagingAndSortingRepository<StaffModule, String> {

    Page<StaffModule> findAllByModule_Id(Pageable pageable, String module);

    // Associations can only be pointed to directly or via their id property!
    List<StaffModule> findByModule_IdAndStaff_Username(String module, String username);
    List<StaffModule> findAllByModule_IdAndStaff(String module, Staff staff, Pageable pageable);

    Set<StaffModule> findByModule_Id(String module);

}

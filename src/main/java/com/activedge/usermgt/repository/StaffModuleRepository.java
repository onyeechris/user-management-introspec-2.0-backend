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
    StaffModule findByModule_IdAndStaff_Email(String module, String email);

    Set<StaffModule> findByModule_Id(String module);
    @Query("SELECT sm FROM StaffModule sm WHERE sm.staff.id IN (SELECT s.id FROM Staff s WHERE s.username LIKE %:username%)")
    List<StaffModule> findByStaffUsernameContainingIgnoreCase(@Param("username")String username);
}

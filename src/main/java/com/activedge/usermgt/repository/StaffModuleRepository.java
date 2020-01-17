package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.StaffModule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffModuleRepository extends PagingAndSortingRepository<StaffModule, Long> {

    Page<StaffModule> findAllByModule_Code(Pageable pageable, String module);

}

package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends PagingAndSortingRepository<Permission, String> {

    Page<Permission> findAllByModule_Id(Pageable pageable, String module);

}

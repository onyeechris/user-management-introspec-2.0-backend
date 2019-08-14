package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.AuthorityPK;
import com.activedge.usermgt.model.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Page<Permission> findAllByModule_Code(Pageable pageable, String module);

}

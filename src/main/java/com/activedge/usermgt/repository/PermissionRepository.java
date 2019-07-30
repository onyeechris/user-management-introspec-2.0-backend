package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.AuthorityPK;
import com.activedge.usermgt.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

}

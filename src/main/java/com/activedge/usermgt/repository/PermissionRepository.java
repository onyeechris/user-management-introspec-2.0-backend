package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.AuthorityPK;
import com.activedge.usermgt.model.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface PermissionRepository extends PagingAndSortingRepository<Permission, BigInteger> {

    Page<Permission> findAllByModule_Id(Pageable pageable, String module);

}

package com.activedge.usermgt.repository;


import com.activedge.usermgt.model.Authority;
import com.activedge.usermgt.model.AuthorityPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, AuthorityPK> {

}

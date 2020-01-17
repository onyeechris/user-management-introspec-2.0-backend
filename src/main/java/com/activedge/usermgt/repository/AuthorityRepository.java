package com.activedge.usermgt.repository;


import com.activedge.usermgt.model.Authority;
import com.activedge.usermgt.model.AuthorityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
@Repository
public interface AuthorityRepository extends PagingAndSortingRepository<Authority, AuthorityPK> {

}

package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.LdapUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LdapRepository extends PagingAndSortingRepository<LdapUser, String> {

    Optional<LdapUser> findByUserid(String userid);

    Page<LdapUser> findByUsernameLikeIgnoreCase(Pageable pageable, String username);

}

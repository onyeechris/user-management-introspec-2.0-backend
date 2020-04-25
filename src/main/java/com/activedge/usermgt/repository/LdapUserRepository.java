package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.LdapUser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LdapUserRepository extends PagingAndSortingRepository<LdapUser, String> {

    LdapUser findByUserid(String userid);

    List<LdapUser> findByUsernameLikeIgnoreCase(String username);

}

package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.LdapUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.naming.Name;
import java.util.Optional;


@Repository
public interface LdapRepository extends CrudRepository<LdapUser, Name> {

    Optional<LdapUser> findByUsername(String userid);

    Page<LdapUser> findByUsernameLikeIgnoreCase(Pageable pageable, String username);

}

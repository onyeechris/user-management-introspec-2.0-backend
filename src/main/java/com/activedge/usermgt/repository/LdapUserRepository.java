package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.LdapUser;
import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.naming.Name;
import java.time.Instant;
import java.util.List;
import java.util.Optional;


@Repository
public interface LdapUserRepository extends CrudRepository<LdapUser, Name> {

    LdapUser findByUserid(String userid);

    LdapUser findByUsername(String username);

    LdapUser findByUseridAndPassword(String username, String password);

    List<LdapUser> findByUsernameLikeIgnoreCase(String username);

}

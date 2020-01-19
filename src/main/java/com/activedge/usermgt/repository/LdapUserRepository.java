package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.LdapUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.naming.Name;
import java.util.List;


@Repository
public interface LdapUserRepository extends CrudRepository<LdapUser, Name> {

    LdapUser findByUserid(String userid);

    LdapUser findByUsername(String username);

    LdapUser findByUseridAndPassword(String username, String password);

    List<LdapUser> findByUsernameLikeIgnoreCase(String username);

}

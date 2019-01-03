package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.User;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.naming.Name;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Name> {

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

    List<User> findByUsernameLikeIgnoreCase(String username);

//    void save(User user);

}

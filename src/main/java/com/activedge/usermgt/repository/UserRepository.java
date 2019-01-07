package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.Staff;
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

//public interface UserRepository extends CrudRepository<User, Name> {

@Repository
public interface UserRepository extends JpaRepository<Staff, Long> {

//    Optional<User> findOneByActivationKey(String activationKey);
//
//    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);
//
//    Optional<User> findOneByResetKey(String resetKey);
//
//    Optional<User> findOneByEmailIgnoreCase(String email);
//
//    Optional<User> findOneByLogin(String login);
//
//    @EntityGraph(attributePaths = "authorities")
//    Optional<User> findOneWithAuthoritiesById(Long id);
//
//    @EntityGraph(attributePaths = "authorities")
//    Optional<User> findOneWithAuthoritiesByLogin(String login);
//
//    @EntityGraph(attributePaths = "authorities")
//    Optional<User> findOneWithAuthoritiesByEmail(String email);
//
//    Page<User> findAllByLoginNot(Pageable pageable, String login);
//
//    //
//    User findByUsername(String username);
//
//    User findByUsernameAndPassword(String username, String password);
//
//    List<User> findByUsernameLikeIgnoreCase(String username);

}

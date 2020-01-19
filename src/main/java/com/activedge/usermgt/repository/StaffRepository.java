package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StaffRepository extends PagingAndSortingRepository<Staff, String> {
//
//    Optional<User> findOneByActivationKey(String activationKey);
//
////    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);
//
//    Optional<User> findOneByResetKey(String resetKey);
//
    Optional<Staff> findOneByEmailIgnoreCase(String email);
//
//    Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = {"authorities"})
    Optional<Staff> findOneWithAuthoritiesById(String id);

//    @EntityGraph(attributePaths = "authorities")
//    Optional<User> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = {"authorities", "groups"})
    Optional<Staff> findOneWithAuthoritiesByEmail(String email);

//    Page<User> findAllByLoginNot(Pageable pageable, String login);



}

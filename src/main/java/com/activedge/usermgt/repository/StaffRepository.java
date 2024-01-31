package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.Staff;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StaffRepository extends PagingAndSortingRepository<Staff, String> {
    Optional<Staff> findOneByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = {"authorities"})
    Optional<Staff> findOneWithAuthoritiesById(String id);

    @EntityGraph(attributePaths = {"authorities", "groups"})
    Optional<Staff> findOneWithAuthoritiesByEmail(String email);

    @EntityGraph(attributePaths = {"authorities", "groups"})
    Optional<Staff> findOneWithAuthoritiesByUsernameIgnoreCase(String username);

    Optional<Staff> findByEmail(String email);

    Optional<Staff> findByUsername(String username);
    List<Staff> findByUsernameContaining(String username);


}

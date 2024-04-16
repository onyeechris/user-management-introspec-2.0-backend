package com.activedge.usermgt.repository;

import com.activedge.usermgt.license.License;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LicenseRepository extends PagingAndSortingRepository<License, String> {

    @Query(value = "{}", fields = "{ 'no_of_users' : 1}")
    Optional<License> findNoOfUsers();

    Optional<License> findFirstByOrderByIdAsc();

}

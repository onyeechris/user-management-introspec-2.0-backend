package com.activedge.usermgt.repository;

import com.activedge.usermgt.license.License;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenseRepository extends PagingAndSortingRepository<License, String> {

}

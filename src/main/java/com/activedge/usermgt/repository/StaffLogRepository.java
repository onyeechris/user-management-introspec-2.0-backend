package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.log.StaffLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StaffLogRepository extends PagingAndSortingRepository<StaffLog, Integer> {

}

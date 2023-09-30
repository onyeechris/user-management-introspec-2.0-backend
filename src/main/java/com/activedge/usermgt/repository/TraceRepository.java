package com.activedge.usermgt.repository;


import com.activedge.usermgt.model.CustomHttpTrace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TraceRepository extends PagingAndSortingRepository<CustomHttpTrace, String> {

    Page<CustomHttpTrace> findAllByStatus(Integer status, Pageable pageable);
    List<CustomHttpTrace> findByUsernameAndTimestampGreaterThanEqualOrderByTimestampDesc(String username, Date date);
    Page<CustomHttpTrace> findAllByTimestampBetween(Date from, Date to, Pageable pageable);

}

package com.activedge.usermgt.service;


import com.activedge.usermgt.model.CustomHttpTrace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

/**
 * Service Interface for managing CustomHttpTrace.
 */
public interface TraceService {

    Page<CustomHttpTrace> findAllByStatus(Integer status, Pageable pageable);

    Page<CustomHttpTrace> findAll(Date start, Date end, Pageable pageable);
    Page<CustomHttpTrace> findAllByStatusAndDateRange(Integer status, Date from, Date to, Pageable pageable);


}

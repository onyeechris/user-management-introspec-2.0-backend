package com.activedge.usermgt.service;


import com.activedge.usermgt.model.CustomHttpTrace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * Service Interface for managing CustomHttpTrace.
 */
public interface TraceService {

    Page<CustomHttpTrace> findAllByStatus(Integer status, Pageable pageable);
    List<CustomHttpTrace> searchAuditLogsByUsernameAndDate(String username, Date date);
    Page<CustomHttpTrace> findAll(Date start, Date end, Pageable pageable);

}

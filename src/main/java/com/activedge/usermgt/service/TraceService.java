package com.activedge.usermgt.service;


import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.CustomHttpTrace;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing CustomHttpTrace.
 */
public interface TraceService {

    Page<CustomHttpTrace> findAllByStatus(Integer status, Pageable pageable);

    Page<CustomHttpTrace> findAll(Pageable pageable);

}

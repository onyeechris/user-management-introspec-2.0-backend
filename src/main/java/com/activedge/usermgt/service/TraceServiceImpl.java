package com.activedge.usermgt.service;

import com.activedge.usermgt.model.CustomHttpTrace;
import com.activedge.usermgt.repository.TraceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Service Implementation for managing Trace.
 */
@Service
@Transactional
public class TraceServiceImpl implements TraceService {

    private final Logger log = LoggerFactory.getLogger(TraceServiceImpl.class);

    private final TraceRepository traceRepository;

    public TraceServiceImpl(TraceRepository traceRepository) {
        this.traceRepository = traceRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomHttpTrace> findAll(Date start, Date end, Pageable pageable) {
        log.debug("Request to get all Trace");
        return traceRepository.findAllByTimestampBetween(start, end, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomHttpTrace> findAllByStatus(Integer status, Pageable pageable) {
        log.debug("Request to get all Trace by status");
        return traceRepository.findAllByStatus(status, pageable);
    }
}

package com.activedge.usermgt.service;

import com.activedge.usermgt.controller.util.ExcelGenerator;
import com.activedge.usermgt.model.CustomHttpTrace;
import com.activedge.usermgt.repository.TraceRepository;
import io.vavr.Function1;
import io.vavr.Function4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Set;

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
    public Page<CustomHttpTrace> findAllByStatusAndDateRange(Integer status, Date from, Date to, Pageable pageable) {
        return traceRepository.findAllByStatusAndTimestampBetween(status, from, to, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomHttpTrace> findAllByStatus(Integer status, Pageable pageable) {
        log.debug("Request to get all Trace by status");
        return traceRepository.findAllByStatus(status, pageable);
    }

    public Function4<Integer, Date, Date, Pageable, Page<CustomHttpTrace>> fetchAuditLogs = this::getAuditLogs;

    private Page<CustomHttpTrace> getAuditLogs(Integer status, Date from, Date to, Pageable pageable) {
        System.out.printf("page size %s from %s to %s ",pageable.getPageSize(), from, to).println();
        if(status != null) {
            Page<CustomHttpTrace> test = traceRepository.findAllByStatusAndTimestampBetween(status, from, to, pageable);
            System.out.println("test size "+test.getTotalElements());
            return test;
        }
        return traceRepository.findAllByTimestampBetween(from, to, pageable);
    }
    public ByteArrayInputStream getAudit(Integer status, Date from, Date to) throws IOException {
        Function1<Pageable, Page<CustomHttpTrace>> partialFunction = fetchAuditLogs.apply(status, from, to);
       return ExcelGenerator.generateAuditLogs(partialFunction);
    }
}

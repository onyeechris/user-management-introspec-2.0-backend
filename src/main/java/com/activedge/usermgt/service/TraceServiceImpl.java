package com.activedge.usermgt.service;

import com.activedge.usermgt.model.CustomHttpTrace;
import com.activedge.usermgt.repository.TraceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
    @Override
    public Optional<CustomHttpTrace> searchAuditLogsByUsernameOrDate(String username, String dateStr) {
        log.debug("Request to search username or date");
        Date date = parseDate(dateStr); // Call the parseDate method
        Stream<CustomHttpTrace> auditLogs =
                traceRepository.findByUsernameOrTimestampGreaterThanEqualOrderByTimestampDesc(username, date);
        return auditLogs.findFirst();
    }
    private Date parseDate(String dateStr) {
        if (dateStr != null && !dateStr.isEmpty()) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                return dateFormat.parse(dateStr);
            } catch (ParseException e) {
            }
        }
        return null;
    }
}


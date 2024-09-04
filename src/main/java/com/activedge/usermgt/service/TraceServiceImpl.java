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

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import java.util.Optional;
import java.util.stream.Stream;
import java.io.ByteArrayInputStream;
import java.io.IOException;


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

        // Convert Date to LocalDate
        LocalDate startDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Adjust the end date to include the entire end day
        Instant startInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endInstant = endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        // Convert Instant back to Date
        Date formattedStart = Date.from(startInstant);
        Date formattedEnd = Date.from(endInstant);

        // Perform the repository query with the converted dates
        return traceRepository.findAllByTimestampBetween(formattedStart, formattedEnd, pageable);
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
        if(status != null) {
            return traceRepository.findAllByStatusAndTimestampBetween(status, from, to, pageable);
        }
        return traceRepository.findAllByTimestampBetween(from, to, pageable);
    }
    public ByteArrayInputStream getAudit(Integer status, Date from, Date to) throws IOException {
        Function1<Pageable, Page<CustomHttpTrace>> partialFunction = fetchAuditLogs.apply(status, from, to);
       return ExcelGenerator.generateAuditLogs(partialFunction);
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


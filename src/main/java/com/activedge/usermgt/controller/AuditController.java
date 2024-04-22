package com.activedge.usermgt.controller;

import com.activedge.usermgt.controller.util.ExcelGenerator;
import com.activedge.usermgt.controller.util.ResponseWrapper;
import com.activedge.usermgt.model.CustomHttpTrace;
import com.activedge.usermgt.model.dto.CustomHttpTraceDTO;
import com.activedge.usermgt.service.TraceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CustomHttpTrace.
 */
@Slf4j
@RestController
@RequestMapping(AuditController.AUDIT_CONTROLLER)
@RequiredArgsConstructor
public class AuditController {

    static final String AUDIT_CONTROLLER = "audit";
    static final String FILENAME = "AuditLogs";
    static final String AUDIT_CONTROLLER_DOWNLOAD = "download";
    private static final String THREE_DAYS_AGO = "#{new java.util.Date((new java.util.Date()).getTime()-3*24*60*60*1000)}";
    private static final String NOW = "#{new java.util.Date()}";
    private static final int DEFAULT_PAGE_SIZE = 100;
    private static final int DEFAULT_DOWNLOAD_PAGE_SIZE = 10000;

    private final TraceService traceService;

    /**
     * GET all Http trace logs (by date range).
     *
     * @param pageable optional pagination configuration
     * @return the ResponseEntity of http traces
     */
    @GetMapping
    public ResponseEntity<ResponseWrapper> getAllCustomHttpTracesByDateRange(
            @RequestParam(required = false, defaultValue = THREE_DAYS_AGO) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam(required = false, defaultValue = NOW) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
            @PageableDefault(size = DEFAULT_PAGE_SIZE)
            @SortDefault.SortDefaults({@SortDefault(sort = "timestamp", direction = Sort.Direction.DESC)}) Pageable pageable) {
        return new ResponseEntity<>(new ResponseWrapper(traceService.findAll(from, to, pageable)), HttpStatus.OK);
    }

    /**
     * GET all Http trace logs (by date range).
     *
     * @param pageable optional pagination configuration
     * @return the ResponseEntity of http traces
     */
    @GetMapping(AUDIT_CONTROLLER_DOWNLOAD)
    public ResponseEntity<InputStreamResource> getAllCustomHttpTracesByDateRangeExcel(
            @RequestParam(required = false, defaultValue = THREE_DAYS_AGO) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam(required = false, defaultValue = NOW) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
            @PageableDefault(size = DEFAULT_DOWNLOAD_PAGE_SIZE)
            @SortDefault.SortDefaults({@SortDefault(sort = "timestamp", direction = Sort.Direction.DESC)}) Pageable pageable) throws IOException {

        ByteArrayInputStream in = ExcelGenerator.generateAuditLogs(traceService.findAll(from, to, pageable));

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        String currentDateTime = dateFormatter.format(new Date());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/octet-stream");
        headers.add("Content-Disposition", "attachment; filename=" + FILENAME + currentDateTime + ".xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));

    }

    /**
     * GET all Http trace logs by status.
     *
     * @param status   the http trace status to retrieve
     * @param pageable optional pagination configuration
     * @return the ResponseEntity of http traces
     */
    @GetMapping("/{status}")
    public ResponseEntity<ResponseWrapper> getAllCustomHttpTracesByStatus(@PathVariable Integer status, @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        log.debug("REST request to get a page of CustomHttpTrace for status: {}", status);

        // Set up sorting by "timestamp" in descending order for LIFO.
        pageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "timestamp")
        );

        return new ResponseEntity<>(new ResponseWrapper(traceService.findAllByStatus(status, pageable)), HttpStatus.OK);
    }


    @GetMapping("/username-date")
    public ResponseEntity<?> searchAuditLogsByUsernameOrDate(
            @RequestParam(name = "username") Optional<String> username,
            @RequestParam(name = "date") Optional<String> dateStr) {
        if (!username.isPresent() && !dateStr.isPresent()) {
            return new ResponseEntity<>("Username or date must be provided", HttpStatus.BAD_REQUEST);
        }
        Optional<CustomHttpTrace> auditLog = traceService.searchAuditLogsByUsernameOrDate(
                username.orElse(null),
                dateStr.orElse(null));
        return auditLog.map(log -> {
            CustomHttpTraceDTO logDto = new CustomHttpTraceDTO();
            logDto.setTimestamp(log.getTimestamp());logDto.setSeverity(log.getSeverity());logDto.setUsername(log.getUsername());
            logDto.setStatus(log.getStatus());logDto.setSourceIp(log.getSourceIp());
            logDto.setPath(log.getPath());logDto.setQueryParams(log.getQueryParams());
            logDto.setMethod(log.getMethod());logDto.setTimeTaken(log.getTimeTaken());logDto.setPayload(log.getPayload());
            return new ResponseEntity<>(logDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}

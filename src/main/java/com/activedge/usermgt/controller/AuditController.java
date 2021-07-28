package com.activedge.usermgt.controller;

import com.activedge.usermgt.controller.util.ResponseWrapper;
import com.activedge.usermgt.service.TraceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * REST controller for managing CustomHttpTrace.
 */
@Slf4j
@RestController
@RequestMapping(AuditController.AUDIT_CONTROLLER)
@RequiredArgsConstructor
public class AuditController {

    static final String AUDIT_CONTROLLER = "audit";
    private static final String THREE_DAYS_AGO = "#{new java.util.Date((new java.util.Date()).getTime()-3*24*60*60*1000)}";
    private static final String NOW = "#{new java.util.Date()}";
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    private final TraceService traceService;

    /**
     * GET all Http trace logs (by date range).
     *
     * @param pageable optional pagination configuration
     * @return the ResponseEntity of http traces
     */
    @GetMapping
    public ResponseEntity<ResponseWrapper> getAllCustomHttpTracesByDateRange(
            @PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE)
            @RequestParam(required = false, defaultValue = THREE_DAYS_AGO) @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
            @RequestParam(required = false, defaultValue = NOW) @DateTimeFormat(pattern="yyyy-MM-dd") Date to,
            @SortDefault.SortDefaults({@SortDefault(sort = "timestamp", direction = Sort.Direction.DESC)}) Pageable pageable) {

        return new ResponseEntity<>(new ResponseWrapper(traceService.findAll(from, to, pageable)), HttpStatus.OK);
    }

    /**
     * GET all Http trace logs by status.
     *
     * @param status the http trace status to retrieve
     * @param pageable optional pagination configuration
     * @return the ResponseEntity of http traces
     */
    @GetMapping("/{status}")
    public ResponseEntity<ResponseWrapper> getAllCustomHttpTracesByStatus(@PathVariable Integer status, Pageable pageable) {
        log.debug("REST request to get a page of CustomHttpTrace for status: {}", status);
        return new ResponseEntity<>(new ResponseWrapper(traceService.findAllByStatus(status, pageable)), HttpStatus.OK);
    }

}

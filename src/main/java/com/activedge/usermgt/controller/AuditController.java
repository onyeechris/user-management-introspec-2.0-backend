package com.activedge.usermgt.controller;

import com.activedge.usermgt.controller.util.ResponseWrapper;
import com.activedge.usermgt.service.TraceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing CustomHttpTrace.
 */
@Slf4j
@RestController
@RequestMapping("audit")
@RequiredArgsConstructor
public class AuditController {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    private final TraceService traceService;

//    public AuditController(TraceService traceService) {
//        this.traceService = traceService;
//    }

    /**
     * GET all Http trace logs.
     * <p>
     * @param pageable optional pagination configuration
     * @return the ResponseEntity of http traces
     */
    @GetMapping
    public ResponseEntity<ResponseWrapper> getAllCustomHttpTraces(@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) @SortDefault.SortDefaults({@SortDefault(sort = "timestamp", direction = Sort.Direction.DESC)}) Pageable pageable) {
        System.out.println("in....");
        return new ResponseEntity<>(new ResponseWrapper(traceService.findAll(pageable)), HttpStatus.OK);
    }

    /**
     * GET all Http trace logs by status.
     * <p>
     * @param status the http trace status to retrieve
     * @param pageable optional pagination configuration
     * @return the ResponseEntity of http traces
     */
    @GetMapping("/{status}")
    public ResponseEntity<ResponseWrapper> getAllCustomHttpTracesByStatus(@PathVariable Integer status, Pageable pageable) {
        log.debug("REST request to get a page of CustomHttpTrace for status: {}", status);
        System.out.println("in.......");
        return new ResponseEntity<>(new ResponseWrapper(traceService.findAllByStatus(status, pageable)), HttpStatus.OK);
    }

}

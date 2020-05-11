package com.activedge.usermgt.controller;

import com.activedge.usermgt.controller.util.ResponseWrapper;
import com.activedge.usermgt.model.CustomHttpTrace;
import com.activedge.usermgt.service.TraceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * REST controller for managing CustomHttpTrace.
 */
@RestController
@Api(value="audit", description="Audit Logs")
public class AuditController {

    private final Logger log = LoggerFactory.getLogger(AuditController.class);

    private static final String ENTITY_NAME = "audit";

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    private final TraceService traceService;

    public AuditController(TraceService traceService) {
        this.traceService = traceService;
    }

    /**
     * GET  /traces : get all the traces.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of traces in body
     */
    @GetMapping("/"+ENTITY_NAME)
    @ApiOperation(value = "Get all existing "+ENTITY_NAME)
    public ResponseEntity<ResponseWrapper> getAllCustomHttpTraces(
            @RequestParam(value = "app", defaultValue="all") String app,
            @ApiIgnore
            @PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "timestamp", direction = Sort.Direction.DESC)
            }) Pageable pageable,
            @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of CustomHttpTrace for app: {}", app);
        Page<CustomHttpTrace> page;

        page = traceService.findAll(pageable);

        return new ResponseEntity<>(new ResponseWrapper(page), HttpStatus.OK);
    }

    /**
     * GET  /traces : get all the traces by status.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of traces in body
     */
    @GetMapping("/"+ENTITY_NAME+"/{status}")
    @ApiOperation(value = "By status, get all existing "+ENTITY_NAME)
    public ResponseEntity<ResponseWrapper> getAllCustomHttpTracesByStatus(@PathVariable Integer status, @ApiIgnore Pageable pageable) {
        log.debug("REST request to get a page of CustomHttpTrace for status: {}", status);
        Page<CustomHttpTrace> page;

        page = traceService.findAllByStatus(status, pageable);

        return new ResponseEntity<>(new ResponseWrapper(page), HttpStatus.OK);
    }

}

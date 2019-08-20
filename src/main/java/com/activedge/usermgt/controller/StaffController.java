package com.activedge.usermgt.controller;

import com.activedge.usermgt.controller.util.HeaderUtil;
import com.activedge.usermgt.controller.util.PaginationUtil;
import com.activedge.usermgt.controller.util.ResponseWrapper;
import com.activedge.usermgt.model.Module;
import com.activedge.usermgt.model.dto.NewStaffDTO;
import com.activedge.usermgt.model.dto.StaffDTO;
import com.activedge.usermgt.model.log.MakerItem;
import com.activedge.usermgt.repository.ModuleRepository;
import com.activedge.usermgt.repository.redis.MakerItemRepository;
import com.activedge.usermgt.security.SecurityUtils;
import com.activedge.usermgt.service.LdapUserService;
import com.activedge.usermgt.service.ModuleService;
import com.activedge.usermgt.service.StaffService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Staff.
 */
@RestController
@RequestMapping("/")
@Api(value="staff", description="Operations pertaining to bank's staff")
public class StaffController {

    private final Logger log = LoggerFactory.getLogger(StaffController.class);

    private static final String ENTITY_NAME = "staffs";

    private final StaffService staffService;
    private final LdapUserService ldapUserService;
    private final ModuleRepository moduleRepository;


    public StaffController(StaffService staffService, LdapUserService ldapUserService, ModuleRepository moduleRepository) {
        this.staffService = staffService;
        this.ldapUserService = ldapUserService;
        this.moduleRepository = moduleRepository;
    }

    /**
     * POST  /staff : Create a new staff.
     *
     * @param staffDTO the staffDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new staffDTO, or with status 400 (Bad Request) if the staff has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping(value = "/"+ENTITY_NAME, produces = "application/json")
    @ApiOperation(value = "Create a new "+ENTITY_NAME)
    public ResponseEntity<StaffDTO> createStaff(@Valid @RequestBody NewStaffDTO staffDTO, Errors errors) throws Exception {
        log.info("---REST request to save a {} : {}, token: {}", ENTITY_NAME, staffDTO, SecurityUtils.getCurrentUserLogin());

        if (errors.hasErrors()) {
            log.error("Error in creating new user detected...\n{}", errors.getAllErrors());
            throw new ValidationException(errors.getAllErrors().stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(", ")));
        }

        staffDTO.setId(null);
        StaffDTO result = staffService.save(staffDTO);

        return ResponseEntity.created(new URI("/api/"+ENTITY_NAME+"/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /staff : Updates an existing staff.
     *
     * @param staffDTO the staffDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated staffDTO,
     * or with status 400 (Bad Request) if the staffDTO is not valid,
     * or with status 500 (Internal Server Error) if the staffDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/"+ENTITY_NAME)
    @ApiOperation(value = "Update an existing "+ENTITY_NAME)
    public ResponseEntity<StaffDTO> updateStaff(@Valid @RequestBody StaffDTO staffDTO, Errors errors) throws Exception {
        log.debug("REST request to update {} : {}", ENTITY_NAME, staffDTO);

        if (errors.hasErrors() || staffDTO.getId() == null) {
            log.error("Error in creating new user detected...\n{}", errors.getAllErrors());
            throw new ValidationException(errors.getAllErrors().stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
        }

        StaffDTO result = staffService.save(staffDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, staffDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /staff : get all the staff.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of staff in body
     */
    @GetMapping("/"+ENTITY_NAME)
    @ApiOperation(value = "Get all existing "+ENTITY_NAME)
    public ResponseEntity<ResponseWrapper> getAllStaff(@RequestHeader(value = "Module", required = true) String mdl, Pageable pageable) throws ServletRequestBindingException {
        log.debug("REST request to get a page of "+ENTITY_NAME);

        Page<StaffDTO> page = null;

        Optional<Module> module = this.moduleRepository.findById(mdl);

        if(!module.isPresent()) {
//            page = staffService.findAllBy(module, pageable);
        } else {
            page = staffService.findAll(pageable);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/"+ENTITY_NAME);

        return new ResponseEntity<>(new ResponseWrapper(page), headers, HttpStatus.OK);
    }

    /**
     * GET  /staff/:id : get the "id" staff.
     *
     * @param id the id of the staffDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the staffDTO, or with status 404 (Not Found)
     */
    @GetMapping("/"+ENTITY_NAME+"/{id}")
    @ApiOperation(value = "Get a single "+ENTITY_NAME+" based on their id")
    public ResponseEntity<StaffDTO> getStaff(@PathVariable Long id) throws Exception {
        log.debug("REST request to get {} : {}", ENTITY_NAME, id);
        Optional<StaffDTO> staffDTO = staffService.findOne(id);

        if (!staffDTO.isPresent()) {
            throw new ValidationException("No "+ENTITY_NAME+" was found for id " + id);
        }

        HttpHeaders headers = HeaderUtil.createAlert("retrieve", "/api/"+ENTITY_NAME+"/" + id);

        return new ResponseEntity<>(staffDTO.get(), headers, HttpStatus.OK);

    }

    /**
     * DELETE  /staff/:id : delete the "id" staff.
     *
     * @param id the id of the staffDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/"+ENTITY_NAME+"/{id}")
    @ApiOperation(value = "Delete a single "+ENTITY_NAME)
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        log.debug("REST request to delete {} : {}", ENTITY_NAME, id);
        staffService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}

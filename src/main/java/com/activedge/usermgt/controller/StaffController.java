package com.activedge.usermgt.controller;

import com.activedge.usermgt.controller.util.HeaderUtil;
import com.activedge.usermgt.controller.util.ResponseWrapper;
import com.activedge.usermgt.model.Module;
import com.activedge.usermgt.model.dto.NewStaffDTO;
import com.activedge.usermgt.model.dto.StaffDTO;
import com.activedge.usermgt.repository.ModuleRepository;
import com.activedge.usermgt.security.SecurityUtils;
import com.activedge.usermgt.service.StaffService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.net.URI;
import java.net.URISyntaxException;
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

    @Autowired
    @Qualifier("db")
    private StaffService staffService;

    @Autowired
    @Qualifier("ldap")
    private StaffService ldapService;

    private final Environment env;

    private final ApplicationContext appCtx;

    private final ModuleRepository moduleRepository;

    public StaffController(ModuleRepository moduleRepository, Environment env, ApplicationContext appCtx) {
        this.moduleRepository = moduleRepository;
        this.env = env;
        this.appCtx = appCtx;
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
    public ResponseEntity<StaffDTO> createStaff(@Valid @RequestBody NewStaffDTO staffDTO, @ApiIgnore Errors errors) throws Exception {
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
    public ResponseEntity<StaffDTO> updateStaff(@Valid @RequestBody StaffDTO staffDTO, @ApiIgnore Errors errors) throws Exception {
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
    public ResponseEntity<ResponseWrapper> getAllStaff(@RequestHeader(value = "Module", required = true) String mdl, @ApiIgnore Pageable pageable) throws ServletRequestBindingException {
        log.debug("REST request to get a page of "+ENTITY_NAME);

        Page<StaffDTO> page = null;

        Optional<Module> module = this.moduleRepository.findById(mdl);
        System.out.println(module);
        if(!module.isPresent()) {
//            page = staffService.findAllBy(module, pageable);
        } else {
            page = staffService.findAll(pageable);
        }

        return new ResponseEntity<>(new ResponseWrapper(page), HttpStatus.OK);
    }

    /**
     * GET  /staff/:id : get the "id" staff.
     *
     * @param id the id of the staffDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the staffDTO, or with status 404 (Not Found)
     */
    @GetMapping("/"+ENTITY_NAME+"/{id}")
    @ApiOperation(value = "Get a single "+ENTITY_NAME+" based on their id")
    public ResponseEntity<StaffDTO> getStaff(@PathVariable String id) throws Exception {
        log.debug("REST request to get {} : {}", ENTITY_NAME, id);
        Optional<StaffDTO> staffDTO = staffService.findOne(id);

        if (!staffDTO.isPresent()) {
            throw new ValidationException("No "+ENTITY_NAME+" was found for id " + id);
        }

        return new ResponseEntity<>(staffDTO.get(), HttpStatus.OK);

    }

    /**
     * GET  /staff/import/:username : import staff with username from LDAP.
     *
     * @param username the username of the staffDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the staffDTO, or with status 404 (Not Found)
     */
    @GetMapping("/"+ENTITY_NAME+"/import/{username}")
    @ApiOperation(value = "Import a staff from LDAP using their username")
    public ResponseEntity<StaffDTO> importStaff(@PathVariable String username) throws Exception {
        log.debug("REST request to import {} : {}", ENTITY_NAME, username);

        StaffService service = appCtx.getBean(env.getProperty("introspecsso.backend"), StaffService.class);

        Optional<StaffDTO> staffDTO = ldapService.search(username);

        if (!staffDTO.isPresent()) {
            throw new ValidationException("No "+ENTITY_NAME+" was found for username " + username);
        }

        return new ResponseEntity<>(staffDTO.get(), HttpStatus.OK);

    }

    /**
     * DELETE  /staff/:id : delete the "id" staff.
     *
     * @param id the id of the staffDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/"+ENTITY_NAME+"/{id}")
    @ApiOperation(value = "Delete a single "+ENTITY_NAME)
    public ResponseEntity<Void> deleteStaff(@PathVariable String id) {
        log.debug("REST request to delete {} : {}", ENTITY_NAME, id);
        staffService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}

package com.activedge.usermgt.controller;

import com.activedge.usermgt.controller.util.HeaderUtil;
import com.activedge.usermgt.controller.util.ResponseWrapper;
import com.activedge.usermgt.model.dto.StaffModuleDTO;
import com.activedge.usermgt.repository.ModuleRepository;
import com.activedge.usermgt.service.StaffModuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing StaffModule.
 * Access UserApp controller for assigning staffs/users to Introspec Apps
 */
@RestController
@RequestMapping("/")
public class UserAppController {

    private final Logger log = LoggerFactory.getLogger(UserAppController.class);

    private static final String ENTITY_NAME = "userapps";

    private final StaffModuleService staffModuleService;
    private final ModuleRepository moduleRepository;

    public UserAppController(StaffModuleService staffModuleService, ModuleRepository moduleRepository) {
        this.staffModuleService = staffModuleService;
        this.moduleRepository = moduleRepository;
    }

    /**
     * POST  /staffModules : Create a new staffModule.
     *
     * @param staffModuleDTO the staffModuleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new staffModuleDTO, or with status 400 (Bad Request) if the staffModule has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/"+ENTITY_NAME)
    public ResponseEntity<StaffModuleDTO> createStaffModule(@RequestHeader(value = "Module", required = true) String mdl, @Valid @RequestBody StaffModuleDTO staffModuleDTO, Errors errors) throws URISyntaxException, ServletRequestBindingException {
        log.debug("REST request to save {} : {}", ENTITY_NAME, staffModuleDTO);

        if (errors.hasErrors()) {
            log.error("Error in creating new staffModule detected...\n{}", errors.getAllErrors());
            throw new ValidationException(errors.getAllErrors().stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(", ")));
        }

        staffModuleDTO.setId(null);
        staffModuleDTO.setModule(mdl);

        StaffModuleDTO result = staffModuleService.save(staffModuleDTO);

        return ResponseEntity.created(new URI("/auth-service/"+ENTITY_NAME+"/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /staffModules : Updates an existing staffModule.
     *
     * @param staffModuleDTO the staffModuleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated staffModuleDTO,
     * or with status 400 (Bad Request) if the staffModuleDTO is not valid,
     * or with status 500 (Internal Server Error) if the staffModuleDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/"+ENTITY_NAME)
    public ResponseEntity<StaffModuleDTO> updateStaffModule(@RequestHeader(value = "Module", required = true) String module, @Valid @RequestBody StaffModuleDTO staffModuleDTO, Errors errors) throws URISyntaxException, ServletRequestBindingException {
        log.debug("REST request to update StaffModule : {}", staffModuleDTO);

        if (errors.hasErrors() || staffModuleDTO.getId() == null) {
            log.error("Error in creating new user detected...\n{}", errors.getAllErrors());
            throw new ValidationException(errors.getAllErrors().stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
        }

        staffModuleDTO.setModule(module);

        StaffModuleDTO result = staffModuleService.save(staffModuleDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, staffModuleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /staffModules : get all the staffModules.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of staffModules in body
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @GetMapping("/"+ENTITY_NAME)
    public ResponseEntity<ResponseWrapper> getAllStaffModules(
            @RequestHeader(value = "Module", required = true) String module,
            @RequestHeader(value = "Authorization", required = true) String authUser,
            Pageable pageable) {

        return new ResponseEntity<>(new ResponseWrapper(staffModuleService.findAllByModule(module, pageable)), HttpStatus.OK);
    }

    /**
     * GET  /staffModules/:id : get the "id" staffModule.
     *
     * @param id the id of the staffModuleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the staffModuleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/"+ENTITY_NAME+"/{id}")
    public ResponseEntity<StaffModuleDTO> getStaffModule(@PathVariable String id) {
        log.debug("REST request to get StaffModule : {}", id);
        Optional<StaffModuleDTO> staffModuleDTO = staffModuleService.findOne(id);

        if (!staffModuleDTO.isPresent()) {
            throw new ValidationException("No "+ENTITY_NAME+" was found for id " + id);
        }

        return new ResponseEntity<>(staffModuleDTO.get(), HttpStatus.OK);
    }

    /**
     * DELETE  /staffModules/:id : delete the "id" staffModule.
     *
     * @param id the id of the staffModuleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/"+ENTITY_NAME+"/{id}")
    public ResponseEntity<Void> deleteStaffModule(@PathVariable String id) {
        log.debug("REST request to delete StaffModule : {}", id);
        staffModuleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

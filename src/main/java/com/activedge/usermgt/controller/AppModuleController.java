package com.activedge.usermgt.controller;

import com.activedge.usermgt.controller.util.HeaderUtil;
import com.activedge.usermgt.controller.util.PaginationUtil;
import com.activedge.usermgt.controller.util.ResponseWrapper;
import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.dto.ModuleDTO;
import com.activedge.usermgt.service.ModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Module.
 */
@RestController
//@RequestMapping("/")
@Api(value="appmodule", description="Various App Module")
public class AppModuleController {

    private final Logger log = LoggerFactory.getLogger(AppModuleController.class);

    private static final String ENTITY_NAME = "appmodule";

    private final ModuleService moduleService;

    public AppModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    /**
     * POST  /modules : Create a new modules.
     *
     * @param moduleDTO the moduleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new moduleDTO, or with status 400 (Bad Request) if the modules has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/"+ENTITY_NAME)
    @ApiOperation(value = "Create a new "+ENTITY_NAME)
    public ResponseEntity<ModuleDTO> createModules(@Valid @RequestBody ModuleDTO moduleDTO, @ApiIgnore Errors errors) throws URISyntaxException, NotFoundException, ActivityRequiredException {
        log.debug("REST request to save {} : {}", ENTITY_NAME, moduleDTO);

        if (errors.hasErrors() || moduleDTO.getId() == null) {
            log.error("Error in creating new {} detected...\n{}", ENTITY_NAME, errors.getAllErrors());
            throw new ValidationException(errors.getAllErrors().stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(", ")));
        }

        ModuleDTO result = moduleService.save(moduleDTO);

        return ResponseEntity.created(new URI("/auth-service/"+ENTITY_NAME+"/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /modules : Updates an existing modules.
     *
     * @param moduleDTO the moduleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated moduleDTO,
     * or with status 400 (Bad Request) if the moduleDTO is not valid,
     * or with status 500 (Internal Server Error) if the moduleDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/"+ENTITY_NAME)
    @ApiOperation(value = "Update an existing "+ENTITY_NAME)
    public ResponseEntity<ModuleDTO> updateModules(@Valid @RequestBody ModuleDTO moduleDTO, @ApiIgnore Errors errors) throws URISyntaxException, NotFoundException, ActivityRequiredException {
        log.debug("REST request to update {} : {}", ENTITY_NAME, moduleDTO);

        if (errors.hasErrors() || moduleDTO.getId() == null) {
            log.error("Error in creating new {} detected...\n{}", ENTITY_NAME, errors.getAllErrors());
            throw new ValidationException(errors.getAllErrors().stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
        }

        ModuleDTO result = moduleService.save(moduleDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /modules : get all the modules.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of modules in body
     */
    @GetMapping("/"+ENTITY_NAME)
    @ApiOperation(value = "Get all existing "+ENTITY_NAME)
    public ResponseEntity<ResponseWrapper> getAllModules(@RequestParam(value = "app", defaultValue="all") String app, @ApiIgnore Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Module for app: {}", app);
        Page<ModuleDTO> page;

        page = moduleService.findAll(pageable);

        return new ResponseEntity<>(new ResponseWrapper(page), HttpStatus.OK);
    }

    /**
     * GET  /modules/:id : get the "id" modules.
     *
     * @param id the id of the modulesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the modulesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/"+ENTITY_NAME+"/{id}")
    @ApiOperation(value = "Get a single "+ENTITY_NAME+" based on their id")
    public ResponseEntity<ModuleDTO> getModules(@RequestParam(value = "app", defaultValue="all") String app, @PathVariable String id) {
        log.debug("REST request to get Module :{}, App:{}", id, app);
        Optional<ModuleDTO> modulesDTO = moduleService.findOne(id);

        if (!modulesDTO.isPresent()) {
            throw new ValidationException("No "+ENTITY_NAME+" was found for id " + id);
        }

        return new ResponseEntity<>(modulesDTO.get(), HttpStatus.OK);
    }

    /**
     * DELETE  /modules/:id : delete the "id" modules.
     *
     * @param id the id of the modulesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/"+ENTITY_NAME+"/{id}")
    @ApiOperation(value = "Delete a single "+ENTITY_NAME)
    public ResponseEntity<Void> deleteModules(@PathVariable String id) {
        log.debug("REST request to delete GROUP : {}", id);
        moduleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}

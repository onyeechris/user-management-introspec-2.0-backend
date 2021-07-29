package com.activedge.usermgt.controller;

import com.activedge.usermgt.controller.util.HeaderUtil;
import com.activedge.usermgt.controller.util.ResponseWrapper;
import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.dto.ModuleDTO;
import com.activedge.usermgt.service.ModuleService;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

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
public class AppModuleController {

    private final Logger log = LoggerFactory.getLogger(AppModuleController.class);

    private static final String ENTITY_NAME = "appmodule";

    private final ModuleService moduleService;

    public AppModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    /**
     * This is an endpoint to create a new application module.
     *
     * @param moduleDTO the moduleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new moduleDTO, or with status 400 (Bad Request) if the modules has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/"+ENTITY_NAME)
    public ResponseEntity<ModuleDTO> createModules(@Valid @RequestBody ModuleDTO moduleDTO, Errors errors) throws URISyntaxException, NotFoundException, ActivityRequiredException {
        log.debug("REST request to save {} : {}", ENTITY_NAME, moduleDTO);

        if (errors.hasErrors() || moduleDTO.getId() == null) {
            log.error("Error in creating new {} detected...\n{}", ENTITY_NAME, errors.getAllErrors());
            throw new ValidationException(errors.getAllErrors().stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(", ")));
        }

        ModuleDTO result = moduleService.save(moduleDTO);

        System.out.println(result);

        return ResponseEntity.created(new URI("/auth-service/"+ENTITY_NAME+"/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * Update an existing module.
     *
     * @param moduleDTO the moduleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated moduleDTO,
     * or with status 400 (Bad Request) if the moduleDTO is not valid,
     * or with status 500 (Internal Server Error) if the moduleDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/"+ENTITY_NAME)
    public ResponseEntity<ModuleDTO> updateModules(@Valid @RequestBody ModuleDTO moduleDTO, Errors errors) throws URISyntaxException, NotFoundException, ActivityRequiredException {
        log.debug("REST request to update {} : {}", ENTITY_NAME, moduleDTO);

        if (errors.hasErrors() || moduleDTO.getId() == null) {
            log.error("Error in creating new {} detected...\n{}", ENTITY_NAME, errors.getAllErrors());
            throw new ValidationException(errors.getAllErrors().stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
        }

        ModuleDTO result = moduleService.save(moduleDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * Get all registered modules.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of modules in body
     */
    @GetMapping("/"+ENTITY_NAME)
    public ResponseEntity<ResponseWrapper> getAllModules(Pageable pageable) {
        log.debug("REST request to get all appModules");

        Page<ModuleDTO> page;

        page = moduleService.findAll(pageable);

        return new ResponseEntity<>(new ResponseWrapper(page), HttpStatus.OK);
    }

    /**
     * GET a single module by its "id".
     *
     * @param id the id of the modulesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the modulesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/"+ENTITY_NAME+"/{id}")
    public ResponseEntity<ModuleDTO> getModules(@RequestParam(value = "app", defaultValue="all") String app, @PathVariable String id) {
        log.debug("REST request to get Module :{}, App:{}", id, app);
        Optional<ModuleDTO> modulesDTO = moduleService.findOne(id);

        if (!modulesDTO.isPresent()) {
            throw new ValidationException("No "+ENTITY_NAME+" was found for id " + id);
        }

        return new ResponseEntity<>(modulesDTO.get(), HttpStatus.OK);
    }

    /**
     * Delete a single module by its id.
     *
     * @param id the id of the modulesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/"+ENTITY_NAME+"/{id}")
    public ResponseEntity<Void> deleteModules(@PathVariable String id) {
        log.debug("REST request to delete GROUP : {}", id);
        moduleService.delete(id);
//        return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}

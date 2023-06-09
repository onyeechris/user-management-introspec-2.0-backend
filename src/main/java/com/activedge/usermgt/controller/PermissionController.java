package com.activedge.usermgt.controller;

import com.activedge.usermgt.controller.util.HeaderUtil;
import com.activedge.usermgt.controller.util.ResponseWrapper;
import com.activedge.usermgt.model.dto.PermissionDTO;
import com.activedge.usermgt.repository.AuthorityRepository;
import com.activedge.usermgt.service.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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
 * REST controller for managing Permission.
 */
@RestController
@RequestMapping("/")
public class PermissionController {

    private final Logger log = LoggerFactory.getLogger(PermissionController.class);

    private static final String ENTITY_NAME = "permissions";

    private final PermissionService permissionService;

    private final AuthorityRepository authorityRepository;

    public PermissionController(PermissionService permissionService, AuthorityRepository authorityRepository) {
        this.permissionService = permissionService;
        this.authorityRepository = authorityRepository;
    }

    /**
     * POST  /permissions : Create a new permission.
     *
     * @param permissionDTO the permissionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new permissionDTO, or with status 400 (Bad Request) if the permission has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/"+ENTITY_NAME)
    public ResponseEntity<PermissionDTO> createPermission(@RequestHeader(value = "Module", required = true) String module, @Valid @RequestBody PermissionDTO permissionDTO, Errors errors) throws URISyntaxException, ServletRequestBindingException {
        log.debug("REST request to save {} : {}", ENTITY_NAME, permissionDTO);

        if (errors.hasErrors()) {
            log.error("Error in creating new permission detected...\n{}", errors.getAllErrors());
            throw new ValidationException(errors.getAllErrors().stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(", ")));
        }

        permissionDTO.setId(null);
        permissionDTO.setModul(module);

        PermissionDTO result = permissionService.save(permissionDTO);

        return ResponseEntity.created(new URI("/api/"+ENTITY_NAME+"/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /permissions : Updates an existing permission.
     *
     * @param permissionDTO the permissionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated permissionDTO,
     * or with status 400 (Bad Request) if the permissionDTO is not valid,
     * or with status 500 (Internal Server Error) if the permissionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/"+ENTITY_NAME)
    public ResponseEntity<PermissionDTO> updatePermission(@RequestHeader(value = "Module", required = true) String module, @Valid @RequestBody PermissionDTO permissionDTO, Errors errors) throws URISyntaxException, ServletRequestBindingException {
        log.debug("REST request to update Permission : {}", permissionDTO);

        if (errors.hasErrors() || permissionDTO.getId() == null) {
            log.error("Error in creating new user detected...\n{}", errors.getAllErrors());
            throw new ValidationException(errors.getAllErrors().stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
        }

        PermissionDTO result = permissionService.save(permissionDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, permissionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /permissions : get all the permissions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of permissions in body
     */
    @GetMapping("/"+ENTITY_NAME)
    public ResponseEntity<ResponseWrapper> getAllPermissions(@RequestHeader(value = "Module", required = true) String module, Pageable pageable) {
        log.info("REST request to get a page of Permissions on module {}", module);

        Page<PermissionDTO> page = permissionService.findAll(module, pageable);

        return new ResponseEntity<>(new ResponseWrapper(page), HttpStatus.OK);
    }

    /**
     * GET  /permissions/:id : get the "id" permission.
     *
     * @param id the id of the permissionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the permissionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/"+ENTITY_NAME+"/{id}")
    public ResponseEntity<PermissionDTO> getPermission(@PathVariable String id) {
        log.debug("REST request to get Permission : {}", id);
        Optional<PermissionDTO> permissionDTO = permissionService.findOne(id);

        if (!permissionDTO.isPresent()) {
            throw new ValidationException("No "+ENTITY_NAME+" was found for id " + id);
        }

        return new ResponseEntity<>(permissionDTO.get(), HttpStatus.OK);
    }

    /**
     * DELETE  /permissions/:id : delete the "id" permission.
     *
     * @param id the id of the permissionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/"+ENTITY_NAME+"/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable String id) {
        log.debug("REST request to delete Permission : {}", id);
        permissionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

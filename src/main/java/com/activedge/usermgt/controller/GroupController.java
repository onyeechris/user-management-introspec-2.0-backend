package com.activedge.usermgt.controller;

import com.activedge.usermgt.controller.util.BaseEntity;
import com.activedge.usermgt.controller.util.HeaderUtil;
import com.activedge.usermgt.controller.util.PaginationUtil;
import com.activedge.usermgt.controller.util.ResponseWrapper;
import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.GroupPK;
import com.activedge.usermgt.model.Module;
import com.activedge.usermgt.model.dto.GroupDTO;
import com.activedge.usermgt.repository.ModuleRepository;
import com.activedge.usermgt.service.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST controller for managing Group.
 */
@RestController
@RequestMapping("/")
@Api(value="group", description="Staff permission group. A staff inherits ALL permissions assigned to the group.")
public class GroupController extends BaseEntity {

    private final Logger log = LoggerFactory.getLogger(GroupController.class);

    private static final String ENTITY_NAME = "groups";

    private final GroupService groupService;

    private ModuleRepository moduleRepository;

    public GroupController(GroupService groupService, ModuleRepository moduleRepository) {
        super(moduleRepository);
        this.groupService = groupService;
    }

    /**
     * POST  /groups : Create a new groups.
     *
     * @param groupDTO the groupDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new groupDTO, or with status 400 (Bad Request) if the groups has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/"+ENTITY_NAME)
    @ApiOperation(value = "Create a new "+ENTITY_NAME)
    public ResponseEntity<GroupDTO> createGroups(
            HttpServletRequest request, HttpServletResponse response,
            @Valid @RequestBody GroupDTO groupDTO,
            @RequestHeader(value = "Module", required = true) String module,
            @ApiIgnore Errors errors) throws URISyntaxException, NotFoundException, ActivityRequiredException {
        log.debug("REST request to save {} : {}", ENTITY_NAME, groupDTO);

        if (errors.hasErrors()) {
            log.error("Error in creating new {} detected...\n{}", ENTITY_NAME, errors.getAllErrors());
            throw new ValidationException(errors.getAllErrors().stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(", ")));
        }

        groupDTO.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        groupDTO.setMod(module);
        GroupDTO result = groupService.save(groupDTO);

        return ResponseEntity.created(new URI("/auth-service/"+ENTITY_NAME+"/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /groups : Updates an existing groups.
     *
     * @param groupDTO the groupDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated groupDTO,
     * or with status 400 (Bad Request) if the groupDTO is not valid,
     * or with status 500 (Internal Server Error) if the groupDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/"+ENTITY_NAME+"/{flag:[0|1]}")
    @ApiOperation(value = "Update an existing "+ENTITY_NAME)
    public ResponseEntity<GroupDTO> updateGroups(@RequestHeader(value = "Module", required = true) String module, @Valid @RequestBody GroupDTO groupDTO, @ApiIgnore Errors errors, @ApiParam(value = "A 0|1 value to delete|add permissions to group", required = true) @PathVariable int flag) throws URISyntaxException, NotFoundException, ActivityRequiredException {
        log.debug("REST request to update {} : {}", ENTITY_NAME, groupDTO);

        if (errors.hasErrors() || groupDTO.getId() == null) {
            log.error("Error in creating new {} detected...\n{}", ENTITY_NAME, errors.getAllErrors());
            throw new ValidationException(errors.getAllErrors().stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
        }

        groupDTO.setMod(module);
        GroupDTO result = groupService.save(groupDTO, flag);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /groups : get all the groups.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of groups in body
     */
    @GetMapping("/"+ENTITY_NAME)
    @ApiOperation(value = "Get all existing "+ENTITY_NAME)
    public ResponseEntity<ResponseWrapper> getAllGroups(
            @RequestHeader(value = "Module", required = true) String module,
            @RequestParam(value = "app", defaultValue="all") String app,
            @ApiIgnore Pageable pageable,
            @RequestParam(required = false, defaultValue = "false") boolean eagerload,
            HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException {
        log.debug("REST request to get a page of Group for app: {}", app);
        Page<GroupDTO> page;

        log.debug("Module is: {}", module);

        if (eagerload) {
            page = groupService.findAllWithEagerRelationships(pageable);
        } else {
            page = groupService.findAll(module, pageable);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/groups?eagerload=%b", eagerload));

        return new ResponseEntity<>(new ResponseWrapper(page), headers, HttpStatus.OK);
    }

    /**
     * GET  /groups/:id : get the "id" groups.
     *
     * @param id the id of the groupsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the groupsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/"+ENTITY_NAME+"/{id}")
    @ApiOperation(value = "Get a single "+ENTITY_NAME+" based on their id")
    public ResponseEntity<GroupDTO> getGroups(@RequestHeader(value = "Module", required = true) String mdl, @RequestParam(value = "app", defaultValue="all") String app, @PathVariable String id) throws ServletRequestBindingException {
        log.debug("REST request to get Group :{}, App:{}", id, app);

        Module module = this.getModule(mdl);

        Optional<GroupDTO> groupsDTO = groupService.findOne(new GroupPK(module, id));

        if (!groupsDTO.isPresent()) {
            throw new ValidationException("No "+ENTITY_NAME+" was found for id " + id);
        }

        return new ResponseEntity<>(groupsDTO.get(), HttpStatus.OK);
    }

    /**
     * DELETE  /groups/:id : delete the "id" groups.
     *
     * @param id the id of the groupsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/"+ENTITY_NAME+"/{id}")
    @ApiOperation(value = "Delete a single "+ENTITY_NAME)
    public ResponseEntity<Void> deleteGroups(@RequestHeader(value = "Module", required = true) String mdl, @PathVariable String id) throws ServletRequestBindingException {
        log.debug("REST request to delete GROUP : {}", id);

        Module module = this.getModule(mdl);

        groupService.delete(new GroupPK(module, id));

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

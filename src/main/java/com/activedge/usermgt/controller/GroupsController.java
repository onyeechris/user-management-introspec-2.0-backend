package com.activedge.usermgt.controller;

import com.activedge.usermgt.controller.util.HeaderUtil;
import com.activedge.usermgt.controller.util.PaginationUtil;
import com.activedge.usermgt.model.dto.GroupsDTO;
import com.activedge.usermgt.service.GroupsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Group.
 */
@RestController
@RequestMapping("/api")
public class GroupsController {

    private final Logger log = LoggerFactory.getLogger(GroupsController.class);

    private static final String ENTITY_NAME = "groups";

    private final GroupsService groupsService;

    public GroupsController(GroupsService groupsService) {
        this.groupsService = groupsService;
    }

    /**
     * POST  /groups : Create a new groups.
     *
     * @param groupsDTO the groupsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new groupsDTO, or with status 400 (Bad Request) if the groups has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/groups")
    public ResponseEntity<GroupsDTO> createGroups(@Valid @RequestBody GroupsDTO groupsDTO) throws URISyntaxException {
        log.debug("REST request to save Group : {}", groupsDTO);
        if (groupsDTO.getId() != null) {
            throw new ValidationException("A new groups cannot already have an ID");
        }
        GroupsDTO result = groupsService.save(groupsDTO);
        return ResponseEntity.created(new URI("/api/groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /groups : Updates an existing groups.
     *
     * @param groupsDTO the groupsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated groupsDTO,
     * or with status 400 (Bad Request) if the groupsDTO is not valid,
     * or with status 500 (Internal Server Error) if the groupsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/groups")
    public ResponseEntity<GroupsDTO> updateGroups(@Valid @RequestBody GroupsDTO groupsDTO) throws URISyntaxException {
        log.debug("REST request to update Group : {}", groupsDTO);
        if (groupsDTO.getId() == null) {
            throw new ValidationException("Invalid id");
        }
        GroupsDTO result = groupsService.save(groupsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, groupsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /groups : get all the groups.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of groups in body
     */
    @GetMapping("/groups")
    public ResponseEntity<List<GroupsDTO>> getAllGroups(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Group");
        Page<GroupsDTO> page;
        if (eagerload) {
            page = groupsService.findAllWithEagerRelationships(pageable);
        } else {
            page = groupsService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/groups?eagerload=%b", eagerload));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /groups/:id : get the "id" groups.
     *
     * @param id the id of the groupsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the groupsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/groups/{id}")
    public ResponseEntity<GroupsDTO> getGroups(@PathVariable Long id) {
        log.debug("REST request to get Group : {}", id);
        Optional<GroupsDTO> groupsDTO = groupsService.findOne(id);

        if (!groupsDTO.isPresent()) {
            throw new ValidationException("No user was found for id " + id);
        }

        HttpHeaders headers = HeaderUtil.createAlert("retrieve", "/api/groups/" + id);

        return new ResponseEntity<>(groupsDTO.get(), headers, HttpStatus.OK);
    }

    /**
     * DELETE  /groups/:id : delete the "id" groups.
     *
     * @param id the id of the groupsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/groups/{id}")
    public ResponseEntity<Void> deleteGroups(@PathVariable Long id) {
        log.debug("REST request to delete Group : {}", id);
        groupsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

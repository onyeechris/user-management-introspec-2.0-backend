package com.activedge.usermgt.service;


import com.activedge.usermgt.model.dto.GroupsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Group.
 */
public interface GroupsService {

    /**
     * Save a groups.
     *
     * @param groupsDTO the entity to save
     * @return the persisted entity
     */
    GroupsDTO save(GroupsDTO groupsDTO);

    /**
     * Get all the groups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GroupsDTO> findAll(Pageable pageable);

    /**
     * Get all the Group with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<GroupsDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" groups.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GroupsDTO> findOne(Long id);

    /**
     * Delete the "id" groups.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

package com.activedge.usermgt.service;


import com.activedge.usermgt.model.dto.GroupDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Group.
 */
public interface GroupService {

    /**
     * Save a groups.
     *
     * @param groupDTO the entity to save
     * @return the persisted entity
     */
    GroupDTO save(GroupDTO groupDTO);

    /**
     * Get all the groups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GroupDTO> findAll(Pageable pageable);

    /**
     * Get all the Group with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<GroupDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" groups.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GroupDTO> findOne(Long id);

    /**
     * Delete the "id" groups.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

package com.activedge.usermgt.service;


import com.activedge.usermgt.model.dto.PermissionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Permission.
 */
public interface PermissionService {

    /**
     * Save a permission.
     *
     * @param permissionDTO the entity to save
     * @return the persisted entity
     */
    PermissionDTO save(PermissionDTO permissionDTO);

    /**
     * Get all the permissions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PermissionDTO> findAll(String module, Pageable pageable);


    /**
     * Get the "id" permission.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PermissionDTO> findOne(String id);

    /**
     * Delete the "id" permission.
     *
     * @param id the id of the entity
     */
    void delete(String id);
}

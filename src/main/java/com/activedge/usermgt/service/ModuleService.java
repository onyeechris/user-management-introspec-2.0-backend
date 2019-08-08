package com.activedge.usermgt.service;


import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.dto.ModuleDTO;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Module.
 */
public interface ModuleService {

    /**
     * Save a modules.
     *
     * @param moduleDTO the entity to save
     * @return the persisted entity
     */
    ModuleDTO save(ModuleDTO moduleDTO) throws NotFoundException, ActivityRequiredException;

    /**
     * Get all the modules.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ModuleDTO> findAll(Pageable pageable);
    
    /**
     * Get the "id" modules.
     *
     * @param code the id of the entity
     * @return the entity
     */
    Optional<ModuleDTO> findOne(String code);

    /**
     * Delete the "id" modules.
     *
     * @param code the id of the entity
     */
    void delete(String code);
}

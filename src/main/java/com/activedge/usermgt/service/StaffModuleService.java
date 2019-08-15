package com.activedge.usermgt.service;


import com.activedge.usermgt.model.dto.StaffModuleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing StaffModule.
 */
public interface StaffModuleService {

    /**
     * Save a staffModule.
     *
     * @param staffModuleDTO the entity to save
     * @return the persisted entity
     */
    StaffModuleDTO save(StaffModuleDTO staffModuleDTO);

    /**
     * Get all the staffModules.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StaffModuleDTO> findAll(Pageable pageable);

    /**
     * Get all the staffModules by Module.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StaffModuleDTO> findAllByModule(String module, Pageable pageable);


    /**
     * Get the "id" staffModule.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<StaffModuleDTO> findOne(Long id);

    /**
     * Delete the "id" staffModule.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

package com.activedge.usermgt.service;


import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.dto.NewStaffDTO;
import com.activedge.usermgt.model.dto.StaffDTO;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Staff.
 */
public interface StaffService {

    /**
     * Save a staff.
     *
     * @param staffDTO the entity to save
     * @return the persisted entity
     */
    StaffDTO save(StaffDTO staffDTO) throws ActivityRequiredException, NotFoundException;

    /**
     * Save a new staff.
     *
     * @param staffDTO the entity to save
     * @return the persisted entity
     */
    StaffDTO save(NewStaffDTO staffDTO) throws ActivityRequiredException;

    /**
     * Get all the staff.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StaffDTO> findAll(Pageable pageable);

    Page<StaffDTO> findAllStaff(Pageable pageable, String mdl);
    /**
     * Get the "id" staff.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<StaffDTO> findOne(String id);


    Optional<StaffDTO> search(String searchId);
    List<StaffDTO> wildcardSearch(String username);



    /**
     * Delete the "id" staff.
     *
     * @param id the id of the entity
     */
    void delete(String id);
}

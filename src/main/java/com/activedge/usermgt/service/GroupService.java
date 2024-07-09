package com.activedge.usermgt.service;


import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.Group;
import com.activedge.usermgt.model.GroupPK;
import com.activedge.usermgt.model.dto.GroupDTO;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Group.
 */
public interface GroupService {

    /**
     * Save a groups.
     *
     * @param groupDTO the entity to save
     * @param flag to either delete or append attached permissions
     * @return the persisted entity
     */
    GroupDTO save(GroupDTO groupDTO, int flag) throws NotFoundException, ActivityRequiredException;
    GroupDTO save(GroupDTO groupDTO) throws NotFoundException, ActivityRequiredException;

    /**
     * Get all the groups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GroupDTO> findAll(String module, Pageable pageable);

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
    Optional<GroupDTO> findOne(GroupPK id);


    /**
     * Delete the "id" groups.
     *
     * @param id the id of the entity
     */
    void delete(GroupPK id);

    /**
     * Get the "name" groups.
     *
     * @param name the name of the entity
     * @return the entity
     */
    Page<GroupDTO> searchGroupsByName(String name, String module, Pageable pageable);
    public List<Group> findByModuleIdAndStaffId(String moduleId, String staffId);



}

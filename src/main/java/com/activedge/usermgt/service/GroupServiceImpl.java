package com.activedge.usermgt.service;

import com.activedge.usermgt.model.Group;
import com.activedge.usermgt.model.Permission;
import com.activedge.usermgt.model.dto.GroupDTO;
import com.activedge.usermgt.model.dto.PermissionDTO;
import com.activedge.usermgt.model.mapper.GroupMapper;
import com.activedge.usermgt.model.mapper.PermissionMapper;
import com.activedge.usermgt.repository.GroupRepository;
import com.activedge.usermgt.util.Lambda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.*;

/**
 * Service Implementation for managing Group.
 */
@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    private final Logger log = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupRepository groupRepository;

    private final GroupMapper groupMapper;
    private final PermissionMapper permissionMapper;

    public GroupServiceImpl(GroupRepository groupRepository, GroupMapper groupMapper, PermissionMapper permissionMapper) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
        this.permissionMapper = permissionMapper;
    }

    /**
     * Save a groups.
     *
     * @param groupDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GroupDTO save(GroupDTO groupDTO, int flag) {
//        Group group = groupMapper.toEntity(groupDTO);
        log.info("Request to save Group : {}", groupDTO);
        Group g;


        if(groupDTO.getId() != null) {
            Optional<Group> group = this.findById(groupDTO.getId());

            if(!group.isPresent()) try { // recheck
                throw new ClassNotFoundException("No Id["+groupDTO.getId()+"] found !");
            } catch (ClassNotFoundException e) { }

            g = group.get();

            groupDTO.setName(groupDTO.getName() == null ? g.getName() : groupDTO.getName());
            groupDTO.setDescription(groupDTO.getDescription() == null ? g.getDescription() : groupDTO.getDescription());
            if(flag == 1) {
                for (Permission p : g.getPermissions()) {
                    // add permission attached to entity
                    groupDTO.getPermissions().add(permissionMapper.toDto(p));
                }
            } else {
                // delete permission attached to entity
                for(PermissionDTO p: groupDTO.getPermissions()) {
                    System.out.println("For " + p);
                    if (!g.getPermissions().add(permissionMapper.toEntity(p))) {
                        System.out.println("Removing... " + p);
                        g.getPermissions().remove(permissionMapper.toEntity(p));
                    }
                }
                groupDTO.setPermissions(groupMapper.toDto(g).getPermissions());
            }
        }

        g = groupRepository.save(groupMapper.toEntity(groupDTO));

        return groupMapper.toDto(g);
    }

    /**
     * Get all the groups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Group");
        return groupRepository.findAll(pageable)
            .map(groupMapper::toDto);
    }

    /**
     * Get all the Group with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<GroupDTO> findAllWithEagerRelationships(Pageable pageable) {
        return groupRepository.findAllWithEagerRelationships(pageable).map(groupMapper::toDto);
    }
    

    /**
     * Get one groups by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GroupDTO> findOne(Long id) {
        log.debug("Request to get Group : {}", id);
        return groupRepository.findOneWithEagerRelationships(id)
            .map(groupMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<Group> findById(Long id) {
        return groupRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the groups by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Group : {}", id);
        groupRepository.deleteById(id);
    }
}

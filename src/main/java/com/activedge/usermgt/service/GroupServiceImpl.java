package com.activedge.usermgt.service;

import com.activedge.usermgt.model.Group;
import com.activedge.usermgt.model.dto.GroupDTO;
import com.activedge.usermgt.model.mapper.GroupMapper;
import com.activedge.usermgt.repository.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Group.
 */
@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    private final Logger log = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupRepository groupRepository;

    private final GroupMapper groupMapper;

    public GroupServiceImpl(GroupRepository groupRepository, GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
    }

    /**
     * Save a groups.
     *
     * @param groupDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GroupDTO save(GroupDTO groupDTO) {
        log.debug("Request to save Group : {}", groupDTO);

        Group group = groupMapper.toEntity(groupDTO);

        if(group.getId() == null) {
            group.setStaff(null);
            group.setPermissions(null);
        } else {
            Group g = groupMapper.toEntity(this.findOne(group.getId()).get());
            log.debug("Updating Group...{}", group.getId());
            g.setName(group.getName() == null ? g.getName() : group.getName());
            g.setDescription(group.getDescription() == null ? g.getDescription() : group.getDescription());
            group = g;
        }

        group = groupRepository.save(group);

        return groupMapper.toDto(group);
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

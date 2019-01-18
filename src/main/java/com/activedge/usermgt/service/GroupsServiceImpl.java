package com.activedge.usermgt.service;

import com.activedge.usermgt.model.Group;
import com.activedge.usermgt.model.dto.GroupDTO;
import com.activedge.usermgt.model.mapper.GroupsMapper;
import com.activedge.usermgt.repository.GroupsRepository;
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
public class GroupsServiceImpl implements GroupsService {

    private final Logger log = LoggerFactory.getLogger(GroupsServiceImpl.class);

    private final GroupsRepository groupsRepository;

    private final GroupsMapper groupsMapper;

    public GroupsServiceImpl(GroupsRepository groupsRepository, GroupsMapper groupsMapper) {
        this.groupsRepository = groupsRepository;
        this.groupsMapper = groupsMapper;
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

        Group group = groupsMapper.toEntity(groupDTO);
        group = groupsRepository.save(group);
        return groupsMapper.toDto(group);
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
        return groupsRepository.findAll(pageable)
            .map(groupsMapper::toDto);
    }

    /**
     * Get all the Group with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<GroupDTO> findAllWithEagerRelationships(Pageable pageable) {
        return groupsRepository.findAllWithEagerRelationships(pageable).map(groupsMapper::toDto);
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
        return groupsRepository.findOneWithEagerRelationships(id)
            .map(groupsMapper::toDto);
    }

    /**
     * Delete the groups by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Group : {}", id);
        groupsRepository.deleteById(id);
    }
}

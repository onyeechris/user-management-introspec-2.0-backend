package com.activedge.usermgt.service;

import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.Group;
import com.activedge.usermgt.model.GroupPK;
import com.activedge.usermgt.model.Permission;
import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.dto.GroupDTO;
import com.activedge.usermgt.model.dto.PermissionDTO;
import com.activedge.usermgt.model.dto.StaffDTO;
import com.activedge.usermgt.model.mapper.GroupMapper;
import com.activedge.usermgt.model.mapper.ModuleMapper;
import com.activedge.usermgt.model.mapper.PermissionMapper;
import com.activedge.usermgt.model.mapper.StaffMapper;
import com.activedge.usermgt.repository.GroupRepository;
import com.activedge.usermgt.repository.ModuleRepository;
import com.activedge.usermgt.repository.StaffRepository;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing Group.
 */
@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    private final Logger log = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupRepository groupRepository;
    private final StaffRepository staffRepository;
    private final ModuleRepository moduleRepository;

    private final GroupMapper groupMapper;
    private final ModuleMapper moduleMapper;
    private final PermissionMapper permissionMapper;
    private final StaffMapper staffMapper;

    public GroupServiceImpl(GroupRepository groupRepository, ModuleRepository moduleRepository, GroupMapper groupMapper, PermissionMapper permissionMapper, ModuleMapper moduleMapper, StaffMapper staffMapper, StaffRepository staffRepository) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
        this.permissionMapper = permissionMapper;
        this.staffMapper = staffMapper;
        this.moduleMapper = moduleMapper;
        this.moduleRepository = moduleRepository;
        this.staffRepository = staffRepository;
    }

    /**
     * Save a groups.
     *
     * @param groupDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GroupDTO save(GroupDTO groupDTO, int flag) throws NotFoundException, ActivityRequiredException {
//        Group group = groupMapper.toEntity(groupDTO);
        log.info("Request to save Group : {}", groupDTO);
        Group g;
        GroupPK groupPK = new GroupPK(moduleMapper.fromId(groupDTO.getMod()), groupDTO.getId());

        Optional<Group> group = this.findById(groupPK);

        if (!group.isPresent())
            throw new NotFoundException("No Group [" + groupDTO.getId() + "] found for module[" + groupDTO.getMod() + "]!");

        g = group.get();

        groupDTO.setName(groupDTO.getName() == null ? g.getName() : groupDTO.getName());
        groupDTO.setDescription(groupDTO.getDescription() == null ? g.getDescription() : groupDTO.getDescription());

        switch (flag) {
            // 1 => Add permission(s) | staffs to group if any is present
            case 1:
                if (!groupDTO.getPermissions().isEmpty()) {
                    for (Permission p : g.getPermissions()) {
                        // add permission attached to entity
                        groupDTO.getPermissions().add(permissionMapper.toDto(p));
                    }
                } else {
                    groupDTO.setPermissions(permissionMapper.toDtoSet(g.getPermissions()));
                }
                // ---                                                              --- //
                if (!groupDTO.getStaffs().isEmpty()) {
                    for (Staff s : g.getStaffs()) {
                        // add staff attached to entity
                        groupDTO.getStaffs().add(staffMapper.toDto(s));
                    }
                } else {
                    groupDTO.setStaffs(staffMapper.toDtoSet(g.getStaffs()));
                }
                break;

            // otherwise delete if flag is not set to true
            default:
                // delete permission attached to entity
                if (!groupDTO.getPermissions().isEmpty()) {
                    for (PermissionDTO p : groupDTO.getPermissions()) {
                        if (!g.getPermissions().add(permissionMapper.toEntity(p))) {
                            g.getPermissions().remove(permissionMapper.toEntity(p));
                        }
                    }
                    groupDTO.setPermissions(groupMapper.toDto(g).getPermissions());
                } else {
                    groupDTO.setPermissions(permissionMapper.toDtoSet(g.getPermissions()));
                }

                // delete staff attached to entity
                if (!groupDTO.getStaffs().isEmpty()) {
                    for (StaffDTO s : groupDTO.getStaffs()) {
                        if (!g.getStaffs().add(staffMapper.toEntity(s))) {
                            g.getStaffs().remove(staffMapper.toEntity(s));
                        }
                    }
                    groupDTO.setStaffs(groupMapper.toDto(g).getStaffs());
                } else {
                    groupDTO.setStaffs(staffMapper.toDtoSet(g.getStaffs()));
                }
                break;

        }

        log.debug("Updating group... {}", groupDTO);

        g = groupMapper.toEntity(groupDTO);

        log.debug("Converted group ... {} permissions...{}, staffs...{}", g, g.getPermissions(), g.getStaffs());


        g.setIsDeleted(false);

        return groupMapper.toDto(groupRepository.save(g));
    }

    @Override
    public GroupDTO save(GroupDTO groupDTO) throws NotFoundException, ActivityRequiredException {
        log.debug("Request to save Group : {}", groupDTO);

        Group g;

        // create new group
        groupDTO.setPermissions(new HashSet<>());
        groupDTO.setStaffs(new HashSet<>());

        g = groupMapper.toEntity(groupDTO);

        g.setIsDeleted(false);

        log.debug("Saving group... {}", g);

        return groupMapper.toDto(groupRepository.save(g));
    }

    /**
     * Get all the groups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GroupDTO> findAll(String module, Pageable pageable) {
        return groupRepository.findAllByModule_Id(module, pageable)
                .map(groupMapper::toDto);
    }

    /**
     * Get all the Group with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<GroupDTO> findAllWithEagerRelationships(Pageable pageable) {
        log.debug("Request to get module Group" + groupRepository.findAll(pageable));
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
    public Optional<GroupDTO> findOne(GroupPK id) {
        log.debug("Request to get Group : {}", id);
        return groupRepository.findById(id)
                .map(groupMapper::toDto);
    }

    /**
     * Get groups by name.
     *
     * @param name the name of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GroupDTO> searchGroupsByName(String name, String module, Pageable pageable) {
        return groupRepository.findByName(name, module, pageable).map(groupMapper::toDto);
    }


    @Transactional(readOnly = true)
    public Optional<Group> findById(GroupPK id) {
        return groupRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the groups by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(GroupPK id) {
        log.debug("Request to delete Group : {}", id);
        groupRepository.deleteById(id);
    }

    @Override
    public boolean updateGroupsByUsername(String username, String newGroup) {

        //TODO: Check logs for username being updated in both staff and group
        Optional<Staff> staffOptional = staffRepository.findByUsername(username);
        if (staffOptional.isPresent()) {
            Optional<Group> groupOptional = groupRepository.findByName(newGroup);
            if (groupOptional.isPresent()) {
                Group group = groupOptional.get();
                Staff staff = staffOptional.get();
                Set<Group> groups = new HashSet<>();
                groups.add(group);
                staff.setGroups(groups);
                staffRepository.save(staff);
                log.info("group saved successfully");
                return true; // Group updated successfully
            }else {
                log.error("New group '{}' not found", newGroup);
                return false; // New group not found
            }
        }
        log.error("Staff member with username '{}' not found", username);
        return false; // Staff member not found
    }

}


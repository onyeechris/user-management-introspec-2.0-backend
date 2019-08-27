package com.activedge.usermgt.service;

import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.Group;
import com.activedge.usermgt.model.GroupPK;
import com.activedge.usermgt.model.Module;
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
import com.activedge.usermgt.repository.redis.MakerItemRepository;
import com.activedge.usermgt.util.Lambda;
import com.activedge.usermgt.util.facade.GroupSpy;
import com.activedge.usermgt.util.facade.Spy;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service Implementation for managing Group.
 */
@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    private final Logger log = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupRepository groupRepository;

    private MakerItemRepository makerItemRepository;

    private final GroupMapper groupMapper;
    private final ModuleMapper moduleMapper;
    private final PermissionMapper permissionMapper;
    private final StaffMapper staffMapper;

    public GroupServiceImpl(GroupRepository groupRepository, GroupMapper groupMapper, PermissionMapper permissionMapper, ModuleMapper moduleMapper, StaffMapper staffMapper, MakerItemRepository makerItemRepository) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
        this.permissionMapper = permissionMapper;
        this.staffMapper = staffMapper;
        this.moduleMapper = moduleMapper;
        this.makerItemRepository = makerItemRepository;
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

        if(!group.isPresent()) throw new NotFoundException("No Group ["+groupDTO.getId()+"] found for module["+groupDTO.getMod()+"]!");

        g = group.get();

        groupDTO.setName(groupDTO.getName() == null ? g.getName() : groupDTO.getName());
        groupDTO.setDescription(groupDTO.getDescription() == null ? g.getDescription() : groupDTO.getDescription());

        switch (flag) {
            // 1 => Add permission(s) | staffs to group if any is present
            case 1:
                if(!groupDTO.getPermissions().isEmpty()) {
                    for (Permission p : g.getPermissions()) {
                        // add permission attached to entity
                        groupDTO.getPermissions().add(permissionMapper.toDto(p));
                    }
                } else {
                    groupDTO.setPermissions(permissionMapper.toDtoSet(g.getPermissions()));
                }
                // ---                                                              --- //
                if(!groupDTO.getStaffs().isEmpty()) {
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
                if(!groupDTO.getPermissions().isEmpty()) {
                    for(PermissionDTO p: groupDTO.getPermissions()) {
                        if (!g.getPermissions().add(permissionMapper.toEntity(p))) {
                            g.getPermissions().remove(permissionMapper.toEntity(p));
                        }
                    }
                    groupDTO.setPermissions(groupMapper.toDto(g).getPermissions());
                } else {
                    groupDTO.setPermissions(permissionMapper.toDtoSet(g.getPermissions()));
                }

                // delete staff attached to entity
                if(!groupDTO.getStaffs().isEmpty()) {
                    for(StaffDTO s: groupDTO.getStaffs()) {
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

        /*
        if(flag == 1) {
            for (Permission p : g.getPermissions()) {
                // add permission attached to entity
                groupDTO.getPermissions().add(permissionMapper.toDto(p));
            }
        } else {
            // delete permission attached to entity
            for(PermissionDTO p: groupDTO.getPermissions()) {
                if (!g.getPermissions().add(permissionMapper.toEntity(p))) {
                    g.getPermissions().remove(permissionMapper.toEntity(p));
                }
            }
            groupDTO.setPermissions(groupMapper.toDto(g).getPermissions());
        }
        */

        log.debug("Updating group... {}", groupDTO);


//        else {
//            groupDTO.setId(null);
//            groupDTO.setPermissions(new HashSet<>());
//            log.info("Saving group... {}", groupDTO);
//        }

        g = groupMapper.toEntity(groupDTO);



        log.debug("Converted group ... {} permissions...{}, staffs...{}", g, g.getPermissions(), g.getStaffs());

//      Enable Type
//        Spy spyGroupObj = new GroupSpy(g, this.makerItemRepository);
//        spyGroupObj.checkModel();

        // added for tests
//        Group gg = groupMapper.toEntity(groupDTO);
//        gg.setCreatedBy("test");
//        gg.setCreatedDate(LocalDateTime.now());
//        {
//            "id": "b3b0c1b47a0140688edc853f3f78b995",
//                "name": "GroupName",
//                "description": "Group name description",
//                "module": "ATM",
//                "permissions": [
//            {
//                "id": 1,
//                    "action": "CREATE-ACCOUNT",
//                    "description": "creating account endpoint"
//            }
//    ],
//            "staffs": [
//            {
//                "id": 3,
//                    "email": "admin@aet.com",
//                    "groups": [],
//                "activated": true
//            }
//    ]
//        }
//        g = groupRepository.save(gg);

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

        log.debug("Saving group... {}", groupDTO);

        g = groupMapper.toEntity(groupDTO);

        g.setIsDeleted(false);

//      Enable Type
//        Spy spyGroupObj = new GroupSpy(g, this.makerItemRepository);
//        spyGroupObj.checkModel();

        // added for tests
//        Group gg = groupMapper.toEntity(groupDTO);
//        gg.setCreatedBy("test");
//        gg.setCreatedDate(LocalDateTime.now());

//        g = groupRepository.save(gg);

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
//        log.debug("Request to get all Group" + groupRepository.findAll(pageable).getContent());
        return groupRepository.findAllByModule_Code(module, pageable)
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
//    @Override
//    @Transactional(readOnly = true)
//    public Optional<GroupDTO> findOne(GroupPK id) {
//        log.debug("Request to get Group : {}", id);
//        return groupRepository.findOneWithEagerRelationships(id)
//            .map(groupMapper::toDto);
//    }
    @Override
    @Transactional(readOnly = true)
    public Optional<GroupDTO> findOne(GroupPK id) {
        log.debug("Request to get Group : {}", id);
        return groupRepository.findById(id)
            .map(groupMapper::toDto);
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
//        GroupDTO gdto = findOne(id).get();
//        Group grp = groupMapper.toEntity(gdto);
//        grp.setIsDeleted(true);
//        groupRepository.save(grp);
        groupRepository.deleteById(id);
    }
}

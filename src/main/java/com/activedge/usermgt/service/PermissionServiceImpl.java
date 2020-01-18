package com.activedge.usermgt.service;

import com.activedge.usermgt.model.AuthorityPK;
import com.activedge.usermgt.model.Permission;
import com.activedge.usermgt.model.dto.PermissionDTO;
import com.activedge.usermgt.model.mapper.PermissionMapper;
import com.activedge.usermgt.repository.PermissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.Optional;

/**
 * Service Implementation for managing Permission.
 */
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private final Logger log = LoggerFactory.getLogger(PermissionServiceImpl.class);

    private final PermissionRepository permissionRepository;

    private final PermissionMapper permissionMapper;

    public PermissionServiceImpl(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    /**
     * Save a permission.
     *
     * @param permissionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PermissionDTO save(PermissionDTO permissionDTO) {
        log.info("Request to save Permission : {}", permissionDTO);

        Permission permission = permissionMapper.toEntity(permissionDTO);
        log.info("Permission : {}", permission);
        if(permission.getId() == null) {
            permission.setGrps(null);
        } else {
            Permission p = permissionMapper.toEntity(this.findOne(permission.getId()).get());
            log.debug("Updating Permission...{}", permission.getId());
            p.setAction(permission.getAction() == null ? p.getAction() : permission.getAction());
            p.setDescription(permission.getDescription() == null ? p.getDescription() : permission.getDescription());
            permission = p;
        }

//        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
//        entityManager.persist(permission);

        permission = permissionRepository.save(permission);

        return permissionMapper.toDto(permission);
    }

    /**
     * Get all the permissions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PermissionDTO> findAll(String module, Pageable pageable) {
        log.debug("Request to get all Permissions");
        return permissionRepository.findAllByModule_Id(pageable, module)
            .map(permissionMapper::toDto);
    }


    /**
     * Get one permission by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PermissionDTO> findOne(BigInteger id) {
        log.debug("Request to get Permission : {}", id);
        return permissionRepository.findById(id)
            .map(permissionMapper::toDto);
    }

    /**
     * Delete the permission by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(BigInteger id) {
        log.debug("Request to delete Permission : {}", id);
        permissionRepository.deleteById(id);
    }
}

package com.activedge.usermgt.service;

import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.Module;
import com.activedge.usermgt.model.Permission;
import com.activedge.usermgt.model.dto.ModuleDTO;
import com.activedge.usermgt.model.dto.PermissionDTO;
import com.activedge.usermgt.model.mapper.ModuleMapper;
import com.activedge.usermgt.model.mapper.PermissionMapper;
import com.activedge.usermgt.repository.ModuleRepository;
import com.activedge.usermgt.util.facade.Spy;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;

/**
 * Service Implementation for managing Module.
 */
@Service
@Transactional
public class ModuleServiceImpl implements ModuleService {

    private final Logger log = LoggerFactory.getLogger(ModuleServiceImpl.class);

    private final ModuleRepository moduleRepository;

    private final ModuleMapper moduleMapper;
    private final PermissionMapper permissionMapper;

    public ModuleServiceImpl(ModuleRepository moduleRepository, ModuleMapper moduleMapper, PermissionMapper permissionMapper) {
        this.moduleRepository = moduleRepository;
        this.moduleMapper = moduleMapper;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public ModuleDTO save(ModuleDTO moduleDTO) throws NotFoundException, ActivityRequiredException {
        log.info("Request to save Module : {}", moduleDTO);

        Module module = moduleMapper.toEntity(moduleDTO);
        log.info("Module : {}", module);

        module = moduleRepository.save(module);

        return moduleMapper.toDto(module);

    }

    /**
     * Get all the modules.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ModuleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Module");
        return moduleRepository.findAll(pageable)
            .map(moduleMapper::toDto);
    }

    /**
     * Get one modules by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ModuleDTO> findOne(String id) {
        log.debug("Request to get Module : {}", id);
        return moduleRepository.findById(id)
            .map(moduleMapper::toDto);
    }

    /**
     * Delete the modules by id.
     *
     * @param code the id of the entity
     */
    @Override
    public void delete(String code) {
        log.debug("Request to delete Module : {}", code);
        moduleRepository.deleteById(code);
    }
}

package com.activedge.usermgt.service;

import com.activedge.usermgt.model.StaffModule;
import com.activedge.usermgt.model.dto.StaffModuleDTO;
import com.activedge.usermgt.model.mapper.StaffModuleMapper;
import com.activedge.usermgt.repository.StaffModuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing StaffModule.
 */
@Service
@Transactional
public class StaffModuleServiceImpl implements StaffModuleService {

    private final Logger log = LoggerFactory.getLogger(StaffModuleServiceImpl.class);

    private final StaffModuleRepository staffModuleRepository;

    private final StaffModuleMapper staffModuleMapper;

    public StaffModuleServiceImpl(StaffModuleRepository staffModuleRepository, StaffModuleMapper staffModuleMapper) {
        this.staffModuleRepository = staffModuleRepository;
        this.staffModuleMapper = staffModuleMapper;
    }

    /**
     * Save a staffModule.
     *
     * @param staffModuleDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StaffModuleDTO save(StaffModuleDTO staffModuleDTO) {
        log.info("Request to save StaffModule : {}", staffModuleDTO);

        StaffModule staffModule = staffModuleMapper.toEntity(staffModuleDTO);

        staffModule = staffModuleRepository.save(staffModule);

        return staffModuleMapper.toDto(staffModule);
    }

    /**
     * Get all the staffModules.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StaffModuleDTO> findAllByModule(String module, Pageable pageable) {
        log.debug("Request to get all StaffModules by {}", module);
        return staffModuleRepository.findAllByModule_Id(pageable, module)
            .map(staffModuleMapper::toDto);
    }

    /**
     * Get all the staffModules.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StaffModuleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StaffModules");
        return staffModuleRepository.findAll(pageable)
            .map(staffModuleMapper::toDto);
    }

    /**
     * Get one staffModule by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StaffModuleDTO> findOne(Long id) {
        log.debug("Request to get StaffModule : {}", id);
        return staffModuleRepository.findById(id)
            .map(staffModuleMapper::toDto);
    }

    /**
     * Delete the staffModule by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StaffModule : {}", id);
        staffModuleRepository.deleteById(id);
    }
}

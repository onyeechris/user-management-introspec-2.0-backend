package com.activedge.usermgt.service;

import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.StaffModule;
import com.activedge.usermgt.model.dto.StaffModuleDTO;
import com.activedge.usermgt.model.mapper.StaffMapper;
import com.activedge.usermgt.model.mapper.StaffModuleMapper;
import com.activedge.usermgt.repository.StaffModuleRepository;
import com.activedge.usermgt.repository.StaffRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing StaffModule.
 */
@Service
@Transactional
public class StaffModuleServiceImpl implements StaffModuleService {

    private final Logger log = LoggerFactory.getLogger(StaffModuleServiceImpl.class);

    private final StaffModuleRepository staffModuleRepository;

    private final StaffModuleMapper staffModuleMapper;

    private final StaffRepository staffRepository;

    public StaffModuleServiceImpl(StaffModuleRepository staffModuleRepository, StaffModuleMapper staffModuleMapper, StaffRepository staffRepository) {
        this.staffModuleRepository = staffModuleRepository;
        this.staffModuleMapper = staffModuleMapper;
        this.staffRepository = staffRepository;
    }

    /**
     * Save a staffModule.
     *
     * @param staffModuleDTO the entity to save
     * @return the persisted entity
     */
//    @Override
//    public StaffModuleDTO save(StaffModuleDTO staffModuleDTO) {
//        log.info("Request to save StaffModule : {}", staffModuleDTO);
//
//        StaffModule staffModule = staffModuleMapper.toEntity(staffModuleDTO);
//
//        staffModule = staffModuleRepository.save(staffModule);
//
//        return staffModuleMapper.toDto(staffModule);
//    }


    @Override
    public StaffModuleDTO save(StaffModuleDTO staffModuleDTO) {
        log.info("Request to save StaffModule: {}", staffModuleDTO);
        StaffModule staffModule = staffModuleMapper.toEntity(staffModuleDTO);
        Staff staff = staffRepository.findByUsername(staffModuleDTO.getStaff().getUsername()).orElse(null);
        log.info("staff module staff {}",staff);
        Optional<StaffModule> existingStaffModule = staffModuleRepository.findByModuleAndStaff(staffModule.getModule(), staff);
        if (existingStaffModule.isPresent()) {
            StaffModule existingModule = existingStaffModule.get();
            existingModule.setGrade(staffModule.getGrade());
            existingModule.setAssignAt(staffModule.getAssignAt());
            existingModule = staffModuleRepository.save(existingModule);
            return staffModuleMapper.toDto(existingModule);
        } else {

            staffModule = staffModuleRepository.save(staffModule);
            return staffModuleMapper.toDto(staffModule);
        }
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

    @Override
    public boolean matchModuleAndEmail(String module, String email) {
        Set<StaffModule> sm = staffModuleRepository.findByModule_Id(module);
        return sm.stream().anyMatch(x -> {
            String eml = x.getStaff().getEmail() == null ? "" : x.getStaff().getEmail();
            String usn = x.getStaff().getUsername() == null ? "" : x.getStaff().getUsername();
            return eml.equalsIgnoreCase(email) || usn.equalsIgnoreCase(email);
        });
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
    public Optional<StaffModuleDTO> findOne(String id) {
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
    public void delete(String id) {
        log.debug("Request to delete StaffModule : {}", id);
        staffModuleRepository.deleteById(id);
    }
}

package com.activedge.usermgt.service;

import com.activedge.usermgt.config.Constants;
import com.activedge.usermgt.model.Authority;
import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.dto.StaffDTO;
import com.activedge.usermgt.model.mapper.StaffMapper;
import com.activedge.usermgt.repository.StaffRepository;
import com.activedge.usermgt.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing Staff.
 */
@Service
@Transactional
public class StaffServiceImpl implements StaffService {

    private final Logger log = LoggerFactory.getLogger(StaffServiceImpl.class);

    private final StaffRepository staffRepository;

    private final BCryptPasswordEncoder encoder;

    private final StaffMapper staffMapper;

    public StaffServiceImpl(StaffRepository staffRepository, StaffMapper staffMapper, BCryptPasswordEncoder encoder) {
        this.staffRepository = staffRepository;
        this.staffMapper = staffMapper;
        this.encoder = encoder;
    }

    /**
     * Save a staff.
     *
     * @param staffDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StaffDTO save(StaffDTO staffDTO) {
        log.debug("Request to save Staff : {}", staffDTO);

        Staff staff = staffMapper.toEntity(staffDTO);
        if(staff.getId() == null) {
            // new user, set default authority, and encode password
            Set<Authority> authorities = new HashSet<>();
            Authority authority = new Authority();
            authority.setName(AuthoritiesConstants.USER);
            authorities.add(authority);
            staff.setAuthorities(authorities);
            staff.setPassword(encoder.encode(staff.getPassword()));
        } else {
            // get previous record and update appropriately
            Staff s = staffMapper.toEntity(this.findOne(staff.getId()).get());
            log.debug("Updating Staff...{}", staff.getId());
            s.setFirstName(staff.getFirstName() == null ? s.getFirstName() : staff.getFirstName());
            s.setLastName(staff.getLastName() == null ? s.getLastName() : staff.getLastName());
            s.setPhone(staff.getPhone() == null ? s.getPhone() : staff.getPhone());
            s.setEmail(staff.getEmail() == null ? s.getEmail() : staff.getEmail());
            s.setPassword(staff.getPassword() == null ? s.getPassword() : encoder.encode(staff.getPassword()));
            s.setGroup(staff.getGroup() == null ? s.getGroup() : staff.getGroup());
            s.setHireDate(staff.getHireDate() == null ? s.getHireDate() : staff.getHireDate());
            if (false) { // check if item is closed
                staff = s;
            } else if(false) { // check if this update requires a checker's action
                // serialize "s" for checker
            } else { // put the update on pending status

            }
        }

        staff = staffRepository.save(staff);

        return staffMapper.toDto(staff);
    }

    /**
     * Get all the staff.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StaffDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Staff");
        return staffRepository.findAll(pageable)
            .map(staffMapper::toDto);
    }


    /**
     * Get one staff by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StaffDTO> findOne(Long id) {
        log.debug("Request to get Staff : {}", id);
        return staffRepository.findById(id)
            .map(staffMapper::toDto);
    }

    /**
     * Delete the staff by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Staff : {}", id);
        staffRepository.deleteById(id);
    }
}

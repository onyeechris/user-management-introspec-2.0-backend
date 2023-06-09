package com.activedge.usermgt.service;

import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.Authority;
import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.dto.NewStaffDTO;
import com.activedge.usermgt.model.dto.StaffDTO;
import com.activedge.usermgt.model.mapper.StaffMapper;
import com.activedge.usermgt.repository.StaffRepository;
import dev.samstevens.totp.secret.SecretGenerator;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
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
@Transactional
@Service("db")
public class StaffServiceImpl implements StaffService {

    private final Logger log = LoggerFactory.getLogger(StaffServiceImpl.class);

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private StaffMapper staffMapper;
    @Autowired
    private SecretGenerator secretGenerator;

    /**
     * Save a staff.
     *
     * @param staffDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StaffDTO save(StaffDTO staffDTO) throws ActivityRequiredException, NotFoundException {
        log.info("Updating Staff: {}", staffDTO);
        Staff staff = staffMapper.toEntity(staffDTO);

        // self update
        Optional<Staff> os = this.findById(staff.getId());
        if(os.isPresent()) {
            Staff s = os.get();
            s.setFirst_name(staff.getFirst_name() == null ? s.getFirst_name() : staff.getFirst_name());
            s.setLast_name(staff.getLast_name() == null ? s.getLast_name() : staff.getLast_name());
            s.setPhone(staff.getPhone() == null ? s.getPhone() : staff.getPhone());
            s.setEmail(staff.getEmail() == null ? s.getEmail() : staff.getEmail());
            s.setPassword(staff.getPassword() == null ? s.getPassword() : encoder.encode(staff.getPassword()));
            s.setHireDate(staff.getHireDate() == null ? s.getHireDate() : staff.getHireDate());
            if(staff.getType() == null) {
                s.setType(s.getType());
                s.setAuthorities(s.getAuthorities());
            } else {
                s.setType(staff.getType());
                // clear old authorities -> refactor to remove a single authority if need be.
                s.getAuthorities().clear();

                // add new authority
                Authority authority = new Authority();
                authority.setId("ROLE_" + staff.getType());
                authority.setName("ROLE_" + staff.getType());

                s.getAuthorities().add(authority);
            }
            s.setActivated(staff.isActivated() == null ? s.isActivated() : staff.isActivated());
            staff = s;
            log.info("Updating Staff ... {}", staff);
        } else {
            throw new javassist.NotFoundException("Id["+staff.getId()+"] not found.");
        }

        staff = staffRepository.save(staff);

        return staffMapper.toDto(staff);

    }

    @Override
    public StaffDTO savePreference(String staffId, StaffDTO staffDTO) throws ActivityRequiredException, NotFoundException {
        log.info("Updating Staff Preference: {}", staffId);
        Staff staff = staffMapper.toEntity(staffDTO);

        // self update
        Optional<Staff> os = this.findById(staffId);
        if (os.isPresent()) {
            Staff s = os.get();
            s.setFirst_name(s.getFirst_name());
            s.setLast_name(s.getLast_name());
            s.setPhone(s.getPhone());
            s.setEmail(s.getEmail());
            s.setPassword(s.getPassword());
            s.setHireDate(s.getHireDate());
            s.setType(s.getType());
            s.setAuthorities(s.getAuthorities());
            s.setActivated(s.isActivated());
            if(staffDTO.getEnable2FA()){
                s.setIs2FAEnabled(true);
                s.setSecret(secretGenerator.generate());
            }else{
                s.setIs2FAEnabled(false);
                s.setSecret("");
            }

            staff = s;
            log.info("Updating Staff Preference... {}", staff);
        } else {
            throw new javassist.NotFoundException("Id[" + staff.getId() + "] not found.");
        }

        staff = staffRepository.save(staff);

        return staffMapper.toDto(staff);
    }

    @Override
    public StaffDTO save(NewStaffDTO staffDTO) throws ActivityRequiredException {
        log.info("Logging StaffDTO:{} by User:{}, Password:{}", staffDTO, staffDTO.getPassword());

        Staff staff = staffMapper.toEntity(staffDTO);
        staff.setPassword(staffDTO.getPassword());

        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setId("ROLE_" + staff.getType());
        authority.setName("ROLE_" + staff.getType());
        authorities.add(authority);

        staff.setAuthorities(authorities);
        staff.setPassword(encoder.encode(staff.getPassword()));
        staff.setActivated(true);

        log.info("Saving Staff...{} Authorities: {}", staff, staff.getAuthorities());

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
    public Optional<StaffDTO> findOne(String id) {
        log.debug("Request to get Staff : {}", id);
        return staffRepository.findById(id)
            .map(staffMapper::toDto);
    }

    @Override
    public Optional<StaffDTO> search(String searchId) {
        return staffRepository.findByUsername(searchId)
                .map(staffMapper::toDto);
    }

    /**
     * Get one staff by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Staff> findById(String id) {
        log.debug("Request to get Staff : {}", id);
        return staffRepository.findOneWithAuthoritiesById(id);
    }

    /**
     * Delete the staff by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Staff : {}", id);
        staffRepository.deleteById(id);
    }

}

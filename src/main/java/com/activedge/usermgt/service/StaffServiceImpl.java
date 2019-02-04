package com.activedge.usermgt.service;

import com.activedge.usermgt.config.Constants;
import com.activedge.usermgt.model.Authority;
import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.dto.NewStaffDTO;
import com.activedge.usermgt.model.dto.StaffDTO;
import com.activedge.usermgt.model.enumeration.MakerChecker;
import com.activedge.usermgt.model.enumeration.Notification;
import com.activedge.usermgt.model.log.MakerItem;
import com.activedge.usermgt.model.mapper.StaffMapper;
import com.activedge.usermgt.repository.StaffRepository;
import com.activedge.usermgt.security.AuthoritiesConstants;
import com.activedge.usermgt.security.SecurityUtils;
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
//        String currentUserPosition = staffRepository.findOneByEmailIgnoreCase(SecurityUtils.getCurrentUserLogin().get()).get().getMakerChecker().name();

        log.info("Updating Staff: {}", staffDTO);
        Staff staff = staffMapper.toEntity(staffDTO);

        // self update
        if(staffDTO.getRedis_key() == null) {
            Staff s = this.findById(staff.getId()).get();

            s.setFirstName(staff.getFirstName() == null ? s.getFirstName() : staff.getFirstName());
            s.setLastName(staff.getLastName() == null ? s.getLastName() : staff.getLastName());
            s.setPhone(staff.getPhone() == null ? s.getPhone() : staff.getPhone());
            s.setEmail(staff.getEmail() == null ? s.getEmail() : staff.getEmail());
            s.setPassword(staff.getPassword() == null ? s.getPassword() : encoder.encode(staff.getPassword()));
            s.setGroup(staff.getGroup() == null ? s.getGroup() : staff.getGroup());
            s.setHireDate(staff.getHireDate() == null ? s.getHireDate() : staff.getHireDate());
            s.setMakerChecker(staff.getMakerChecker() == null ? s.getMakerChecker() : staff.getMakerChecker());
            s.setActivated(staff.isActivated() == null ? s.isActivated() : staff.isActivated());
            staff = s;
            log.info("Updating Staff...{}", staff);
        } else {
            staff.setId(null);
        }

        staff = staffRepository.save(staff);

        return staffMapper.toDto(staff);

    }

    @Override
    public StaffDTO save(NewStaffDTO staffDTO) {
        String currentUserPosition = staffRepository.findOneByEmailIgnoreCase(SecurityUtils.getCurrentUserLogin().get()).get().getMakerChecker().name();

        log.info("Saving Staff:{} by User:{}, Password:{}", staffDTO, currentUserPosition, staffDTO.getPassword());

        Staff staff = staffMapper.toEntity(staffDTO);
        staff.setPassword(staffDTO.getPassword());

        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        Authority authority1 = new Authority();
        authority.setName(staff.getMakerChecker().equals(MakerChecker.NONE) ? AuthoritiesConstants.USER : AuthoritiesConstants.ADMIN);
        authority1.setName((staff.getMakerChecker().equals(MakerChecker.NONE) ? AuthoritiesConstants.USER : staff.getMakerChecker().toString()));
        authorities.add(authority);
        authorities.add(authority1);

        staff.setAuthorities(authorities);
        staff.setPassword(encoder.encode(staff.getPassword()));
        staff.setActivated(false);

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
     * Get one staff by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Staff> findById(Long id) {
        log.debug("Request to get Staff : {}", id);
        return staffRepository.findOneWithAuthoritiesById(id);
    }

    /**
     * Delete the staff by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Staff : {}", id);
        StaffDTO s = findOne(id).get();
        Staff sf = staffMapper.toEntity(s);
        sf.setActivated(false);
        staffRepository.save(sf);
    }
}

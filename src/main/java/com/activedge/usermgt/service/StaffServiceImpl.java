package com.activedge.usermgt.service;

import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.Authority;
import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.dto.NewStaffDTO;
import com.activedge.usermgt.model.dto.StaffDTO;
import com.activedge.usermgt.model.mapper.StaffMapper;
import com.activedge.usermgt.repository.StaffRepository;
import com.activedge.usermgt.repository.redis.MakerItemRepository;
import javassist.NotFoundException;
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

    private MakerItemRepository makerItemRepository;

    private final BCryptPasswordEncoder encoder;

    private final StaffMapper staffMapper;

    public StaffServiceImpl(StaffRepository staffRepository, StaffMapper staffMapper, BCryptPasswordEncoder encoder, MakerItemRepository makerItemRepository) {
        this.staffRepository = staffRepository;
        this.staffMapper = staffMapper;
        this.encoder = encoder;
        this.makerItemRepository = makerItemRepository;
    }

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
            s.setType(staff.getType() == null ? s.getType() : staff.getType());
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
    public StaffDTO save(NewStaffDTO staffDTO) throws ActivityRequiredException {
//        String currentUserPosition = staffRepository.findOneByEmailIgnoreCase(SecurityUtils.getCurrentUserLogin().get()).get().getType().name();

        log.info("Logging StaffDTO:{} by User:{}, Password:{}", staffDTO, staffDTO.getPassword());

        Staff staff = staffMapper.toEntity(staffDTO);
        staff.setPassword(staffDTO.getPassword());

        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setCode("ROLE_" + staff.getType());
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
//        Staff sf = staffRepository.findById(id).get();
//        sf.setActivated(false);
//        log.debug("Staff to save: {}", sf);
//        staffRepository.save(sf);
        staffRepository.deleteById(id);
    }

}

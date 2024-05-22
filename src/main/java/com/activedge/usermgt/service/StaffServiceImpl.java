package com.activedge.usermgt.service;

import com.activedge.usermgt.config.Constants;
import com.activedge.usermgt.controller.util.EmailUtil;
import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.exception.UnauthorizedException;
import com.activedge.usermgt.model.Authority;
import com.activedge.usermgt.model.Group;
import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.dto.NewStaffDTO;
import com.activedge.usermgt.model.dto.StaffDTO;
import com.activedge.usermgt.model.enumeration.Type;
import com.activedge.usermgt.model.mapper.GroupMapper;
import com.activedge.usermgt.model.mapper.StaffMapper;
import com.activedge.usermgt.repository.GroupRepository;
import com.activedge.usermgt.repository.StaffRepository;
import dev.samstevens.totp.secret.SecretGenerator;
import javassist.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import javassist.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;

/**
 * Service Implementation for managing Staff.
 */
@Transactional
@Service("db")
public class StaffServiceImpl implements StaffService {
    private static final String RESET_EMAIL_SENT_MESSAGE = "Please check your email to set a new password";
    private static final String RESET_EMAIL="New password set successfully login with new password";

    @Autowired
    private EmailUtil emailUtil;
    private final Logger log = LoggerFactory.getLogger(StaffServiceImpl.class);

    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private StaffMapper staffMapper;
    @Autowired
    private  GroupMapper groupMapper;
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
            s.setEnable2FA(s.is2FAEnabled());
            s.setSecret(s.getSecret());
            s.setDefault2FA(s.getDefault2FA());
            if(StringUtils.isNotBlank(staffDTO.getEnroll()) && Boolean.parseBoolean(staffDTO.getEnroll())){
                s.setEnrol(true);
            }else{
                s.setEnrol(false);
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
    public int enable2faForAllStaff(String staffId, StaffDTO staffDTO) throws ActivityRequiredException, NotFoundException {
        Iterable<Staff> allStaff = staffRepository.findAll();
        for(Staff staff:allStaff){
            if (staffDTO.getEnable2FA() && staffDTO.getDefault2FA()) {
                staff.setEnable2FA(true);
                staff.setSecret(secretGenerator.generate());
                staff.setEnrol(false);
                staff.setDefault2FA(true);
            }else if(staffDTO.getEnable2FA() && !staffDTO.getDefault2FA()){
                staff.setEnable2FA(true);
                staff.setSecret("");
                staff.setEnrol(false);
                staff.setDefault2FA(false);
            }else {
                staff.setEnable2FA(false);
                staff.setSecret("");
                staff.setDefault2FA(false);
                staff.setEnrol(false);
            }
            staffRepository.save(staff);
        }
        return ((Collection<Staff>)allStaff).size();
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

       assignGroupsForUser(staff);

        return staffMapper.toDto(staff);
    }

    private Set<Group> assignGroupsForUser(Staff staff) {
        Set<Group> groups = new HashSet<>();
        Group userGroup = null;
        switch (staff.getType()) {
            case ADMIN:
                userGroup  = groupRepository.findByName(Constants.FIND_BY_NAME_ADMIN)
                        .orElse(null);
                if (userGroup != null) {
                    groups.add(userGroup);
                }
                break;

            case USER:
                userGroup  = groupRepository.findByName(Constants.FIND_BY_NAME_USERS)
                        .orElse(null);
                if (userGroup != null) {
                    groups.add(userGroup);
                }
                break;

            case AUDITOR:
                userGroup  = groupRepository.findByName(Constants.FIND_BY_NAME_AUDITORS)
                        .orElse(null);
                if (userGroup != null) {
                    groups.add(userGroup);
                }
                break;
            default:
                userGroup  = groupRepository.findByName(Constants.FIND_BY_NAME_INTROSPEC)
                        .orElse(null);
                if (userGroup != null) {
                    groups.add(userGroup);
                }
                break;
        }

        for ( Group group: groups){
            Set <Staff> staffSet = group.getStaffs();
            staffSet.add(staff);
            group.setStaffs(staffSet);
        }
        groupRepository.saveAll(groups);
        return groups;
    }

    @Override
    public void resetPasswordByAdmin(String username, String adminUsername, String newPassword) throws NotFoundException, UnauthorizedException {

        Optional<Staff> adminUser = staffRepository.findByUsername(adminUsername);

        if (adminUser.isPresent()) {
            Staff admin = adminUser.get();

            // Check if the admin user has Admin rights
            if (admin.getType() == Type.ADMIN) {
                Optional<Staff> userToReset = staffRepository.findByUsername(username);

                if (userToReset.isPresent()) {
                    Staff user = userToReset.get();

                    // Update the user's password
                    user.setPassword(encoder.encode(newPassword));



                    // Save the updated user entity
                    staffRepository.save(user);

                } else {

                    throw new NotFoundException("User not found with username: " + username);
                }
            } else {
                throw new UnauthorizedException("Only Admin users are allowed to reset passwords.");
            }
        } else {
            throw new NotFoundException("Admin user not found with username: " + adminUsername);
        }
    }

    @Override
    @Transactional
    public void processCSV(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String headerLine = reader.readLine();
            System.out.println("Header Line: " + headerLine);
            String[] headers = headerLine.split(",");
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Staff entity = CSVData(headers, data);
                if (!isEmptyEntity(entity)) {
                    staffRepository.save(entity);
                    System.out.println("Entity saved successfully");
                } else {
                    System.out.println("Skipped saving empty entity");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Staff CSVData(String[] headers, String[] data) {
        Staff entity = new Staff();

        for (int i = 0; i < headers.length; i++) {
            String header = headers[i].trim();
            String value = data[i].trim();
            System.out.println("Header: " + header + ", Value: '" + value + "'");
            switch (header) {
                case "first_name":
                    entity.setFirst_name(value);
                    break;
                case "last_name":
                    entity.setLast_name(value);
                    break;
                case "email":
                    entity.setEmail(value);
                    break;
                case "username":
                    entity.setUsername(value);
                    break;
                case "phone":
                    entity.setPhone(value);
                    break;
                case "hire_date":
                    if (!value.isEmpty()) {
                        try {
                            entity.setHireDate(LocalDate.parse(value, DateTimeFormatter.ofPattern("MM/dd/yyyy")));
                        } catch (DateTimeParseException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "type":
                    entity.setType(Type.valueOf(value));
                    break;
            }
        }
        System.out.println("Entity after CSVData: " + entity);

        return entity;
    }

    private boolean isEmptyEntity(Staff entity) {
        // Check if any relevant fields are not set
        boolean isEmpty = entity.getFirst_name() == null ||
                entity.getLast_name() == null ||
                entity.getEmail() == null ||
                entity.getUsername() == null ||
                entity.getPhone() == null ||
                entity.getHireDate() == null ||
                entity.getType() == null;

        System.out.println("Checking for empty entity - Is empty? " + isEmpty);
        System.out.println("Entity details: " + entity);
        return isEmpty;
    }


    @Override
    public String forgotPassword(String email) {
        Staff staff =staffRepository.findByEmail(email)
                .orElseThrow(
                        ()-> new RuntimeException("Staff not found with this email: "+email)
                );
        try {
            String subject = "Set Password";
            emailUtil.sendSetPassword(email,subject);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to set password please try again"+e);
        }
        return RESET_EMAIL_SENT_MESSAGE;
    }

    public String resetPassword(String email, String newPassword, String confirmPassword) {
        // Check if passwords match
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        // Retrieve staff by email
        Staff staff = staffRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Staff not found with this email: " + email));

        // Encode the new password
        String hashedPassword = encoder.encode(newPassword);

        // Update staff password
        staff.setPassword(hashedPassword);
        staffRepository.save(staff);

        return RESET_EMAIL;
    }



    @Override
    public void updateUserStatus(String userId, boolean active) {
        Staff staff = staffRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Staff not found"));
        staff.setActive(active);
        staffRepository.save(staff);
    }

    @Override
    public boolean isUsernameOrEmailDuplicate(String username, String email) {
        return staffRepository.existsByUsernameOrEmail(username, email);
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
        return staffRepository.findAllByActiveTrue(pageable)
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

        Optional<Staff> staffOptional = staffRepository.findByIdAndActiveIsTrue(id);
        if (staffOptional.isPresent()) {
            Staff staff = staffOptional.get();
            Set<Group> groups = new HashSet<>(groupRepository.findAllByStaffsContaining(staff));
            staff.setGroups(groups);
            StaffDTO staffDTO = staffMapper.toDto(staff);
            return Optional.of(staffDTO);
        } else {
            return Optional.empty();
        }
    }


    @Override
    public Optional<StaffDTO> findByUsername(String username) {
        return staffRepository.findByUsername(username)
                .map(staffMapper::toDto);
    }

    @Override
    public List<StaffDTO> wildcardSearch(String username) {
        return staffRepository.findByUsernameContaining(username)
                .stream()
                .map(staffMapper::toDto)
                .collect(Collectors.toList());
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
        Optional<Staff> staff = staffRepository.findById(id);
        if(staff.isPresent()){
            List<Group> groups = groupRepository.findAllByStaffsContaining(staff.get());
            for(Group group : groups){
                Set<Staff> staffSet = group.getStaffs();
                staffSet.remove(staff.get());
                group.setStaffs(staffSet);
            }
            groupRepository.saveAll(groups);
        }
        staffRepository.deleteById(id);
    }

}

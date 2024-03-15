package com.activedge.usermgt.service;

import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.exception.UnauthorizedException;
import com.activedge.usermgt.exception.UserLimitExceededException;
import com.activedge.usermgt.license.EncryptionService;
import com.activedge.usermgt.model.LdapUser;
import com.activedge.usermgt.license.License;
import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.dto.NewStaffDTO;
import com.activedge.usermgt.model.dto.StaffDTO;
import com.activedge.usermgt.model.enumeration.Type;
import com.activedge.usermgt.repository.LdapRepository;
import com.activedge.usermgt.repository.LicenseRepository;
import com.activedge.usermgt.service.adapter.StaffDTOAdapter;
import dev.samstevens.totp.secret.SecretGenerator;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service("ldap")
public class LdapService implements StaffService {

    @Autowired
    private LdapRepository ldapRepository;
    @Autowired
    private SecretGenerator secretGenerator;
    @Autowired
    @Qualifier("db")
    private StaffService staffService;
    @Autowired
    private LicenseRepository licenseRepository;
    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private StaffDTOAdapter staffDTOAdapter;

    @Override
    public StaffDTO save(StaffDTO staffDTO) throws ActivityRequiredException, NotFoundException {
        log.debug("Unimplemented method[save]");
        // Retrieve the license information
        log.debug("Retrieving license information...");
        Optional<License> licenseOptional = licenseRepository.findNoOfUsers();
        License license = licenseOptional.orElseThrow(() -> new NotFoundException("License not found."));
        log.debug("License retrieved successfully: {}", license);

        // Decrypt the number of users allowed from the license
        log.debug("Decrypting number of users from license...");
        String encryptedNoOfUsers = license.getNo_of_users();
        String decryptedNoOfUsers;
        try {
            decryptedNoOfUsers = encryptionService.decrypt(encryptedNoOfUsers);
        } catch (Exception e) {
            log.error("Error decrypting no_of_users", e);
            throw new RuntimeException("Error decrypting no_of_users", e);
        }
        log.debug("Number of users decrypted successfully: {}", decryptedNoOfUsers);

        // Convert the decrypted value to an integer
        log.debug("Parsing decrypted number of users to integer...");
        int maxUsers = Integer.parseInt(decryptedNoOfUsers);
        log.debug("Maximum users allowed: {}", maxUsers);

        // Count the current number of users in the LDAP (assuming LDAP specific method to count users)
        log.debug("Counting current number of users in the LDAP...");
        long currentUsersCountInLdap = ldapRepository.count();
        log.debug("Current number of users in the LDAP: {}", currentUsersCountInLdap);

        // Check if the user limit is exceeded
        if (currentUsersCountInLdap >= maxUsers) {
            log.warn("User limit exceeded in LDAP. Cannot create new user.");
            throw new UserLimitExceededException("User limit exceeded in LDAP. Cannot create new user.");
        }
        return null;
    }

    @Override
    public StaffDTO savePreference(String staffId, StaffDTO staffDTO) throws ActivityRequiredException, NotFoundException {
        log.info("Updating Staff Preference: {}", staffId);
        return null;
    }

    @Override
    public int enable2faForAllStaff(String staffId, StaffDTO staffDTO) throws ActivityRequiredException, NotFoundException {
        return 0;
    }

    @Override
    public StaffDTO save(NewStaffDTO staffDTO) throws ActivityRequiredException {
        log.debug("Unimplemented method[save]");
       return null;
    }

    @Override
    public Page<StaffDTO> findAll(Pageable pageable) {
        return staffDTOAdapter.transform(ldapRepository.findAll());
    }

    @Override
    public Optional<StaffDTO> findOne(String id) {
        Optional<LdapUser> staff = ldapRepository.findByUsername(id);
        return staffDTOAdapter.transform(staff);
    }

    @Override
    public Optional<StaffDTO> findByUsername(final String username) {
        Optional<LdapUser> newUser = ldapRepository.findByUsername(username);
        Optional<StaffDTO> user = staffDTOAdapter.transform(newUser);
        StaffDTO uzer = new StaffDTO();

        if(user.isPresent()) {
            try {
                StaffDTO usr = user.get();
                // check that this user was not previously imported before saving
                Optional<StaffDTO> s1 = staffService.findByUsername(usr.getUsername());
                if(s1.isPresent()) {
                    uzer = s1.get();
                } else {
                    log.info("...saving imported staff");
                    uzer = staffService.save(new NewStaffDTO(usr.getFirst_name(), usr.getLast_name(), usr.getUsername(), usr.getEmail(), Type.USER));
                }
            } catch (ActivityRequiredException e) {
                log.error(e.getMessage());
            }
        }
        return Optional.of(uzer);
    }

    @Override
    public void resetPasswordByAdmin(String username, String adminUsername, String newPassword) throws NotFoundException, UnauthorizedException {

    }


    @Override
    public List<StaffDTO> wildcardSearch(String username) {
        List<LdapUser> users = ldapRepository.findByUsernameContaining(username);
        List<StaffDTO> staffList = new ArrayList<>();
        for(LdapUser user : users){
            Optional<StaffDTO> staff = staffDTOAdapter.transform(Optional.of(user));
            if(staff.isPresent()){
                StaffDTO usr = staff.get();
                List<StaffDTO> s1 = staffService.wildcardSearch(usr.getUsername());
                if(!s1.isEmpty()){
                    staffList.add(s1.get(0));
                }
            }
        }
        return staffList;
    }


    @Override
    public void delete(String id) {
        //ldapRepository.deleteById(id);
    }

    private String digestSHA(final String password) {
        String base64;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA");
            digest.update(password.getBytes());
            base64 = Base64.getEncoder()
                    .encodeToString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return "{SHA}" + base64;
    }

}

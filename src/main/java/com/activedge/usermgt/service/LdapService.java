package com.activedge.usermgt.service;

import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.LdapUser;
import com.activedge.usermgt.model.dto.NewStaffDTO;
import com.activedge.usermgt.model.dto.StaffDTO;
import com.activedge.usermgt.model.enumeration.Type;
import com.activedge.usermgt.repository.LdapRepository;
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
    private StaffDTOAdapter staffDTOAdapter;

    @Override
    public StaffDTO save(StaffDTO staffDTO) throws ActivityRequiredException, NotFoundException {
        log.debug("Unimplemented method[save]");
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
    public List<StaffDTO> search(final String searchId) {
        List<LdapUser> users = ldapRepository.findByUsernameContaining(searchId);
        List<StaffDTO> staffList = new ArrayList<>();
        for (LdapUser user : users) {
            Optional<StaffDTO> staff = staffDTOAdapter.transform(Optional.of(user));
            staff.ifPresent(staffList::add);
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

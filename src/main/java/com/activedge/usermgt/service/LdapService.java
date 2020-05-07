package com.activedge.usermgt.service;

import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.LdapUser;
import com.activedge.usermgt.model.dto.NewStaffDTO;
import com.activedge.usermgt.model.dto.StaffDTO;
import com.activedge.usermgt.model.enumeration.Type;
import com.activedge.usermgt.repository.LdapRepository;
import com.activedge.usermgt.service.adapter.StaffDTOAdapter;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Service("ldap_service")
public class LdapService implements StaffService {

    @Autowired
    private LdapRepository ldapRepository;

    @Autowired
    @Qualifier("db_service")
    private StaffService staffService;

    @Autowired
    private StaffDTOAdapter staffDTOAdapter;

    @Override
    public StaffDTO save(StaffDTO staffDTO) throws ActivityRequiredException, NotFoundException {
        log.debug("Unimplemented method[save]");
        return null;
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
    public Optional<StaffDTO> search(final String searchId) {
        Optional<LdapUser> newUser = ldapRepository.findByUsername(searchId);
        Optional<StaffDTO> user = staffDTOAdapter.transform(newUser);
        StaffDTO uzer = new StaffDTO();

        if(user.isPresent()) {
            try {
                StaffDTO usr = user.get();
                // check that this user was not previously imported before saving
                Optional<StaffDTO> s1 = staffService.search(usr.getPhone());
                if(s1.isPresent()) {
                    uzer = s1.get();
                } else {
                    log.info("...saving imported staff");
                    uzer = staffService.save(new NewStaffDTO(usr.getPhone(), usr.getFirst_name(), usr.getLast_name(),  usr.getEmail(), usr.getPhone(), Type.USER));
                }
            } catch (ActivityRequiredException e) {
                log.error(e.getMessage());
            }
        }
        return Optional.of(uzer);
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

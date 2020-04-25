package com.activedge.usermgt.service;

import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.LdapUser;
import com.activedge.usermgt.model.dto.NewStaffDTO;
import com.activedge.usermgt.model.dto.StaffDTO;
import com.activedge.usermgt.repository.LdapUserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LdapService implements StaffService {

    @Autowired
    private LdapUserRepository ldapUserRepository;

    @Override
    public StaffDTO save(StaffDTO staffDTO) throws ActivityRequiredException, NotFoundException {
        LdapUser newUser = new LdapUser();
        ldapUserRepository.save(newUser);
        return null;
    }

    @Override
    public StaffDTO save(NewStaffDTO staffDTO) throws ActivityRequiredException {
        LdapUser newUser = new LdapUser(staffDTO.getEmail(),digestSHA(staffDTO.getPassword()));
        ldapUserRepository.save(newUser);
        return null;
    }

    @Override
    public Page<StaffDTO> findAll(Pageable pageable) {
        return ldapUserRepository.findAll();
    }

    @Override
    public Optional<StaffDTO> findOne(String id) {
        Optional<LdapUser> staff = ldapUserRepository.findById(id);
        return staff;
    }

    @Override
    public StaffDTO search(final String searchId) {
        return ldapUserRepository.findByUserid(searchId);
    }

    @Override
    public void delete(String id) {
        ldapUserRepository.deleteById(id);
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

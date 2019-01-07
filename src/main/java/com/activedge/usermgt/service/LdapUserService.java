package com.activedge.usermgt.service;

import com.activedge.usermgt.model.LdapUser;
import com.activedge.usermgt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LdapUserService {

    @Autowired
    private UserRepository userRepository;

    public Boolean authenticate(final String username, final String password) {
        LdapUser user = null; // userRepository.findByUsernameAndPassword(username, password);
        return user != null;
    }

    public List<String> search(final String username) {
        List<LdapUser> userList = null; // userRepository.findByUsernameLikeIgnoreCase(username);
        if (userList == null) {
            return Collections.emptyList();
        }

        return userList.stream()
                .map(LdapUser::getUsername)
                .collect(Collectors.toList());
    }

    public void create(final String username, final String password) {
        LdapUser newUser = new LdapUser(username,digestSHA(password));
        newUser.setId(LdapUtils.emptyLdapName());
//        userRepository.save(newUser);

    }

    public void modify(final String username, final String password) {
        LdapUser user = null; // userRepository.findByUsername(username);
        user.setPassword(password);
//        userRepository.save(user);
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

package com.activedge.usermgt.service;

import com.activedge.usermgt.model.LdapUser;
import com.activedge.usermgt.repository.LdapUserRepository;
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
    private LdapUserRepository ldapUserRepository;

    public Boolean authenticate(final String username, final String password) {
        LdapUser user = null;//ldapUserRepository.findByUseridAndPassword(username, password);
        return user != null;
    }

    public LdapUser getByUserid(final String userid) {
        LdapUser staff = ldapUserRepository.findByUserid(userid);
        return staff;
    }

    public LdapUser getByUsername(final String username) {
        LdapUser staff = ldapUserRepository.findByUsername(username);
        return staff;
    }

    public void getAll() {
        System.out.println("Getting all users...");
        ldapUserRepository.findAll().forEach(ldapUser -> {
            System.out.println(ldapUser);}
        );
        System.out.println("Done!");
    }

    public List<String> search(final String username) {
        List<LdapUser> userList = ldapUserRepository.findByUsernameLikeIgnoreCase(username);
        if (userList.isEmpty()) {
            System.out.println("Empty result ...");
            return Collections.emptyList();
        }

        return userList.stream()
                .map(LdapUser::getUsername)
                .collect(Collectors.toList());
    }

    public void create(final String username, final String password) {
        LdapUser newUser = new LdapUser(username,digestSHA(password));
        newUser.setId(LdapUtils.emptyLdapName());
        ldapUserRepository.save(newUser);

    }

    public void modify(final String username, final String password) {
        LdapUser user = ldapUserRepository.findByUsername(username);
//        user.setPassword(password);
        ldapUserRepository.save(user);
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

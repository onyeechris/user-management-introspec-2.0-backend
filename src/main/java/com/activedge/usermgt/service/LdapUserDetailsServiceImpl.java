package com.activedge.usermgt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
public class LdapUserDetailsServiceImpl implements LdapAuthoritiesPopulator {

    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations dirContextOperations, String username) {
        log.info(">>> Username is {} - DirContextOperations: {}", username, dirContextOperations);
//        UserEntity userEntity = userService.findByUsername(username);
//
//        if (userEntity == null) {
//            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
//        }
//        Collection<? extends GrantedAuthority> authorities = userAuthorityService.getGrantedAuthorities(userEntity.isAdmin());

        return null;
    }
}

package com.activedge.usermgt.service;

import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.repository.StaffRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class acts like a provider for the user;
 * meaning it loads the user from the database (or any data source).
 * It doesn’t do authentication. It just loads the user given his username.
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Staff> authUser = staffRepository.findOneWithAuthoritiesByEmail(username);

//        System.out.println(">>> findOneWithAuthoritiesByEmail: " + authUser.get());

        if(authUser.isPresent()) {
            List<GrantedAuthority> grantedAuthorities = authUser.get().getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                    .collect(Collectors.toList());
            return new User(authUser.get().getEmail(), authUser.get().getPassword(), authUser.get().isActivated(), true, true, true, grantedAuthorities);
        }

        throw new UsernameNotFoundException("Username: " + username + " not found");

    }

}

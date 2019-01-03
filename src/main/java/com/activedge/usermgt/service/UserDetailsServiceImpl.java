package com.activedge.usermgt.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * This class acts like a provider for the user;
 * meaning it loads the user from the database (or any data source).
 * It doesn’t do authentication. It just loads the user given his username.
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("UserDetailsService: " + username);
        // temporarily hard coding the users. All passwords must be encoded.
        final List<AppUser> users = Arrays.asList(
                new AppUser(1, "uzer", encoder.encode("01234"), "USER"),
                new AppUser(2, "admean", encoder.encode("56789"), "ADMIN")
        );

        // micmic account fetching...
        for(AppUser appUser: users) {
            if(appUser.getUsername().equals(username)) {
                try {
                    // append "ROLE_" to user roles as required by spring
                    List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                            .commaSeparatedStringToAuthorityList("ROLE_" + appUser.getRole());

                    // The "User" class is provided by Spring and represents a model class for user to be returned by UserDetailsService
                    // And used by auth manager to verify and check user authentication.
                    return new User(appUser.getUsername(), appUser.getPassword(), grantedAuthorities);
                } catch (Exception e){
                    System.out.println("Exception. User could not be authenticated! " + e.getMessage());
                }
            }
        }
        System.out.println("UserDetailsService could not be authenticated! " + username);
        // If user not found. Throw this exception.
        throw new UsernameNotFoundException("Username: " + username + " not found");
    }

    // A (temporary) class represent the user saved in the database or gotten from an external source.
    @Data
    private static class AppUser {
        private Integer id;
        private String username;
        private String password;
        private String role;

        public AppUser(Integer id, String username, String password, String role) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.role = role;
        }

    }

}

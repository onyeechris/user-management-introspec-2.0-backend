package com.activedge.usermgt.security;

import com.activedge.usermgt.repository.StaffRepository;
import com.activedge.usermgt.service.CustomUserDetailsService;
import com.activedge.usermgt.service.LdapUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@EnableWebSecurity
public class SecurityCredentials extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService; // loads the user from the database (or any data source) service.

    @Autowired
    private StaffRepository staffRepository;

    @Lazy
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Lazy
    @Autowired
    LdapUserService ldapUserService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthenticationFilter jwtAuthFilter() {
        return new JwtAuthenticationFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                // use stateless session; session won't be used to store user's state.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // handle an authorized attempts
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                // Add a filter to check token for secured resource
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/favicon.ico",
                        "/manifest.json",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/", "/index.html", "/static/**", "/swagger-ui.html**", "/v2/api-docs", "/webjars/**", "/swagger-resources/**", "/actuator/**", "favicon.ico").permitAll()

                .antMatchers(HttpMethod.GET, "/permissions/**").hasAnyRole("ADMIN", "DEV", "AUDITOR")
                .antMatchers("/permissions/**").hasRole("DEV")

                .antMatchers(HttpMethod.GET, "/groups/**").hasAnyRole("ADMIN", "DEV", "AUDITOR")
                .antMatchers("/groups/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET, "/staffs/**").hasAnyRole("ADMIN", "DEV", "AUDITOR")
                .antMatchers("/staffs/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET, "/appmodule/**").hasAnyRole("ADMIN", "DEV", "AUDITOR")
                .antMatchers("/appmodule/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET, "/userapps/**").hasAnyRole("ADMIN", "USER", "DEV", "AUDITOR")
                .antMatchers("/userapps/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET, "/management/audits/**").hasAnyRole("AUDITOR", "ADMIN")

                .antMatchers(HttpMethod.POST, "/auth").permitAll()

                // any other requests must be authenticated
                .anyRequest().authenticated();

    }

    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    // define the password encoder to be used by the auth manager to compare and verify passwords.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // check JDBC
        auth
            .userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

        // check AD
        /*
        auth
            .ldapAuthentication()
//                .userDnPatterns("uid={0},ou=users,ou=guests")
            .userSearchBase("ou=users")
            .userSearchFilter("uid={0}")
            .contextSource(contextSource())
            .passwordCompare()
//                .passwordEncoder()
            .passwordAttribute("mail");
            */

/*
        auth
            .ldapAuthentication()
            .userDnPatterns("uid={0},ou=people")
            .userSearchBase("ou=people")
            .userSearchFilter("uid={0}")
            .groupSearchBase("ou=group") // map LDAP groups to roles in Spring
//            .groupSearchFilter("uniqueMember={0}")
            .contextSource(contextSource())
//            .contextSource()
//                .url("ldap://localhost:12345/dc=memorynotfound,dc=com")
//                .and()
            .passwordCompare();
//            .passwordEncoder(new LdapShaPasswordEncoder())
//            .passwordEncoder(passwordEncoder())
//            .passwordAttribute("userPassword");
        */

    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LdapContextSource contextSource() {
//        return  new DefaultSpringSecurityContextSource(
//                Collections.singletonList("ldap://localhost:12345"), "dc=memorynotfound,dc=com");
//                Collections.singletonList("ldap://ldap.forumsys.com:389"), "dc=example,dc=com");
//                Collections.singletonList("ldap://www.zflexldap.com:389"), "cn=ro_admin,ou=sysadmins,dc=zflexsoftware,dc=com");
            LdapContextSource contextSource = new LdapContextSource();
            contextSource.setUrl("ldap://ldap.forumsys.com:389");
            contextSource.setBase("ou=guests,dc=zflexsoftware,dc=com");
            contextSource.setUserDn("cn=ro_admin,ou=sysadmins,dc=zflexsoftware,dc=com");
            contextSource.setPassword("zflexpass");
            return contextSource;
    }
}

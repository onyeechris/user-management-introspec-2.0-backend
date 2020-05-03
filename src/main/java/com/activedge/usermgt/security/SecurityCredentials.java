package com.activedge.usermgt.security;

import com.activedge.usermgt.repository.StaffRepository;
import com.activedge.usermgt.service.CustomUserDetailsService;
import com.activedge.usermgt.service.conditions.LdapCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
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
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private StaffRepository staffRepository;

    @Lazy
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private Environment env;

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

                .antMatchers(HttpMethod.GET, "/audit/**").hasAnyRole("AUDITOR", "ADMIN")

                .antMatchers(HttpMethod.POST, "/auth", "/auth/test-ldap").permitAll()

                // any other requests must be authenticated
                .anyRequest().authenticated();

    }

    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        if(env.getRequiredProperty("introspecsso.backend").equalsIgnoreCase("ldap")) {
            // check AD
            auth
                .ldapAuthentication()
                .userSearchBase("ou=people")
                .userSearchFilter("uid={0}")
                .groupSearchBase("ou=people") // Optional: map LDAP groups to roles in Spring
                .groupSearchFilter("member={0}")
                .contextSource(contextSource());
                //.passwordCompare()
                //.passwordEncoder(new LdapShaPasswordEncoder());
                //.passwordAttribute("userPass");
//        } else {
            // check JDBC
            auth
                .userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//        }
    }

    @Bean
    @Conditional(LdapCondition.class)
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Conditional(LdapCondition.class)
    public LdapContextSource contextSource() {
            LdapContextSource contextSource = new LdapContextSource();
//            contextSource.setUrl("ldap://www.zflexldap.com:389");
//            contextSource.setBase("ou=guests,dc=zflexsoftware,dc=com");
//            contextSource.setUserDn("cn=ro_admin,ou=sysadmins,dc=zflexsoftware,dc=com");
//            contextSource.setPassword("zflexpass");
//            contextSource.setUrl("ldap://ldap.forumsys.com:389");
//            contextSource.setBase("dc=example,dc=com");
//            contextSource.setUserDn("cn=read-only-admin,dc=example,dc=com");
//            contextSource.setPassword("password");
            contextSource.setUrl("ldap://localhost:389");
            contextSource.setBase("dc=planetexpress,dc=com");
            contextSource.setUserDn("cn=admin,dc=planetexpress,dc=com");
            contextSource.setPassword("GoodNewsEveryone"); // https://github.com/rroemhild/docker-test-openldap
            return contextSource;
    }
}

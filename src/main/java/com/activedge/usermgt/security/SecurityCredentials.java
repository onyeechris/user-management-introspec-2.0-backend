package com.activedge.usermgt.security;

import com.activedge.usermgt.repository.StaffRepository;
import com.activedge.usermgt.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
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
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.naming.NamingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

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
    private static final String[] AUTH_WHITE_LIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/introspec-cas/**",
            "/v2/api-docs/**",
            "/swagger-resources/**"
    };

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
                .antMatchers(AUTH_WHITE_LIST).permitAll()
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

//                .antMatchers(HttpMethod.GET, "/mfa/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/mfa/**").hasRole("ADMIN")
                .antMatchers("/enrol/**").hasAnyRole("PRE_VERIFICATION_USER","ADMIN", "USER", "DEV", "AUDITOR")

                .antMatchers(HttpMethod.POST, "/auth", "/auth/test-ldap", "/auth/yek_cne", "/auth/test-ldap-search").permitAll()

                // any other requests must be authenticated
                .anyRequest().authenticated();
    }

    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public LdapAuthoritiesPopulator ldapAuthoritiesPopulator() throws Exception {
        CustomLdapAuthoritiesPopulator populator = new CustomLdapAuthoritiesPopulator(contextSource(), env.getRequiredProperty("ldap.base"));
        populator.setIgnorePartialResultException(true);
        populator.setIgnoreNameNotFoundException(true);
        return populator;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
//            .authenticationProvider(customGoogleAuthenticatorConfig)
            .userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

        auth
            .ldapAuthentication()
            .userSearchBase(env.getRequiredProperty("ldap.ou"))
            .userSearchFilter(env.getRequiredProperty("ldap.filter") + "={0}")
            .ldapAuthoritiesPopulator(ldapAuthoritiesPopulator())
            .contextSource(contextSource());
    }

    @Bean
    @Primary
    public LdapTemplate ldapTemplate() throws Exception {
        LdapTemplate ldapTemplate = new LdapTemplate(contextSource());
        ldapTemplate.setIgnorePartialResultException(true);
        ldapTemplate.setIgnoreNameNotFoundException(true);
        ldapTemplate.afterPropertiesSet();
        return ldapTemplate;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LdapContextSource contextSource() throws NoSuchAlgorithmException, KeyManagementException, NamingException {
        Map<String, Object> props = new HashMap<>();
        props.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        props.put("java.naming.ldap.factory.socket", "com.activedge.usermgt.security.certs.MySSLSocketFactory");

        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(env.getRequiredProperty("ldap.url"));
        contextSource.setBase(env.getRequiredProperty("ldap.base"));
        contextSource.setUserDn(env.getRequiredProperty("ldap.user"));
        contextSource.setPassword(env.getRequiredProperty("ldap.password"));
        contextSource.setPooled(false);
        contextSource.setBaseEnvironmentProperties(props);
        contextSource.afterPropertiesSet();
        return contextSource;
    }
}

package com.activedge.usermgt.security;

import com.activedge.usermgt.config.JwtConfig;
import com.activedge.usermgt.repository.StaffRepository;
import com.activedge.usermgt.service.LdapUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@Slf4j
@EnableWebSecurity
public class SecurityCredentials extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService; // loads the user from the database (or any data source) service.

    @Autowired
    private JwtConfig jwtConfig; // jwt define config class

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Lazy
    @Autowired
    LdapUserService ldapUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                // use stateless session; session won't be used to store user's state.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // handle an authorized attempts
                .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> {
                    log.error("Error caught - Authentication failed for object {}, path:{}", e.getMessage(), req.getRequestURI());
                    rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                })
                .and()
                // Add a filter to validate user credentials and add token in the response header
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(staffRepository, authenticationManager(), jwtConfig, ldapUserService, encoder))

                // Add a filter to check token for secured resource
                .addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)

                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/", "/swagger-ui.html**", "/v2/api-docs", "/webjars/**", "/swagger-resources/**", "/actuator/**", "favicon.ico").permitAll()

                .antMatchers(HttpMethod.GET, "/auth-service/permissions/**").hasAnyRole("CHECKER", "MAKER", "DEV")
                .antMatchers("/auth-service/permissions/**").hasRole("DEV")

                .antMatchers(HttpMethod.GET, "/auth-service/groups/**").hasAnyRole("CHECKER", "MAKER", "DEV")
                .antMatchers(HttpMethod.POST, "/auth-service/groups/**").hasRole("MAKER")
                .antMatchers("/auth-service/groups/**").hasAnyRole("CHECKER", "MAKER")

                .antMatchers(HttpMethod.GET, "/auth-service/staff/**").hasAnyRole("CHECKER", "MAKER", "DEV")
                .antMatchers(HttpMethod.POST, "/auth-service/staff/**").hasRole("MAKER")
                .antMatchers("/auth-service/staff/**").hasAnyRole("CHECKER", "MAKER")

                .antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()

                // any other requests must be authenticated
                .anyRequest().authenticated().and().cors();

    }

    // define the password encoder to be used by the auth manager to compare and verify passwords.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

        auth
            .ldapAuthentication()
            .userDnPatterns("uid={0},ou=people")
            .userSearchBase("ou=people")
            .userSearchFilter("uid={0}")
            .groupSearchBase("ou=group") // map LDAP groups to roles in Spring
            .groupSearchFilter("uniqueMember={0}")
            .contextSource(contextSource())
            .passwordCompare()
            .passwordEncoder(new LdapShaPasswordEncoder())
            .passwordAttribute("userPassword");

    }

    @Bean
    public JwtConfig jwtConfig() {
        return new JwtConfig();
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
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedOrigins("*")
                        .allowedHeaders("*");
            }
        };
    }

    @Bean
    public DefaultSpringSecurityContextSource contextSource() {
        return  new DefaultSpringSecurityContextSource(
                Collections.singletonList("ldap://localhost:12345"), "dc=memorynotfound,dc=com");
    }

}

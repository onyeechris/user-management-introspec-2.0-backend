package com.activedge.usermgt.config;

import com.activedge.usermgt.security.JwtTokenAuthenticationFilter;
import com.sun.jndi.ldap.LdapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.authentication.AuthenticationManager;
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

@EnableWebSecurity
public class SecurityCredentials extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService; // loads the user from the database (or any data source) service.

    @Autowired
    private JwtConfig jwtConfig; // jwt define config class

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                // use stateless session; session won't be used to store user's state.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // handle an authorized attempts
                .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> {
                    System.out.print("Error caught - Authentication failed! " + e.getMessage());
                    rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                })
                .and()
                // Add a filter to validate user credentials and add token in the response header
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig))
                // Add a filter to check token for secured resource
                .addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/", "/swagger-ui.html**", "/v2/api-docs", "/webjars/**", "/swagger-resources/**", "/actuator/**", "favicon.ico").permitAll()
                .antMatchers(HttpMethod.GET, "/api/permissions/**").hasAnyRole("INTROSPEC-SYSADMIN", "INTROSPEC-SYSDEV")
                .antMatchers("/api/permissions/**").hasRole("INTROSPEC-SYSDEV")
                .antMatchers(HttpMethod.GET, "/api/groups/**").hasAnyRole("INTROSPEC-SYSADMIN", "INTROSPEC-SYSDEV")
                .antMatchers("/api/groups/**").hasRole("INTROSPEC-SYSADMIN")
                .antMatchers(HttpMethod.GET, "/api/staff/**").hasAnyRole("INTROSPEC-SYSADMIN", "INTROSPEC-SYSDEV")
                .antMatchers("/api/staff/**").hasRole("INTROSPEC-SYSADMIN")
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
            .groupSearchBase("ou=groups") // map LDAP groups to roles in Spring
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

//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .ldapAuthentication()
//                .userDnPatterns("uid={0},ou=people")
//                .userSearchBase("ou=people")
//                .userSearchFilter("uid={0}")
//                .groupSearchBase("ou=groups") // map LDAP groups to roles in Spring
//                .groupSearchFilter("uniqueMember={0}")
//                .contextSource(contextSource())
//                .passwordCompare()
//                .passwordEncoder(new LdapShaPasswordEncoder())
//                .passwordAttribute("userPassword");
//    }

//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Bean("oin")
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    @Bean
    public DefaultSpringSecurityContextSource contextSource() {
        return  new DefaultSpringSecurityContextSource(
                Collections.singletonList("ldap://localhost:12345"), "dc=memorynotfound,dc=com");
    }

}

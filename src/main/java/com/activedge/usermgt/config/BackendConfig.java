package com.activedge.usermgt.config;

import com.activedge.usermgt.service.backends.AuthService;
import com.activedge.usermgt.service.backends.DbBackend;
import com.activedge.usermgt.service.backends.LdapBackend;
import com.activedge.usermgt.service.backends.conditions.DbCondition;
import com.activedge.usermgt.service.backends.conditions.LdapCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BackendConfig {
    @Bean
    @Conditional(LdapCondition.class)
    public AuthService getLdapBackend(){
        return new LdapBackend();
    }

    @Bean
    @Conditional(DbCondition.class)
    public AuthService getDbBackend(){
        return new DbBackend();
    }
}
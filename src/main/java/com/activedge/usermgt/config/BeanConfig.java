package com.activedge.usermgt.config;

import com.activedge.usermgt.service.LdapService;
import com.activedge.usermgt.service.StaffService;
import com.activedge.usermgt.service.StaffServiceImpl;
import com.activedge.usermgt.service.conditions.DbCondition;
import com.activedge.usermgt.service.conditions.LdapCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    @Conditional(LdapCondition.class)
    public StaffService getLdapService(){
        return new LdapService();
    }

    @Bean
    @Conditional(DbCondition.class)
    public StaffService getDbService(){
        return new StaffServiceImpl();
    }
}

package com.activedge.usermgt.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "ldap", ignoreInvalidFields = true, ignoreUnknownFields = true)
@Getter
@Setter
public class LdapSetting {
    private String url;
    private String base;
    private String user;
    private String password;
    private String ou;
    private String filter;
}

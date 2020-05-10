package com.activedge.usermgt.security;

import org.springframework.ldap.core.ContextSource;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;

public class CustomLdapAuthoritiesPopulator extends DefaultLdapAuthoritiesPopulator {

    public CustomLdapAuthoritiesPopulator(ContextSource contextSource, String groupSearchBase) {
        super(contextSource, groupSearchBase);
    }

    public void setIgnoreNameNotFoundException(boolean ignore) {
        this.getLdapTemplate().setIgnoreNameNotFoundException(ignore);
    }

}

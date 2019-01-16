package com.activedge.usermgt.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_INTROSPEC-SYSADMIN";

    public static final String USER = "ROLE_INTROSPEC-SYSUSER";

    public static final String DEVELOPER = "ROLE_INTROSPEC-SYSDEV";

    private AuthoritiesConstants() {
    }
}

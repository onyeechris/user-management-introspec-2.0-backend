package com.activedge.usermgt.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String DEVELOPER = "ROLE_DEV";
    public static final String ROLE_PRE_VERIFICATION_USER = "ROLE_PRE_VERIFICATION_USER";

    private AuthoritiesConstants() {
    }
}

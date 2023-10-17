package com.activedge.usermgt.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final int PASSWORD_MIN_LENGTH = 6;

    public static final int PASSWORD_MAX_LENGTH = 100;

    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String FIND_BY_NAME_ADMIN = "Group Admins";
    public static final String FIND_BY_NAME_USERS = "Group Users";
    public static final String FIND_BY_NAME_AUDITORS = "Auditors";
    public static final String FIND_BY_NAME_INTROSPEC = "INTROSPEC-DEFAULT";

    public static final String PASSWORD_ENCRYPTION_KEY = "PASSWORD_ENCRYPTION_KEY";

    private Constants() {
    }
}

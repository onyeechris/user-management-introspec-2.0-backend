package com.activedge.usermgt.model.enumeration;

public enum Notification {
    WRONG_ACCOUNT_TYPE("Wrong Account Type"),
    UNAUTHORIZED_ACCOUNT("Unauthorized Account");

    private final String name;

    private Notification(String value) {
        this.name = value;
    }

    public String value() {
        return this.name;
    }

    @Override
    public String toString() {
        return name;
    }

}

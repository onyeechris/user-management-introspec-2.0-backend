package com.activedge.usermgt.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

@Component
public class SessionCountLogger {

    @Autowired
    private SessionRegistry sessionRegistry;

    public void logSessionCount() {
        int sessionCount = sessionRegistry.getAllPrincipals().size();
        System.out.println("Number of active sessions: " + sessionCount);
        // You can log the session count using your preferred logging framework
    }
}

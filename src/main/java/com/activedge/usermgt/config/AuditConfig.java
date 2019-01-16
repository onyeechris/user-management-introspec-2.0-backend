package com.activedge.usermgt.config;


import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditConfig implements AuditorAware<String>{

    @Override
    public Optional<String> getCurrentAuditor() {
        return SecurityContextHolder.getContext().getAuthentication() == null ? Optional.of("SYSTEM") : Optional.of(SecurityContextHolder.getContext().getAuthentication().getName());
    }

}

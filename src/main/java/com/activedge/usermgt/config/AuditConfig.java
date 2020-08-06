package com.activedge.usermgt.config;


import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import java.util.Optional;

@Component
public class AuditConfig implements AuditorAware<String>{

    @Override
    public Optional<String> getCurrentAuditor() {

        return SecurityContextHolder.getContext().getAuthentication() == null ? Optional.of("SYSTEM") : Optional.of(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Bean
    public Queue queue() {
        return new ActiveMQQueue("auditlog.queue");
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    public HttpTraceRepository httpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }

}

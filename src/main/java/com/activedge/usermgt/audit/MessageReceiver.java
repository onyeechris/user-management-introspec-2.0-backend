package com.activedge.usermgt.audit;

import com.activedge.usermgt.model.CustomHttpTrace;
import com.activedge.usermgt.repository.TraceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
class MessageReceiver {

    @Autowired
    private TraceRepository repository;

    @JmsListener(destination = "auditlog.queue")
    public void receiveQueue(CustomHttpTrace httpTrace) {

        repository.save(httpTrace);

    }

}
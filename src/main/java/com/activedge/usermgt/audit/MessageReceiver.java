package com.activedge.usermgt.audit;

import com.activedge.usermgt.model.log.ReqBody;
import com.activedge.usermgt.repository.ReqBodyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
class MessageReceiver {

    @Autowired
    private ReqBodyRepository repository;

    @JmsListener(destination = "auditlog.queue")
    public void receiveQueue(String s) {
        // Todo: Extract String into object
        System.out.println(s);
        String msg = s.replaceAll("; ", "--");
        String[] message = msg.split(";");
        ReqBody reqBody = new ReqBody();
        try {
            reqBody.setUrl(message[0]);
            reqBody.setClient(message[1]);
            reqBody.setUser(message[2]);
            reqBody.setHeaders(message[3]);
            reqBody.setPayload(message.length > 5 ? message[5] : "");
        } finally {
            reqBody.setDump(msg);
        }

        repository.save(reqBody);

    }
}
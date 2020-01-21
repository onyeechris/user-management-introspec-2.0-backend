package com.activedge.usermgt.model.event;

import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.enumeration.Action;
import com.activedge.usermgt.model.log.StaffLog;
import com.activedge.usermgt.service.BeanUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;
import java.io.IOException;

import static com.activedge.usermgt.model.enumeration.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

@Slf4j
public class StaffEntityListener {

    private static ObjectMapper mapper = null;

    @Value("${makerChecker.enabled}")
    private Boolean mc_enabled;

    @PrePersist
    public void prePersist(Staff target) throws ActivityRequiredException, JsonProcessingException, IOException {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(Staff target) throws JsonProcessingException, ActivityRequiredException, IOException {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(Staff target) throws ActivityRequiredException {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(Staff target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new StaffLog(target, action));
    }

}

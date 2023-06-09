package com.activedge.usermgt.model.event;

import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.Group;
import com.activedge.usermgt.model.enumeration.Action;
import com.activedge.usermgt.model.log.GroupLog;
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
public class GroupEntityListener {

    private static ObjectMapper mapper = null;

    @Value("${makerChecker.enabled}")
    private Boolean mc_enabled;

    @PrePersist
    public void prePersist(Group target) throws ActivityRequiredException, JsonProcessingException, IOException {
        System.out.println("...@PrePersist");
        perform(target, INSERTED);

    }

    @PreUpdate
    public void preUpdate(Group target) throws JsonProcessingException, ActivityRequiredException, IOException {
        System.out.println("...@PreUpdate");
        perform(target, UPDATED);

    }

    @PreRemove
    public void preRemove(Group target) throws ActivityRequiredException {
        System.out.println("...@preRemove()");
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(Group target, Action action) {
        log.info("About to save group ... {}", target);
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new GroupLog(target, action));
    }

}

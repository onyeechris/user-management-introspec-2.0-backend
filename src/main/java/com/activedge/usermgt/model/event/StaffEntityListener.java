package com.activedge.usermgt.model.event;

import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.enumeration.Action;
import com.activedge.usermgt.model.log.StaffLog;
import com.activedge.usermgt.repository.StaffLogRepository;
import com.activedge.usermgt.service.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.activedge.usermgt.model.enumeration.Action.DELETED;
import static com.activedge.usermgt.model.enumeration.Action.INSERTED;
import static com.activedge.usermgt.model.enumeration.Action.UPDATED;
import static javax.transaction.Transactional.TxType.MANDATORY;

public class StaffEntityListener {

    @PrePersist
    public void prePersist(Staff target) {
        System.out.println("...In prePersist()");
        perform(target, INSERTED);
    }
    @PreUpdate
    public void preUpdate(Staff target) {
        System.out.println("...In preUpdate()");
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(Staff target) {
        System.out.println("...In preRemove()");
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(Staff target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new StaffLog(target, action));
    }
}


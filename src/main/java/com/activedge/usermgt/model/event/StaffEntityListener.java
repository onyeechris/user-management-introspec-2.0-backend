package com.activedge.usermgt.model.event;

import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.enumeration.Action;
import com.activedge.usermgt.model.log.MakerItem;
import com.activedge.usermgt.model.log.StaffLog;
import com.activedge.usermgt.repository.StaffLogRepository;
import com.activedge.usermgt.repository.redis.MakerItemRepository;
import com.activedge.usermgt.security.SecurityUtils;
import com.activedge.usermgt.service.BeanUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;
import javax.validation.ValidationException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.activedge.usermgt.model.enumeration.Action.DELETED;
import static com.activedge.usermgt.model.enumeration.Action.INSERTED;
import static com.activedge.usermgt.model.enumeration.Action.UPDATED;
import static javax.transaction.Transactional.TxType.MANDATORY;

@Slf4j
public class StaffEntityListener {

    @Autowired
    private MakerItemRepository makerItemRepository;

    private static MakerItem makerItem = null;
    private static ObjectMapper mapper = null;

    @Value("${makerChecker.enabled}")
    private Boolean mc_enabled;

    @PrePersist
    public void prePersist(Staff target) throws ActivityRequiredException, JsonProcessingException {
        perform(target, INSERTED);

        if(mc_enabled) {
            if(SecurityUtils.isCurrentUserInRole("ROLE_MAKER") && !target.isAuthorized()) {
                add2queue("CREATE STAFF", target);

                throw new ActivityRequiredException("CREATE staff still pending. CHECKER action required!");
            } else if(SecurityUtils.isCurrentUserInRole("ROLE_CHECKER") && target.getRedisKey() != null && target.getRedisKey().matches("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b")) {
                if(makerItemRepository.findById(target.getRedisKey()).isPresent()) {
                    makerItemRepository.deleteById(target.getRedisKey());
                    target.setRedisKey(null);
                } else {
                    throw new ValidationException("Oops! no pending record found for ref[" + target.getRedisKey() +"]");
                }
            } else {
                throw new ValidationException("Oops! you don't have the ROLE(MAKER) to create a transaction.");
            }
        }

    }

    @PreUpdate
    public void preUpdate(Staff target) throws JsonProcessingException, ActivityRequiredException {
        perform(target, UPDATED);

        if(mc_enabled) {
            if(SecurityUtils.isCurrentUserInRole("ROLE_MAKER") && target.getRedisKey() == null) {
                add2queue("UPDATE STAFF", target);
                throw new ActivityRequiredException("UPDATE staff still pending. CHECKER action required!");
            } else if (SecurityUtils.isCurrentUserInRole("ROLE_CHECKER") && target.getRedisKey() != null && target.getRedisKey().matches("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b")) {
                if(makerItemRepository.findById(target.getRedisKey()).isPresent()) {
                    makerItemRepository.deleteById(target.getRedisKey());
                    target.setRedisKey(null);
                } else {
                    throw new ValidationException("Oops! no pending record found for ref[" + target.getRedisKey() +"]");
                }
            } else {
                throw new ValidationException("Oops! you don't have the ROLE(CHECKER) to UPDATE a transaction.");
            }
        }

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

    private void add2queue(String action, Object obj) throws JsonProcessingException {
        MakerItem makerItem = getMakerItem();
        makerItem.setId(UUID.randomUUID().toString()+":"+SecurityUtils.getCurrentUserLogin().get());
        makerItem.setAction(action);
        makerItem.setPayload(getMapper().writeValueAsString(obj)); // //JSON from String to Object: Staff obj = mapper.readValue(jsonInString, Staff.class);
        makerItem.setMaker(SecurityUtils.getCurrentUserLogin().get());
        makerItem.setAt(java.time.LocalDateTime.now());
        makerItemRepository.save(makerItem);

        log.info("Redis ref: {}", makerItem);

    }

    private static MakerItem getMakerItem() {
        if(makerItem == null) {
            makerItem = new MakerItem();
        }
        return makerItem;
    }

    private static ObjectMapper getMapper() {
        if(mapper == null) {
            mapper = new ObjectMapper();
        }
        return mapper;
    }

}

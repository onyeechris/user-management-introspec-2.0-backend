package com.activedge.usermgt.model.event;

import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.enumeration.Action;
import com.activedge.usermgt.model.log.StaffLog;
import com.activedge.usermgt.service.BeanUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        System.out.println("...@PrePersist");
        /*
        if(mc_enabled) {
            if(SecurityUtils.isCurrentUserInRole("ROLE_MAKER")) {
                add2queue("CREATE STAFF", target);

                throw new ActivityRequiredException("CREATE staff still pending. CHECKER action required!");
            } else if(SecurityUtils.isCurrentUserInRole("ROLE_CHECKER") && target.getRedisKey() != null && target.getRedisKey().matches("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b")) {
                Optional<MakerItem> makerItem = makerItemRepository.findById(target.getRedisKey());
                if(makerItem.isPresent()) {
                    Staff staff = mapper.readValue(makerItem.get().getPayload(), Staff.class);
                    Set<Authority> authorities = staff.getAuthorities();
                    Group group = staff.getGroup();
                    group.setPermissions(null);
                    group.setStaff(null);
                    System.out.println("Authorities: " + authorities + " Group: " + group);
                    staff.setAuthorities(authorities);
                    staff.setGroup(group);
//                    target.setPassword(staff.getPassword());
                    target = staff;
                    System.out.println("maker is ... " + makerItem.get().getMaker());
                    target.setCreatedBy(makerItem.get().getMaker());
                    makerItemRepository.deleteById(makerItem.get().getId());
//                    target.setRedisKey(null);
                } else {
                    throw new ValidationException("Oops! no pending/todo record found for ref[" + target.getRedisKey() +"]");
                }
            } else {
                throw new ValidationException("Oops! you don't have the ROLE(MAKER) to create a transaction.");
            }
        }
        */
        perform(target, INSERTED);

    }

    @PreUpdate
    public void preUpdate(Staff target) throws JsonProcessingException, ActivityRequiredException, IOException {
        System.out.println("...@PreUpdate");
        /*
        if(mc_enabled) {
            if(SecurityUtils.isCurrentUserInRole("ROLE_MAKER") && target.getRedisKey() == null) {
                log.info("adding update staff to log...{}", target);
                add2queue("UPDATE STAFF", target);
                throw new ActivityRequiredException("UPDATE staff still pending. CHECKER action required!");
            } else if (SecurityUtils.isCurrentUserInRole("ROLE_CHECKER") && target.getRedisKey() != null && target.getRedisKey().matches("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b")) {
                log.info("checking... update staff to log...{}", target);
                Optional<MakerItem> makerItem = makerItemRepository.findById(target.getRedisKey());
                if(makerItem.isPresent()) {
                    makerItemRepository.deleteById(target.getRedisKey());
                    target.setRedisKey(null);
                } else {
                    throw new ValidationException("Oops! no pending record found for ref[" + target.getRedisKey() + "]");
                }
            } else {
                throw new ValidationException("Oops! you don't have the ROLE(CHECKER) to UPDATE a transaction.");
            }
        }
        */

        perform(target, UPDATED);

    }

    @PreRemove
    public void preRemove(Staff target) throws ActivityRequiredException {
        System.out.println("...@preRemove()");
//        if(!SecurityUtils.isCurrentUserInRole("ROLE_CHECKER")) {
//            throw new ActivityRequiredException("Only Supervisors can delete!");
//        }
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(Staff target, Action action) {
        log.info("About to save staff ... {}", target);
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new StaffLog(target, action));
    }

}

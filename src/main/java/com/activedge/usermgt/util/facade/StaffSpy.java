package com.activedge.usermgt.util.facade;

import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.model.log.MakerItem;
import com.activedge.usermgt.repository.redis.MakerItemRepository;
import com.activedge.usermgt.security.SecurityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ValidationException;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class StaffSpy extends Spy implements RedisQueue {

    private MakerItemRepository makerItemRepository;

    private Staff staff;

    public StaffSpy(Staff staff, MakerItemRepository makerItemRepository) {
        this.staff = staff;
        this.makerItemRepository = makerItemRepository;
    }

    @Override
    void logRequest() throws ActivityRequiredException {
        log.info("Logging staff request...{}", this.staff);
        String action = this.staff.getId() != null ? "UPDATE STAFF" : "CREATE STAFF";
        add2Queue(action, this.staff);
        throw new ActivityRequiredException("Staff request still pending. CHECKER action required!");
    }

    @Override
    void approveRequest() {
        log.info("Approving staff request... {}", this.staff);
        if(this.staff.getRedisKey() != null && this.staff.getRedisKey().matches("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b")) {
            Optional<MakerItem> makerItem = makerItemRepository.findById(this.staff.getRedisKey());
            if(makerItem.isPresent()) {
                Staff staf = null;
                try {
                    staf = getMapper().readValue(makerItem.get().getPayload(), Staff.class);
                } catch (IOException e){e.printStackTrace();}
                log.info("staf is {}", staf);
                this.staff.setId(staf.getId());
                this.staff.setFirstName(staf.getFirstName());
                this.staff.setLastName(staf.getLastName());
                this.staff.setPhone(staf.getPhone());
                this.staff.setEmail(staf.getEmail());
                this.staff.setPassword(staf.getPassword());
                this.staff.setGroups(staf.getGroups());
                this.staff.setHireDate(staf.getHireDate());
                this.staff.setMakerChecker(staf.getMakerChecker());
                this.staff.setActivated(staf.isActivated());
                this.staff.setAuthorities(staf.getAuthorities());
                this.staff.setRedisKey(staf.getRedisKey());
                if(staf.getId() == null) {
                    // creating...
                    this.staff.setCreatedBy(makerItem.get().getMaker());
                    this.staff.setCreatedDate(makerItem.get().getAt());
                } else {
                    // updating...
                    this.staff.setLastModifiedBy(makerItem.get().getMaker());
                    this.staff.setLastModifiedDate(makerItem.get().getAt());
                }
                // delete this log from redis
                delete4rmQueue(makerItem.get().getId());
            } else {
                throw new ValidationException("Oops! no pending/todo record found for ref[" + this.staff.getRedisKey() +"]");
            }
        } else {
            throw new ValidationException("Oops! wrong or incorrect redis key.");
        }
    }

    @Override
    public void add2Queue(String action, Object target) {
        log.info("Action:{} - target:{}", action, target);
        MakerItem makerItem = getMakerItem();
        makerItem.setId(UUID.randomUUID().toString());
        makerItem.setAction(action);
        try {
            makerItem.setPayload(getMapper().writeValueAsString(target)); // //JSON from String to Object: Staff obj = mapper.readValue(jsonInString, Staff.class);
        } catch (JsonProcessingException e) { e.printStackTrace(); }
        makerItem.setMaker(SecurityUtils.getCurrentUserLogin().get());
        makerItem.setAt(java.time.LocalDateTime.now());
        makerItemRepository.save(makerItem);

        log.info("Redis ref: {}", makerItem);
    }

    @Override
    public void delete4rmQueue(String id) {
        log.info("Deleting from queue...");
        makerItemRepository.deleteById(id);
    }

}

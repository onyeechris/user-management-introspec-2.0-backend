package com.activedge.usermgt.util.facade;

import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.security.SecurityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class StaffSpy extends Spy implements RedisQueue {

    private Staff staff;

    public StaffSpy(Staff staff) {
        this.staff = staff;
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
        /*
        if(this.staff.getRedisKey() != null && this.staff.getRedisKey().matches("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b")) {
            Optional<MakerItem> makerItem = makerItemRepository.findById(this.staff.getRedisKey());
            if(makerItem.isPresent()) {
                Staff staf = null;
                try {
                    staf = getMapper().readValue(makerItem.get().getPayload(), Staff.class);
                } catch (IOException e){e.printStackTrace();}
                log.info("staf is {}", staf);
                this.staff.setId(staf.getId());
                this.staff.setFirst_name(staf.getFirst_name());
                this.staff.setLast_name(staf.getLast_name());
                this.staff.setPhone(staf.getPhone());
                this.staff.setEmail(staf.getEmail());
                this.staff.setPassword(staf.getPassword());
                this.staff.setGroups(staf.getGroups());
                this.staff.setHireDate(staf.getHireDate());
                this.staff.setType(staf.getType());
                this.staff.setActivated(staf.isActivated());
                this.staff.setAuthorities(staf.getAuthorities());
                this.staff.setRedisKey(staf.getRedisKey());
                if(staf.getId() == null) {
                    // creating...
//                    this.staff.setCreatedBy(makerItem.get().getMaker());
//                    this.staff.setCreatedDate(makerItem.get().getAt());
                } else {
                    // updating...
//                    this.staff.setLastModifiedBy(makerItem.get().getMaker());
//                    this.staff.setLastModifiedDate(makerItem.get().getAt());
                }
                // delete this log from redis
                delete4rmQueue(makerItem.get().getId());
            } else {
                throw new ValidationException("Oops! no pending/todo record found for ref[" + this.staff.getRedisKey() +"]");
            }
        } else {
            throw new ValidationException("Oops! wrong or incorrect redis key.");
        }
        */
    }

    @Override
    public void add2Queue(String action, Object target) {
        log.info("Action:{} - target:{}", action, target);
    }

    @Override
    public void delete4rmQueue(String id) {
        log.info("Deleting from queue...");
    }

}

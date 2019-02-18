package com.activedge.usermgt.util.facade;

import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.Group;
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
public class GroupSpy extends Spy implements RedisQueue {

    private MakerItemRepository makerItemRepository;

    private Group group;

    public GroupSpy(Group group, MakerItemRepository makerItemRepository) {
        this.makerItemRepository = makerItemRepository;
        this.group = group;
    }

    @Override
    void logRequest() throws ActivityRequiredException {
        log.info("Logging group request..." + this.group);
        String action = this.group.getId() != null ? "UPDATE GROUP" : "CREATE GROUP";
        add2Queue(action, this.group);
        throw new ActivityRequiredException("Group request still pending. CHECKER action required!");
    }

    @Override
    void approveRequest() {
        log.info("Approving group request... {}", this.group);
        if(this.group.getRedisKey() != null && this.group.getRedisKey().matches("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b")) {
            Optional<MakerItem> makerItem = makerItemRepository.findById(this.group.getRedisKey());
            if(makerItem.isPresent()) {
                Group grp = null;
                try {
                    grp = getMapper().readValue(makerItem.get().getPayload(), Group.class);
                } catch (IOException e){e.printStackTrace();}

                this.group.setId(grp.getId());
                this.group.setName(grp.getName());
                this.group.setDescription(grp.getDescription());
                this.group.setPermissions(grp.getPermissions());
                if(grp.getId() == null) {
                    // creating...
                    this.group.setCreatedBy(makerItem.get().getMaker());
                    this.group.setCreatedDate(makerItem.get().getAt());
                } else {
                    // updating...
                    this.group.setLastModifiedBy(makerItem.get().getMaker());
                    this.group.setLastModifiedDate(makerItem.get().getAt());
                }
                // delete this log from redis
                delete4rmQueue(makerItem.get().getId());
            } else {
                throw new ValidationException("Oops! no pending/todo record found for ref[" + this.group.getRedisKey() +"]");
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
            makerItem.setPayload(getMapper().writeValueAsString(target));
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

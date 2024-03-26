package com.activedge.usermgt.service;

import com.activedge.usermgt.license.License;
import com.activedge.usermgt.model.Group;
import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.repository.GroupRepository;
import com.activedge.usermgt.repository.LicenseRepository;
import com.activedge.usermgt.repository.StaffRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LicenseService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private GroupService groupService;

//    public boolean isExpiredAndUpdateGroup(String LicenseId) {
//        License license = licenseRepository.findFirstByOrderByIdAsc().orElse(null);
//            if (license == null) {
//                log.error("No license found for this Application");
//                return false;
//            }
//            if(license.isExpired()){
//                //update Group of a Staff
//                Group limitedUserGroup = groupRepository.findByName("Limited Users");
//                if (limitedUserGroup != null){
//                    Iterable<Staff> staffIterable = staffRepository.findAll();
//                    List<Staff> staffMembers = new ArrayList<>();
//                    staffIterable.forEach(staffMembers::add); // Convert to List
//                    for (Staff staff : staffMembers){
//                        staff.setGroups(Collections.singleton(limitedUserGroup));
//                        staffRepository.save(staff);
//                    }
//                    log.info("Updated group of all staff members to 'Limited User' due to license expiration.");
//                }else {
//                    log.error("Limited User group not found.");
//                    return false;
//                }
//            }
//
//        return license.isExpired();
//    }

    public boolean isExpiredAndUpdateGroup(String licenseId) {
        Optional<License> licenseOptional = licenseRepository.findFirstByOrderByIdAsc();
        if (!licenseOptional.isPresent()) {
            log.error("No license found for this Application");
            return false;
        }

        License license = licenseOptional.get();
        if (license.isExpired()) {
            // Assuming "Limited User" is the new group when the license is expired
            boolean groupUpdated = groupService.updateGroupsByUsername("username", "Limited Users");

            if (groupUpdated) {
                log.info("Staff group updated to Limited User due to expired license");
            } else {
                log.error("Failed to update staff group to Limited User");
            }
        }

        return license.isExpired();
    }
}
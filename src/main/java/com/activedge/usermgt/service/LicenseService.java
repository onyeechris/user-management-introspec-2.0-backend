package com.activedge.usermgt.service;

import com.activedge.usermgt.license.EncryptionService;
import com.activedge.usermgt.license.License;
import com.activedge.usermgt.model.Group;
import com.activedge.usermgt.model.Staff;
import com.activedge.usermgt.repository.GroupRepository;
import com.activedge.usermgt.repository.LicenseRepository;
import com.activedge.usermgt.repository.StaffRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    @Autowired
    private EncryptionService encryptionService;

    public boolean isExpired(License license) {
        LocalDate expiryWithGrace = getExpiryWithGrace(license);
        if (expiryWithGrace == null) {
            // Error occurred while calculating expiry with grace
            return false;
        }

        // Check if the current date is after the new expiry date
        return LocalDate.now().isAfter(expiryWithGrace);
    }

    public LocalDate getExpiryWithGrace(License license) {
        // Ensure expiry and grace are not null or empty
        if (license.getExpiry() == null || license.getExpiry().isEmpty() ||
                license.getGrace() == null || license.getGrace().isEmpty()) {
            log.error("Expiry or grace is null or empty");
            return null; // or throw an exception
        }

        // Decrypt expiry and grace values
        String decryptedExpiry;
        String decryptedGrace;
        try {
            decryptedExpiry = encryptionService.decrypt(license.getExpiry());
            decryptedGrace = encryptionService.decrypt(license.getGrace());
        } catch (Exception e) {
            // Handle decryption error
            log.error("Error decrypting expiry or grace", e);
            return null; // or throw an exception
        }

        // Parse expiry date from string to LocalDate
        LocalDate expiryDate;
        try {
            expiryDate = LocalDate.parse(decryptedExpiry, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            // Handle parsing error
            log.error("Error parsing expiry date", e);
            return null; // or throw an exception
        }

        // Parse grace period from string to long
        long gracePeriod;
        try {
            gracePeriod = Long.parseLong(decryptedGrace);
        } catch (NumberFormatException e) {
            // Handle parsing error
            log.error("Error parsing grace period", e);
            return null; // or throw an exception
        }

        // Calculate the new expiry date with grace period
        LocalDate expiryWithGrace = expiryDate.plusDays(gracePeriod);
        log.info("Expiry date with grace: {}", expiryWithGrace);

        return expiryWithGrace;
    }


    public boolean isExpiredAndUpdateGroup(String username) {
        Optional<License> licenseOptional = licenseRepository.findFirstByOrderByIdAsc();
        if (!licenseOptional.isPresent()) {
            log.error("No license found for this Application");
            return false;
        }

        License license = licenseOptional.get();
        if (isExpired(license)) {

            // Assuming "Limited User" is the new group when the license is expired
            boolean groupUpdated = groupService.updateGroupsByUsername(username, "Limited Users");

            if (groupUpdated) {
                log.info("Staff group updated to Limited User due to expired license");
            } else {
                log.error("Failed to update staff group to Limited User");
            }
        }

        return isExpired(license);
    }
}
package com.activedge.usermgt.service;

import com.activedge.usermgt.license.License;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
public class NotificationService {

    private LicenseService licenseService;

    @Autowired
    public NotificationService(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    public void sendNotification(License license) {
        LocalDate expiryWithGrace = licenseService.getExpiryWithGrace(license);
        if (expiryWithGrace != null) {
            // Calculate days until expiry
            long daysToExpire = LocalDate.now().until(expiryWithGrace).getDays();
            log.info("days to expire is: " + daysToExpire + " days.");
            long daysExpired = expiryWithGrace.until(LocalDate.now()).getDays();

            // Construct notification message
            String message = daysToExpire == 0 ? "Your license expires today." :
                    daysToExpire == 1 ? "Your license expires tomorrow." :
                            daysToExpire < 0 ? "Your license expired " + Math.abs(daysExpired) + " days ago." :
                                    "Your license will expire in " + daysToExpire + " days.";

            // Send notification message
            log.info("Sending notification: {}", message);
        } else {
            log.error("Unable to calculate expiry with grace for license {}", license.getId());
        }
    }

}

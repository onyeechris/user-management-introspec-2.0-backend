package com.activedge.usermgt.license;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Document(collection = "license")
@Getter @Setter @ToString
public class License {

    private EncryptionService encryptionService;

    @Id
    private String id;

    @Column(name = "type")
    private String type;

    @Column(name = "no_of_users")
    private String no_of_users;

    @Column(name = "hardware")
    private String hardware;

    @Column(name = "unit_charge")
    private String unit_charge;

    @Column(name = "expiry")
    private String expiry;

    @Column(name = "total_price")
    private String total_price;

    @Column(name = "grace")
    private String grace;

    @Column(name = "status")
    private String status;

    @Column(name = "partial_access")
    private String partial_access;

    @Column(name = "license")
    private String licence;

    @Column(name = "updated_at")
    private String updated_at;



//    public boolean isExpired() {
//
//        if (encryptionService == null) {
//            log.error("Encryption service is not initialized");
//            throw new IllegalStateException("Encryption service is not initialized");
//        }
//
//        // Decrypt expiry and grace values
//        String decryptedExpiry;
//        String decryptedGrace;
//        try {
//            log.info("expiry details in try method: " + expiry);
//            decryptedExpiry = encryptionService.decrypt(expiry);
//            decryptedGrace = encryptionService.decrypt(grace);
//        } catch (Exception e) {
//            // Handle decryption error
//            log.error("Error decrypting expiry or grace", e);
//            return false; // or throw an exception
//        }
//
//        // Parse expiry date from string to LocalDate
//        LocalDate expiryDate = LocalDate.parse(decryptedExpiry, DateTimeFormatter.ISO_DATE);
//
//        // Parse grace period from string to long
//        long gracePeriod;
//        try {
//            gracePeriod = Long.parseLong(decryptedGrace);
//        } catch (NumberFormatException e) {
//            // Handle parsing error
//            log.error("Error parsing grace period", e);
//            return false; // or throw an exception
//        }
//
//        // Calculate the new expiry date with grace period
//        LocalDate expiryWithGrace = expiryDate.plusDays(gracePeriod);
//        log.info(expiryWithGrace +" is the date");
//
//        // Check if the current date is after the new expiry date
//        return LocalDate.now().isAfter(expiryWithGrace);
//
//    }

}

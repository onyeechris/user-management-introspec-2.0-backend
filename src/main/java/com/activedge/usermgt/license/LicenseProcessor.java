package com.activedge.usermgt.license;

public class LicenseProcessor {
    public static void processLicense(License myData) {
        String licenseId = myData.getId();
        String licenseKey = myData.getLicence();
        String expirationDate = myData.getExpiry();

        // Add your logic to process the license information
        // Example: licenseService.processLicense(licenseId, licenseKey, expirationDate);
    }
}

package com.activedge.usermgt.license;

public class LicenseProcessor {
    public static void processLicense(License license) {
        String licenseId = license.getId();
        String licenseKey = license.getLicence();
        String expirationDate = license.getExpiry();

        // Add your logic to process the license information
        // Example: licenseService.processLicense(licenseId, licenseKey, expirationDate);
    }
}

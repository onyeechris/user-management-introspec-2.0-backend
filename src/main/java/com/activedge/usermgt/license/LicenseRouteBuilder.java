package com.activedge.usermgt.license;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;
import org.apache.camel.builder.RouteBuilder;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class LicenseRouteBuilder extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        from("file:/home/adedamolababatunde/Documents/personal files/license?delete=true")
                .choice()
                .when(header("CamelFileNameOnly").startsWith("Bank GPG"))
                .process(this::decryptLicenseFile)
                .when(header("CamelFileNameOnly").startsWith("KEY-Bank"))
                .process(this::readEncryptionKey)
                .otherwise()
                .log("Unknown file type: ${header.CamelFileNameOnly}")
                .end();
    }

    private void decryptLicenseFile(Exchange exchange) throws Exception {
        // Extract necessary data from the exchange
        byte[] encryptedData = exchange.getIn().getBody(byte[].class);

        // Retrieve the encryption key from the exchange properties
        String encryptionKey = exchange.getProperty("encryptionKey", String.class);

        byte[] decryptedData = DecryptionUtility.decryptLicense(encryptedData, encryptionKey);

        // Example: Print a message after decryption
        System.out.println("License file decrypted successfully.");

        // Set the decrypted data as the new body of the exchange
        exchange.getIn().setBody(decryptedData);
    }

    private void readEncryptionKey(Exchange exchange) throws Exception {
        // Read the contents of the KEY-Bank file
        String keyFilePath = exchange.getIn().getHeader(Exchange.FILE_PATH, String.class);

        String encryptionKey = new String(Files.readAllBytes(Paths.get(keyFilePath)));

        // Store the encryption key in exchange properties for later use
        exchange.setProperty("encryptionKey", encryptionKey);

        // Example: Print a message after reading the key
        System.out.println("Encryption key read successfully.");
    }
}



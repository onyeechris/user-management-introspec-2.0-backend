package com.activedge.usermgt.license;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;
import org.apache.camel.builder.RouteBuilder;

@Component
public class LicenseRouteBuilder extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        from("file:/home/adedamolababatunde/Documents/personal files/license?delete=true")
                .choice()
                .when(header("CamelFileNameOnly").startsWith("Bank GPG"))
                .process(this::decryptLicenseFile)
                .when(header("CamelFileNameOnly").startsWith("KEY-Bank"))
                .process(this::decryptKeyFile)
                .otherwise()
                .log("Unknown file type: ${header.CamelFileNameOnly}")
                .end();
    }

    private void decryptLicenseFile(Exchange exchange) throws Exception {
        // Extract necessary data from the exchange
        byte[] encryptedData = exchange.getIn().getBody(byte[].class);
        // Assuming the key is hardcoded for simplicity; replace with secure key management
        byte[] keyBytes = "YourLicenseDecryptionKey".getBytes();

        // Perform license decryption logic...
        byte[] decryptedData = yourDecryptionMethod(encryptedData, keyBytes);

        // Example: Print a message after decryption
        System.out.println("License file decrypted successfully.");

        // Set the decrypted data as the new body of the exchange
        exchange.getIn().setBody(decryptedData);
    }

    private void decryptKeyFile(Exchange exchange) throws Exception {
        // Extract necessary data from the exchange
        byte[] encryptedData = exchange.getIn().getBody(byte[].class);
        // Assuming the key is hardcoded for simplicity; replace with secure key management
        byte[] keyBytes = "YourKeyDecryptionKey".getBytes();

        // Perform key decryption logic...
        byte[] decryptedData = yourDecryptionMethod(encryptedData, keyBytes);

        // Example: Print a message after decryption
        System.out.println("Key file decrypted successfully.");

        // Set the decrypted data as the new body of the exchange
        exchange.getIn().setBody(decryptedData);
    }

    // Replace this with your actual decryption method
    private byte[] yourDecryptionMethod(byte[] encryptedData, byte[] keyBytes) {
        // Implement your decryption logic using the provided key
        // For example, you can use a cryptographic library like Bouncy Castle or Java's javax.crypto
        // This is a simplified example, and you should replace it with your actual decryption code
        // ...

        return decryptedData;
    }
}


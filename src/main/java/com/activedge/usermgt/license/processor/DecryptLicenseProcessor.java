package com.activedge.usermgt.license.processor;

import com.activedge.usermgt.license.DecryptionUtility;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class DecryptLicenseProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        log.info("Decryption Processor");

            // Extract necessary data from the exchange
            byte[] encryptedData = exchange.getIn().getBody(byte[].class);

            // Retrieve the encryption key from the exchange properties
            String encryptionKey = exchange.getProperty("encryptionKey", String.class);

            byte[] decryptedData = DecryptionUtility.decryptLicense(encryptedData, encryptionKey);

            // Example: Print a message after decryption
            System.out.println("License file decrypted successfully.");
            log.info("License file decrypted successfully.");

            // Set the decrypted data as the new body of the exchange
            exchange.getIn().setBody(decryptedData);
        }
    }
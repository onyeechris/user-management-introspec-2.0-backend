package com.activedge.usermgt.license;

import com.activedge.usermgt.repository.LicenseRepository;
import com.google.gson.Gson;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LicenseRouteBuilder extends RouteBuilder {
    Logger logger = LoggerFactory.getLogger(LicenseRouteBuilder.class);

    @Autowired
    private EncryptionService encryptionService;

    @Value("${file.location}")
    private String fileLocation;

    @Autowired
    private LicenseRepository licenseRepository;


    @Override
    public void configure() throws Exception {
        from("file:" + fileLocation)
                .routeId("fileRoute")
                .log("File received: ${headers.CamelFileName}")
                .choice()
                .when(header("CamelFileName").startsWith("Eco-Bank"))
                        .log("Processing file: ${headers.CamelFileName}")
                        .log("File content before unmarshal: ${body}")
                        .unmarshal().json(JsonLibrary.Gson, Map.class) // Use Gson for JSON parsing
                        .log("File content after unmarshal: ${body}")
                        .process(exchange -> {
                            Message message = exchange.getMessage();  // Use getMessage() instead of getIn()
                            Map<String, Object> jsonData = message.getBody(Map.class);
                            // Encrypt specific fields
                            encryptField(jsonData, "no_of_users");
                            encryptField(jsonData, "expiry");
                            encryptField(jsonData, "total_price");
                            encryptField(jsonData, "grace");
                            encryptField(jsonData, "partial_access");
                            encryptField(jsonData, "licence");

                            // Additional logging to output the body of the message
                            logger.info("File content: {}", jsonData);

                            // Convert the Map back to JSON for MongoDB
                            String encryptedJson = new Gson().toJson(jsonData);
                            // Save to MongoDB
                            License license = new Gson().fromJson(encryptedJson, License.class);
                            licenseRepository.save(license);
                        })
                        .otherwise()
                        .log("Skipping file: ${headers.CamelFileName}")
                        .end();
    }

private void encryptField(Map<String, Object> jsonData, String fieldName) {
    if (jsonData.containsKey(fieldName)) {
        try {
            Object fieldValue = jsonData.get(fieldName);

            // Check the type of the field value
            if (fieldValue instanceof String) {
                // If it's a String, encrypt it
                String encryptedValue = encryptionService.encrypt((String) fieldValue);
                jsonData.put(fieldName, encryptedValue);
            } else if (fieldValue instanceof Double) {
                // If it's a BigDecimal, convert to String and then encrypt
                String stringValue = fieldValue.toString();
                String encryptedValue = encryptionService.encrypt(stringValue);
                jsonData.put(fieldName, encryptedValue);
            } else {
                // Handle other data types if needed
                logger.warn("Unsupported data type for field '{}'", fieldName);
            }
        } catch (Exception e) {
            // Handle encryption exception
            logger.error("Error encrypting field '{}'", fieldName, e);
        }
    }
}
}




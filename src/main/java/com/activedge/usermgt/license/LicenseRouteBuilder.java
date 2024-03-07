package com.activedge.usermgt.license;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LicenseRouteBuilder extends RouteBuilder {
    Logger logger = LoggerFactory.getLogger(LicenseRouteBuilder.class);

    @Override
    public void configure() throws Exception {
        from("file:{{file.location}}")
                .routeId("fileRoute")
                .log("File received: ${headers.CamelFileName}")
                .choice()
                .when(header("CamelFileName").startsWith("Eco Bank"))
                        .log("Processing file: ${headers.CamelFileName}")
                        .unmarshal().json(JsonLibrary.Gson, Map.class) // Use Gson for JSON parsing
                        .process(exchange -> {
                            // Access the parsed JSON data
                            Message message = exchange.getMessage();  // Use getMessage() instead of getIn()
                            Map<String, Object> jsonData = message.getBody(Map.class);
                            // Further processing...
                        })
                        .otherwise()
                        .log("Skipping file: ${headers.CamelFileName}")
                        .end();
    }
}




//                .convertBodyTo(String.class) // Ensure the body is a String
//                .process(exchange -> {
//                    String jsonString = exchange.getIn().getBody(String.class);
//
//                    // Use the JsonParser class to parse the JSON
//                    License license = JsonParser.parseJson(jsonString);
//
//                    // Access individual fields
//                    String type = license.getType();
//                    String numberOfUsers = license.getNo_of_users();
//                    Integer hardware = license.getHardware();
//
//                    // Move the logic to save data to MongoDB into the JsonParser class
//                    JsonParser.saveDataToMongoDB(license);
//
//                    // Process license information using the LicenseProcessor class
//                    LicenseProcessor.processLicense(license);
//                })
//                .log("Data saved to MongoDB and license processed"); // Log a message after processing


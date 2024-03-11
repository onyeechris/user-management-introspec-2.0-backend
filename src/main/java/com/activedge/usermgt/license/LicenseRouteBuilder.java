package com.activedge.usermgt.license;

import com.activedge.usermgt.repository.LicenseRepository;
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
                            License license = exchange.getIn().getBody(License.class);
                            // Access the parsed JSON data
                            Message message = exchange.getMessage();  // Use getMessage() instead of getIn()
                            Map<String, Object> jsonData = message.getBody(Map.class);
                            // Further processing...

                            // Additional logging to output the body of the message
                            logger.info("File content: {}", jsonData);
                        })
                        .otherwise()
                        .log("Skipping file: ${headers.CamelFileName}")
                        .end();
    }
}




package com.activedge.usermgt.license;

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
        from("file:/path/to/listen")
                .routeId("fileRoute")
                .log("File received: ${headers.CamelFileName}")
                .unmarshal().json(JsonLibrary.Gson, Map.class) // Use Gson for JSON parsing
                .process(exchange -> {
                    // Access the parsed JSON data
                    Map<String, Object> jsonData = exchange.getIn().getBody(Map.class);

                    // Access individual fields
                    String type = (String) jsonData.get("type");
                    String numberOfUsers = (String) jsonData.get("no_of_users");
                    Integer hardware = (Integer) jsonData.get("hardware");

                    // Your encryption logic here

                    // Set the encrypted data back to the body
                });
}
}

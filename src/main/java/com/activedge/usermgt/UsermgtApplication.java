package com.activedge.usermgt;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

import java.util.UUID;

import static com.activedge.usermgt.config.Constants.PASSWORD_ENCRYPTION_KEY;

@SpringBootApplication
@EnableJms
@OpenAPIDefinition(info = @Info(title = "Introspec CAS", version = "2.0", description = "Central Authentication System with single-sign-on, ldap and 2FA enabled"))
@SecurityScheme(name = "introspec-cas", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class UsermgtApplication implements CommandLineRunner {

    @Value("${spring.application.name}")
    String contextPath;

    public static void main(String[] args) {
            System.setProperty(PASSWORD_ENCRYPTION_KEY, UUID.randomUUID().toString());
            SpringApplication application = new SpringApplication(UsermgtApplication.class);
            application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
            System.out.println("............................... Application Started ............................................ ");
            System.out.println("............................... Logs :: ${project.basedir}/logs ................................ ");
            System.out.println("............................... Documentation :: http://localhost:9100/" + contextPath + " ............ ");
    }
}
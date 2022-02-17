package com.activedge.usermgt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

import java.util.UUID;

import static com.activedge.usermgt.config.Constants.PASSWORD_ENCRYPTION_KEY;

@SpringBootApplication
@EnableJms
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
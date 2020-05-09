package com.activedge.usermgt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class UsermgtApplication {

        public static void main(String[] args) {
                SpringApplication application = new SpringApplication(UsermgtApplication.class);
                application.run(args);
        }

}
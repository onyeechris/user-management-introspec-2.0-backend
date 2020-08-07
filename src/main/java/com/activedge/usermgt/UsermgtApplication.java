package com.activedge.usermgt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class UsermgtApplication implements CommandLineRunner {

        public static void main(String[] args) {
                SpringApplication application = new SpringApplication(UsermgtApplication.class);
                application.run(args);
        }

        @Override
        public void run(String... args) throws Exception {
                System.out.println();
                System.out.println("............................... Application Started. See fileLog details ................................ ");
        }
}
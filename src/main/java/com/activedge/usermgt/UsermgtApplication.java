package com.activedge.usermgt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

import java.io.IOException;
import java.sql.SQLException;

@SpringBootApplication
@EnableJms
public class UsermgtApplication {

        public static void main(String[] args) throws SQLException, IOException {
                SpringApplication application = new SpringApplication(UsermgtApplication.class);
                application.run(args);
        }

}
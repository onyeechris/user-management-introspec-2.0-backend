package com.activedge.usermgt;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.Queue;
import java.io.IOException;
import java.sql.SQLException;

@SpringBootApplication
@EnableJms
public class UsermgtApplication {

        public static void main(String[] args) throws SQLException, IOException {
                SpringApplication application = new SpringApplication(UsermgtApplication.class);
                application.run(args);
        }

        @Bean
        public Queue queue() {
                return new ActiveMQQueue("auditlog.queue");
        }

}
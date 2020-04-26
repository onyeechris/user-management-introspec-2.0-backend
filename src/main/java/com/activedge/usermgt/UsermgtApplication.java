package com.activedge.usermgt;

import com.activedge.usermgt.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class UsermgtApplication implements CommandLineRunner {

        @Autowired
        @Qualifier("ldap_service")
        private StaffService ldapService;

        public static void main(String[] args) {
                SpringApplication application = new SpringApplication(UsermgtApplication.class);
                application.run(args);
        }

        @Override
        public void run(String... args) throws Exception {
                System.out.println(ldapService.findAll(null));
        }
}
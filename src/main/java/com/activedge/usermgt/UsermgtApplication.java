package com.activedge.usermgt;

import com.activedge.usermgt.model.dto.NewStaffDTO;
import com.activedge.usermgt.model.dto.StaffDTO;
import com.activedge.usermgt.model.enumeration.Type;
import com.activedge.usermgt.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

import java.util.List;

@SpringBootApplication
@EnableJms
public class UsermgtApplication implements CommandLineRunner {
        @Autowired
        @Qualifier("ldap_service")
        private StaffService ldapService;

        @Autowired
        @Qualifier("db_service")
        private StaffService staffService;

        public static void main(String[] args) {
                SpringApplication application = new SpringApplication(UsermgtApplication.class);
                application.run(args);
        }

        @Override
        public void run(String... args) throws Exception {
                System.out.println("Importing Ldap User(s)...");
                List<StaffDTO> staffs = ldapService.findAll(null).getContent();
                for(StaffDTO usr: staffs) {
                        if(!staffService.search(usr.getPhone()).isPresent()) {
                                staffService.save(new NewStaffDTO(usr.getPhone(), usr.getFirst_name(), usr.getLast_name(), usr.getEmail(), usr.getPhone(), Type.USER));
                        }
                }
                System.out.println("Ldap User(s) imported!");
        }
}
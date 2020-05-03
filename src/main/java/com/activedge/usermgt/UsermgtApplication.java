package com.activedge.usermgt;

import com.activedge.usermgt.service.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
@Slf4j
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
                /*
                String sourceHost = "localhost";
                String sourcePort = "389";
                String sourceBase = "dc=planetexpress,dc=com";
                String sourceBindAccount = "cn=admin,dc=planetexpress,dc=com";
                String sourcePassword = "GoodNewsEveryone";
                String base = "ou=people";
                String filter = "uid=leela";
                String protocol = "ldap";
                try {
                        log.info("Connecting to LDAP " + sourceHost + ":" + sourcePort + "...");
                        LdapContextSource sourceLdapCtx = new LdapContextSource();
                        sourceLdapCtx.setUrl(protocol + "://" + sourceHost + ":" + sourcePort + "/");
                        sourceLdapCtx.setUserDn(sourceBindAccount);
                        sourceLdapCtx.setBase(sourceBase);
                        sourceLdapCtx.setPassword(sourcePassword);
                        sourceLdapCtx.setDirObjectFactory(DefaultDirObjectFactory.class);
                        sourceLdapCtx.afterPropertiesSet();
                        LdapTemplate ldapTemplate = new LdapTemplate(sourceLdapCtx);
                        // Authenticate:
                        ldapTemplate.getContextSource().getContext(sourceBindAccount, sourcePassword);
                        log.info("....Authenticated !");
                        List ls = ldapTemplate.search(
                                base,
                                filter,
                                (AttributesMapper) attrs -> {
                                        List<String> memberof = new ArrayList();
                                        for (Enumeration vals = attrs.getAll(); vals.hasMoreElements();) {
                                                memberof.add(vals.nextElement().toString());
                                        }
                                        return memberof;
                                });
//                                (AttributesMapper<String>) attrs -> (String) attrs
//                                        .get("cn")
//                                        .get());
                        System.out.println(ls);
                } catch (Exception e) {
                        throw new Exception("Failed to connect to LDAP - " + e.getMessage(), e);
                }
                */


                /*
                System.out.println("Importing Ldap User(s)...");
                List<StaffDTO> staffs = ldapService.findAll(null).getContent();
                for(StaffDTO usr: staffs) {
                        if(!staffService.search(usr.getPhone()).isPresent()) {
                                staffService.save(new NewStaffDTO(usr.getPhone(), usr.getFirst_name(), usr.getLast_name(), usr.getEmail(), usr.getPhone(), Type.USER));
                        }
                }
                System.out.println("Ldap User(s) imported!");
                */
        }
}
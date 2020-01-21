package com.activedge.usermgt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.IOException;
import java.sql.SQLException;

@EnableJpaAuditing
@SpringBootApplication
public class UsermgtApplication implements WebMvcConfigurer {
        public static void main(String[] args) throws SQLException, IOException {
                SpringApplication application = new SpringApplication(UsermgtApplication.class);
                application.run(args);
        }
}
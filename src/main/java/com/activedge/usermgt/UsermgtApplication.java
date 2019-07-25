package com.activedge.usermgt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

@EnableJpaAuditing
@SpringBootApplication
//public class UsermgtApplication extends WebMvcConfigurerAdapter {
public class UsermgtApplication implements WebMvcConfigurer {

	public static void main(String[] args) throws SQLException {

        SpringApplication application = new SpringApplication(UsermgtApplication.class);
        // little hack to initialize schemas on first application startup
        DataSource dataSource  = DataSourceBuilder
                .create()
                .username("postgres")
                .password("passadmin")
                .url("jdbc:postgresql://localhost:5432/usermgt")
                .driverClassName("org.postgresql.Driver")
                .build();

        Properties properties = new Properties();

        String initialize = dataSource.getConnection().getMetaData().getTables(null, null, "staff", null).next() ? "never" : "always";

        properties.put("spring.datasource.initialization-mode", initialize);

        application.setDefaultProperties(properties);
        application.run(args);

	}

//	@Override
//	public void addViewControllers(final ViewControllerRegistry registry) {
//		super.addViewControllers(registry);
//		registry.addViewController("/home").setViewName("forward:/"); // forward to react defined route
//		registry.addViewController("/user/*").setViewName("forward:/"); // forward to react defined route
//	}

}
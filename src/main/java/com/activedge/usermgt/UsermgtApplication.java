package com.activedge.usermgt;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;

@EnableJpaAuditing
@SpringBootApplication
// public class UsermgtApplication extends WebMvcConfigurerAdapter {
public class UsermgtApplication implements WebMvcConfigurer {

        public static void main(String[] args) throws SQLException, IOException {

                SpringApplication application = new SpringApplication(UsermgtApplication.class);

                Properties prop = new Properties();

                // load a properties file
                prop.load(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/application.yml"));

                // little hack to initialize schemas on first application startup
                HikariConfig hikariConfig = new HikariConfig();
                hikariConfig.setDriverClassName(prop.getProperty("datasource.driver-class-name"));
                hikariConfig.setJdbcUrl(prop.getProperty("datasource.url"));
                hikariConfig.setUsername(prop.getProperty("datasource.username"));
                hikariConfig.setPassword(prop.getProperty("datasource.password"));

                hikariConfig.setMaximumPoolSize(5);
                hikariConfig.setMinimumIdle(3);

                HikariDataSource dataSource = new HikariDataSource(hikariConfig);

//                DataSource dataSource = DataSourceBuilder.create()
//                        .username("postgres")
//                        .password("passadmin")
//                        .url("jdbc:postgresql://localhost:5432/usermgt?maximum-pool-size=2")
//                        .driverClassName("org.postgresql.Driver").build();

                Properties properties = new Properties();

                String initialize = dataSource.getConnection().getMetaData().getTables(null, null, "staff", null).next()
                                ? "never"
                                : "always";

                properties.put("spring.datasource.initialization-mode", initialize);

                application.setDefaultProperties(properties);
                application.run(args);

        }

        // @Override
        // public void addViewControllers(final ViewControllerRegistry registry) {
        // super.addViewControllers(registry);
        // registry.addViewController("/home").setViewName("forward:/"); // forward to
        // react defined route
        // registry.addViewController("/user/*").setViewName("forward:/"); // forward to
        // react defined route
        // }

}
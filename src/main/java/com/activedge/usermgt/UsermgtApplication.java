package com.activedge.usermgt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableJpaAuditing
@EnableJpaRepositories
@SpringBootApplication
public class UsermgtApplication extends WebMvcConfigurerAdapter {
//public class UsermgtApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(UsermgtApplication.class, args);
	}

//	@Override
//	public void addViewControllers(final ViewControllerRegistry registry) {
//		super.addViewControllers(registry);
//		registry.addViewController("/home").setViewName("forward:/"); // forward to react defined route
//		registry.addViewController("/user/*").setViewName("forward:/"); // forward to react defined route
//	}

}

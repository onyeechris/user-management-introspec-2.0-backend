package com.activedge.usermgt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.activedge.usermgt.controller")) // RequestHandlerSelectors.any()
                .paths(regex("/.*|/auth")) // generate documentation only for the path starting with /product ELSE PathSelectors.any()
                .build()
                .apiInfo(metaData());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//        registry.addResourceHandler("/static/**")
//                .addResourceLocations("classpath:/resources/static/");
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("IntroSpec User Management REST API")
                .description("Single SignOn Module for all Introspec Application")
                .version("0.0.1")
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .contact(new Contact("ActivEdge Support", "http://www.activedgetechnologies.com/SitePages/Web/about.html", "support@activedgetechnologies.com"))
                .build();
    }

    @Override
    public void addViewControllers (ViewControllerRegistry registry) {
        RedirectViewControllerRegistration r = registry.addRedirectViewController("/", "/swagger-ui.html");
        r.setStatusCode(HttpStatus.SEE_OTHER);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add( new PageableHandlerMethodArgumentResolver());
    }

}

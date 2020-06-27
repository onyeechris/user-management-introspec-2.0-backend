package com.activedge.usermgt.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Configuration
public class CorsConfig implements Filter {

    @Value("#{'${cors.allowed_origins}'.split(',')}")
    private Set<String> origins;

    @Value("#{'${cors.allowed_headers}'.split(',')}")
    private Set<String> headers;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        Set<String> extOrigins = origins.stream().map(x -> x.split("://|:")[1]).collect(Collectors.toSet());

        if(extOrigins.contains(request.getServerName())){
            chain.doFilter(request, response);
        } else {
            throw new java.io.InvalidObjectException("Header injection exception thrown");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void destroy() { }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(false);
        config.setAllowedOrigins(new ArrayList<>(getOrigins()));
        config.setAllowedMethods(Arrays.asList("POST", "OPTIONS", "GET", "DELETE", "PUT"));
        config.setAllowedHeaders(new ArrayList<>(getHeaders()));
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
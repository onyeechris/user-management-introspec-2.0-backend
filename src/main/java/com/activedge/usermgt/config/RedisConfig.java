package com.activedge.usermgt.config;


import com.github.caryyu.spring.embedded.redisserver.RedisServerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
//@EnableRedisRepositories(basePackages = "com.activedge.usermgt.repository.redis")
public class RedisConfig {

    @Bean
    public RedisServerConfiguration redisServerConfiguration() {
        return new RedisServerConfiguration();
    }

    /*
    private RedisServer redisServer;

    @Value("${spring.redis.port}")
    private int redisPort;

    @PostConstruct
    public void startRedis() throws IOException {
        redisServer = new RedisServer(redisPort);
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }
    */

}

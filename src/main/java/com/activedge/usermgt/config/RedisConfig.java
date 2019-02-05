package com.activedge.usermgt.config;


import com.github.caryyu.spring.embedded.redisserver.RedisServerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
//@EnableRedisRepositories(basePackages = "com.activedge.usermgt.repository.redis")
public class RedisConfig {

//    @Value("${spring.redis.port}")
//    private int redisPort;

    @Value("${spring.redis.port:6380}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct public void startRedis() throws IOException, URISyntaxException {
        //redisPort = org.springframework.util.SocketUtils.findAvailableTcpPort(6380);
        // RedisExecProvider customRedisExec = RedisExecProvider.defaultProvider().override
        // (OsArchitecture.detect().os(), OsArchitecture.detect().arch(), "redis-server-2.8.19.exe");
         redisServer = RedisServer.builder()
                 .port(redisPort) //.redisExecProvider(customRedisExec) //com.github.kstyrc (not com.orange.redis-embedded)
         .setting("maxmemory 128M") //maxheap 128M
         .build();

         redisServer.start();
    }

    @PreDestroy public void stopRedis() throws InterruptedException {
        if (redisServer != null) {
            redisServer.stop();
        }
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

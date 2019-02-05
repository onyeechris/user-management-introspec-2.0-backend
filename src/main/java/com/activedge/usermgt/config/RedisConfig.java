package com.activedge.usermgt.config;


import com.github.caryyu.spring.embedded.redisserver.RedisServerConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import redis.clients.jedis.JedisPoolConfig;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
//@EnableRedisRepositories(basePackages = "com.activedge.usermgt.repository.redis")
public class RedisConfig implements DisposableBean, EnvironmentAware, InitializingBean {

    private Log log = LogFactory.getLog(this.getClass());
    private RedisServer redisServer;
    private Environment environment;

    public int getPort() {
        int v = environment.getProperty("spring.redis.port",Integer.class,0);
        v = v == 0 ? environment.getProperty("global.redis.port",Integer.class,6379) : v;
        return v;
    }

    private boolean isEmbedded() {
        Boolean v = environment.getProperty("spring.redis.embedded",Boolean.class,null);
        v = v == null ? environment.getProperty("global.redis.embedded",Boolean.class,false) : v;
        return v;
    }

    @Override
    public void destroy() throws Exception {
        if(redisServer != null) {
            redisServer.stop();
            redisServer = null;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!isEmbedded()) {
            return;
        }

        try {
            int port = getPort();
            redisServer = new RedisServer(port);
            redisServer.start();

            if(log.isInfoEnabled()) {
                log.info("Starting local embedded redis server successfully, port is " + port);
            }
        } catch (IOException e) {
            throw new FatalBeanException("Failed to start local embedded redis server ", e);
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
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

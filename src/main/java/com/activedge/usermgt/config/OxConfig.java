package com.activedge.usermgt.config;

import com.mongodb.Mongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ox.engine.Ox;
import ox.engine.exception.InvalidMongoConfiguration;

@Profile("mongo")
@Configuration
public class OxConfig {

    @Bean
    @Autowired
    public Ox init(Mongo mongo) throws InvalidMongoConfiguration {

        Ox ox = Ox.setUp(mongo, "com.activedge.usermgt.model.ox.migrations", "usermgt");
        ox.up();
        return ox;

    }
}

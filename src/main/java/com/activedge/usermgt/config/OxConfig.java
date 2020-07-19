package com.activedge.usermgt.config;

import com.mongodb.Mongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

import ox.engine.Ox;
import ox.engine.exception.InvalidMongoConfiguration;

@Profile("mongo")
@Configuration
public class OxConfig {
    @Autowired
    MongoTemplate mongoTemplate;

    @Bean
    @Autowired
    public Ox init(Mongo mongo) throws InvalidMongoConfiguration {

        Ox ox = Ox.setUp(mongo, "com.activedge.usermgt.model.ox.migrations", mongoTemplate.getDb().getName());
        ox.up();
        return ox;

    }
}

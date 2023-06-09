package com.activedge.usermgt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Profile("mongo")
@Configuration
@EnableMongoAuditing
@EnableMongoRepositories({"com.activedge.usermgt.repository", "com.activedge.usermgt.config"})
public class MongoConfig {
}

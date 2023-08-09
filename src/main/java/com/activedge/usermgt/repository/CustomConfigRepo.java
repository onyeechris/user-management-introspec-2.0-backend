package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.CustomConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomConfigRepo extends MongoRepository<CustomConfig, String> {
}

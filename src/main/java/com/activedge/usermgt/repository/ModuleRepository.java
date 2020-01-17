package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.Module;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModuleRepository extends PagingAndSortingRepository<Module, String> {

    @Profile("jpa")
    @Query("select modules from Module modules left join fetch modules.staffModules where modules.id =:id")
    @org.springframework.data.mongodb.repository.Query(value = "{'name': ?0}")
    Optional<Module> findOneWithEagerRelationships(@Param("id") String id);

}

package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, String> {

    @Query("select modules from Module modules left join fetch modules.staffModules where modules.id =:id")
    Optional<Module> findOneWithEagerRelationships(@Param("id") String id);

}

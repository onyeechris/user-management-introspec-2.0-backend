package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupsRepository extends JpaRepository<Group, Long> {

    @Query(value = "select distinct groups from Group groups left join fetch groups.permissions",
            countQuery = "select count(distinct groups) from Group groups")
    Page<Group> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct groups from Group groups left join fetch groups.permissions")
    List<Group> findAllWithEagerRelationships();

    @Query("select groups from Group groups left join fetch groups.permissions where groups.id =:id")
    Optional<Group> findOneWithEagerRelationships(@Param("id") Long id);

}

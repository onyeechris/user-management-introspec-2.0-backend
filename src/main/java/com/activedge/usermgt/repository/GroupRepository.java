package com.activedge.usermgt.repository;

import com.activedge.usermgt.model.Group;
import com.activedge.usermgt.model.GroupPK;
import com.activedge.usermgt.model.Staff;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends PagingAndSortingRepository<Group, GroupPK> {

    @Profile("jpa")
    @Query(value = "select distinct groups from Group groups left join fetch groups.permissions",
            countQuery = "select count(distinct groups) from Group groups")
    @org.springframework.data.mongodb.repository.Query(value = "{}")
    Page<Group> findAllWithEagerRelationships(Pageable pageable);

    @Profile("jpa")
    @Query(value = "select distinct groups from Group groups left join fetch groups.permissions")
    @org.springframework.data.mongodb.repository.Query(value = "{}")
    List<Group> findAllWithEagerRelationships();

    @Profile("jpa")
    @Query("select groups from Group groups left join fetch groups.permissions where groups.id =:id")
    @org.springframework.data.mongodb.repository.Query(value = "{'_id': ?0}")
    Optional<Group> findOneWithEagerRelationships(@Param("id") GroupPK id);

    @org.springframework.data.mongodb.repository.Query(value = "{'_id': ?0}")
    Optional<Group> findAllById(@Param("id") GroupPK id);

    Page<Group> findAllByModule_Id(String module, Pageable pageable);

    List<Group> findAllByModule_IdAndStaffsContains(String module, Staff staff);

}

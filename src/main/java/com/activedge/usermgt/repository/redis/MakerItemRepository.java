package com.activedge.usermgt.repository.redis;


import com.activedge.usermgt.model.log.MakerItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.repository.query.RedisQueryCreator;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MakerItemRepository extends JpaRepository<MakerItem, String> {

//    @Query("select m from MakerItem m where m.maker = :maker order by  m.at")
//    List<MakerItem> findAllByMaker(@Param("maker") String maker);
    Page<MakerItem> findAllByMaker(String maker, Pageable request);

}

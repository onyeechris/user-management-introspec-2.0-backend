package com.activedge.usermgt.repository;


import com.activedge.usermgt.model.log.ReqBody;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReqBodyRepository extends PagingAndSortingRepository<ReqBody, String> {

}

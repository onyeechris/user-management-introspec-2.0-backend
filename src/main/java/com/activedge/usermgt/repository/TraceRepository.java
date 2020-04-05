package com.activedge.usermgt.repository;


import com.activedge.usermgt.model.CustomHttpTrace;
import com.activedge.usermgt.model.log.ReqBody;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraceRepository extends PagingAndSortingRepository<CustomHttpTrace, String> {

}

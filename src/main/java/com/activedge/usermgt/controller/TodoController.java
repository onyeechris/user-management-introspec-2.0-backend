package com.activedge.usermgt.controller;

import com.activedge.usermgt.controller.util.PaginationUtil;
import com.activedge.usermgt.controller.util.ResponseWrapper;
import com.activedge.usermgt.model.log.MakerItem;
import com.activedge.usermgt.repository.redis.MakerItemRepository;
import com.activedge.usermgt.security.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/api")
@Api(value="todo", description="Operations to fetching CHECKER items list")
public class TodoController {

    private static final String ENTITY_NAME = "todos";

    @Autowired
    MakerItemRepository makerItemRepository;

    private final Logger log = LoggerFactory.getLogger(StaffController.class);

    @GetMapping("/"+ENTITY_NAME)
    @ApiOperation(value = "Get all existing "+ENTITY_NAME+" logs.")
    public ResponseEntity<ResponseWrapper> getAllTodo(Pageable pageable) {
        log.debug("REST request to get a page of "+ENTITY_NAME);

        Page<MakerItem> makerItems;

        if(SecurityUtils.isCurrentUserInRole("ROLE_CHECKER")) {
            // get logs by all MAKERS for any CHECKER to approve
            makerItems = makerItemRepository.findAll(pageable);
        }  else if(SecurityUtils.isCurrentUserInRole("ROLE_MAKER")) {
            // get logs for a MAKER. A MAKER should only see his/her todo request.
            makerItems = makerItemRepository.findAllByMaker(SecurityUtils.getCurrentUserLogin().get(), pageable);
        } else {
            makerItems = new Page<MakerItem>() {
                @Override
                public int getTotalPages() {
                    return 0;
                }

                @Override
                public long getTotalElements() {
                    return 0;
                }

                @Override
                public <U> Page<U> map(Function<? super MakerItem, ? extends U> function) {
                    return null;
                }

                @Override
                public int getNumber() {
                    return 0;
                }

                @Override
                public int getSize() {
                    return 0;
                }

                @Override
                public int getNumberOfElements() {
                    return 0;
                }

                @Override
                public List<MakerItem> getContent() {
                    return null;
                }

                @Override
                public boolean hasContent() {
                    return false;
                }

                @Override
                public Sort getSort() {
                    return null;
                }

                @Override
                public boolean isFirst() {
                    return false;
                }

                @Override
                public boolean isLast() {
                    return false;
                }

                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public boolean hasPrevious() {
                    return false;
                }

                @Override
                public Pageable nextPageable() {
                    return null;
                }

                @Override
                public Pageable previousPageable() {
                    return null;
                }

                @Override
                public Iterator<MakerItem> iterator() {
                    return null;
                }
            };
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(makerItems, "/api/"+ENTITY_NAME);

        return new ResponseEntity<>(new ResponseWrapper(makerItems), headers, HttpStatus.OK);
    }

}

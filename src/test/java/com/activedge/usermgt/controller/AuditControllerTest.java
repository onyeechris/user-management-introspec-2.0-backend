package com.activedge.usermgt.controller;

import com.activedge.usermgt.MockMvcBase;
import com.activedge.usermgt.model.CustomHttpTrace;
import com.activedge.usermgt.service.TraceService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AuditControllerTest extends MockMvcBase {

    @MockBean
    private TraceService traceService;


    @Test
    void fetchAllTracelogTest() throws Exception {

        when(this.traceService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(new CustomHttpTrace())));

        this.mockMvc.perform(get("/audit?page=1&size=15")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void fetchTracelogByStatusTest() throws Exception {

        when(this.traceService.findAllByStatus(anyInt(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(new CustomHttpTrace())));

        this.mockMvc
                .perform(RestDocumentationRequestBuilders.get("/audit/{status}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}

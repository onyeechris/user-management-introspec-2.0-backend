package com.activedge.usermgt.controller.integration;

import com.activedge.usermgt.MockMvcBase;
import com.activedge.usermgt.controller.AuditController;
import com.activedge.usermgt.model.CustomHttpTrace;
import com.activedge.usermgt.service.TraceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuditController.class)
public class AuditControllerTest extends MockMvcBase {

    @MockBean
    private TraceService traceService;

    CustomHttpTrace trace1;

    CustomHttpTrace trace2;

    @BeforeEach
    public void setUp() {
        trace1 = new CustomHttpTrace.CustomHttpTraceBuilder()
                .timestamp(new Date())
                .status(401)
                .username("user")
                .sourceIp("127.0.0.1")
                .path("/")
                .method("GET")
                .payload("")
                .rawBody("Test body")
                .build();

        trace2 = new CustomHttpTrace.CustomHttpTraceBuilder()
                .timestamp(new Date())
                .status(401)
                .username("tester")
                .sourceIp("198.8.243.1")
                .path("/status")
                .method("GET")
                .payload("")
                .rawBody("Raw body")
                .build();

    }

    @Test
    void fetchAllTracelogTest() throws Exception {

        when(this.traceService.findAll(any(Date.class), any(Date.class), any(Pageable.class))).thenReturn(new PageImpl<>(Arrays.asList(trace1, trace2)));

        this.mockMvc.perform(get("/audit?page=1&size=15&from=2020-12-31&to=2021-12-31")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void downloadAllTracelogTest() throws Exception {

        when(this.traceService.findAllByStatusAndDateRange(anyInt(), any(Date.class), any(Date.class), any(Pageable.class))).thenReturn(new PageImpl<>(new ArrayList<>()));

        MvcResult result = this.mockMvc.perform(get("/audit/download?status=0&page=1&size=15&from=2020-12-31&to=2021-12-31")
                .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals(200, result.getResponse().getStatus());
        Assertions.assertEquals("application/octet-stream", result.getResponse().getContentType());

    }


    @Test
    void fetchTracelogByStatusTest() throws Exception {

        when(this.traceService.findAllByStatus(anyInt(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(trace2)));

        this.mockMvc
                .perform(RestDocumentationRequestBuilders.get("/audit/{status}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}

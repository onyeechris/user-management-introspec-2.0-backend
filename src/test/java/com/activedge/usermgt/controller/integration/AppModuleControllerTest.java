package com.activedge.usermgt.controller.integration;

import com.activedge.usermgt.MockMvcBase;
import com.activedge.usermgt.controller.AppModuleController;
import com.activedge.usermgt.model.dto.ModuleDTO;
import com.activedge.usermgt.service.ModuleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppModuleController.class)
class AppModuleControllerTest extends MockMvcBase {

    @MockBean
    private ModuleService moduleService;

    private ModuleDTO moduleDTO1, moduleDTO2;

    @BeforeEach
    void init() {
        moduleDTO1 = new ModuleDTO("abc123", "UserMgt", "UserMgt desc", "USERMGT");
        moduleDTO2 = new ModuleDTO("efg456", "UserMgt2", "UserMgt desc2", "USERMGT");
    }

    @Test
    public void saveAppmoduleTest() throws Exception {

        when(this.moduleService.save(any(ModuleDTO.class))).thenReturn(moduleDTO1);

        this.mockMvc.perform( MockMvcRequestBuilders
            .post("/appmodule")
            .content(asJsonString(moduleDTO1))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    public void getAllAppmodulesTest() throws Exception {

        when(this.moduleService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Arrays.asList(moduleDTO1, moduleDTO2)));

        this.mockMvc.perform(MockMvcRequestBuilders
            .get("/appmodule")
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.payload").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.payload[*].id").isNotEmpty());

    }

    @Test
    public void getAppmoduleTest() throws Exception {

        when(this.moduleService.findOne(anyString())).thenReturn(Optional.of(moduleDTO2));

        this.mockMvc.perform( MockMvcRequestBuilders
            .get("/appmodule/{id}", "efg456")
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("efg456"));

    }

    @Test
    public void updateAppmoduleTest() throws Exception {

        when(this.moduleService.save(any(ModuleDTO.class))).thenReturn(moduleDTO1);

        this.mockMvc.perform( MockMvcRequestBuilders
                .put("/appmodule")
                .content(asJsonString(moduleDTO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(moduleDTO1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(moduleDTO1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(moduleDTO1.getDescription()));
    }

    @Test
    public void deleteAppmoduleTest() throws Exception
    {
        this.mockMvc.perform( MockMvcRequestBuilders.delete("/appmodule/{id}", 1) )
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

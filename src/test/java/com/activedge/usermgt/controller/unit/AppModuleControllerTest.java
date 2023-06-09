package com.activedge.usermgt.controller.unit;

import com.activedge.usermgt.controller.AppModuleController;
import com.activedge.usermgt.controller.util.ResponseWrapper;
import com.activedge.usermgt.exception.ActivityRequiredException;
import com.activedge.usermgt.model.dto.ModuleDTO;
import com.activedge.usermgt.service.ModuleService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppModuleControllerTest {

    @InjectMocks
    AppModuleController appModuleController;

    @Mock
    ModuleService moduleService;

    ModuleDTO moduleDTO1 = null, moduleDTO2 = null;

    @BeforeEach
    void init() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        moduleDTO1 = new ModuleDTO("abc123", "UserMgt", "UserMgt desc", "USERMGT");
        moduleDTO2 = new ModuleDTO("efg456", "UserMgt2", "UserMgt desc2", "USERMGT");
    }

    @Test
    public void saveAppmoduleTest() throws NotFoundException, ActivityRequiredException, URISyntaxException {


        when(moduleService.save(any(ModuleDTO.class))).thenReturn(moduleDTO1);

        Errors errors = new BeanPropertyBindingResult(moduleDTO1, "moduleDTO");

        ResponseEntity<ModuleDTO> responseEntity = appModuleController.createModules(moduleDTO1, errors);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        assertThat(responseEntity.getBody().getId()).isEqualTo("abc123");
    }

    @Test
    public void getAllModulesTest()
    {
        when(moduleService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Arrays.asList(moduleDTO1, moduleDTO2)));

        Pageable pageable = PageRequest.of(0, 20);

        ResponseEntity<ResponseWrapper> result = appModuleController.getAllModules(pageable);

        assertThat(result.getStatusCodeValue()).isEqualTo(200);

        assertThat(Objects.requireNonNull(result.getBody()).getMeta().getNumberOfElements()).isEqualTo(2);

        assertThat(Objects.requireNonNull(result.getBody()).getMeta().getPageSize()).isEqualTo(0);
    }

}

package com.activedge.usermgt.controller.integration;

import com.activedge.usermgt.MockMvcBase;
import com.activedge.usermgt.controller.UserJWTController;
import com.activedge.usermgt.model.LdapSetting;
import com.activedge.usermgt.service.MapValidationErrorService;
import com.activedge.usermgt.service.StaffModuleService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserJWTController.class)
class UserJWTControllerTest extends MockMvcBase {

    @Mock
    UserJWTController userJWTController;

    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    Authentication auth;

    @MockBean
    private StaffModuleService staffModuleService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private MapValidationErrorService mapValidationErrorService;

    @MockBean
    private LdapSetting ldapSetting;

//    @Test
    public void authenticateTest() throws Exception {

        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        String module = "USER";
        Authentication authentication = Mockito.mock(Authentication.class);

        when(this.staffModuleService.matchModuleAndEmail(anyString(), anyString())).thenReturn(true);

        when(this.jwtTokenProvider.getJwtToken(authentication, module,false)).thenReturn(jwt);

        when(this.authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);

        when(userJWTController.audit(httpServletRequest, authentication)).thenReturn(true);

        this.mockMvc.perform( MockMvcRequestBuilders
            .post("/auth")
            .content("{\n" +
                    "  \"username\": \"user@example.com\",\n" +
                    "  \"password\": \"***\"\n" +
                    "}")
            .header("module", module)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("Bearer " + jwt));
    }
}

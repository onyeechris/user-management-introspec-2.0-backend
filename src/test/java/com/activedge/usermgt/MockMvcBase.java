package com.activedge.usermgt;

import capital.scalable.restdocs.AutoDocumentation;
import capital.scalable.restdocs.SnippetRegistry;
import capital.scalable.restdocs.jackson.JacksonResultHandlers;
import capital.scalable.restdocs.response.ResponseModifyingPreprocessors;
import com.activedge.usermgt.repository.StaffRepository;
import com.activedge.usermgt.security.JwtAuthenticationEntryPoint;
import com.activedge.usermgt.security.SecurityCredentials;
import com.activedge.usermgt.service.CustomUserDetailsService;
import com.activedge.usermgt.service.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.cli.CliDocumentation;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.jms.Queue;
import javax.servlet.Filter;

import static capital.scalable.restdocs.misc.AuthorizationSnippet.documentAuthorization;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
public abstract class MockMvcBase {

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JmsMessagingTemplate jmsMessagingTemplate;

    @MockBean
    private Queue queue;

    @MockBean
    private SecurityCredentials securityCredentials;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private StaffRepository staffRepository;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    @Qualifier("jwtAuthFilter")
    private Filter filter;

    @MockBean
    @Qualifier("springSecurityFilterChain")
    private Filter filter2;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) throws Exception {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
//                .addFilters(springSecurityFilterChain)
                .alwaysDo(JacksonResultHandlers.prepareJackson(objectMapper))
                .alwaysDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(
                                ResponseModifyingPreprocessors.replaceBinaryContent(),
                                ResponseModifyingPreprocessors.limitJsonArrayLength(objectMapper),
                                prettyPrint())))
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation)
                                .uris()
                                .withScheme("http")
                                .withHost("localhost")
                                .withPort(443)
                                .and().snippets()
                                .withDefaults(CliDocumentation.curlRequest(),
                                        HttpDocumentation.httpRequest(),
                                        HttpDocumentation.httpResponse(),
                                        PayloadDocumentation.requestBody(),
                                        PayloadDocumentation.responseBody(),
                                        AutoDocumentation.requestFields(),
                                        AutoDocumentation.responseFields(),
                                        AutoDocumentation.pathParameters(),
                                        AutoDocumentation.requestParameters(),
                                        AutoDocumentation.description(),
                                        AutoDocumentation.methodAndPath(),
                                        AutoDocumentation.authorization("Bearer: ..."),
//                                AutoDocumentation.section()
                                        AutoDocumentation.sectionBuilder()
                                                .snippetNames(
                                                        SnippetRegistry.PATH_PARAMETERS,
                                                        SnippetRegistry.REQUEST_PARAMETERS,
                                                        SnippetRegistry.REQUEST_FIELDS,
                                                        SnippetRegistry.RESPONSE_FIELDS,
                                                        SnippetRegistry.HTTP_RESPONSE
                                                )
                                                .skipEmpty(true)
                                                .build()
                                )
                )
                .build();
    }

    protected RequestPostProcessor userToken() {
        return request -> {
            // If the tests requires setup logic for users, you can place it here.
            // Authorization headers or cookies for users should be added here as well.
            String accessToken;
            try {
                accessToken = getAccessToken("test", "test");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            request.addHeader("Authorization", "Bearer " + accessToken);
            return documentAuthorization(request, "User access token required.");
        };
    }

    private String getAccessToken(String username, String password) throws Exception {

        String contentType = MediaType.APPLICATION_JSON + ";charset=UTF-8";

        String body = mockMvc
            .perform(
                post("/auth")
                    .contentType(
                        MediaType.APPLICATION_JSON)
                    .param("username", username)
                    .param("password", password))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.status", is(true)))
            .andExpect(jsonPath("$.token", is(startsWith("Bearer"))))
            .andReturn().getResponse().getContentAsString();

        return body.substring(17, 53);
    }
}
package com.activedge.usermgt.filter;

import com.activedge.usermgt.UsermgtApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static io.jsonwebtoken.lang.Assert.notNull;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UsermgtApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JwtUsernameAndPasswordAuthenticationFilterTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private HttpHeaders headers = new HttpHeaders();

    @Before
    public void before() {
        headers.add("Authorization", "");
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testLogin() {

        Map<String, String> req = new HashMap<>();
        req.put("username", "admean");
        req.put("password", "56789");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(req, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/auth"),
                HttpMethod.POST, entity, String.class);

        assertEquals(response.getStatusCodeValue(), 200);
        notNull(response.getBody());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}

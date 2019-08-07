package com.activedge.usermgt.controller;

import com.activedge.usermgt.model.Permission;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.*;
import org.skyscreamer.jsonassert.comparator.ArraySizeComparator;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;
import org.skyscreamer.jsonassert.comparator.JSONComparator;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.*;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/*
 * See http://jsonassert.skyscreamer.org/apidocs/org/skyscreamer/jsonassert/ArrayValueMatcher.html
 * https://www.baeldung.com/jsonassert
 */

@RunWith(SpringJUnit4ClassRunner.class)
@EnableSpringDataWebSupport
@WebAppConfiguration
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PermissionControllerTest {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Before
    public void before() {
        headers.add("Authorization", createHttpAuthenticationHeaderValue(
                "sysdev@aet.com", "sysdevsecret"));
        headers.add("Module", "ATM");
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetPermissions() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/auth-service/permissions"),
                HttpMethod.GET, entity, String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());

        // Get length of array we will verify
        // Verify if all id's in payload is integer value
        int aLength = ((JSONArray)((JSONObject)JSONParser.parseJSON(response.getBody())).get("payload")).length();
        // create array of customizations one for each array element
        RegularExpressionValueMatcher<Object> regExValueMatcher = new RegularExpressionValueMatcher<Object>("\\d+");  // matches one or more digits
        Customization[] customizations = new Customization[aLength];
        for (int i=0; i<aLength; i++) {
            String contextPath = "payload["+i+"].id";
            customizations[i] = new Customization(contextPath, regExValueMatcher);
        }
        CustomComparator regExComparator = new CustomComparator(JSONCompareMode.STRICT_ORDER, customizations);
        ArrayValueMatcher<Object> regExArrayValueMatcher = new ArrayValueMatcher<Object>(regExComparator);
        Customization regExArrayValueCustomization = new Customization("payload", regExArrayValueMatcher);
        CustomComparator regExCustomArrayValueComparator = new CustomComparator(JSONCompareMode.STRICT_ORDER, new Customization[] { regExArrayValueCustomization });


        assertTrue(jsonObject.has("payload"));

        assertTrue(jsonObject.has("meta"));

        JSONAssert.assertEquals("{payload:[{id:X}]}", response.getBody(), regExCustomArrayValueComparator);

    }

    @Test
    public void addPermission() {

        Permission permission = new Permission();

        HttpEntity<Permission> entity = new HttpEntity<Permission>(permission, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/students/Student1/courses"),
                HttpMethod.POST, entity, String.class);

        String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);

        assertTrue(actual.contains("/students/Student1/courses/"));

    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private String createHttpAuthenticationHeaderValue(String username, String password) {
        Map<String, String> req = new HashMap<>();
        req.put("username", username);
        req.put("password", password);

        ObjectMapper mapper = new ObjectMapper();

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(req, headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/auth-service/auth"), HttpMethod.POST, entity, String.class);

        try {
            // convert JSON string to Map
            Map<String, String> map = mapper.readValue(response.getBody(), Map.class);

            return map.get("token");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

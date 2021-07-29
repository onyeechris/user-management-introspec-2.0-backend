package com.activedge.usermgt.controller.integration;

//import com.activedge.usermgt.model.dto.PermissionDTO;

/*
 * See http://jsonassert.skyscreamer.org/apidocs/org/skyscreamer/jsonassert/ArrayValueMatcher.html
 * https://www.baeldung.com/jsonassert
 */

// @RunWith(SpringJUnit4ClassRunner.class)
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
// @FixMethodOrder(MethodSorters.JVM)
// @ActiveProfiles({"mongo"})
public class PermissionControllerTest {
     /*

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    private static String id;

    @Before
    public void before() {
        headers.add("Authorization", createHttpAuthenticationHeaderValue(
                "sysdev@aet.com", "sysdevsecret"));
        headers.add("Module", "ATM");
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddPermission() throws JSONException {
        PermissionDTO permission = new PermissionDTO();

        permission.setAction("Test Endpoint");
        permission.setDescription("My test endpoint description");

        HttpEntity<PermissionDTO> entity = new HttpEntity<PermissionDTO>(permission, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/auth-service/permissions"),
                HttpMethod.POST, entity, String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());

        id = jsonObject.getString("id");

        String expected = "{\n" +
                "    \"action\": \"Test Endpoint\",\n" +
                "    \"description\": \"My test endpoint description\"\n" +
                "}";

        JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    public void testGetPermissions() throws JSONException {
        /*
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/auth-service/permissions"),
                HttpMethod.GET, entity, String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());

        // Get length of array we will verify
        // Verify if all id's in payload is integer value
        int aLength = ((JSONArray)((JSONObject)JSONParser.parseJSON(response.getBody())).get("payload")).length();
        // create array of customizations one for each array element
        RegularExpressionValueMatcher<Object> regExValueMatcher = new RegularExpressionValueMatcher<Object>("\\w+");  // matches one or more digits
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
    public void testUpdatePermission() throws JSONException {
        
        PermissionDTO permission = new PermissionDTO();

        permission.setId(id);
        permission.setAction("Test Endpoint changed");
        permission.setDescription("My test endpoint description too");

        HttpEntity<PermissionDTO> entity = new HttpEntity<PermissionDTO>(permission, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/auth-service/permissions"),
                HttpMethod.PUT, entity, String.class);

        String expected = "{\n" +
                "    \"action\": \"Test Endpoint changed\",\n" +
                "    \"description\": \"My test endpoint description too\"\n" +
                "}";

        JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    public void testDeletePermission() throws JSONException, InterruptedException {
        Thread.sleep(50); // little pause for update to complete

        HttpEntity<PermissionDTO> entity = new HttpEntity<PermissionDTO>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/auth-service/permissions/" + id),
                HttpMethod.DELETE, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
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
    */
}

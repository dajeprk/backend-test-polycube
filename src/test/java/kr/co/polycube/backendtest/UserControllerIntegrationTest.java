package kr.co.polycube.backendtest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.polycube.backendtest.Repository.UserRepository;
import kr.co.polycube.backendtest.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    boolean resetRepo = false;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void testCreateUserSuccessful() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"testuser\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();
    }

    @Test
    public void testFindUserSuccessful() throws Exception {
        MvcResult result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"testuser\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseContent);
        String id = jsonNode.get("id").asText();

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("testuser"));
    }

    @Test
    public void testUpdateUserSuccessful() throws Exception {
        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"testuser\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseContent);
        String id = jsonNode.get("id").asText();

        mockMvc.perform(put("/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"updateduser\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("updateduser"));
    }

    @Test
    public void testCreateUserFailNameNull() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": null}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason").value("Name is a required field."));
    }

    @Test
    public void testCreateUserFailInvalidRequestBody() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": null"))
                .andExpect(status().isBadRequest());
    }
;
    @Test
    public void testFindUserFail() throws Exception {
        String nonExistentId = UUID.randomUUID().toString();

        mockMvc.perform(get("/users/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reason").value("User with id {" + nonExistentId + "} does not exist."));
    }

    @Test
    public void testUpdateUserFail() throws Exception {
        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"testuser\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseContent);
        String id = jsonNode.get("id").asText();

        mockMvc.perform(put("/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason").value("Name must be at least 5 characters long."));
    }

    @Test
    public void testRequestNonExistentURL() throws Exception {
        mockMvc.perform(put("/userss")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":  \"testuser\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reason").value("The requested URL /userss was not found"));
    }

    @Test
    public void testNonExistentHTTPMethod() throws Exception {
        mockMvc.perform(delete("/users"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.reason").value("The requested HTTP method DELETE is not supported"));
    }

    @Test
    public void testSpecialCharactersInUrl() throws Exception {
        mockMvc.perform(get("/users/{id}", "special@!*@!)#)!_*$_@!)%_@?=!&_!^&!characters"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason").value("Illegal characters in URL."));

        mockMvc.perform(get("/users/{id}?name=test!!", UUID.randomUUID().toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason").value("Illegal characters in URL."));
    }
}

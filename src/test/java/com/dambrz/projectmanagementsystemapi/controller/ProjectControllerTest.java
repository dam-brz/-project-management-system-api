package com.dambrz.projectmanagementsystemapi.controller;

import com.dambrz.projectmanagementsystemapi.TestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.dambrz.projectmanagementsystemapi.security.SecurityConstraints.HEADER_STRING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ProjectControllerTest extends TestHelper {

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getValidUserAsJsonString()));
    }

    @AfterEach
    void clearDb() {
        projectRepository.deleteAll();
    }

    @Test
    void testCreateNewProject() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();
        performCreateNewProject(token, project).andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("getInvalidProjectsAsJson")
    void testSaveShouldReturnBadRequestStatus(String arg) throws Exception {
        projectRepository.save(createValidSampleProject());
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        performCreateNewProject(token, arg).andExpect(status().isBadRequest());
    }

    @Test
    void testGetProjectByProjectIdentifier() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        ResultActions createNewProjectResult = performCreateNewProject(token, getValidProjectAsJsonString());
        String projectIdentifier = getProjectIdentifierFromResponseContent(createNewProjectResult);

        mockMvc.perform(get("/api/projects/" + projectIdentifier).header(HEADER_STRING, token))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProjectByProjectIdentifierShouldThrow() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        ResultActions createNewProjectResult = performCreateNewProject(token, getValidProjectAsJsonString());
        String projectIdentifier = getProjectIdentifierFromResponseContent(createNewProjectResult);

        mockMvc.perform(get("/api/projects/" + projectIdentifier.concat("1")).header(HEADER_STRING, token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllProjects() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        mockMvc.perform(get("/api/projects").header(HEADER_STRING, token)).andExpect(status().isOk());
    }

    private ResultActions performLogin(String userAsJsonString) throws Exception {
        return mockMvc.perform(
                post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userAsJsonString));
    }

    private ResultActions performCreateNewProject(String token, String projectAsJsonString) throws Exception {
        return mockMvc.perform(post("/api/projects")
                .header(HEADER_STRING, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectAsJsonString));
    }
}
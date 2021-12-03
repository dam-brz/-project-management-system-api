package com.dambrz.projectmanagementsystemapi.controller;

import com.dambrz.projectmanagementsystemapi.TestHelper;
import com.dambrz.projectmanagementsystemapi.model.Project;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ProjectControllerTest extends TestHelper {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        projectRepository.deleteAll();
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getValidUserAsJsonString()));
    }

    @Test
    void testCreateNewProject() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();
        performCreateNewProject(token, project).andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("getInvalidProjectsAsJson")
    void testSaveShouldReturnBadRequestStatus(String arg) throws Exception {
        projectRepository.save(createSampleProject());
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        performCreateNewProject(token, arg).andExpect(status().isBadRequest());
    }

    @Test
    void testGetProjectByProjectIdentifier() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        performCreateNewProject(token, getValidProjectAsJsonString());

        mockMvc.perform(get("/api/projects/TEST1").header(HEADER_STRING, token))
                .andExpect(status().isOk());

    }

    @Test
    void testGetProjectByProjectIdentifierShouldThrow() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        performCreateNewProject(token, getValidProjectAsJsonString());

        mockMvc.perform(get("/api/projects/TEST12").header(HEADER_STRING, token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllProjects() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        mockMvc.perform(get("/api/projects").header(HEADER_STRING, token)).andExpect(status().isOk());
    }

    @Test
    void deleteProject() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        performCreateNewProject(token, getValidProjectAsJsonString());

        String content = mockMvc.perform(delete("/api/projects/TEST1").header(HEADER_STRING, token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertThat(content).isEqualTo(getDeleteResponseAsJsonString(true));
    }

    @Test
    void deleteProjectShouldThrowBadRequest() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        performCreateNewProject(token, getValidProjectAsJsonString());

        String content = mockMvc.perform(delete("/api/projects/TEST12").header(HEADER_STRING, token))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertThat(content).isEqualTo(getDeleteResponseAsJsonString(false));
    }

    @Test
    void updateProject() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        performCreateNewProject(token, getValidProjectAsJsonString());
        Project project = objectMapper.readValue(getValidProjectAsJsonString(), Project.class);
        project.setDescription("AaAaa");
        String updatedProjectAsJsonString = objectMapper.writeValueAsString(project);

        mockMvc.perform(
                put("/api/projects/" + project.getProjectIdentifier())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_STRING, token)
                        .content(updatedProjectAsJsonString)).andExpect(status().isOk());

        Assertions.assertThat(getValidProjectAsJsonString()).isNotEqualTo(updatedProjectAsJsonString);

    }

    @Test
    void testUpdateProjectShouldThrowBadRequest() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        performCreateNewProject(token, getValidProjectAsJsonString());
        Project project = objectMapper.readValue(getValidProjectAsJsonString(), Project.class);
        project.setDescription(null);
        String updatedProjectAsJsonString = objectMapper.writeValueAsString(project);

        mockMvc.perform(
                put("/api/projects/" + project.getProjectIdentifier())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_STRING, token)
                        .content(updatedProjectAsJsonString)).andExpect(status().isBadRequest());

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
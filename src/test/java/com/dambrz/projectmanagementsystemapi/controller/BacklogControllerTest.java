package com.dambrz.projectmanagementsystemapi.controller;

import com.dambrz.projectmanagementsystemapi.TestHelper;
import com.dambrz.projectmanagementsystemapi.model.Project;
import com.dambrz.projectmanagementsystemapi.model.ProjectTask;
import com.dambrz.projectmanagementsystemapi.model.enums.EPriority;
import com.dambrz.projectmanagementsystemapi.model.enums.EStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.dambrz.projectmanagementsystemapi.security.SecurityConstraints.HEADER_STRING;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class BacklogControllerTest extends TestHelper {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testAddProjectTaskToBacklog() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        performCreateNewProject(token, project)
                .andExpect(status().isOk());

        String projectIdentifier = objectMapper
                .readValue(getValidProjectAsJsonString(), Project.class)
                .getProjectIdentifier();

        String projectTaskAsJson = getValidProjectTaskAsJsonString();

        mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                .header(HEADER_STRING, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectTaskAsJson))
                .andExpect(status().isOk());
    }

    @Test
    void testAddProjectTaskToBacklogShouldThrowsBadRequest() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        performCreateNewProject(token, project)
                .andExpect(status().isOk());

        String projectIdentifier = objectMapper
                .readValue(project, Project.class)
                .getProjectIdentifier();

        mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getInvalidValidProjectTaskAsJsonString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddProjectTaskShouldAutoSetPriority() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        performCreateNewProject(token, project)
                .andExpect(status().isOk());

        String projectIdentifier = objectMapper
                .readValue(project, Project.class)
                .getProjectIdentifier();

        String projectTaskAsJson = getValidProjectTaskAsJsonString();

        mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectTaskAsJson))
                .andExpect(status().isOk());

        ProjectTask task = projectTaskRepository.findProjectTaskByProjectSequence("TEST1-1").get();

        assertThat(task.getPriority()).isEqualTo(EPriority.HIGH.getPriorityCode());
    }


    @Test
    void testAddProjectTaskShouldAutoSetStatus() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        performCreateNewProject(token, project)
                .andExpect(status().isOk());

        String projectIdentifier = objectMapper
                .readValue(project, Project.class)
                .getProjectIdentifier();

        String projectTaskAsJson = getValidProjectTaskAsJsonString();

        mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectTaskAsJson))
                .andExpect(status().isOk());

        ProjectTask task = projectTaskRepository.findProjectTaskByProjectSequence("TEST1-1").get();

        assertThat(task.getStatus()).isEqualTo(EStatus.TO_DO.toString());
    }

    @Test
    void testGetProjectBacklog() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        performCreateNewProject(token, project)
                .andExpect(status().isOk());

        String projectIdentifier = objectMapper
                .readValue(project, Project.class)
                .getProjectIdentifier();

        String projectTaskAsJson = getValidProjectTaskAsJsonString();

        mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectTaskAsJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProjectBacklogShouldThrowBadRequest() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        performCreateNewProject(token, project)
                .andExpect(status().isOk());

        String projectIdentifier = objectMapper
                .readValue(project, Project.class)
                .getProjectIdentifier();

        String projectTaskAsJson = getValidProjectTaskAsJsonString();

        mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectTaskAsJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/backlogs/" + projectIdentifier.concat("1"))
                        .header(HEADER_STRING, token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getProjectTask() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        performCreateNewProject(token, project)
                .andExpect(status().isOk());

        String projectIdentifier = objectMapper
                .readValue(project, Project.class)
                .getProjectIdentifier();

        String projectTaskAsJson = getValidProjectTaskAsJsonString();

        mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectTaskAsJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/backlogs/" + projectIdentifier + "/" + "TEST1-1")
                        .header(HEADER_STRING, token))
                .andExpect(status().isOk());

    }

    @Test
    void updateProjectTask() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        performCreateNewProject(token, project)
                .andExpect(status().isOk());

        String projectIdentifier = objectMapper
                .readValue(project, Project.class)
                .getProjectIdentifier();

        String projectTaskAsJson = getValidProjectTaskAsJsonString();

        mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectTaskAsJson))
                .andExpect(status().isOk());

        String projectTaskAsJsonString = mockMvc
                .perform(get("/api/backlogs/" + projectIdentifier + "/" + "TEST1-1").header(HEADER_STRING, token)).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ProjectTask projectTask = objectMapper.readValue(projectTaskAsJsonString, ProjectTask.class);
        projectTask.setPriority(EPriority.MEDIUM.getPriorityCode());
        projectTask.setStatus(EStatus.DONE.toString());
        String projectTaskSequence = projectTask.getProjectSequence();
        String updatedProjectTaskAsJsonString = objectMapper.writeValueAsString(projectTask);

        mockMvc.perform(put("/api/backlogs/" + projectIdentifier + "/" + projectTaskSequence)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProjectTaskAsJsonString))
                .andExpect(status().isOk());

    }
    @Test
    void updateProjectTaskShouldThrowBadRequest() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        performCreateNewProject(token, project)
                .andExpect(status().isOk());

        String projectIdentifier = objectMapper
                .readValue(project, Project.class)
                .getProjectIdentifier();

        String projectTaskAsJson = getValidProjectTaskAsJsonString();

        mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectTaskAsJson))
                .andExpect(status().isOk());

        String projectTaskAsJsonString = mockMvc
                .perform(get("/api/backlogs/" + projectIdentifier + "/" + "TEST1-1").header(HEADER_STRING, token)).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ProjectTask projectTask = objectMapper.readValue(projectTaskAsJsonString, ProjectTask.class);
        projectTask.setPriority(EPriority.MEDIUM.getPriorityCode());
        projectTask.setStatus(EStatus.DONE.toString());
        String projectTaskSequence = projectTask.getProjectSequence();
        String updatedProjectTaskAsJsonString = objectMapper.writeValueAsString(projectTask);

        mockMvc.perform(put("/api/backlogs/" + projectIdentifier + "/" + projectTaskSequence)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProjectTaskAsJsonString))
                .andExpect(status().isOk());

        projectTask.setSummary(null);
        String updatedProjectTaskAsJsonStringWithNullSummary = objectMapper.writeValueAsString(projectTask);

        mockMvc.perform(put("/api/backlogs/" + projectIdentifier + "/" + projectTaskSequence)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProjectTaskAsJsonStringWithNullSummary))
                .andExpect(status().isBadRequest());

    }

    @Test
    void deleteProjectTask() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        performCreateNewProject(token, project)
                .andExpect(status().isOk());

        String projectIdentifier = objectMapper
                .readValue(project, Project.class)
                .getProjectIdentifier();

        String projectTaskAsJson = getValidProjectTaskAsJsonString();

        mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectTaskAsJson))
                .andExpect(status().isOk());

        String projectTaskAsJsonString = mockMvc
                .perform(get("/api/backlogs/" + projectIdentifier + "/" + "TEST1-1").header(HEADER_STRING, token)).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ProjectTask projectTask = objectMapper.readValue(projectTaskAsJsonString, ProjectTask.class);
        String projectTaskSequence = projectTask.getProjectSequence();

        mockMvc.perform(delete("/api/backlogs/" + projectIdentifier + "/" + projectTaskSequence)
                        .header(HEADER_STRING, token))
                .andExpect(status().isOk());
    }

    @Test
    void deleteProjectTaskFalse() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        performCreateNewProject(token, project)
                .andExpect(status().isOk());

        String projectIdentifier = objectMapper
                .readValue(project, Project.class)
                .getProjectIdentifier();

        String projectTaskAsJson = getValidProjectTaskAsJsonString();

        mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectTaskAsJson))
                .andExpect(status().isOk());

        String projectTaskAsJsonString = mockMvc
                .perform(get("/api/backlogs/" + projectIdentifier + "/" + "TEST1-1").header(HEADER_STRING, token)).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ProjectTask projectTask = objectMapper.readValue(projectTaskAsJsonString, ProjectTask.class);
        String projectTaskSequence = projectTask.getProjectSequence() + 1;

        String content = mockMvc.perform(delete("/api/backlogs/" + projectIdentifier + "/" + projectTaskSequence)
                        .header(HEADER_STRING, token))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertThat(content).isEqualTo(getDeleteResponseAsJsonString(false));
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
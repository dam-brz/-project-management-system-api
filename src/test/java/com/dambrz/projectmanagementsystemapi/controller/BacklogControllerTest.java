package com.dambrz.projectmanagementsystemapi.controller;

import com.dambrz.projectmanagementsystemapi.TestHelper;
import com.dambrz.projectmanagementsystemapi.model.Project;
import com.dambrz.projectmanagementsystemapi.model.ProjectTask;
import com.fasterxml.jackson.databind.ObjectMapper;
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

        String createProjectResultAsString = performCreateNewProject(token, project)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String projectIdentifier = objectMapper
                .readValue(createProjectResultAsString, Project.class)
                .getProjectIdentifier();

        String projectTaskAsJson = objectMapper
                .writeValueAsString(createInvalidSampleProjectTask());

        mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                .header(HEADER_STRING, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectTaskAsJson))
                .andExpect(status().isCreated());
    }

    @Test
    void testAddProjectTaskToBacklogShouldThrowsBadRequest() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        String createProjectResultAsString = performCreateNewProject(token, project)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String projectIdentifier = objectMapper
                .readValue(createProjectResultAsString, Project.class)
                .getProjectIdentifier();

        ProjectTask task = createInvalidSampleProjectTask();
        task.setSummary(null);

        String projectTaskAsJson = objectMapper
                .writeValueAsString(task);

        mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectTaskAsJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddProjectTaskShouldAutoSetPriority() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        String createProjectResultAsString = performCreateNewProject(token, project)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String projectIdentifier = objectMapper
                .readValue(createProjectResultAsString, Project.class)
                .getProjectIdentifier();

        ProjectTask projectTask = createInvalidSampleProjectTask();
        projectTask.setPriority(0);

        String projectTaskAsJson = objectMapper
                .writeValueAsString(projectTask);

        ResultActions addProjectTaskResult = mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectTaskAsJson))
                .andExpect(status().isCreated());

        ProjectTask task = objectMapper
                .readValue(addProjectTaskResult.andReturn().getResponse().getContentAsString(), ProjectTask.class);

        assertThat(task.getPriority()).isEqualTo(3);
    }

    @Test
    void testAddProjectTaskShouldAutoSetPriorityFromNull() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        String createProjectResultAsString = performCreateNewProject(token, project)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String projectIdentifier = objectMapper
                .readValue(createProjectResultAsString, Project.class)
                .getProjectIdentifier();

        ProjectTask projectTask = createInvalidSampleProjectTask();
        projectTask.setPriority(null);

        String projectTaskAsJson = objectMapper
                .writeValueAsString(projectTask);

        ResultActions addProjectTaskResult = mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectTaskAsJson))
                .andExpect(status().isCreated());

        ProjectTask task = objectMapper
                .readValue(addProjectTaskResult.andReturn().getResponse().getContentAsString(), ProjectTask.class);

        assertThat(task.getPriority()).isEqualTo(3);
    }

    @Test
    void testAddProjectTaskShouldAutoSetStatus() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        String createProjectResultAsString = performCreateNewProject(token, project)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String projectIdentifier = objectMapper
                .readValue(createProjectResultAsString, Project.class)
                .getProjectIdentifier();

        ProjectTask projectTask = createInvalidSampleProjectTask();
        projectTask.setStatus("");

        String projectTaskAsJson = objectMapper
                .writeValueAsString(projectTask);

        ResultActions addProjectTaskResult = mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectTaskAsJson))
                .andExpect(status().isCreated());

        ProjectTask task = objectMapper
                .readValue(addProjectTaskResult.andReturn().getResponse().getContentAsString(), ProjectTask.class);

        assertThat(task.getStatus()).isEqualTo("TO_DO");
    }

    @Test
    void testAddProjectTaskShouldAutoSetStatusFromNull() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        String createProjectResultAsString = performCreateNewProject(token, project)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String projectIdentifier = objectMapper
                .readValue(createProjectResultAsString, Project.class)
                .getProjectIdentifier();

        ProjectTask projectTask = createInvalidSampleProjectTask();
        projectTask.setStatus(null);

        String projectTaskAsJson = objectMapper
                .writeValueAsString(projectTask);

        ResultActions addProjectTaskResult = mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectTaskAsJson))
                .andExpect(status().isCreated());

        ProjectTask task = objectMapper
                .readValue(addProjectTaskResult.andReturn().getResponse().getContentAsString(), ProjectTask.class);

        assertThat(task.getStatus()).isEqualTo("TO_DO");
    }

    @Test
    void testGetProjectBacklog() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        String createProjectResultAsString = performCreateNewProject(token, project)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String projectIdentifier = objectMapper
                .readValue(createProjectResultAsString, Project.class)
                .getProjectIdentifier();

        String projectTaskAsJson = objectMapper
                .writeValueAsString(createInvalidSampleProjectTask());

        mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectTaskAsJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProjectBacklogShouldThrowBadRequest() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        String createProjectResultAsString = performCreateNewProject(token, project)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String projectIdentifier = objectMapper
                .readValue(createProjectResultAsString, Project.class)
                .getProjectIdentifier();

        String projectTaskAsJson = objectMapper
                .writeValueAsString(createInvalidSampleProjectTask());

        mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectTaskAsJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/backlogs/" + projectIdentifier.concat("1"))
                        .header(HEADER_STRING, token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getProjectTask() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        String createProjectResultAsString = performCreateNewProject(token, project)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String projectIdentifier = objectMapper
                .readValue(createProjectResultAsString, Project.class)
                .getProjectIdentifier();

        String projectTaskAsJson = objectMapper
                .writeValueAsString(createInvalidSampleProjectTask());

        ResultActions createProjectTaskResult = mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectTaskAsJson))
                .andExpect(status().isCreated());

        String projectTaskAsJsonString = createProjectTaskResult.andReturn().getResponse().getContentAsString();
        String projectTaskSequence = objectMapper.readValue(projectTaskAsJsonString, ProjectTask.class).getProjectSequence();

        mockMvc.perform(get("/api/backlogs/" + projectIdentifier + "/" + projectTaskSequence)
                        .header(HEADER_STRING, token))
                .andExpect(status().isOk());

    }

    @Test
    void updateProjectTask() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        String createProjectResultAsString = performCreateNewProject(token, project)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String projectIdentifier = objectMapper
                .readValue(createProjectResultAsString, Project.class)
                .getProjectIdentifier();

        String projectTaskAsJson = objectMapper
                .writeValueAsString(createInvalidSampleProjectTask());

        ResultActions createProjectTaskResult = mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectTaskAsJson))
                .andExpect(status().isCreated());

        String projectTaskAsJsonString = createProjectTaskResult.andReturn().getResponse().getContentAsString();
        ProjectTask projectTask = objectMapper.readValue(projectTaskAsJsonString, ProjectTask.class);
        projectTask.setPriority(2);
        projectTask.setStatus("Done");
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
    void updateProjectTaskShouldThrowBadRequest() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        String createProjectResultAsString = performCreateNewProject(token, project)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String projectIdentifier = objectMapper
                .readValue(createProjectResultAsString, Project.class)
                .getProjectIdentifier();

        String projectTaskAsJson = objectMapper
                .writeValueAsString(createInvalidSampleProjectTask());

        ResultActions createProjectTaskResult = mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectTaskAsJson))
                .andExpect(status().isCreated());

        String projectTaskAsJsonString = createProjectTaskResult.andReturn().getResponse().getContentAsString();
        ProjectTask projectTask = objectMapper.readValue(projectTaskAsJsonString, ProjectTask.class);
        projectTask.setSummary(null);
        String projectTaskSequence = projectTask.getProjectSequence();
        String updatedProjectTaskAsJsonString = objectMapper.writeValueAsString(projectTask);

        mockMvc.perform(put("/api/backlogs/" + projectIdentifier + "/" + projectTaskSequence)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProjectTaskAsJsonString))
                .andExpect(status().isBadRequest());

    }

    @Test
    void deleteProjectTask() throws Exception {
        ResultActions loginResult = performLogin(getValidLoginRequestAsJsonString());
        String token = getJWTokenFromResponseContent(loginResult);
        String project = getValidProjectAsJsonString();

        String createProjectResultAsString = performCreateNewProject(token, project)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String projectIdentifier = objectMapper
                .readValue(createProjectResultAsString, Project.class)
                .getProjectIdentifier();

        String projectTaskAsJson = objectMapper
                .writeValueAsString(createInvalidSampleProjectTask());

        ResultActions createProjectTaskResult = mockMvc.perform(post("/api/backlogs/" + projectIdentifier)
                        .header(HEADER_STRING, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectTaskAsJson))
                .andExpect(status().isCreated());

        String projectTaskAsJsonString = createProjectTaskResult.andReturn().getResponse().getContentAsString();
        ProjectTask projectTask = objectMapper.readValue(projectTaskAsJsonString, ProjectTask.class);
        String projectTaskSequence = projectTask.getProjectSequence();

        mockMvc.perform(delete("/api/backlogs/" + projectIdentifier + "/" + projectTaskSequence)
                        .header(HEADER_STRING, token))
                .andExpect(status().isOk());


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
package com.dambrz.projectmanagementsystemapi;

import com.dambrz.projectmanagementsystemapi.model.Project;
import com.dambrz.projectmanagementsystemapi.model.ProjectTask;
import com.dambrz.projectmanagementsystemapi.repository.BacklogRepository;
import com.dambrz.projectmanagementsystemapi.repository.ProjectRepository;
import com.dambrz.projectmanagementsystemapi.repository.ProjectTaskRepository;
import com.dambrz.projectmanagementsystemapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
public abstract class TestHelper {

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected ProjectRepository projectRepository;
    @Autowired
    protected ProjectTaskRepository projectTaskRepository;
    @Autowired
    protected BacklogRepository backlogRepository;

    protected Project createSampleProject() {
        return new Project("TEST", "TEST", "Test description");
    }

    protected String getValidUserAsJsonString() {
        return "{\"username\":\"sample@user.com\",\"fullName\":\"Sample User\"," +
                "\"password\":\"password\",\"confirmPassword\":\"password\"}";
    }

    protected static Stream<String> getInvalidUsersAsJson() {
        return Stream.of(
                "{\"username\":\"sample@user.com\",\"fullName\":\"Sample User\"," +
                        "\"password\":\"notMatchPassword\",\"confirmPassword\":\"password\"}",
                "{\"fullName\":\"Sample User\",\"password\":\"password\"," +
                        "\"confirmPassword\":\"password\"}",
                "{\"username\":\"sample@user.com\",\"password\":\"notMatchPassword\"," +
                        "\"confirmPassword\":\"password\"}",
                "{\"username\":\"sample@user.com\",\"fullName\":\"Sample User\"," +
                        "\"password\":\"password\",\"confirmPassword\":\"password\"}",
                ""
        );
    }

    protected String getValidLoginRequestAsJsonString() {
        return "{\"username\":\"sample@user.com\",\"password\":\"password\"}";
    }

    protected static Stream<String> getInvalidLoginRequestsAsJson() {
        return Stream.of(
                "{\"username\":\"sample@user.com\",\"password\":\"invalidPassword\"}",
                "{\"username\":\"invalid@user.com\",\"password\":\"password\"}",
                "{\"username\":\"invalid@user.com\"}",
                "{\"password\":\"password\"}",
                ""
        );
    }

    protected String getValidProjectAsJsonString() {
        return "{\"projectIdentifier\":\"TEST1\",\"projectName\":\"TEST\",\"description\":\"Test description\"}";
    }

    protected static Stream<String> getInvalidProjectsAsJson() {
        return Stream.of(
                "{\"projectIdentifier\":\"TEST1\",\"projectName\":\"\",\"description\":\"Test description\"}",
                "{\"projectIdentifier\":\"TEST2\",\"description\":\"Test description\"}",
                "{\"projectIdentifier\":\"TEST3\",\"projectName\":\"TEST\",\"description\":\"\"}",
                "{\"projectIdentifier\":\"TEST4\",\"projectName\":\"}",
                "{\"projectIdentifier\":\"\",\"projectName\":\"TEST\",\"description\":\"Test description\"}",
                "{\"projectName\":\"TEST\",\"description\":\"Test description\"}",
                "{\"projectIdentifier\":\"TES\",\"projectName\":\"TEST\",\"description\":\"Test description\"}",
                "{\"projectIdentifier\":\"TEST1020200\",\"projectName\":\"TEST\",\"description\":\"Test description\"}",
                "{\"projectIdentifier\":\"TEST\",\"projectName\":\"TEST\",\"description\":\"Test description\"}"


        );
    }

    protected String getValidProjectTaskAsJsonString() {
        return "{\"projectSequence\":\"TEST-1\",\"summary\":\"TS\",\"acceptanceCriteria\":\"AC\",\"acceptanceCriteria\":\"AC\",\"dueDate\":\"2021-12-03\"}";
    }

    protected String getInvalidValidProjectTaskAsJsonString() {
        return "{\"projectSequence\":\"TEST-1\",\"summary\":\"\",\"acceptanceCriteria\":\"AC\",\"acceptanceCriteria\":\"AC\",\"dueDate\":\"2021-12-03\"}";
    }

    protected String getDeleteResponseAsJsonString(boolean success) {
        return "{\"success\":" + success + "}";
    }

    protected String getFailedDeleteResponseAsJsonString() {
        return "{\"projectIdentifier\":\"Project doesn't exists\"}";
    }

    protected String getJWTokenFromResponseContent(ResultActions actions) throws Exception {
        return actions
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replace("{\"success\":true,\"token\":\"", "")
                .replace("\",\"roles\":[]}", "");
    }

//    protected String getProjectIdentifierFromResponseContent(ResultActions actions) throws Exception {
//        return actions
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString()
//                .substring(50, 55);
//    }
}

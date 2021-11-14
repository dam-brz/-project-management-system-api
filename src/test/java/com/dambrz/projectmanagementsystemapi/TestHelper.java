package com.dambrz.projectmanagementsystemapi;

import com.dambrz.projectmanagementsystemapi.model.Backlog;
import com.dambrz.projectmanagementsystemapi.model.Project;
import com.dambrz.projectmanagementsystemapi.model.ProjectTask;
import com.dambrz.projectmanagementsystemapi.model.User;
import com.dambrz.projectmanagementsystemapi.repository.BacklogRepository;
import com.dambrz.projectmanagementsystemapi.repository.ProjectRepository;
import com.dambrz.projectmanagementsystemapi.repository.ProjectTaskRepository;
import com.dambrz.projectmanagementsystemapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Stream;

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

    protected User createValidSampleUser() {
        return new User("sample@user.com", "Sample User", "password", "password");
    }

    protected User createInValidSampleUser() {
        return new User(null, "Sample User", "password", "password");
    }

    protected List<User> createSampleUsersList() {
        return List.of(
                new User("sample1@user.com", "Sample1 User1", "password", "password"),
                new User("sample2@user.com", "Sample2 User2", "password", "password"),
                new User("sample3@user.com", "Sample3 User3", "password", "password")
        );
    }

    protected Project createValidSampleProject() {
        return new Project("TEST", "TEST", "Test description");
    }

    protected static Stream<Project> createSampleInvalidProjectsList() {
        return Stream.of(
                new Project(null, "TEST1", "Test description1", "sample1@user.com"),
                new Project("", "TEST1", "Test description1", "sample1@user.com"),
                new Project("TEST2", null, "Test description2", "sample2@user.com"),
                new Project("TEST3", "TES", "Test description3", "sample3@user.com"),
                new Project("TEST3", "testTestTest", "Test description3", "sample3@user.com"),
                new Project("TEST3", "", "Test description3", "sample3@user.com")
        );
    }

    protected List<Project> createSampleProjectsList() {
        return List.of(
                new Project("TEST1", "TEST1", "Test description1", "sample1@user.com"),
                new Project("TEST2", "TEST2", "Test description2", "sample2@user.com"),
                new Project("TEST3", "TEST3", "Test description3", "sample3@user.com")
        );
    }

    protected List<Project> getSampleProjectsList(Iterable<Project> projects) {
        return (List<Project>) projects;
    }

    protected Backlog createValidSampleBacklog() {
        return new Backlog();
    }

    protected ProjectTask createValidSampleProjectTask() {
        Backlog backlog = backlogRepository.save(new Backlog());
        return new ProjectTask("TEST-1", "Test summary", 1, "TEST1", backlog);
    }

    protected ProjectTask createInvalidSampleProjectTask() {
        return new ProjectTask("TEST-1", "Test summary", 1, "TEST1");
    }

    protected List<ProjectTask> createProjectTaskList() {
        Backlog backlog = backlogRepository.save(new Backlog());
        return List.of(
                new ProjectTask("TEST-1", "Test summary1", 1, "TEST1", backlog),
                new ProjectTask("TEST-2", "Test summary2", 1, "TEST1", backlog),
                new ProjectTask("TEST-3", "Test summary3", 1, "TEST1", backlog)
        );
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
}

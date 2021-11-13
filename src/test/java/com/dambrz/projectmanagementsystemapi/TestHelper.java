package com.dambrz.projectmanagementsystemapi;

import com.dambrz.projectmanagementsystemapi.model.Backlog;
import com.dambrz.projectmanagementsystemapi.model.Project;
import com.dambrz.projectmanagementsystemapi.model.ProjectTask;
import com.dambrz.projectmanagementsystemapi.model.User;
import com.dambrz.projectmanagementsystemapi.repository.BacklogRepository;
import com.dambrz.projectmanagementsystemapi.repository.ProjectRepository;
import com.dambrz.projectmanagementsystemapi.repository.ProjectTaskRepository;
import com.dambrz.projectmanagementsystemapi.repository.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ActiveProfiles("test")
@ExtendWith(value = SpringExtension.class)
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
        User user = new User();
        user.setUsername("sample@user.com");
        user.setFullName("Sample User");
        user.setPassword("password");
        user.setConfirmPassword("password");

        return user;
    }

    protected List<User> createSampleUsersList() {
        User user = new User();
        user.setUsername("user1@sample.com");
        user.setFullName("User1 User");
        user.setPassword("password");
        user.setConfirmPassword("password");

        User user2 = new User();
        user2.setUsername("user2@sample.com");
        user2.setFullName("User2 User");
        user2.setPassword("password");
        user2.setConfirmPassword("password");

        User user3 = new User();
        user3.setUsername("user3@sample.com");
        user3.setFullName("User3 User");
        user3.setPassword("password");
        user3.setConfirmPassword("password");
        return List.of(user, user2, user3);
    }

    protected Project createValidSampleProject() {
        Project project = new Project();
        project.setProjectIdentifier("TEST");
        project.setProjectName("TEST");
        project.setDescription("Test description");

        return project;
    }

    protected List<Project> createSampleProjectsList() {
        Project project1 = new Project();
        project1.setProjectIdentifier("TEST1");
        project1.setProjectName("TEST1");
        project1.setDescription("Test1 description");
        project1.setProjectLeader("user1@sample.com");

        Project project2 = new Project();
        project2.setProjectIdentifier("TEST2");
        project2.setProjectName("TEST2");
        project2.setDescription("Test2 description");
        project2.setProjectLeader("user2@sample.com");


        Project project3 = new Project();
        project3.setProjectIdentifier("TEST3");
        project3.setProjectName("TEST3");
        project3.setDescription("Test3 description");
        project3.setProjectLeader("user3@sample.com");

        return List.of(project1, project2, project3);
    }

    protected Backlog createValidSampleBacklog() {
        return new Backlog();
    }

    protected ProjectTask createInvalidSampleProjectTask() {
        ProjectTask projectTask1 = new ProjectTask();
        projectTask1.setProjectIdentifier("TEST");
        projectTask1.setSummary("TEST SUMMARY");
        projectTask1.setProjectSequence("TEST1-1");
        projectTask1.setPriority(1);

        return projectTask1;
    }
}

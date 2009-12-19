/**
   Copyright 2009 TiTA Project, Vienna University of Technology
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
   
       http://www.apache.org/licenses/LICENSE\-2.0
       
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
  
 */

package at.ac.tuwien.ifs.tita.issuetracker.mantis.issue.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mantisbt.connect.MCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueStatus;
import at.ac.tuwien.ifs.tita.issuetracker.exceptions.ProjectNotFoundException;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IProjectTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.issue.service.TaskService;
import at.ac.tuwien.ifs.tita.issuetracker.mantis.base.MantisBaseTest;

/**
 * Test for testing task service that imports all tasks comming from an issue tracker.
 * @author herbert
 *
 */
public class TaskServiceTest extends MantisBaseTest {

    private final IssueTrackerLogin defaultLogin = new IssueTrackerLogin(1L, "administrator",
            "root", new IssueTracker(1L, "test-mantis", "http://localhost/mantisbt-1.1.8"));

    private final Integer numberOfProjects = 3;
    private TaskService taskService = new TaskService(defaultLogin);
    private final Logger log = LoggerFactory.getLogger(TaskServiceTest.class);  
    private final int numberOfTasksForEachProject = 2;

    private List<Long> taskIds = new ArrayList<Long>();
    private List<Long> projectIds = new ArrayList<Long>();

    /**
     * Prepare mantis connection and create a setup in mantis with projects and
     * tasks.
     */
    @Override
    @Before
    public void setUp() {
        super.setUp();

        try {
            createSetup(numberOfProjects, numberOfTasksForEachProject);
        } catch (MCException e) {
            fail("Mantis connection error.");
        } catch (ProjectNotFoundException e) {
            fail("Project must be set.");
        }
    }

    /**
     * Delete mantis projects for all tests.
     * 
     * @throws InterruptedException e
     */
    @After
    public void tearDown() throws InterruptedException {
        deleteSetupAndChanges();
    }

    /**
     * The test case should manage the update for all projects and issues. The
     * time will be measured for the update and should be about 10 second.
     * 
     * @throws ProjectNotFoundException pnfe
     */
    @Test
    public void updateAllShouldFetchAllIssueForTheProjects() throws ProjectNotFoundException {

        try {

            Long taskid = 0L;

            // Adding one task to each project to issue tracker
            taskid = createTestTask("tita_test_issue_new0",
                    "issue_summary_new", "projectName0");
            taskIds.add(taskid);
            taskid = createTestTask("tita_test_issue_new1",
                    "issue_summary_new", "projectName1");
            taskIds.add(taskid);
            taskid = createTestTask("tita_test_issue_new2",
                    "issue_summary_new", "projectName2");
            taskIds.add(taskid);

            startTimer("Start of update for all projects:");
            taskService.updateAll();
            stopTimer("Stopping the upate for all projects.");

            System.out.print(getPerformanceOutput());

            assertEquals(numberOfProjects.intValue(), taskService.getProjects()
                    .size());
            assertEquals(numberOfTasksForEachProject + 1, taskService
                    .getTasks(
                            taskService.getProjects().get(
                                    projectIds.get(0)), IssueStatus.NEW)
                    .size());
            assertNotNull(taskService.getIssueTrackerDao().findProject(
                    "projectName0"));

        } catch (MCException e) {
            fail("Creating or deleting projects and issues failed.");
            e.getStackTrace();
        }
    }

    /**
     * The test case should update a single project. After the update new tasks
     * for the project should be involved.
     * 
     * @throws ProjectNotFoundException pnfe
     * 
     */
    @Test
    public void updateProject() throws ProjectNotFoundException {
        try {

            Long taskid = 0L;

            // Adding a task to the project in the issue tracker
            taskid = createTestTask("tita_test_issue_new0",
                    "issue_summary_new", "projectName0");
            taskIds.add(taskid);

            startTimer("Start of update for one projects:");
            taskService.updateProject(taskService.getProjects().get(
                    projectIds.get(0)));
            stopTimer("Stopping the upate for one projects.");

            System.out.print(getPerformanceOutput());

            IProjectTrackable pro = taskService.getIssueTrackerDao()
                    .findProject(projectIds.get(0));

            assertEquals(numberOfProjects.intValue(), taskService.getProjects()
                    .size());
            assertEquals(numberOfTasksForEachProject + 1, taskService
                    .getTasks(pro, IssueStatus.NEW).size());

        } catch (MCException e) {
            fail("Creating or deleting projects and issues failed.");
            e.getStackTrace();
        }
    }

    /**
     * The test case shows how the task service reacts when no project is set.
     * 
     * @throws ProjectNotFoundException pnfe
     */
    @Test(expected = ProjectNotFoundException.class)
    public void getTasksShouldThrowProjectNotFoundException() throws ProjectNotFoundException {

        taskService = new TaskService(defaultLogin);
        taskService.getTasks(null, IssueStatus.NEW);

    }

    /**
     * The test case shows how the task service reacts when are no elements are
     * found for the project or status.
     * 
     * A null should be returned, that says, that a information message for the
     * user is necessary as feedback.
     * 
     * @throws ProjectNotFoundException pnfe
     */
    @Test
    public void getTasks() throws ProjectNotFoundException {

        taskService = new TaskService(defaultLogin);
        IProjectTrackable project = taskService.getIssueTrackerDao()
                .findProject(projectIds.get(0));

        assertNull(taskService.getTasks(project, IssueStatus.ASSIGNED));

    }

    /**
     * The method creates a setup in mantis to evaluate the performance and the
     * update mechanism from TiTA.
     * 
     * @param amountOfProjects p
     * @param amountOfTasksForEachProject p
     * @throws MCException e
     * @throws ProjectNotFoundException ef
     */
    private void createSetup(int amountOfProjects,
            int amountOfTasksForEachProject) throws MCException,
            ProjectNotFoundException {
        List<Long> taskids = new ArrayList<Long>();
        List<Long> projectids = new ArrayList<Long>();

        startTimer("Starting setup:");

        Long id = 1L;
        Long taskid = 1L;

        for (int i = 0; i < amountOfProjects; i++) {
            id = createTestProject("projectName" + i, "description" + i, true,
                    false);
            projectids.add(id);

            for (int j = 0; j < amountOfTasksForEachProject; j++) {
                taskid = createTestTask("tita_test_issue" + i + j,
                        "issue_summary" + i + j, "projectName" + i);
                taskids.add(taskid);
            }
        }
        taskIds = taskids;
        projectIds = projectids;

        assertEquals(0, taskService.getProjects().size());

        taskService = new TaskService(defaultLogin);

        assertEquals(amountOfProjects, taskService.getProjects().size());
        assertEquals(amountOfTasksForEachProject, taskService.getTasks(
                taskService.getProjects().get(id), IssueStatus.NEW).size());

        stopTimer("End of the setup.");
        System.out.print(getPerformanceOutput());
    }

    /**
     * The method undo the setup for mantis.
     * 
     * @throws InterruptedException ie
     */
    private void deleteSetupAndChanges() throws InterruptedException {
        //CHECKSTYLE:OFF
        Thread.sleep(2000);

        startTimer("Starting deleting:");

        // delete tasks
        for (int i = 0; i < taskIds.size(); i++) {
            deleteTestTask(taskIds.get(i));
        }
        // delete projects
        for (int i = 0; i < numberOfProjects; i++) {
            deleteTestProject("projectName" + i);
        }

        stopTimer("End of deleting.");
        System.out.print(getPerformanceOutput());
    }
}

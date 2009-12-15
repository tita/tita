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

    private static final Integer C_NUMBER_OF_PROJECTS = 3;
    private TaskService taskService = new TaskService();
    private final Logger log = LoggerFactory.getLogger(TaskServiceTest.class);  
    private int numberOfTasksForEachProject = 2;

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
            createSetup(C_NUMBER_OF_PROJECTS, this.numberOfTasksForEachProject);
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
            this.taskIds.add(taskid);
            taskid = createTestTask("tita_test_issue_new1",
                    "issue_summary_new", "projectName1");
            this.taskIds.add(taskid);
            taskid = createTestTask("tita_test_issue_new2",
                    "issue_summary_new", "projectName2");
            this.taskIds.add(taskid);

            this.log.info("Starting update");
            long starttime = System.currentTimeMillis();
            this.taskService.updateAll();
            long endtime = System.currentTimeMillis();
            long duration = endtime - starttime;
            this.log.info("End of update");
            //CHECKSTYLE:OFF
            this.log.info("Duration:" + duration / 1000 + " sec.");
            //CHECKSTYLE:ON
            assertEquals(C_NUMBER_OF_PROJECTS.intValue(), this.taskService.getProjects()
                    .size());
            assertEquals(this.numberOfTasksForEachProject + 1, this.taskService
                    .getTasks(
                            this.taskService.getProjects().get(
                                    this.projectIds.get(0)), IssueStatus.NEW)
                    .size());
            assertNotNull(this.taskService.getIssueTrackerDao().findProject(
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
            this.taskIds.add(taskid);

            this.log.info("Starting update");
            long starttime = System.currentTimeMillis();
            this.taskService.updateProject(this.taskService.getProjects().get(
                    this.projectIds.get(0)));
            long endtime = System.currentTimeMillis();
            long duration = endtime - starttime;
            this.log.info("End of update");
            //CHECKSTYLE:OFF
            this.log.info("Duration:" + duration / 1000 + " sec.");
            //CHECKSTYLE:ON
            IProjectTrackable pro = this.taskService.getIssueTrackerDao()
                    .findProject(this.projectIds.get(0));

            assertEquals(C_NUMBER_OF_PROJECTS.intValue(), this.taskService.getProjects()
                    .size());
            assertEquals(this.numberOfTasksForEachProject + 1, this.taskService
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

        this.taskService = new TaskService();
        this.taskService.getTasks(null, IssueStatus.NEW);

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

        this.taskService = new TaskService();
        IProjectTrackable project = this.taskService.getIssueTrackerDao()
                .findProject(this.projectIds.get(0));

        assertNull(this.taskService.getTasks(project, IssueStatus.ASSIGNED));

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

        this.log.info("Starting setup");
        long starttimesetup = System.currentTimeMillis();

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
        this.taskIds = taskids;
        this.projectIds = projectids;

        assertEquals(0, this.taskService.getProjects().size());

        this.taskService = new TaskService();

        assertEquals(amountOfProjects, this.taskService.getProjects().size());
        assertEquals(amountOfTasksForEachProject, this.taskService.getTasks(
                this.taskService.getProjects().get(id), IssueStatus.NEW).size());

        long endtimesetup = System.currentTimeMillis();
        long durationsetup = endtimesetup - starttimesetup;
        durationsetup = endtimesetup - starttimesetup;

        this.log.info("End of setup");
        //CHECKSTYLE:OFF
        this.log.info("Duration Setup:" + durationsetup / 1000 + " sec.");
        //CHECKSTYLE:ON
    }

    /**
     * The method undo the setup for mantis.
     * 
     * @throws InterruptedException ie
     */
    private void deleteSetupAndChanges() throws InterruptedException {
        //CHECKSTYLE:OFF
        Thread.sleep(2000);

        this.log.info("Starting deleting");
        long starttimedeleting = System.currentTimeMillis();

        // delete tasks
        for (int i = 0; i < this.taskIds.size(); i++) {
            deleteTestTask(this.taskIds.get(i));
        }
        // delete projects
        for (int i = 0; i < this.C_NUMBER_OF_PROJECTS; i++) {
            deleteTestProject("projectName" + i);
        }
        long endtimedeleting = System.currentTimeMillis();
        long durationdeleting = endtimedeleting - starttimedeleting;
        durationdeleting = endtimedeleting - starttimedeleting;

        this.log.info("End of Deleting");
        this.log
                .info("Duration Deleting:" + durationdeleting / 1000 + " sec.");
        //CHECKSTYLE:ON
    }
}

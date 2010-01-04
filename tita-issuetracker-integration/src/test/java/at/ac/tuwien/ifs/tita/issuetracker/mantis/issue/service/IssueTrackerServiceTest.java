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
import at.ac.tuwien.ifs.tita.issuetracker.issue.service.IssueTrackerService;
import at.ac.tuwien.ifs.tita.issuetracker.mantis.base.MantisBaseTest;

/**
 * Test for testing task service that imports all tasks comming from an issue tracker.
 * @author herbert
 *
 */
public class IssueTrackerServiceTest extends MantisBaseTest {

    protected IssueTrackerService issueTrackerService = new IssueTrackerService(this.defaultLogin);

    protected final Integer numberOfProjects = 3;
    protected final int numberOfTasksForEachProject = 2;

    protected List<Long> taskIds = new ArrayList<Long>();
    protected List<Long> projectIds = new ArrayList<Long>();

    private final Logger log = LoggerFactory.getLogger(IssueTrackerServiceTest.class);

    /**
     * Prepare mantis connection and create a setup in mantis with projects and
     * tasks.
     */
    @Override
    @Before
    public void setUp() {
        super.setUp();

        try {
            createSetup(this.numberOfProjects, this.numberOfTasksForEachProject);
        } catch (MCException e) {
            fail("Mantis connection error.");
        } catch (ProjectNotFoundException e) {
            fail("Project must be set.");
        } catch (InterruptedException e) {
            fail("Interruption Failure.");
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
     * @throws ProjectNotFoundException
     *             pnfe
     * @throws InterruptedException
     *             ie
     */
    @Test
    public void updateAllShouldFetchAllIssueForTheProjects() throws ProjectNotFoundException,
            InterruptedException {

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

            startTimer("Start of update for all projects:");
            this.issueTrackerService.updateAll();
            stopTimer("Stopping the upate for all projects.");

            // CHECKSTYLE:OFF
            // Sleeping because the updating is made on a other thread. This
            // thread will be quicker.
            Thread.sleep(10000);
            // CHECKSTYLE:ON

            System.out.print(getPerformanceOutput());

            assertEquals(this.numberOfProjects.intValue(), this.issueTrackerService.getProjects().size());
            assertEquals(this.numberOfTasksForEachProject + 1, this.issueTrackerService.getIssueTrackerTasks(
                            this.issueTrackerService.getProjects().get(this.projectIds.get(0)),
                            IssueStatus.NEW).size());

            assertNotNull(this.issueTrackerService.getIssueTrackerDao().findProject(
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
     * @throws ProjectNotFoundException
     *             pnfe
     * @throws InterruptedException
     *             ie
     *
     */
    @Test
    public void updateProject() throws ProjectNotFoundException, InterruptedException {
        try {

            Long taskid = 0L;

            // Adding a task to the project in the issue tracker
            taskid = createTestTask("tita_test_issue_new0",
                    "issue_summary_new", "projectName0");
            this.taskIds.add(taskid);

            startTimer("Start of update for one projects:");
            this.issueTrackerService.updateProject(this.issueTrackerService.getProjects().get(
                    this.projectIds.get(0)));
            stopTimer("Stopping the upate for one projects.");

            // CHECKSTYLE:OFF
            Thread.sleep(20000); // Sleeping because the updating is made on a
            // other thread.
            // This thread will be quicker.
            // CHECKSTYLE:ON

            System.out.print(getPerformanceOutput());
            IProjectTrackable foundProject = null;

            for (IProjectTrackable pro : this.issueTrackerService.getProjects().values()) {
                if (pro.getName().equals("projectName0")) {
                    foundProject = pro;
                }
            }

            assertEquals(this.numberOfProjects.intValue(), this.issueTrackerService.getProjects()
                    .size());
            assertEquals(this.numberOfTasksForEachProject + 1, this.issueTrackerService
                    .getIssueTrackerTasks(foundProject, IssueStatus.NEW).size());

        } catch (MCException e) {
            fail("Creating or deleting projects and issues failed.");
            e.getStackTrace();
        }
    }

    /**
     * The test case should update a single project with the parameter
     * projectId. After the update new tasks for the project should be involved.
     *
     * This test case does not run, because a additional project is produced.
     *
     * @throws ProjectNotFoundException
     *             pnfe
     * @throws InterruptedException
     *             ie
     */
    @Test
    public void updateProjectByProjectId() throws ProjectNotFoundException, InterruptedException {
        try {

            Long taskid = 0L;

            // Adding a task to the project in the issue tracker
            taskid = createTestTask("tita_test_issue_new0", "issue_summary_new", "projectName0");
            this.taskIds.add(taskid);

            IProjectTrackable foundProject = null;

            for (IProjectTrackable pro : this.issueTrackerService.getProjects().values()) {
                if (pro.getName().equals("projectName0")) {
                    foundProject = pro;
                }
            }

            startTimer("Start of update for one projects:");
            this.issueTrackerService.updateProject(foundProject);
            stopTimer("Stopping the upate for one projects.");

            // CHECKSTYLE:OFF
            Thread.sleep(20000); // Sleeping because the updating is made on a
            // other thread.
            // This thread will be quicker.
            // CHECKSTYLE:ON

            System.out.print(getPerformanceOutput());

            assertEquals(this.numberOfProjects.intValue(), this.issueTrackerService.getProjects()
                    .size());
            assertEquals(this.numberOfTasksForEachProject + 1, this.issueTrackerService
                    .getIssueTrackerTasks(foundProject, IssueStatus.NEW).size());

        } catch (MCException e) {
            fail("Creating or deleting projects and issues failed.");
            e.getStackTrace();
        }
    }

    /**
     * The test case shows how the issue tracker service reacts when no project
     * is set and checking for status.
     *
     * @throws ProjectNotFoundException
     *             pnfe
     */
    @Test(expected = ProjectNotFoundException.class)
    public void getIssueTrackerTasksShouldThrowProjectNotFoundException()
            throws ProjectNotFoundException {

        this.issueTrackerService = new IssueTrackerService(this.defaultLogin);
        this.issueTrackerService.getIssueTrackerTasks(null, IssueStatus.NEW);

    }

    /**
     * The test case shows how the issue tracker service reacts when are no
     * elements are found for the project.
     *
     * A null should be returned, that says, that a information message for the
     * user is necessary as feedback.
     *
     * @throws ProjectNotFoundException
     *             pnfe
     * @throws InterruptedException - ie
     */
    @Test
    public void getIssueTrackerTasks() throws ProjectNotFoundException, InterruptedException {

        this.issueTrackerService = new IssueTrackerService(this.defaultLogin);
        IProjectTrackable project = this.issueTrackerService.getIssueTrackerDao()
                .findProject(this.projectIds.get(0));

        startTimer("Start of update for one projects:");
        this.issueTrackerService.updateProject(project);
        stopTimer("Stopping the upate for one projects.");

        // CHECKSTYLE:OFF
        Thread.sleep(20000); // Sleeping because the updating is made on a
        // other thread.
        // This thread will be quicker.
        // CHECKSTYLE:ON
        
        assertNotNull(this.issueTrackerService.getIssueTrackerTasks(project));

    }

    /**
     * The test case shows how the task service reacts when no project is set.
     *
     * @throws ProjectNotFoundException
     *             pnfe
     */
    @Test(expected = ProjectNotFoundException.class)
    public void getIssueTrackerTasksWithoutIssueStatusShouldThrowProjectNotFoundException()
            throws ProjectNotFoundException {

        this.issueTrackerService = new IssueTrackerService(this.defaultLogin);
        this.issueTrackerService.getIssueTrackerTasks(null);

    }

    /**
     * The test case shows how the tasks for projects can be found.
     *
     * @throws ProjectNotFoundException
     *             pnfe
     * @throws InterruptedException - ie
     */
    @Test
    public void getIssueTrackerTasksByProjectId() throws ProjectNotFoundException, InterruptedException {
    
        this.issueTrackerService = new IssueTrackerService(this.defaultLogin);
        IProjectTrackable project = this.issueTrackerService.getIssueTrackerDao().findProject(
                this.projectIds.get(0));

        startTimer("Start of update for one projects:");
        this.issueTrackerService.updateProject(project);
        stopTimer("Stopping the upate for one projects.");

        // CHECKSTYLE:OFF
        Thread.sleep(20000); // Sleeping because the updating is made on a
        // other thread.
        // This thread will be quicker.
        // CHECKSTYLE:ON
        
        assertNotNull(this.issueTrackerService.getIssueTrackerTasksByProjectId(project.getId()));
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
    public void getIssueTrackerTasksOnlyAssignedTasks() throws ProjectNotFoundException {

        this.issueTrackerService = new IssueTrackerService(this.defaultLogin);
        IProjectTrackable foundProject = null;

        for (IProjectTrackable pro : this.issueTrackerService.getProjects().values()) {
            if (pro.getName().equals("projectName0")) {
                foundProject = pro;
            }
        }

        assertNull(this.issueTrackerService
                .getIssueTrackerTasks(foundProject, IssueStatus.ASSIGNED));

    }

    /**
     * The method creates a setup in mantis to evaluate the performance and the
     * update mechanism from TiTA.
     *
     * @param amountOfProjects
     *            p
     * @param amountOfTasksForEachProject
     *            p
     * @throws MCException
     *             e
     * @throws ProjectNotFoundException
     *             ef
     * @throws InterruptedException
     *             ie
     */
    protected void createSetup(int amountOfProjects,
            int amountOfTasksForEachProject) throws MCException,
 ProjectNotFoundException, InterruptedException {
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
        this.taskIds = taskids;
        this.projectIds = projectids;

        assertEquals(0, this.issueTrackerService.getProjects().size());

        this.issueTrackerService = new IssueTrackerService(this.defaultLogin);
        this.issueTrackerService.updateAll();

        // CHECKSTYLE:OFF
        // Sleeping because the updating is made on a other thread. This
        // thread will be quicker.
        Thread.sleep(15000);
        // CHECKSTYLE:ON

        assertEquals(amountOfProjects, this.issueTrackerService.getProjects().size());
        assertEquals(amountOfTasksForEachProject, this.issueTrackerService.getIssueTrackerTasks(
                this.issueTrackerService.getProjects().get(id), IssueStatus.NEW).size());

        stopTimer("End of the setup.");
        System.out.print(getPerformanceOutput());
    }

    /**
     * The method undo the setup for mantis.
     *
     * @throws InterruptedException ie
     */
    protected void deleteSetupAndChanges() throws InterruptedException {
        //CHECKSTYLE:OFF
        Thread.sleep(2000);

        startTimer("Starting deleting:");

        // delete tasks
        for (int i = 0; i < this.taskIds.size(); i++) {
            deleteTestTask(this.taskIds.get(i));
        }
        // delete projects
        for (int i = 0; i < this.numberOfProjects; i++) {
            deleteTestProject("projectName" + i);
        }

        stopTimer("End of deleting.");
        System.out.print(getPerformanceOutput());
    }
}

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

public class TaskServiceTest extends MantisBaseTest {

    private TaskService taskService = new TaskService();

    Logger log = LoggerFactory.getLogger(TaskServiceTest.class);

    private int numberOfProjects = 3;
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
            createSetup(this.numberOfProjects, this.numberOfTasksForEachProject);
        } catch (MCException e) {
            fail("Mantis connection error.");
        } catch (ProjectNotFoundException e) {
            fail("Project must be set.");
        }
    }

    /**
     * Delete mantis projects for all tests.
     * 
     * @throws InterruptedException
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
     */
    @Test
    public void updateAll_shouldFetchAllIssueForTheProjects() throws ProjectNotFoundException {

        try {

            Long taskid = 0L;

            // Adding one task to each project to issue tracker
            taskid = createTestTask("tita_test_issue_new0", "issue_summary_new", "projectName0");
            this.taskIds.add(taskid);
            taskid = createTestTask("tita_test_issue_new1", "issue_summary_new", "projectName1");
            this.taskIds.add(taskid);
            taskid = createTestTask("tita_test_issue_new2", "issue_summary_new", "projectName2");
            this.taskIds.add(taskid);

            this.log.info("Starting update");
            long starttime = System.currentTimeMillis();
            this.taskService.updateAll();
            long endtime = System.currentTimeMillis();
            long duration = endtime - starttime;
            this.log.info("End of update");
            this.log.info("Duration:" + duration / 1000 + " sec.");

            assertEquals(this.numberOfProjects, this.taskService.getProjects().size());
            assertEquals(this.numberOfTasksForEachProject + 1, this.taskService.getTasks(
                    this.taskService.getProjects().get(this.projectIds.get(0)), IssueStatus.NEW).size());
            assertNotNull(this.taskService.getIssueTrackerDao().findProject("projectName0"));

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
     * 
     */
    @Test
    public void updateProject() throws ProjectNotFoundException {
        try {

            Long taskid = 0L;

            // Adding a task to the project in the issue tracker
            taskid = createTestTask("tita_test_issue_new0", "issue_summary_new", "projectName0");
            this.taskIds.add(taskid);

            this.log.info("Starting update");
            long starttime = System.currentTimeMillis();
            this.taskService.updateProject(this.taskService.getProjects().get(this.projectIds.get(0)));
            long endtime = System.currentTimeMillis();
            long duration = endtime - starttime;
            this.log.info("End of update");
            this.log.info("Duration:" + duration / 1000 + " sec.");

            IProjectTrackable pro = this.taskService.getIssueTrackerDao().findProject(this.projectIds.get(0));

            assertEquals(this.numberOfProjects, this.taskService.getProjects().size());
            assertEquals(this.numberOfTasksForEachProject + 1, this.taskService.getTasks(pro, IssueStatus.NEW).size());

        } catch (MCException e) {
            fail("Creating or deleting projects and issues failed.");
            e.getStackTrace();
        }
    }

    /**
     * The test case shows how the task service reacts when no project is set.
     * 
     * @throws ProjectNotFoundException
     */
    @Test(expected = ProjectNotFoundException.class)
    public void getTasks_shouldThrowProjectNotFoundException() throws ProjectNotFoundException {

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
     * @throws ProjectNotFoundException
     */
    @Test
    public void getTasks() throws ProjectNotFoundException {

        this.taskService = new TaskService();
        IProjectTrackable project = this.taskService.getIssueTrackerDao().findProject(this.projectIds.get(0));

        assertNull(this.taskService.getTasks(project, IssueStatus.ASSIGNED));

    }

    /**
     * The method creates a setup in mantis to evaluate the performance and the
     * update mechanism from TiTA.
     * 
     * @param numberOfProjects
     * @param numberOfTasksForEachProject
     * @throws MCException
     * @throws ProjectNotFoundException
     */
    private void createSetup(int numberOfProjects, int numberOfTasksForEachProject) throws MCException,
            ProjectNotFoundException {
        List<Long> taskids = new ArrayList<Long>();
        List<Long> projectids = new ArrayList<Long>();

        this.log.info("Starting setup");
        long starttime_setup = System.currentTimeMillis();

        Long id = 1L;
        Long taskid = 1L;

        for (int i = 0; i < numberOfProjects; i++) {
            id = createTestProject("projectName" + i, "description" + i, true, false);
            projectids.add(id);

            for (int j = 0; j < numberOfTasksForEachProject; j++) {
                taskid = createTestTask("tita_test_issue" + i + j, "issue_summary" + i + j, "projectName" + i);
                taskids.add(taskid);
            }
        }
        this.taskIds = taskids;
        this.projectIds = projectids;

        assertEquals(0, this.taskService.getProjects().size());

        this.taskService = new TaskService();

        assertEquals(numberOfProjects, this.taskService.getProjects().size());
        assertEquals(numberOfTasksForEachProject, this.taskService.getTasks(this.taskService.getProjects().get(id),
                IssueStatus.NEW).size());

        long endtime_setup = System.currentTimeMillis();
        long duration_setup = endtime_setup - starttime_setup;
        duration_setup = endtime_setup - starttime_setup;

        this.log.info("End of setup");
        this.log.info("Duration Setup:" + duration_setup / 1000 + " sec.");
    }

    /**
     * The method undo the setup for mantis.
     * 
     * @throws InterruptedException
     */
    private void deleteSetupAndChanges() throws InterruptedException {

        Thread.sleep(2000);

        this.log.info("Starting deleting");
        long starttime_deleting = System.currentTimeMillis();

        // delete tasks
        for (int i = 0; i < this.taskIds.size(); i++) {
            deleteTestTask(this.taskIds.get(i));
        }
        // delete projects
        for (int i = 0; i < this.numberOfProjects; i++) {
            deleteTestProject("projectName" + i);
        }
        long endtime_deleting = System.currentTimeMillis();
        long duration_deleting = endtime_deleting - starttime_deleting;
        duration_deleting = endtime_deleting - starttime_deleting;

        this.log.info("End of Deleting");
        this.log.info("Duration Deleting:" + duration_deleting / 1000 + " sec.");
    }
}

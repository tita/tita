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
package at.ac.tuwien.ifs.tita.common.test.service.task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.ac.tuwien.ifs.tita.business.service.tasks.ITaskService;
import at.ac.tuwien.ifs.tita.business.service.tasks.TaskService;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.conv.ProjectStatus;
import at.ac.tuwien.ifs.tita.issuetracker.exceptions.ProjectNotFoundException;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;
import at.ac.tuwien.ifs.tita.common.test.service.task.IssueTrackerServiceTest;

/**
 * Task Service Testcases.
 *
 * @author Christoph
 *
 */
public class TaskServiceTest extends IssueTrackerServiceTest {

    private TiTAProject titaProject;
    private List<IssueTrackerLogin> logins;

    private ITaskService taskService = new TaskService();

    /**
     * Prepare mantis connection and create a setup in mantis with projects and
     * tasks.
     */
    @Before
    @Override
    public void setUp() {

        // try {
        this.logins = new ArrayList<IssueTrackerLogin>();
        this.logins.add(this.defaultLogin);

        // Creating issueTrackerProjects
        // setup defines the projects in mantis
        IssueTrackerProject issueTrackerProject0 = new IssueTrackerProject();
        issueTrackerProject0.setProjectName("projectName0");
        IssueTrackerProject issueTrackerProject1 = new IssueTrackerProject();
        issueTrackerProject1.setProjectName("projectName1");
        Set<IssueTrackerProject> issueTrackerProjects = new HashSet<IssueTrackerProject>();
        issueTrackerProjects.add(issueTrackerProject0);
        issueTrackerProjects.add(issueTrackerProject1);

        // Creating a titaproject with the issueTrackerProjects
        this.titaProject = new TiTAProject();
        this.titaProject.setName("TestProjektTita");
        this.titaProject.setDeleted(false);
        this.titaProject.setProjectStatus(new ProjectStatus(1L, "open"));
        this.titaProject.setIssueTrackerProjects(issueTrackerProjects);

        this.taskService.setLogins(this.logins);
        this.taskService.setProject(this.titaProject);

        super.setUp();
        String test = "";

        // } catch (MCException e) {
        // fail("Mantis connection error.");
        // } catch (ProjectNotFoundException e) {
        // fail("Project must be set.");
        // } catch (InterruptedException e) {
        // fail("Interruption Failure.");
        // }
    }

    /**
     * Delete mantis projects for all tests.
     *
     * @throws InterruptedException
     *             e
     */
    @After
    @Override
    public void tearDown() throws InterruptedException {
        this.titaProject = null;
        deleteSetupAndChanges();
    }

    /**
     * The test case should fetch the tasks from the issue tracker projects
     * 'projectName0' and 'projectName1' from mantis and provide as a list.
     *
     * @throws ProjectNotFoundException
     *             pnfe
     */
    @Test
    public void fetchTaskFromIssueTrackerProjects() throws ProjectNotFoundException {

        Assert.assertNull(this.taskService.getMapOfTasksFromAllProjectsIncludedInTiTAProject());
        this.taskService.fetchTaskFromIssueTrackerProjects();
        Assert.assertNotNull(this.taskService.getMapOfTasksFromAllProjectsIncludedInTiTAProject());
        getOutputOfTasks(this.taskService.getMapOfTasksFromAllProjectsIncludedInTiTAProject());
    }

    /**
     * Method.
     */
    public void getIssueTrackerTasksGroupByIssueTracker() {

    }

    /**
     * Helper method.
     *
     * @param tasks
     *            a map of tasks
     * @return the output as a well formed string for developing tests
     */
    private String getOutputOfTasks(Map<Long, ITaskTrackable> tasks) {

        String output = "";

        for (ITaskTrackable task : tasks.values()) {
            output += "Description: " + task.getDescription() + " Projekt: "
                    + task.getProject().getName();
        }

        return null;
    }

}

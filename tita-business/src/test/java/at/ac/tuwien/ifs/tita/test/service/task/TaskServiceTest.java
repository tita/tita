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
package at.ac.tuwien.ifs.tita.test.service.task;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import at.ac.tuwien.ifs.tita.business.service.tasks.ITaskService;
import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.dao.project.TiTAProjectDao;
import at.ac.tuwien.ifs.tita.dao.user.UserDAO;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.TiTAUserProject;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;
import at.ac.tuwien.ifs.tita.entity.conv.Role;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueStatus;
import at.ac.tuwien.ifs.tita.issuetracker.exceptions.ProjectNotFoundException;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;

/**
 * Task Service Testcases.
 *
 * @author Christoph
 *
 */

public class TaskServiceTest extends IssueTrackerServiceTest {

    private TiTAProject titaProject;
    private TiTAUser titaUser;

    private Set<IssueTrackerLogin> logins;

    @Qualifier("titaProjectDao")
    @Autowired
    private TiTAProjectDao titaProjectDao;

    @Autowired
    private UserDAO titaUserDao;

    @Autowired
    private IUserService userService;

    @Autowired
    private ITaskService taskService;

    /**
     * Prepare mantis connection and create a setup in mantis with projects and
     * tasks.
     */
    @Before
    public void prepareMantisAndTitaSetup() {

        logins = new HashSet<IssueTrackerLogin>();
        logins.add(defaultLogin);

        super.prepareSetup();

        // Creating issueTrackerProjects
        // setup defines the projects in mantis
        IssueTrackerProject issueTrackerProject0 = new IssueTrackerProject();
        issueTrackerProject0.setIsstProjectId(projectIds.get(0));
        issueTrackerProject0.setProjectName("projectName0");
        IssueTrackerProject issueTrackerProject1 = new IssueTrackerProject();
        issueTrackerProject1.setIsstProjectId(projectIds.get(1));
        issueTrackerProject1.setProjectName("projectName1");
        Set<IssueTrackerProject> issueTrackerProjects = new HashSet<IssueTrackerProject>();
        issueTrackerProjects.add(issueTrackerProject0);
        issueTrackerProjects.add(issueTrackerProject1);

        // Creating a tita project with the issueTrackerProjects
        titaProject = new TiTAProject();
        titaProject.setName("TestProjektTita");
        titaProject.setDeleted(false);
        // this.titaProject.setProjectStatus(new ProjectStatus(1L, "open"));
        titaProject.setIssueTrackerProjects(issueTrackerProjects);

        issueTrackerProject0.setTitaProject(titaProject);
        issueTrackerProject1.setTitaProject(titaProject);

        titaProjectDao.save(titaProject);
        titaProjectDao.flushnClear();

        // Creating a titaUser and associations
        Role r1 = new Role(1L, "role 1");
        userService.saveRole(r1);

        IssueTracker iT1 = new IssueTracker(1L, "Mantis", "http://localhost/mantisbt-1.1.8");
        logins.iterator().next().setIssueTracker(iT1);

        titaUser = new TiTAUser("timeconsumer", "timeconsumer", "Christoph", "Zehetner",
                "christoph.zehetner@gmx.at", false, r1, null, logins);

        // CHECKSTYLE:OFF
        TiTAUserProject titaUserProject = new TiTAUserProject(titaUser, titaProject, 12L);
        // CHECKSTYLE:ON

        Set<TiTAUserProject> tup1 = new HashSet<TiTAUserProject>();
        tup1.add(titaUserProject);
        titaUser.setTitaUserProjects(tup1);

        taskService.saveIssueTracker(iT1);

        titaUserDao.save(titaUser);
        titaUserDao.flush();

    }

    /**
     * Delete mantis projects for all tests.
     *
     * @throws InterruptedException e
     */
    @After
    public void undoSetup() throws InterruptedException {
        super.deleteSetup();
        userService.saveRole(titaUser.getRole());
        titaUserDao.delete(titaUser);
        titaProjectDao.delete(titaProjectDao.findById(titaProject.getId()));
    }

    /**
     * The test case should fetch the tasks from the issue tracker projects
     * 'projectName0' and 'projectName1' from mantis and provide as a list.
     *
     *
     *
     * @throws ProjectNotFoundException pnfe
     */
    @Test
    public void fetchTaskFromIssueTrackerProjects() throws ProjectNotFoundException {

        Map<Long, ITaskTrackable> map = taskService.getMapOfTasksFromAllProjectsIncludedInTiTAProject();

        // Assert.assertNull(this.taskService.getMapOfTasksFromAllProjectsIncludedInTiTAProject());
        taskService.fetchTaskFromIssueTrackerProjects(titaProject.getId(), titaUser.getId());
        // CHECKSTYLE:OFF
        assertEquals(4, taskService.getMapOfTasksFromAllProjectsIncludedInTiTAProject().size());
        // CHECKSTYLE:ON
        // getOutputOfTasks(this.taskService.getMapOfTasksFromAllProjectsIncludedInTiTAProject());

    }

    /**
     * The test case should return 4 tasks, because every task has the status
     * 'NEW'.
     *
     * @throws ProjectNotFoundException pnfe
     */

    @Test
    public void sortingTasksByIssueStatus() throws ProjectNotFoundException {
        taskService.fetchTaskFromIssueTrackerProjects(titaProject.getId(), titaUser.getId());

        Map<Long, ITaskTrackable> map = taskService.sortingTasksByIssueStatus(IssueStatus.NEW);

        // CHECKSTYLE:OFF
        assertEquals(4, taskService.getMapOfTasksFromAllProjectsIncludedInTiTAProject().size());

        assertEquals(4, taskService.sortingTasksByIssueStatus(IssueStatus.NEW).size());
        // CHECKSTYLE:ON
        // getOutputOfTasks(this.taskService.getMapOfTasksFromAllProjectsIncludedInTiTAProject());
    }

    /**
     * The test case should return 4 tasks, because every task is provided from
     * mantis with the same url.
     *
     * @throws ProjectNotFoundException pnfe
     */
    @Test
    public void sortingTasksByIssueTracker() throws ProjectNotFoundException {
        taskService.fetchTaskFromIssueTrackerProjects(titaProject.getId(), titaUser.getId());
        // CHECKSTYLE:OFF
        String test = logins.iterator().next().getIssueTracker().getUrl();

        assertEquals(4, taskService.sortingTasksByIssueTracker(
                logins.iterator().next().getIssueTracker().getUrl()).size());
        // CHECKSTYLE:ON
        getOutputOfTasks(taskService.getMapOfTasksFromAllProjectsIncludedInTiTAProject());
    }

    /**
     * Helper method.
     *
     * @param tasks a map of tasks
     * @return the output as a well formed string for developing tests
     */
    private String getOutputOfTasks(Map<Long, ITaskTrackable> tasks) {

        String output = "";

        for (ITaskTrackable task : tasks.values()) {
            output += "Description: " + task.getDescription() + " Projekt: " + task.getProject().getName();
        }

        return null;
    }
}

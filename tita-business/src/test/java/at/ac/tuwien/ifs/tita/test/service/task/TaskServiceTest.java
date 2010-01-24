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
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import at.ac.tuwien.ifs.tita.business.service.tasks.ITaskService;
import at.ac.tuwien.ifs.tita.business.service.time.IEffortService;
import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.dao.interfaces.ITiTAProjectDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.IUserDAO;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.TiTAUserProject;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;
import at.ac.tuwien.ifs.tita.entity.conv.Role;
import at.ac.tuwien.ifs.tita.entity.util.UserProjectTaskEffort;
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
    private ITiTAProjectDao titaProjectDao;

    @Autowired
    private IUserDAO titaUserDao;

    @Autowired
    private IUserService userService;

    @Autowired
    private ITaskService taskService;

    @Resource(name = "timeEffortService")
    private IEffortService timeEffortService;

    private IssueTrackerProject issueTrackerProject0;
    private IssueTrackerProject issueTrackerProject1;

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
        issueTrackerProject0 = new IssueTrackerProject();
        issueTrackerProject0.setIsstProjectId(projectIds.get(0));
        issueTrackerProject0.setProjectName("projectName0");
        issueTrackerProject1 = new IssueTrackerProject();
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

        titaUser = new TiTAUser("timeconsumer", "timeconsumer", "Christoph", "Zehetner", "christoph.zehetner@gmx.at",
                false, r1, null, logins);

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
     * @throws ProjectNotFoundException
     *             pnfe
     * @throws InterruptedException
     *             ie
     */
    @Test
    public void fetchTaskFromIssueTrackerProjects() throws ProjectNotFoundException,
            InterruptedException {

        Map<Long, ITaskTrackable> map = taskService.getMapOfTasksFromAllProjectsIncludedInTiTAProject();

        // Assert.assertNull(this.taskService.getMapOfTasksFromAllProjectsIncludedInTiTAProject());
        taskService.fetchTaskFromIssueTrackerProjects(titaProject.getId(), titaUser.getId());

        // CHECKSTYLE:OFF
        Thread.sleep(20000);
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

        assertEquals(4, taskService.sortingTasksByIssueTracker(logins.iterator().next().getIssueTracker().getUrl())
                .size());
        // CHECKSTYLE:ON
        getOutputOfTasks(taskService.getMapOfTasksFromAllProjectsIncludedInTiTAProject());
    }

    /**
     * Test for Performance of Person.
     */
    @Test
    public void getPerformanceOfPersonViewTest() {
        // CHECKSTYLE:OFF
        TiTATask titaTask1 = new TiTATask("task1", titaUser, titaProject, null);
        TiTATask titaTask2 = new TiTATask("task2", titaUser, titaProject, null);
        Effort e1 = new Effort(new Date(), 3600000L, false, "titaTaskEffort1", titaUser);
        Effort e2 = new Effort(new Date(), 3600000L, false, "titaTaskEffort2", titaUser);
        Effort e3 = new Effort(new Date(), 3600000L, false, "issTrackTaskEffort1", titaUser);
        Effort e4 = new Effort(new Date(), 3600000L, false, "issTrackTaskEffort2", titaUser);
        Effort e5 = new Effort(new Date(), 3600000L, false, "issTrackTaskEffort3", titaUser);
        Effort e6 = new Effort(new Date(), 3600000L, false, "issTrackTaskEffort4", titaUser);
        IssueTrackerTask issTrackTask1 = null;
        IssueTrackerTask issTrackTask2 = null;

        try {
            taskService.saveTiTATask(titaTask1);
            taskService.saveTiTATask(titaTask2);
            e1.setTitaTask(titaTask1);
            e2.setTitaTask(titaTask1);
            timeEffortService.saveEffort(e1);
            timeEffortService.saveEffort(e2);

            taskService.fetchTaskFromIssueTrackerProjects(titaProject.getId(), titaUser.getId());
            int count = 0;
            for (ITaskTrackable task : taskService.getMapOfTasksFromAllProjectsIncludedInTiTAProject().values()) {
                if (count == 0) {
                    issTrackTask1 = new IssueTrackerTask(issueTrackerProject0, task.getId(), task.getSummary(), null);
                    taskService.saveIssueTrackerTask(issTrackTask1);
                    e3.setIssueTTask(issTrackTask1);
                    e4.setIssueTTask(issTrackTask1);
                } else if (count == 1) {
                    issTrackTask2 = new IssueTrackerTask(issueTrackerProject0, task.getId(), task.getSummary(), null);
                    taskService.saveIssueTrackerTask(issTrackTask2);
                    e5.setIssueTTask(issTrackTask2);
                    e6.setIssueTTask(issTrackTask2);
                }

                count++;
            }

            timeEffortService.saveEffort(e3);
            timeEffortService.saveEffort(e4);
            timeEffortService.saveEffort(e5);
            timeEffortService.saveEffort(e6);

            List<UserProjectTaskEffort> list = taskService.getPerformanceOfPersonView(titaProject, titaUser);
            Assert.assertNotNull(list);
            Assert.assertEquals(4, list.size());
            Assert.assertEquals(7200000L, (long) list.get(0).getDuration());
            Assert.assertEquals(7200000L, (long) list.get(2).getDuration());
        } catch (PersistenceException e) {
            fail();
        } catch (ProjectNotFoundException e) {
            fail();
        } finally {
            timeEffortService.deleteEffort(e1);
            timeEffortService.deleteEffort(e2);
            timeEffortService.deleteEffort(e3);
            timeEffortService.deleteEffort(e4);
            timeEffortService.deleteEffort(e5);
            timeEffortService.deleteEffort(e6);
            taskService.deleteTiTATask(titaTask1);
            taskService.deleteTiTATask(titaTask2);
            taskService.deleteIssueTrackerTask(issTrackTask1);
            taskService.deleteIssueTrackerTask(issTrackTask2);
        }
        // CHECKSTYLE:ON
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

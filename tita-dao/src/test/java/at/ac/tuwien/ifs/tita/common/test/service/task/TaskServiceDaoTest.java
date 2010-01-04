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

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.ifs.tita.business.service.tasks.ITaskService;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;
import at.ac.tuwien.ifs.tita.entity.conv.ProjectStatus;

/**
 * Task Service Dao Testcases.
 * 
 * @author Christoph
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:datasourceContext-test.xml" })
@TransactionConfiguration
@Transactional
public class TaskServiceDaoTest {

    private IssueTrackerLogin defaultLogin = new IssueTrackerLogin("administrator", "root", new IssueTracker(1L,
            "test-mantis", "http://localhost/mantisbt-1.1.8"), null);

    private TiTAProject titaProject;
    private List<IssueTrackerLogin> logins;

    @Resource(name = "taskService")
    private ITaskService service;

    /**
     * Prepare mantis connection and create a setup in mantis with projects and
     * tasks.
     */
    @Before
    public void setUp() {

        // try {
        this.logins = new ArrayList<IssueTrackerLogin>();
        this.logins.add(this.defaultLogin);

        this.titaProject = new TiTAProject();
        this.titaProject.setName("TestProjektTita");
        this.titaProject.setDeleted(false);
        this.titaProject.setProjectStatus(new ProjectStatus(1L, "open"));

        // this.taskService.setLogins(this.logins);
        // this.taskService.setProject(this.titaProject);
        // this.taskService.fetchTaskFromIssueTrackerProjects();
        //
        // } catch (ProjectNotFoundException e) {
        // fail("Project must be set.");
        // }
    }

    /**
     * Delete mantis projects for all tests.
     * 
     * @throws InterruptedException e
     */
    @After
    public void tearDown() throws InterruptedException {
        this.logins = null;

    }

    /**
     * The test case should fetch all tita task, that are created for self
     * defined efforts.
     */
    @Test
    @Ignore
    public void getTiTATasks() {

    }

    /**
     * The test case save a TiTATask for a self defined effort.
     */
    @Test
    public void saveTiTATask() {
        TiTATask titaTask = new TiTATask();
        try {
            this.service.saveTiTATask(titaTask);
            Assert.assertNotNull(titaTask.getId());
            Assert.assertNotNull(this.service.getTiTATaskById(titaTask.getId()));
        } catch (PersistenceException e) {
            fail("");
        } finally {
            this.service.deleteTiTATask(titaTask);
        }
    }

    /**
     * The test case should save a issue tracker task that is provided from the
     * task list.
     */
    @Test
    public void saveIssueTrackerTask() {
        IssueTrackerTask issueTrackerTask = new IssueTrackerTask();
        try {
            this.service.saveIssueTrackerTask(issueTrackerTask);
            Assert.assertNotNull(issueTrackerTask.getId());
            Assert.assertNotNull(this.service.getIssueTrackerTaskById(issueTrackerTask.getId()));
        } catch (PersistenceException e) {
            fail("");
        } finally {
            this.service.deleteIssueTrackerTask(issueTrackerTask);
        }
    }

    /**
     * The test case should save a issue tracker that is provided from the task
     * list.
     */
    @Test
    public void saveIssueTracker() {
        IssueTracker issueTracker = new IssueTracker(1L, "Mantis", "http://localhost/mantisbt-1.1.8");
        try {
            this.service.saveIssueTracker(issueTracker);
            Assert.assertNotNull(issueTracker.getId());
            Assert.assertNotNull(this.service.getIssueTrackerById(issueTracker.getId()));
        } catch (PersistenceException e) {
            fail("");
        } finally {
            this.service.deleteIssueTracker(issueTracker);
        }
    }

}

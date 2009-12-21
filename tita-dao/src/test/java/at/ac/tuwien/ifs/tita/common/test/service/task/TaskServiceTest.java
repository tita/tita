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

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mantisbt.connect.MCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.ifs.tita.business.service.tasks.ITaskService;
import at.ac.tuwien.ifs.tita.business.service.tasks.TaskService;
import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.conv.ProjectStatus;
import at.ac.tuwien.ifs.tita.issuetracker.exceptions.ProjectNotFoundException;
import at.ac.tuwien.ifs.tita.issuetracker.mantis.issue.service.IssueTrackerServiceTest;

/**
 * Task Service Testcases.
 *
 * @author Christoph
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:datasourceContext-test.xml" })
@TransactionConfiguration
@Transactional
public class TaskServiceTest extends IssueTrackerServiceTest {

    private TiTAProject titaProject;

    private List<IssueTrackerLogin> logins;


    @Autowired
    private ITaskService taskService;

    /**
     * Prepare mantis connection and create a setup in mantis with projects and
     * tasks.
     */
    @Override
    @Before
    public void setUp() {
        super.setUp();

        try {
            this.logins = new ArrayList<IssueTrackerLogin>();
            this.logins.add(this.defaultLogin);
            this.taskService = new TaskService(this.titaProject, this.logins);

            this.titaProject = new TiTAProject();
            this.titaProject.setName("TestProjektTita");
            this.titaProject.setDeleted(false);
            this.titaProject.setProjectStatus(new ProjectStatus(1L, "open"));

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
     * @throws InterruptedException
     *             e
     */
    @Override
    @After
    public void tearDown() throws InterruptedException {
        this.logins = null;
        deleteSetupAndChanges();
    }

    /**
     * The test case should fetch all tita task, that are created for self
     * defined efforts.
     */
    @Test
    public void getTiTATasks() {

    }

    /**
     * The test case save a TiTATask for a self defined effort.
     */
    @Test
    public void saveTiTATask() {
        TiTATask titaTask = new TiTATask();
        try {
            this.taskService.saveTiTATask(titaTask);
        } catch (TitaDAOException e) {
            fail("");
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
            this.taskService.saveIssueTrackerTask(issueTrackerTask);
        } catch (TitaDAOException e) {
            fail("");
        }
        Assert.assertNotNull(issueTrackerTask.getId());
    }

}

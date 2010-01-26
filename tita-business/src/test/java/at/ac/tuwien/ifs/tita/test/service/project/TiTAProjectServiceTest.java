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
package at.ac.tuwien.ifs.tita.test.service.project;

import static org.mockito.Mockito.mock;

import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import at.ac.tuwien.ifs.tita.business.service.project.IProjectService;
import at.ac.tuwien.ifs.tita.business.service.project.ProjectService;
import at.ac.tuwien.ifs.tita.dao.GenericHibernateDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.IGenericHibernateDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.IIssueTrackerProjectDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.IIssueTrackerTaskDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.ITiTATaskDao;
import at.ac.tuwien.ifs.tita.dao.issuetracker.IssueTrackerProjectDao;
import at.ac.tuwien.ifs.tita.dao.issuetracker.task.IssueTrackerTaskDao;
import at.ac.tuwien.ifs.tita.dao.project.TiTAProjectDao;
import at.ac.tuwien.ifs.tita.dao.task.TiTATaskDao;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.TiTAUserProject;

/**
 * Tests the ProjectService.
 * 
 * @author ASE Group 10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:datasourceContext-test.xml" })
@TransactionConfiguration
public class TiTAProjectServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private IProjectService service;

    private TiTAProject p1;
    private TiTAProject p2;
    private TiTAProject p3;

    /**
     * Prepare test.
     */
    @Before
    public void setUp() {
        p1 = new TiTAProject();
        p2 = new TiTAProject();
        p3 = new TiTAProject();

        try {
            service.saveProject(p1);
            service.saveProject(p2);
            service.saveProject(p3);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    /**
     * clean up.
     */
    @After
    public void tearDown() {
        try {
            service.deleteProject(p1);
        } catch (Exception e) {
        }
        try {
            service.deleteProject(p2);
        } catch (Exception e) {
        }
        try {
            service.deleteProject(p3);
        } catch (Exception e) {
        }
    }

    /**
     * Mocking projectService for Testing saveProject.
     */
    @Test
    public void testSaveProject() {
        TiTAProjectDao dao = mock(TiTAProjectDao.class);

        ProjectService pservice = new ProjectService();
        pservice.setTitaProjectDao(dao);

        TiTAProject p = mock(TiTAProject.class);

        pservice.saveProject(p);

        Mockito.verify(dao, Mockito.times(1)).save(p);
    }

    /**
     * Mocking projectService for Testing saveProject.
     */
    @Test
    public void testSaveIssueTrackerTask() {
        IIssueTrackerTaskDao dao = mock(IssueTrackerTaskDao.class);

        ProjectService pservice = new ProjectService();
        pservice.setIssueTrackerTaskDao(dao);

        IssueTrackerTask t = mock(IssueTrackerTask.class);

        pservice.saveIssueTrackerTask(t);

        Mockito.verify(dao, Mockito.times(1)).save(t);
    }

    /**
     * Mocking projectService for Testing deleteProject.
     */
    @Test
    public void testDeleteProject() {
        TiTAProjectDao dao = mock(TiTAProjectDao.class);

        ProjectService pservice = new ProjectService();
        pservice.setTitaProjectDao(dao);

        TiTAProject p = mock(TiTAProject.class);

        pservice.deleteProject(p);

        Mockito.verify(dao, Mockito.times(1)).delete(p);
    }

    /**
     * Mocking projectService for Testing getProjectById.
     */
    @Test
    public void testGetProjectById() {
        TiTAProjectDao dao = mock(TiTAProjectDao.class);

        ProjectService pservice = new ProjectService();
        pservice.setTitaProjectDao(dao);

        pservice.getProjectById(1L);

        Mockito.verify(dao, Mockito.times(1)).findById(1L);
    }

    /**
     * Test finding all Tita Projects
     */
    @Test
    public void testFindAllTiTAProjects() {
        List<TiTAProject> list = service.findAllTiTAProjects();
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);
    }

    /**
     * Mocking projectService for Testing findTiTAProjectsForUser.
     */
    @Test
    public void testFindTiTAProjectsForUser() {
        TiTAProjectDao dao = mock(TiTAProjectDao.class);

        ProjectService pservice = new ProjectService();
        pservice.setTitaProjectDao(dao);

        TiTAUser u = mock(TiTAUser.class);

        pservice.findTiTAProjectsForUser(u);

        Mockito.verify(dao, Mockito.times(1)).findTiTAProjectsForUsername(u.getUserName());
    }

    /**
     * Mocking projectService for Testing findIssueTrackerTaskForTiTAProject.
     */
    @Test
    public void testFindIssueTrackerTaskForTiTAProject() {
        IIssueTrackerTaskDao dao = mock(IssueTrackerTaskDao.class);

        ProjectService pservice = new ProjectService();
        pservice.setIssueTrackerTaskDao(dao);

        pservice.findIssueTrackerTaskForTiTAProject(1L, 1L, 1L, 1L);

        Mockito.verify(dao, Mockito.times(1)).findIssueTrackerTask(1L, 1L, 1L, 1L);
    }

    /**
     * Mocking projectService for Testing findTiTAProjectsForUser.
     */
    @Test
    public void testFindIssueTrackerProjectForTiTAProject() {
        IIssueTrackerProjectDao dao = mock(IssueTrackerProjectDao.class);

        ProjectService pservice = new ProjectService();
        pservice.setIssueTrackerProjectDao(dao);

        pservice.findIssueTrackerProjectForTiTAProject(1L, 1L, 1L);

        Mockito.verify(dao, Mockito.times(1)).findIssueTrackerProjectForTiTAProject(1L, 1L, 1L);
    }

    /**
     * Mocking projectService for Testing saveTiTATask.
     */
    @Test
    public void testSaveTiTATask() {
        ITiTATaskDao dao = mock(TiTATaskDao.class);

        ProjectService pservice = new ProjectService();
        pservice.setTitaTaskDao(dao);

        TiTATask t = mock(TiTATask.class);

        pservice.saveTiTATask(t);

        Mockito.verify(dao, Mockito.times(1)).save(t);
    }

    /**
     * Mocking projectService for Testing getOrderedProjects.
     */
    @Test
    public void testGetOrderedProjects() {
        TiTAProjectDao dao = mock(TiTAProjectDao.class);

        ProjectService pservice = new ProjectService();
        pservice.setTitaProjectDao(dao);

        pservice.getOrderedProjects(-1, "name");

        Mockito.verify(dao, Mockito.times(1)).findProjectsOrderedByName(-1, "name");
    }

    /**
     * Mocking projectService for Testing getOrderedProjects.
     */
    @Test
    public void testGetAvailableProjectStati() {
        TiTAProjectDao dao = mock(TiTAProjectDao.class);

        ProjectService pservice = new ProjectService();
        pservice.setTitaProjectDao(dao);

        pservice.getAvailableProjectStati();

        Mockito.verify(dao, Mockito.times(1)).getAvailableProjectStati();
    }

    /**
     * Mocking projectService for Testing getAvailableIssueTracker.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetAvailableIssueTracker() {

        GenericHibernateDao dao = mock(GenericHibernateDao.class);

        ProjectService pservice = new ProjectService();
        pservice.setIssueTrackerDao(dao);

        pservice.getAvailableIssueTracker();

        Mockito.verify(dao, Mockito.times(1)).findAll();
    }

    /**
     * Mocking projectService for Testing saveUserProject.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSaveUserProject() {
        IGenericHibernateDao dao = mock(GenericHibernateDao.class);

        ProjectService pservice = new ProjectService();
        pservice.setUserProjectDao(dao);

        TiTAUserProject up = mock(TiTAUserProject.class);

        pservice.saveUserProject(up);

        Mockito.verify(dao, Mockito.times(1)).save(up);
    }
}

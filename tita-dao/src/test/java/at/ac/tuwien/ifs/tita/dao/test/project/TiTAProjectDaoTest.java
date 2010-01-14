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
package at.ac.tuwien.ifs.tita.dao.test.project;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.ifs.tita.dao.interfaces.IIssueTrackerProjectDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.IIssueTrackerTaskDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.ITiTAProjectDao;
import at.ac.tuwien.ifs.tita.dao.test.util.INativeSqlExecutorDao;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;

/**
 * Testclass for all test concerning tita daos.
 * @author herbert
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:datasourceContext-test.xml" })
@TransactionConfiguration
@Transactional
public class TiTAProjectDaoTest {
    
    @Qualifier("issueTrackerTaskDAO")
    @Autowired
    private IIssueTrackerTaskDao taskDao;
    
    @Qualifier("issueTrackerProjectDAO")
    @Autowired
    private IIssueTrackerProjectDao itpDao;
    
    @Qualifier("titaProjectDAO")
    @Autowired
    private ITiTAProjectDao tpDao;
    
    @Autowired
    private INativeSqlExecutorDao<Object, Long> executor;
    
    /**
     * Prepare on TiTA Project for testing effort dao.
     * @throws Exception ex
     */
    @Before
    public void prepareDB() throws Exception {
        executor.executeSqlFile(this);
    }

    /**
     * Clean db after each test.
     */
    @After
    public void cleanDB(){
        executor.executeSql("delete from EFFORT;"+
                            "delete from TITA_TASK;"+
                            "delete from ISSUE_TRACKER_TASK;" +
                            "delete from ISSUE_TRACKER_PROJECT;"+
                            "delete from USER_PROJECT;"+
                            "delete from TITA_PROJECT;"+
                            "delete from CONV_PROJECT_STATUS;"+
                            "delete from CONV_ISSUE_TRACKER;"+
                            "delete from ISST_LOGIN;"+
                            "delete from TITA_USER;"+
                            "delete from CONV_ROLE;commit;");
    }
    
    /**
     * Methode.
     */
    @Test
    public void findIssueTrackerTaskShouldSucceed(){
        //CHECKSTYLE:OFF
        IssueTrackerTask itt = taskDao.findIssueTrackerTask(1L, 1L, 97L, 27L);
        //CHECKSTYLE:ON
        assertNotNull(itt);
    }
    
    /**
     * Methode.
     */
    @Test
    public void findIssueTrackerProjectForTiTAProjectShouldSucceed(){
        //CHECKSTYLE:OFF
        IssueTrackerProject project = itpDao.findIssueTrackerProjectForTiTAProject(1L, 1L, 96L);
        //CHECKSTYLE:ON
        assertNotNull(project);
    }
    
    /**
     * Methode.
     */
    @Test
    public void findTiTAProjectsForUsernameShouldSucceed(){
        //CHECKSTYLE:OFF
        List<TiTAProject> projects = tpDao.findTiTAProjectsForUsername("hans");
        //CHECKSTYLE:ON
        assertNotNull(projects);
        assertEquals(1, projects.size());
    }
}

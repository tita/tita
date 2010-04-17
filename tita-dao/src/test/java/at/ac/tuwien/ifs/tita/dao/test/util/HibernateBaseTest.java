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
package at.ac.tuwien.ifs.tita.dao.test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

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

import at.ac.tuwien.ifs.tita.dao.interfaces.IEffortDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.IGenericHibernateDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.IIssueTrackerProjectDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.IIssueTrackerTaskDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.ITiTAProjectDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.ITiTATaskDao;
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
import at.ac.tuwien.ifs.tita.entity.conv.ProjectStatus;
import at.ac.tuwien.ifs.tita.entity.conv.Role;

/**
 * Testclass for all test concerning tita daos.
 *
 * @author christoph
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:datasourceContext-test.xml" })
@TransactionConfiguration
@Transactional
public class HibernateBaseTest {

    private static final Long C_ONE = 1L;
    private static final Long C_TWO = 2L;
    private static final Long C_THREE = 3L;
    private static final Long C_FOUR = 4L;

    @Qualifier("issueTrackerDAO")
    @Autowired
    private IGenericHibernateDao<IssueTracker, Long> issueTrackerDao;

    @Qualifier("issueTrackerLoginDAO")
    @Autowired
    private IGenericHibernateDao<IssueTrackerLogin, Long> issueTrackerLoginDao;

    @Qualifier("roleDAO")
    @Autowired
    private IGenericHibernateDao<Role, Long> roleDao;

    @Qualifier("projectStatusDAO")
    @Autowired
    private IGenericHibernateDao<ProjectStatus, Long> projectStatusDao;

    @Qualifier("titaUserProjectDAO")
    @Autowired
    private IGenericHibernateDao<TiTAUserProject, Long> titaUserProjectDao;

    @Autowired
    private IUserDAO titaUserDao;

    @Autowired
    private ITiTAProjectDao titaProjectDao;

    @Autowired
    private IIssueTrackerProjectDao issueTrackerProjectDao;

    @Autowired
    private IIssueTrackerTaskDao issueTrackerTaskDao;

    @Autowired
    private ITiTATaskDao titaTaskDao;

    @Autowired
    private IEffortDao timeEffortDao;

    // Configuration data sets
    private IssueTracker issueTracker;
    private Role admin, timeconsumer, timecontroller;
    private TiTAUser user1, user2, user3, user4, user5, user6;
    private ProjectStatus open, closed;

    // Project data sets
    private TiTAProject titaProject1, titaProject2, titaProject3, titaProject4, titaProject5,
            titaProject6;
    private TiTAUserProject titaUserProject1, titaUserProject2, titaUserProject3, titaUserProject4,
            titaUserProject5, titaUserProject6, titaUserProject7, titaUserProject8,
            titaUserProject9, titaUserProject10, titaUserProject11, titaUserProject12,
            titaUserProject13, titaUserProject14, titaUserProject15, titaUserProject16,
            titaUserProject17, titaUserProject18, titaUserProject19, titaUserProject20,
            titaUserProject21, titaUserProject22, titaUserProject23, titaUserProject24,
            titaUserProject25, titaUserProject26;

    private IssueTrackerProject issueTrackerProject1, issueTrackerProject2, issueTrackerProject3,
            issueTrackerProject4;

    private IssueTrackerLogin issueTrackerLogin1, issueTrackerLogin2, issueTrackerLogin3,
            issueTrackerLogin4, issueTrackerLogin5, issueTrackerLogin6;

    private IssueTrackerTask task1, task2, task3, task4, task5, task6, task7, task8;

    private TiTATask titaTask1, titaTask2, titaTask3, titaTask4, titaTask5, titaTask6, titaTask7,
            titaTask8;

    private Effort effort1, effort2, effort3, effort4, effort5, effort6, effort7, effort8, effort9,
            effort10, effort11, effort12, effort13, effort14, effort15, effort16, effort17,
            effort18, effort19, effort20, effort21, effort22, effort23, effort24, effort25;

    @Before
    public void setUp() {
        // Setup Configuration data sets
        setConfigurationDataSets();

        // Setup Project data sets
        setupTitaUserProjects();

        // Add the projects to the user
        addProjectsToUser();

        // Add issuetracker projects
        createIssueTrackerProjects();
        addIssueTrackerProjectsToTiTAProject();

        // Create and add issuetracker logins for the user
        // only one because only one issetracker is stored already
        createIssueTrackerLogins();
        addIssueTrackerLoginsToUser();

        // create tasks and efforts and the mapping between
        createTasks();
        addIssueTrackerTasksToIssueTrackerProjects();
        addTitaTasksToTitaProject();
        createEfforts();
        addEffortsToTasks();
    }

    @After
    public void tearDown() {

    }

    private void setConfigurationDataSets() {
        // Configuration data sets for tita
        issueTracker = new IssueTracker(C_ONE, "Mantis-OpenEngSB",
                "http://localhost/mantisbt-1.1.8");

        open = new ProjectStatus(C_ONE, "open");
        closed = new ProjectStatus(C_TWO, "closed");

        admin = new Role(C_ONE, "Administrator");
        timeconsumer = new Role(C_TWO, "Time Consumer");
        timecontroller = new Role(C_THREE, "Time Controller");

        user1 = new TiTAUser("admin", "admin", "Andreas", "Pieber", "anpi@gmx.at", false, admin,
                null, null);
        user2 = new TiTAUser("timecontroller", "timecontroller", "Andreas", "Pieber",
                "anpi@gmx.at", false, timecontroller, null, null);
        user3 = new TiTAUser("timeconsumer3", "timeconsumer", "Christoph", "Zehetner",
                "christoph.zehetner@gmx.at", false, timeconsumer, null, null);
        user4 = new TiTAUser("timeconsumer4", "timeconsumer", "Alexander", "Leutgoeb",
                "alexander.leutgoeb@gmx.at5", false, timeconsumer, null, null);
        user5 = new TiTAUser("timeconsumer", "timeconsumer", "Markus", "Siedler",
                "markus.siedler@gmx.at", false, timeconsumer, null, null);
        user6 = new TiTAUser("timeconsumer6", "timeconsumer", "Johannes", "Ferner",
                "johannes.ferner@gmx.at", false, timeconsumer, null, null);

        // Save configuration data set for tita
        issueTrackerDao.save(issueTracker);
        projectStatusDao.save(open);
        projectStatusDao.save(closed);
        roleDao.save(admin);
        roleDao.save(timeconsumer);
        roleDao.save(timecontroller);
        titaUserDao.save(user1);
        titaUserDao.save(user2);
        titaUserDao.save(user3);
        titaUserDao.save(user4);
        titaUserDao.save(user5);
        titaUserDao.save(user6);
    }

    private void setupTitaUserProjects() {

        titaProject1 = new TiTAProject("Initial test project", "OpenEngSB Project", false, open,
                null, null);
        titaProject2 = new TiTAProject("Initial test project2", "OpenEngSB Project2", false, open,
                null, null);
        titaProject3 = new TiTAProject("Initial test project3", "OpenEngSB Project3", false, open,
                null, null);
        titaProject4 = new TiTAProject("Closed project", "OpenEngSB Project4", false,
                closed, null, null);
        titaProject5 = new TiTAProject("Deleted and open", "OpenEngSB Project5", true, open,
                null, null);
        titaProject6 = new TiTAProject("Deleted and closed", "OpenEngSB Project6", true, closed,
                null, null);

        // user1 is always admin for the project
        titaUserProject1 = new TiTAUserProject(user1, titaProject1, 150L);
        titaUserProject2 = new TiTAUserProject(user1, titaProject2, 150L);
        titaUserProject3 = new TiTAUserProject(user1, titaProject3, 150L);
        titaUserProject4 = new TiTAUserProject(user1, titaProject4, 150L);
        titaUserProject5 = new TiTAUserProject(user1, titaProject5, 150L);
        titaUserProject6 = new TiTAUserProject(user1, titaProject6, 150L);

        // user2 is always timecontroller for the project
        titaUserProject7 = new TiTAUserProject(user2, titaProject1, 150L);
        titaUserProject8 = new TiTAUserProject(user2, titaProject2, 150L);
        titaUserProject9 = new TiTAUserProject(user2, titaProject3, 150L);
        titaUserProject10 = new TiTAUserProject(user2, titaProject4, 150L);
        titaUserProject11 = new TiTAUserProject(user2, titaProject5, 150L);
        titaUserProject12 = new TiTAUserProject(user2, titaProject6, 150L);

        // user3 in project 1, 2, 3
        titaUserProject13 = new TiTAUserProject(user3, titaProject1, 150L);
        titaUserProject14 = new TiTAUserProject(user3, titaProject2, 150L);
        titaUserProject15 = new TiTAUserProject(user3, titaProject3, 150L);

        // user4 in project 1, 4, 5
        titaUserProject16 = new TiTAUserProject(user4, titaProject1, 150L);
        titaUserProject17 = new TiTAUserProject(user4, titaProject4, 150L);
        titaUserProject18 = new TiTAUserProject(user4, titaProject5, 150L);

        // user5 in project 2, 3, 4
        titaUserProject19 = new TiTAUserProject(user5, titaProject2, 150L);
        titaUserProject20 = new TiTAUserProject(user5, titaProject3, 150L);
        titaUserProject21 = new TiTAUserProject(user5, titaProject4, 150L);

        // user6 in project 1, 2, 3, 4, 6
        titaUserProject22 = new TiTAUserProject(user6, titaProject1, 150L);
        titaUserProject23 = new TiTAUserProject(user6, titaProject2, 150L);
        titaUserProject24 = new TiTAUserProject(user6, titaProject3, 150L);
        titaUserProject25 = new TiTAUserProject(user6, titaProject4, 150L);
        titaUserProject26 = new TiTAUserProject(user6, titaProject6, 150L);

        titaProjectDao.save(titaProject1);
        titaProjectDao.save(titaProject2);
        titaProjectDao.save(titaProject3);
        titaProjectDao.save(titaProject4);
        titaProjectDao.save(titaProject5);
        titaProjectDao.save(titaProject6);

        titaUserProjectDao.save(titaUserProject1);
        titaUserProjectDao.save(titaUserProject2);
        titaUserProjectDao.save(titaUserProject3);
        titaUserProjectDao.save(titaUserProject4);
        titaUserProjectDao.save(titaUserProject5);
        titaUserProjectDao.save(titaUserProject6);
        titaUserProjectDao.save(titaUserProject7);
        titaUserProjectDao.save(titaUserProject8);
        titaUserProjectDao.save(titaUserProject9);
        titaUserProjectDao.save(titaUserProject10);
        titaUserProjectDao.save(titaUserProject11);
        titaUserProjectDao.save(titaUserProject12);
        titaUserProjectDao.save(titaUserProject13);
        titaUserProjectDao.save(titaUserProject14);
        titaUserProjectDao.save(titaUserProject15);
        titaUserProjectDao.save(titaUserProject16);
        titaUserProjectDao.save(titaUserProject17);
        titaUserProjectDao.save(titaUserProject18);
        titaUserProjectDao.save(titaUserProject19);
        titaUserProjectDao.save(titaUserProject20);
        titaUserProjectDao.save(titaUserProject21);
        titaUserProjectDao.save(titaUserProject22);
        titaUserProjectDao.save(titaUserProject23);
        titaUserProjectDao.save(titaUserProject24);
        titaUserProjectDao.save(titaUserProject25);
        titaUserProjectDao.save(titaUserProject26);

        Set<TiTAUserProject> set_tup1 = new HashSet<TiTAUserProject>();
        set_tup1.add(titaUserProject1);
        set_tup1.add(titaUserProject7);
        set_tup1.add(titaUserProject13);
        set_tup1.add(titaUserProject16);
        set_tup1.add(titaUserProject22);
        titaProject1.setUsers(set_tup1);

        Set<TiTAUserProject> set_tup2 = new HashSet<TiTAUserProject>();
        set_tup2.add(titaUserProject2);
        set_tup2.add(titaUserProject8);
        set_tup2.add(titaUserProject14);
        set_tup2.add(titaUserProject19);
        set_tup2.add(titaUserProject23);
        titaProject2.setUsers(set_tup2);

        Set<TiTAUserProject> set_tup3 = new HashSet<TiTAUserProject>();
        set_tup3.add(titaUserProject3);
        set_tup3.add(titaUserProject9);
        set_tup3.add(titaUserProject15);
        set_tup3.add(titaUserProject20);
        set_tup3.add(titaUserProject24);
        titaProject3.setUsers(set_tup3);

        Set<TiTAUserProject> set_tup4 = new HashSet<TiTAUserProject>();
        set_tup4.add(titaUserProject4);
        set_tup4.add(titaUserProject10);
        set_tup4.add(titaUserProject17);
        set_tup4.add(titaUserProject21);
        set_tup4.add(titaUserProject25);
        titaProject4.setUsers(set_tup4);

        Set<TiTAUserProject> set_tup5 = new HashSet<TiTAUserProject>();
        set_tup5.add(titaUserProject5);
        set_tup5.add(titaUserProject11);
        set_tup5.add(titaUserProject18);
        titaProject5.setUsers(set_tup5);

        Set<TiTAUserProject> set_tup6 = new HashSet<TiTAUserProject>();
        set_tup6.add(titaUserProject6);
        set_tup6.add(titaUserProject12);
        set_tup6.add(titaUserProject26);
        titaProject6.setUsers(set_tup6);

        titaProjectDao.save(titaProject1);
        titaProjectDao.save(titaProject2);
        titaProjectDao.save(titaProject3);
        titaProjectDao.save(titaProject4);
        titaProjectDao.save(titaProject5);
        titaProjectDao.save(titaProject6);
    }

    private void addProjectsToUser() {
        Set<TiTAUserProject> set_tup1 = new HashSet<TiTAUserProject>();
        set_tup1.add(titaUserProject1);
        set_tup1.add(titaUserProject2);
        set_tup1.add(titaUserProject3);
        set_tup1.add(titaUserProject4);
        set_tup1.add(titaUserProject5);
        set_tup1.add(titaUserProject6);
        user1.setTitaUserProjects(set_tup1);

        Set<TiTAUserProject> set_tup2 = new HashSet<TiTAUserProject>();
        set_tup2.add(titaUserProject7);
        set_tup2.add(titaUserProject8);
        set_tup2.add(titaUserProject9);
        set_tup2.add(titaUserProject10);
        set_tup2.add(titaUserProject11);
        set_tup2.add(titaUserProject12);
        user2.setTitaUserProjects(set_tup2);

        Set<TiTAUserProject> set_tup3 = new HashSet<TiTAUserProject>();
        set_tup3.add(titaUserProject13);
        set_tup3.add(titaUserProject14);
        set_tup3.add(titaUserProject15);
        user3.setTitaUserProjects(set_tup3);

        Set<TiTAUserProject> set_tup4 = new HashSet<TiTAUserProject>();
        set_tup4.add(titaUserProject16);
        set_tup4.add(titaUserProject17);
        set_tup4.add(titaUserProject18);
        user4.setTitaUserProjects(set_tup4);

        Set<TiTAUserProject> set_tup5 = new HashSet<TiTAUserProject>();
        set_tup5.add(titaUserProject19);
        set_tup5.add(titaUserProject20);
        set_tup5.add(titaUserProject21);
        user5.setTitaUserProjects(set_tup5);

        Set<TiTAUserProject> set_tup6 = new HashSet<TiTAUserProject>();
        set_tup6.add(titaUserProject22);
        set_tup6.add(titaUserProject23);
        set_tup6.add(titaUserProject24);
        set_tup6.add(titaUserProject25);
        set_tup6.add(titaUserProject26);
        user6.setTitaUserProjects(set_tup6);

        titaUserDao.save(user1);
        titaUserDao.save(user2);
        titaUserDao.save(user3);
        titaUserDao.save(user4);
        titaUserDao.save(user5);
        titaUserDao.save(user6);

    }

    private void createIssueTrackerLogins() {
        issueTrackerLogin1 = new IssueTrackerLogin("login1", "login1", issueTracker, user1);
        issueTrackerLogin2 = new IssueTrackerLogin("login2", "login2", issueTracker, user2);
        issueTrackerLogin3 = new IssueTrackerLogin("login3", "login3", issueTracker, user3);
        issueTrackerLogin4 = new IssueTrackerLogin("login4", "login4", issueTracker, user4);
        issueTrackerLogin5 = new IssueTrackerLogin("login5", "login5", issueTracker, user5);
        issueTrackerLogin6 = new IssueTrackerLogin("login6", "login6", issueTracker, user6);

        issueTrackerLoginDao.save(issueTrackerLogin1);
        issueTrackerLoginDao.save(issueTrackerLogin2);
        issueTrackerLoginDao.save(issueTrackerLogin3);
        issueTrackerLoginDao.save(issueTrackerLogin4);
        issueTrackerLoginDao.save(issueTrackerLogin5);
        issueTrackerLoginDao.save(issueTrackerLogin6);
    }

    private void addIssueTrackerLoginsToUser() {
        Set<IssueTrackerLogin> s1 = new HashSet<IssueTrackerLogin>();
        s1.add(issueTrackerLogin1);
        user1.setIssueTrackerLogins(s1);

        Set<IssueTrackerLogin> s2 = new HashSet<IssueTrackerLogin>();
        s2.add(issueTrackerLogin2);
        user2.setIssueTrackerLogins(s2);

        Set<IssueTrackerLogin> s3 = new HashSet<IssueTrackerLogin>();
        s3.add(issueTrackerLogin3);
        user3.setIssueTrackerLogins(s3);

        Set<IssueTrackerLogin> s4 = new HashSet<IssueTrackerLogin>();
        s4.add(issueTrackerLogin4);
        user4.setIssueTrackerLogins(s4);

        Set<IssueTrackerLogin> s5 = new HashSet<IssueTrackerLogin>();
        s5.add(issueTrackerLogin5);
        user5.setIssueTrackerLogins(s5);

        Set<IssueTrackerLogin> s6 = new HashSet<IssueTrackerLogin>();
        s6.add(issueTrackerLogin6);
        user6.setIssueTrackerLogins(s6);

        titaUserDao.save(user1);
        titaUserDao.save(user2);
        titaUserDao.save(user3);
        titaUserDao.save(user4);
        titaUserDao.save(user5);
        titaUserDao.save(user6);
    }

    private void createIssueTrackerProjects() {
        issueTrackerProject1 = new IssueTrackerProject(issueTracker, C_ONE, "OpenEngsbCore", null);
        issueTrackerProject2 = new IssueTrackerProject(issueTracker, C_TWO, "OpenEngsbUI", null);
        issueTrackerProject3 = new IssueTrackerProject(issueTracker, C_THREE,
                "OpenEngsbAlternative", null);
        issueTrackerProject4 = new IssueTrackerProject(issueTracker, C_FOUR,
                "OpenEngsbExperiments", null);

        // Bug: Es sollte ein issuetracker projekt zu mehereren titaprojekte
        // zugeordnet werden können.
        // Auch wenn es kaum auftritt, es erhöht die Flexibilität
        // Fehlerhaftes anlegen kann somit leicht umgangen werden.
        issueTrackerProject1.setTitaProject(titaProject1);
        issueTrackerProject3.setTitaProject(titaProject3);
        issueTrackerProject4.setTitaProject(titaProject4);

        issueTrackerProjectDao.save(issueTrackerProject1);
        issueTrackerProjectDao.save(issueTrackerProject2);
        issueTrackerProjectDao.save(issueTrackerProject3);
        issueTrackerProjectDao.save(issueTrackerProject4);
    }

    private void addIssueTrackerProjectsToTiTAProject() {
        Set<IssueTrackerProject> issueTrackerProjects1 = new HashSet<IssueTrackerProject>();
        issueTrackerProjects1.add(issueTrackerProject1);
        issueTrackerProjects1.add(issueTrackerProject2);
        issueTrackerProjects1.add(issueTrackerProject3);
        issueTrackerProjects1.add(issueTrackerProject4);
        titaProject1.setIssueTrackerProjects(issueTrackerProjects1);

        Set<IssueTrackerProject> issueTrackerProjects2 = new HashSet<IssueTrackerProject>();
        issueTrackerProjects2.add(issueTrackerProject4);
        titaProject2.setIssueTrackerProjects(issueTrackerProjects2);

        Set<IssueTrackerProject> issueTrackerProjects3 = new HashSet<IssueTrackerProject>();
        issueTrackerProjects3.add(issueTrackerProject1);
        issueTrackerProjects3.add(issueTrackerProject2);
        titaProject3.setIssueTrackerProjects(issueTrackerProjects3);

        Set<IssueTrackerProject> issueTrackerProjects4 = new HashSet<IssueTrackerProject>();
        issueTrackerProjects4.add(issueTrackerProject2);
        issueTrackerProjects4.add(issueTrackerProject3);
        issueTrackerProjects4.add(issueTrackerProject4);
        titaProject4.setIssueTrackerProjects(issueTrackerProjects4);

        Set<IssueTrackerProject> issueTrackerProjects5 = new HashSet<IssueTrackerProject>();
        issueTrackerProjects5.add(issueTrackerProject2);
        titaProject5.setIssueTrackerProjects(issueTrackerProjects5);

        Set<IssueTrackerProject> issueTrackerProjects6 = new HashSet<IssueTrackerProject>();
        issueTrackerProjects6.add(issueTrackerProject2);
        issueTrackerProjects6.add(issueTrackerProject3);
        titaProject6.setIssueTrackerProjects(issueTrackerProjects6);

        titaProjectDao.save(titaProject1);
        titaProjectDao.save(titaProject2);
        titaProjectDao.save(titaProject3);
        titaProjectDao.save(titaProject4);
        titaProjectDao.save(titaProject5);
        titaProjectDao.save(titaProject6);
    }

    private void createTasks() {
        task1 = new IssueTrackerTask(issueTrackerProject1, C_ONE, "test description1", null);
        task2 = new IssueTrackerTask(issueTrackerProject1, C_TWO, "test description2", null);
        task3 = new IssueTrackerTask(issueTrackerProject1, C_THREE, "test description3", null);
        task4 = new IssueTrackerTask(issueTrackerProject1, C_FOUR, "test description4", null);
        task5 = new IssueTrackerTask(issueTrackerProject2, C_ONE + C_FOUR, "test description5",
                null);
        task6 = new IssueTrackerTask(issueTrackerProject2, C_TWO + C_FOUR, "test description6",
                null);
        task7 = new IssueTrackerTask(issueTrackerProject2, C_THREE + C_FOUR, "test description7",
                null);
        task8 = new IssueTrackerTask(issueTrackerProject3, C_FOUR + C_FOUR, "test description8",
                null);

        titaTask1 = new TiTATask("tita task 1", user3, titaProject1, null);
        titaTask2 = new TiTATask("tita task 2", user3, titaProject2, null);
        titaTask3 = new TiTATask("tita task 3", user3, titaProject3, null);
        titaTask4 = new TiTATask("tita task 4", user4, titaProject1, null);
        titaTask5 = new TiTATask("tita task 5", user5, titaProject3, null);
        titaTask6 = new TiTATask("tita task 6", user5, titaProject4, null);
        titaTask7 = new TiTATask("tita task 7", user6, titaProject5, null);
        titaTask8 = new TiTATask("tita task 8", user6, titaProject6, null);

        issueTrackerTaskDao.save(task1);
        issueTrackerTaskDao.save(task2);
        issueTrackerTaskDao.save(task3);
        issueTrackerTaskDao.save(task4);
        issueTrackerTaskDao.save(task5);
        issueTrackerTaskDao.save(task6);
        issueTrackerTaskDao.save(task7);
        issueTrackerTaskDao.save(task8);

        titaTaskDao.save(titaTask1);
        titaTaskDao.save(titaTask2);
        titaTaskDao.save(titaTask3);
        titaTaskDao.save(titaTask4);
        titaTaskDao.save(titaTask5);
        titaTaskDao.save(titaTask6);
        titaTaskDao.save(titaTask7);
        titaTaskDao.save(titaTask8);
    }

    private void addIssueTrackerTasksToIssueTrackerProjects() {
        Set<IssueTrackerTask> s1 = new HashSet<IssueTrackerTask>();
        s1.add(task1);
        s1.add(task2);
        s1.add(task3);
        s1.add(task4);
        issueTrackerProject1.setIssueTrackerTasks(s1);

        Set<IssueTrackerTask> s2 = new HashSet<IssueTrackerTask>();
        s2.add(task5);
        s2.add(task6);
        s2.add(task7);
        issueTrackerProject2.setIssueTrackerTasks(s2);

        Set<IssueTrackerTask> s3 = new HashSet<IssueTrackerTask>();
        s3.add(task8);
        issueTrackerProject3.setIssueTrackerTasks(s3);

        Set<IssueTrackerTask> s4 = new HashSet<IssueTrackerTask>();
        issueTrackerProject4.setIssueTrackerTasks(s4);

        issueTrackerProjectDao.save(issueTrackerProject1);
        issueTrackerProjectDao.save(issueTrackerProject2);
        issueTrackerProjectDao.save(issueTrackerProject3);
        issueTrackerProjectDao.save(issueTrackerProject4);
    }

    private void addTitaTasksToTitaProject() {
        Set<TiTATask> s1 = new HashSet<TiTATask>();
        s1.add(titaTask1);
        s1.add(titaTask4);
        titaProject1.setTitaTasks(s1);

        Set<TiTATask> s2 = new HashSet<TiTATask>();
        s2.add(titaTask2);
        titaProject2.setTitaTasks(s2);

        Set<TiTATask> s3 = new HashSet<TiTATask>();
        s3.add(titaTask3);
        s3.add(titaTask5);
        titaProject3.setTitaTasks(s3);

        Set<TiTATask> s4 = new HashSet<TiTATask>();
        s4.add(titaTask6);
        titaProject4.setTitaTasks(s4);

        Set<TiTATask> s5 = new HashSet<TiTATask>();
        s5.add(titaTask7);
        titaProject5.setTitaTasks(s5);

        Set<TiTATask> s6 = new HashSet<TiTATask>();
        s6.add(titaTask8);
        titaProject6.setTitaTasks(s6);

        titaProjectDao.save(titaProject1);
        titaProjectDao.save(titaProject2);
        titaProjectDao.save(titaProject3);
        titaProjectDao.save(titaProject4);
        titaProjectDao.save(titaProject5);
        titaProjectDao.save(titaProject6);

    }

    private void createEfforts() {

        // user3 - issuetrackertask 1, 2, 3 titatask 1, 2, 3
        effort1 = new Effort(null, task1,
                new GregorianCalendar(2010, Calendar.APRIL, 17).getTime(), 100000L, 200000L,
                100000L, "effort 1", false, "540", user3);
        effort2 = new Effort(null, task2,
                new GregorianCalendar(2010, Calendar.APRIL, 17).getTime(), 100000L, 200000L,
                100000L, "effort 1", false, "540", user3);
        effort3 = new Effort(null, task3,
                new GregorianCalendar(2010, Calendar.APRIL, 17).getTime(), 100000L, 200000L,
                100000L, "effort 1", false, "540", user3);
        effort4 = new Effort(titaTask1, null, new GregorianCalendar(2010, Calendar.APRIL, 17)
                .getTime(), 100000L, 200000L, 100000L, "effort 1", false, "540", user3);
        effort5 = new Effort(titaTask2, null, new GregorianCalendar(2010, Calendar.APRIL, 17)
                .getTime(), 100000L, 200000L, 100000L, "effort 1", false, "540", user3);
        effort6 = new Effort(titaTask3, null, new GregorianCalendar(2010, Calendar.APRIL, 17)
                .getTime(), 100000L, 200000L, 100000L, "effort 1", false, "540", user3);

        // user4 - issuetrackertask 4, 5, 6, 7, 8 titatask 1, 2, 3
        effort7 = new Effort(null, task4,
                new GregorianCalendar(2010, Calendar.APRIL, 17).getTime(), 100000L, 200000L,
                100000L, "effort 1", false, "540", user4);
        effort8 = new Effort(null, task5,
                new GregorianCalendar(2010, Calendar.APRIL, 17).getTime(), 100000L, 200000L,
                100000L, "effort 1", false, "540", user4);
        effort9 = new Effort(null, task6,
                new GregorianCalendar(2010, Calendar.APRIL, 17).getTime(), 100000L, 200000L,
                100000L, "effort 1", false, "540", user4);
        effort10 = new Effort(null, task7, new GregorianCalendar(2010, Calendar.APRIL, 17)
                .getTime(), 100000L, 200000L, 100000L, "effort 1", false, "540", user4);
        effort11 = new Effort(null, task8, new GregorianCalendar(2010, Calendar.APRIL, 17)
                .getTime(), 100000L, 200000L, 100000L, "effort 1", false, "540", user4);
        effort12 = new Effort(titaTask1, null, new GregorianCalendar(2010, Calendar.APRIL, 17)
                .getTime(), 100000L, 200000L, 100000L, "effort 1", false, "540", user4);
        effort13 = new Effort(titaTask2, null, new GregorianCalendar(2010, Calendar.APRIL, 17)
                .getTime(), 100000L, 200000L, 100000L, "effort 1", false, "540", user4);
        effort14 = new Effort(titaTask4, null, new GregorianCalendar(2010, Calendar.APRIL, 17)
                .getTime(), 100000L, 200000L, 100000L, "effort 1", false, "540", user4);

        // user5 - issuetrackertask 1, 2, 6, 7, 8 titatask 4, 5, 6
        effort15 = new Effort(null, task1, new GregorianCalendar(2010, Calendar.APRIL, 17)
                .getTime(), 100000L, 200000L, 100000L, "effort 1", false, "540", user5);
        effort16 = new Effort(null, task2, new GregorianCalendar(2010, Calendar.APRIL, 17)
                .getTime(), 100000L, 200000L, 100000L, "effort 1", false, "540", user5);
        effort17 = new Effort(null, task6, new GregorianCalendar(2010, Calendar.APRIL, 17)
                .getTime(), 100000L, 200000L, 100000L, "effort 1", false, "540", user5);
        effort18 = new Effort(null, task7, new GregorianCalendar(2010, Calendar.APRIL, 17)
                .getTime(), 100000L, 200000L, 100000L, "effort 1", false, "540", user5);
        effort19 = new Effort(null, task8, new GregorianCalendar(2010, Calendar.APRIL, 17)
                .getTime(), 100000L, 200000L, 100000L, "effort 1", false, "540", user5);
        effort20 = new Effort(titaTask4, null, new GregorianCalendar(2010, Calendar.APRIL, 17)
                .getTime(), 100000L, 200000L, 100000L, "effort 1", false, "540", user5);
        effort21 = new Effort(titaTask5, null, new GregorianCalendar(2010, Calendar.APRIL, 17)
                .getTime(), 100000L, 200000L, 100000L, "effort 1", false, "540", user5);
        effort22 = new Effort(titaTask6, null, new GregorianCalendar(2010, Calendar.APRIL, 17)
                .getTime(), 100000L, 200000L, 100000L, "effort 1", false, "540", user5);

        // user5 - issuetrackertask 1 titatask 7, 8
        effort23 = new Effort(null, task1, new GregorianCalendar(2010, Calendar.APRIL, 17)
                .getTime(), 100000L, 200000L, 100000L, "effort 1", false, "540", user6);
        effort24 = new Effort(titaTask7, null, new GregorianCalendar(2010, Calendar.APRIL, 17)
                .getTime(), 100000L, 200000L, 100000L, "effort 1", false, "540", user6);
        effort25 = new Effort(titaTask8, null, new GregorianCalendar(2010, Calendar.APRIL, 17)
                .getTime(), 100000L, 200000L, 100000L, "effort 1", false, "540", user6);

        timeEffortDao.save(effort1);
        timeEffortDao.save(effort2);
        timeEffortDao.save(effort3);
        timeEffortDao.save(effort4);
        timeEffortDao.save(effort5);
        timeEffortDao.save(effort6);
        timeEffortDao.save(effort7);
        timeEffortDao.save(effort8);
        timeEffortDao.save(effort9);
        timeEffortDao.save(effort10);
        timeEffortDao.save(effort11);
        timeEffortDao.save(effort12);
        timeEffortDao.save(effort13);
        timeEffortDao.save(effort14);
        timeEffortDao.save(effort15);
        timeEffortDao.save(effort16);
        timeEffortDao.save(effort17);
        timeEffortDao.save(effort18);
        timeEffortDao.save(effort19);
        timeEffortDao.save(effort20);
        timeEffortDao.save(effort21);
        timeEffortDao.save(effort22);
        timeEffortDao.save(effort23);
        timeEffortDao.save(effort24);
        timeEffortDao.save(effort25);
    }

    private void addEffortsToTasks() {

        // issueTracker tasks
        Set<Effort> s1 = new HashSet<Effort>();
        s1.add(effort1);
        s1.add(effort15);
        s1.add(effort23);
        task1.setIssueTEfforts(s1);

        Set<Effort> s2 = new HashSet<Effort>();
        s2.add(effort2);
        s2.add(effort16);
        task2.setIssueTEfforts(s2);

        task3.addEffort(effort3);
        task4.addEffort(effort7);
        task5.addEffort(effort8);

        Set<Effort> s3 = new HashSet<Effort>();
        s3.add(effort9);
        s3.add(effort17);
        task6.setIssueTEfforts(s3);

        Set<Effort> s4 = new HashSet<Effort>();
        s4.add(effort10);
        s4.add(effort18);
        task7.setIssueTEfforts(s4);

        Set<Effort> s5 = new HashSet<Effort>();
        s5.add(effort11);
        s5.add(effort19);
        task8.setIssueTEfforts(s5);

        // tita tasks
        Set<Effort> s6 = new HashSet<Effort>();
        s6.add(effort4);
        s6.add(effort12);
        titaTask1.setTitaEfforts(s6);

        Set<Effort> s7 = new HashSet<Effort>();
        s7.add(effort5);
        s7.add(effort13);
        titaTask2.setTitaEfforts(s7);

        titaTask3.addEffort(effort6);

        Set<Effort> s8 = new HashSet<Effort>();
        s8.add(effort14);
        s8.add(effort20);
        titaTask4.setTitaEfforts(s8);

        titaTask5.addEffort(effort21);
        titaTask6.addEffort(effort22);
        titaTask7.addEffort(effort24);
        titaTask8.addEffort(effort25);

    }

    private void deleteConfigurationDataSets() {
        titaUserDao.delete(user1);
        titaUserDao.delete(user2);
        titaUserDao.delete(user3);
        titaUserDao.delete(user4);
        titaUserDao.delete(user5);
        titaUserDao.delete(user6);
        projectStatusDao.delete(open);
        projectStatusDao.delete(closed);
        roleDao.delete(admin);
        roleDao.delete(timeconsumer);
        roleDao.delete(timecontroller);
        issueTrackerDao.delete(issueTracker);
    }

    private void deleteTitaProjects() {

        /*
         * due to cascading
         *
         * issueTrackerProjectDao.delete(issueTrackerProject1);
         * issueTrackerProjectDao.delete(issueTrackerProject2);
         * issueTrackerProjectDao.delete(issueTrackerProject3);
         * issueTrackerProjectDao.delete(issueTrackerProject4);
         */

        titaUserProjectDao.delete(titaUserProject1);
        titaUserProjectDao.delete(titaUserProject2);
        titaUserProjectDao.delete(titaUserProject3);
        titaUserProjectDao.delete(titaUserProject4);
        titaUserProjectDao.delete(titaUserProject5);
        titaUserProjectDao.delete(titaUserProject6);
        titaUserProjectDao.delete(titaUserProject7);
        titaUserProjectDao.delete(titaUserProject8);
        titaUserProjectDao.delete(titaUserProject9);
        titaUserProjectDao.delete(titaUserProject10);
        titaUserProjectDao.delete(titaUserProject11);
        titaUserProjectDao.delete(titaUserProject12);
        titaUserProjectDao.delete(titaUserProject13);
        titaUserProjectDao.delete(titaUserProject14);
        titaUserProjectDao.delete(titaUserProject15);
        titaUserProjectDao.delete(titaUserProject16);
        titaUserProjectDao.delete(titaUserProject17);
        titaUserProjectDao.delete(titaUserProject18);
        titaUserProjectDao.delete(titaUserProject19);
        titaUserProjectDao.delete(titaUserProject20);
        titaUserProjectDao.delete(titaUserProject21);
        titaUserProjectDao.delete(titaUserProject22);
        titaUserProjectDao.delete(titaUserProject23);
        titaUserProjectDao.delete(titaUserProject24);
        titaUserProjectDao.delete(titaUserProject25);
        titaUserProjectDao.delete(titaUserProject26);

        titaProjectDao.delete(titaProject1);
        titaProjectDao.delete(titaProject2);
        titaProjectDao.delete(titaProject3);
        titaProjectDao.delete(titaProject4);
        titaProjectDao.delete(titaProject5);
        titaProjectDao.delete(titaProject6);
    }

    private void deleteIssueTrackerLogins() {
        issueTrackerLoginDao.delete(issueTrackerLogin1);
        issueTrackerLoginDao.delete(issueTrackerLogin2);
        issueTrackerLoginDao.delete(issueTrackerLogin3);
        issueTrackerLoginDao.delete(issueTrackerLogin4);
        issueTrackerLoginDao.delete(issueTrackerLogin5);
        issueTrackerLoginDao.delete(issueTrackerLogin6);
    }

    private void deleteEfforts() {
        timeEffortDao.delete(effort1);
        timeEffortDao.delete(effort2);
        timeEffortDao.delete(effort3);
        timeEffortDao.delete(effort4);
        timeEffortDao.delete(effort5);
        timeEffortDao.delete(effort6);
        timeEffortDao.delete(effort7);
        timeEffortDao.delete(effort8);
        timeEffortDao.delete(effort9);
        timeEffortDao.delete(effort10);
        timeEffortDao.delete(effort11);
        timeEffortDao.delete(effort12);
        timeEffortDao.delete(effort13);
        timeEffortDao.delete(effort14);
        timeEffortDao.delete(effort15);
        timeEffortDao.delete(effort16);
        timeEffortDao.delete(effort17);
        timeEffortDao.delete(effort18);
        timeEffortDao.delete(effort19);
        timeEffortDao.delete(effort20);
        timeEffortDao.delete(effort21);
        timeEffortDao.delete(effort22);
        timeEffortDao.delete(effort23);
        timeEffortDao.delete(effort24);
        timeEffortDao.delete(effort25);
    }

    private void deleteTasks() {
        issueTrackerTaskDao.delete(task1);
        issueTrackerTaskDao.delete(task2);
        issueTrackerTaskDao.delete(task3);
        issueTrackerTaskDao.delete(task4);
        issueTrackerTaskDao.delete(task5);
        issueTrackerTaskDao.delete(task6);
        issueTrackerTaskDao.delete(task7);
        issueTrackerTaskDao.delete(task8);

        titaTaskDao.delete(titaTask1);
        titaTaskDao.delete(titaTask2);
        titaTaskDao.delete(titaTask3);
        titaTaskDao.delete(titaTask4);
        titaTaskDao.delete(titaTask5);
        titaTaskDao.delete(titaTask6);
        titaTaskDao.delete(titaTask7);
        titaTaskDao.delete(titaTask8);
    }

    @Test
    public void setupTest() {

        // Test configuration data sets if stored right
        assertNotNull(issueTrackerDao.findById(1L));
        assertNotNull(roleDao.findById(1L));
        assertNotNull(projectStatusDao.findById(1L));
        assertNotNull(titaUserDao.findById(user1.getId()));
        assertEquals(6, titaUserDao.findAll().size());

        // Test project and task data sets, if stored right
        assertNotNull(titaProjectDao.findById(titaProject1.getId()));
        assertEquals(6, titaProjectDao.findAll().size());
        assertNotNull(titaUserProjectDao.findById(titaUserProject1.getId()));
        assertEquals(26, titaUserProjectDao.findAll().size());

        // Check projects for added users
        assertEquals(5, titaProjectDao.findById(titaProject1.getId()).getUsers().size());
        assertEquals(5, titaProjectDao.findById(titaProject2.getId()).getUsers().size());
        assertEquals(5, titaProjectDao.findById(titaProject3.getId()).getUsers().size());
        assertEquals(5, titaProjectDao.findById(titaProject4.getId()).getUsers().size());
        assertEquals(3, titaProjectDao.findById(titaProject5.getId()).getUsers().size());
        assertEquals(3, titaProjectDao.findById(titaProject6.getId()).getUsers().size());

        // Check issuetracker projects from the projects
        assertEquals(4, titaProjectDao.findById(titaProject1.getId()).getIssueTrackerProjects()
                .size());
        assertEquals(1, titaProjectDao.findById(titaProject2.getId()).getIssueTrackerProjects()
                .size());
        assertEquals(2, titaProjectDao.findById(titaProject3.getId()).getIssueTrackerProjects()
                .size());
        assertEquals(3, titaProjectDao.findById(titaProject4.getId()).getIssueTrackerProjects()
                .size());

        assertEquals(2, titaProjectDao.findById(titaProject1.getId()).getTitaTasks().size());
        assertEquals(1, titaProjectDao.findById(titaProject4.getId()).getTitaTasks().size());

        // Check users for added projects
        assertEquals(6, titaUserDao.findById(user1.getId()).getTitaUserProjects().size());
        assertEquals(6, titaUserDao.findById(user2.getId()).getTitaUserProjects().size());
        assertEquals(3, titaUserDao.findById(user3.getId()).getTitaUserProjects().size());
        assertEquals(3, titaUserDao.findById(user4.getId()).getTitaUserProjects().size());
        assertEquals(3, titaUserDao.findById(user5.getId()).getTitaUserProjects().size());
        assertEquals(5, titaUserDao.findById(user6.getId()).getTitaUserProjects().size());

        // Check users for issuetracker logins
        assertEquals(1, titaUserDao.findById(user1.getId()).getIssueTrackerLogins().size());
        assertEquals(1, titaUserDao.findById(user2.getId()).getIssueTrackerLogins().size());
        assertEquals(1, titaUserDao.findById(user3.getId()).getIssueTrackerLogins().size());
        assertEquals(1, titaUserDao.findById(user4.getId()).getIssueTrackerLogins().size());
        assertEquals(1, titaUserDao.findById(user5.getId()).getIssueTrackerLogins().size());
        assertEquals(1, titaUserDao.findById(user6.getId()).getIssueTrackerLogins().size());

        // Check issuetracker projects and tasks
        assertEquals(4, issueTrackerProjectDao.findById(issueTrackerProject1.getId())
                .getIssueTrackerTasks().size());
        assertEquals(0, issueTrackerProjectDao.findById(issueTrackerProject4.getId())
                .getIssueTrackerTasks().size());

        // Check tita tasks
        assertEquals(user3.getUserName(), titaTaskDao.findById(titaTask1.getId()).getUser()
                .getUserName());

        // Check efforts
        assertEquals(25, timeEffortDao.findAll().size());
        assertEquals(3, issueTrackerTaskDao.findById(task1.getId()).getIssueTEfforts().size());

        // Delete Configuration data sets
        deleteEfforts();
        deleteTasks();
        deleteIssueTrackerLogins();
        deleteTitaProjects();
        deleteConfigurationDataSets();

        // Test configuration data sets if deleted correctly
        assertNull(issueTrackerDao.findById(1L));
        assertNull(roleDao.findById(1L));
        assertNull(projectStatusDao.findById(1L));
        assertEquals(0, titaUserDao.findAll().size());

        // Test project data sets, if deleted correctly
        assertNull(titaProjectDao.findById(titaProject1.getId()));
        assertEquals(0, titaProjectDao.findAll().size());
        assertNull(titaUserProjectDao.findById(titaUserProject1.getId()));
        assertEquals(0, titaUserProjectDao.findAll().size());
        assertNull(issueTrackerDao.findById(issueTrackerProject1.getId()));

        // Test user data sets, if deleted correctly
        assertNull(issueTrackerLoginDao.findById(issueTrackerLogin1.getId()));
        assertNull(titaUserProjectDao.findById(titaUserProject1.getId()));

    }
}
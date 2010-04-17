package at.ac.tuwien.ifs.tita.dao.test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

import at.ac.tuwien.ifs.tita.dao.interfaces.IGenericHibernateDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.ITiTAProjectDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.IUserDAO;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
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

    @Qualifier("issueTrackerDAO")
    @Autowired
    private IGenericHibernateDao<IssueTracker, Long> issueTrackerDao;

    @Autowired
    private IUserDAO titaUserDao;

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
    private ITiTAProjectDao titaProjectDao;

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
            issueTrackerProject4, issueTrackerProject5, issueTrackerProject6, issueTrackerProject7,
            issueTrackerProject8, issueTrackerProject9, issueTrackerProject10;

    @Before
    public void setUp() {
        // Setup Configuration data sets
        setConfigurationDataSets();

        // Setup Project data sets
        setupTitaUserProjects();

        // Add the projects to the user
        addProjectsToUser();
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
        user3 = new TiTAUser("timeconsumer", "timeconsumer", "Christoph", "Zehetner",
                "christoph.zehetner@gmx.at", false, timeconsumer, null, null);
        user4 = new TiTAUser("timeconsumer", "timeconsumer", "Alexander", "Leutgoeb",
                "alexander.leutgoeb@gmx.at", false, timeconsumer, null, null);
        user5 = new TiTAUser("timeconsumer", "timeconsumer", "Markus", "Siedler",
                "markus.siedler@gmx.at", false, timeconsumer, null, null);
        user6 = new TiTAUser("timeconsumer", "timeconsumer", "Johannes", "Ferner",
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

    private void addIssueTrackerProjects() {

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

        // Check users for added projects
        assertEquals(6, titaUserDao.findById(user1.getId()).getTitaUserProjects().size());
        assertEquals(6, titaUserDao.findById(user2.getId()).getTitaUserProjects().size());
        assertEquals(3, titaUserDao.findById(user3.getId()).getTitaUserProjects().size());
        assertEquals(3, titaUserDao.findById(user4.getId()).getTitaUserProjects().size());
        assertEquals(3, titaUserDao.findById(user5.getId()).getTitaUserProjects().size());
        assertEquals(5, titaUserDao.findById(user6.getId()).getTitaUserProjects().size());

        // Delete Configuration data sets
        deleteConfigurationDataSets();
        deleteTitaProjects();

        // Test configuration data sets if deleted correctly
        assertNull(issueTrackerDao.findById(1L));
        assertNull(roleDao.findById(1L));
        assertNull(projectStatusDao.findById(1L));
        assertEquals(0, titaUserDao.findAll().size());

        // Test project and task data sets, if stored right
        assertNull(titaProjectDao.findById(titaProject1.getId()));
        assertEquals(0, titaProjectDao.findAll().size());
        assertNull(titaUserProjectDao.findById(titaUserProject1.getId()));
        assertEquals(0, titaUserProjectDao.findAll().size());
    }

}

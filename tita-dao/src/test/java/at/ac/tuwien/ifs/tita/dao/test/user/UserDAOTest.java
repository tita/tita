package at.ac.tuwien.ifs.tita.dao.test.user;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

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
import at.ac.tuwien.ifs.tita.dao.user.UserDAO;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.TiTAUserProject;

/**
 * Test for UserDao.
 *
 * @author Christoph
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:datasourceContext-test.xml" })
@TransactionConfiguration
@Transactional
public class UserDAOTest {

    private static final Long C_150 = 150L;

    private TiTAUser titaUser;
    private TiTAProject titaProject;
    private TiTAUserProject tup;

    @Qualifier("titaProjectDAO")
    @Autowired
    private IGenericHibernateDao<TiTAProject, Long> titaProjectDAO;

    @Autowired
    private UserDAO titaUserDao;

    @Qualifier("userTitaDAO")
    @Autowired
    private IGenericHibernateDao<TiTAUserProject, Long> utpDao;

    /**
     * Prepare a tita user and tita project with titaUserProject entity for
     * testing fetching the target hours.
     */
    @Before
    public void setUp() {

        titaUser = new TiTAUser("test-user", "test-password", "Christoph", "Zehetner",
                "test@example.com", false, null, null, null);
        titaProject = new TiTAProject("test-description", "test-project", false, null, null, null);
        tup = new TiTAUserProject(titaUser, titaProject, C_150);

        Set<TiTAUserProject> stup = new HashSet<TiTAUserProject>();
        stup.add(tup);
        titaUser.setTitaUserProjects(stup);

        titaProjectDAO.save(titaProject);
        titaProjectDAO.flushnClear();
        titaUserDao.save(titaUser);
        titaUserDao.flushnClear();
    }

    /**
     * Undo of the setup.
     */
    @After
    public void tearDown() {
        titaProjectDAO.delete(titaProject.getId());
        // titaProjectDAO.flush();
        titaUserDao.delete(titaUser.getId());
    }

    /**
     * The test case should return the target hours for a tita user and tita
     * project.
     */
    @Test
    public void findTargetHoursForTiTAProjectAndTiTAUser() {
        Long targetHours = titaUserDao.findTargetHoursForTiTAProjectAndTiTAUser(titaUser.getId(),
                titaProject.getId());

        assertEquals(C_150, targetHours, 0.0);
    }

    /**
     * The test case should return null, because there is no entry for a tita
     * user and tita project.
     */
    @Test
    public void findTargetHoursForTiTAProjectAndTiTAUserShouldReturnNull() {
        utpDao.delete(utpDao.findById(tup.getId()));

        Long targetHours = titaUserDao.findTargetHoursForTiTAProjectAndTiTAUser(titaUser.getId(),
                titaProject.getId());

        Assert.assertNull(targetHours);
    }
}

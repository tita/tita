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
package at.ac.tuwien.ifs.tita.dao.test.user;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
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
import at.ac.tuwien.ifs.tita.entity.conv.Role;

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
    private Role role;

    @Qualifier("titaProjectDAO")
    @Autowired
    private IGenericHibernateDao<TiTAProject, Long> titaProjectDAO;

    @Autowired
    private UserDAO titaUserDao;

    @Qualifier("userTitaDAO")
    @Autowired
    private IGenericHibernateDao<TiTAUserProject, Long> utpDao;

    @Qualifier("roleDAO")
    @Autowired
    private IGenericHibernateDao<Role, Long> roleDao;

    /**
     * Prepare a tita user and tita project with titaUserProject entity for
     * testing fetching the target hours.
     */
    @Before
    public void setUp() {

        role = new Role(1L, "Administrator");
        titaUser = new TiTAUser("test-user", "test-password", "Christoph", "Zehetner",
                "test@example.com", false, role, null, null);
        titaProject = new TiTAProject("test-description", "test-project", false, null, null, null);
        tup = new TiTAUserProject(titaUser, titaProject, C_150);

        Set<TiTAUserProject> stup = new HashSet<TiTAUserProject>();
        stup.add(tup);
        titaUser.setTitaUserProjects(stup);

        roleDao.save(role);
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

    /**
     * The test case should return null, because there is no entry for a tita
     * user and tita project.
     */
    @Test
    public void findUsersForTiTAProject() {
        List<TiTAUser> users = titaUserDao.findUsersForTiTAProject(titaProject);
        Assert.assertEquals("Administrator", titaUserDao
                .findById(users.get(0).getId(), false)
                .getRole()
                .getDescription());
        Assert.assertNotNull(users);
        Assert.assertEquals(1, users.size());
    }
}

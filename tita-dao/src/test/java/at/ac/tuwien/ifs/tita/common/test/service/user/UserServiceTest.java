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

package at.ac.tuwien.ifs.tita.common.test.service.user;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.conv.Role;

/**
 * Test.
 * 
 * @author herbert
 * @author rene
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:datasourceContext-test.xml" })
@TransactionConfiguration
public class UserServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private IUserService service;

    /**
     * Prepare database for test -> insert 3 roles.
     * 
     * @return List of roles
     */
    private List<Role> prepareRoles() {
        List<Role> rolesList = new ArrayList<Role>();

        // CHECKSTYLE:OFF
        rolesList.add(new Role(1L, "Administrator"));
        rolesList.add(new Role(2L, "Time consumer"));
        rolesList.add(new Role(3L, "Time controller"));
        // CHECKSTYLE:ON

        try {
            for (Role r : rolesList) {
                service.saveRole(r);
            }
        } catch (PersistenceException e) {
            fail();
        }
        return rolesList;
    }

    /**
     * Prepare database for test -> insert 3 users.
     * 
     * @return List of users
     */
    private List<TiTAUser> prepareTiTAUsers() {
        List<TiTAUser> userList = new ArrayList<TiTAUser>();

        TiTAUser user1 = new TiTAUser();
        TiTAUser user2 = new TiTAUser();
        TiTAUser user3 = new TiTAUser();

        user1.setDeleted(false);
        user2.setDeleted(false);
        user3.setDeleted(false);

        user1.setUserName("user1");
        user2.setUserName("user2");
        user3.setUserName("user3");

        user1.setPassword("pwd1");
        user2.setPassword("pwd2");
        user3.setPassword("pwd3");

        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        try {
            for (TiTAUser u : userList) {
                service.saveUser(u);
            }
        } catch (PersistenceException e) {
            fail();
        }
        return userList;
    }

    /**
     * Delete all inserted roles.
     * 
     * @param roles List
     * @throws TitaDAOException e
     */
    private void deleteRoles(List<Role> roles) throws TitaDAOException {
        for (Role r : roles) {
            service.deleteRole(r);
        }
    }

    /**
     * Delete all inserted users.
     * 
     * @param users List
     * @throws TitaDAOException e
     */
    private void deleteTiTAUsers(List<TiTAUser> users) throws TitaDAOException {
        for (TiTAUser u : users) {
            service.deleteUser(u);
        }
    }

    /**
     * Test.
     */
    @Test
    public void testSaveTiTAUser() {
        TiTAUser u = new TiTAUser();
        u.setDeleted(false);
        u.setUserName("user1");
        u.setPassword("pwd1");
        u.setFirstName("John");
        u.setLastName("Doe");
        u.setEmail("john.doe@usa.com");

        Role r = new Role(1L, "Administrator");

        try {
            Role newRole = service.saveRole(r);
            Assert.assertNotNull(newRole.getId());
            u.setRole(newRole);
            service.saveUser(u);
            Assert.assertNotNull(u.getId());
            Assert.assertNotNull(u.getRole());
        } catch (PersistenceException e) {
            fail();
        } finally {
            service.deleteUser(u);
            service.deleteRole(r);
        }
    }

    /**
     * Test.
     */
    @Test
    public void testDeleteTiTAUser() {
        TiTAUser u = new TiTAUser();
        u.setDeleted(false);
        u.setUserName("user1");
        u.setPassword("pwd1");
        u.setFirstName("John");
        u.setLastName("Doe");
        u.setEmail("john.doe@usa.com");

        Role r = new Role(1L, "Administrator");

        try {
            Role newRole = service.saveRole(r);
            Assert.assertNotNull(newRole.getId());
            u.setRole(newRole);
            service.saveUser(u);
            Assert.assertNotNull(u.getId());
            Assert.assertNotNull(u.getRole());

            service.deleteUser(u);
            Assert.assertNull(service.getUserById(u.getId()));
        } catch (PersistenceException e) {
            fail();
        }
    }

    /**
     * Test: Get TiTAUser by username.
     */
    @Test
    public void testGetTiTAUserByTiTAUsername() {
        List<TiTAUser> users = prepareTiTAUsers();
        List<Role> roles = prepareRoles();

        // CHECKSTYLE:OFF
        for (int i = 0; i < 3; i++) {
            users.get(i).setRole(roles.get(i));
        }
        // CHECKSTYLE:ON

        TiTAUser u = null;

        try {
            for (TiTAUser user : users) {
                service.saveUser(user);
            }
            u = service.getUserByUsername("user1");
            Assert.assertNotNull(u);
            Assert.assertEquals("user1", u.getUserName());
            Assert.assertEquals("pwd1", u.getPassword());
            Assert.assertNotNull(u.getRole());
            Assert.assertEquals("Administrator", u.getRole().getDescription());
        } catch (TitaDAOException e) {
            fail();
        } finally {
            try {
                deleteTiTAUsers(users);
                deleteRoles(roles);
            } catch (TitaDAOException e) {
                fail();
            }
        }
    }

    /**
     * Test: Get all users.
     */
    @Test
    public void testGetTiTAUsers() {
        List<TiTAUser> users = prepareTiTAUsers();
        List<TiTAUser> ulist = new ArrayList<TiTAUser>();

        try {
            ulist = service.getUndeletedUsers();
            Assert.assertNotNull(ulist);
            // CHECKSTYLE:OFF
            Assert.assertEquals(3, ulist.size());
            // CHECKSTYLE:ON
        } catch (TitaDAOException e) {
            fail();
        } finally {
            try {
                deleteTiTAUsers(users);
            } catch (TitaDAOException e) {
                fail();
            }
        }
    }

    /**
     * Test: Get all roles.
     */
    @Test
    public void testGetRoles() {
        List<Role> roles = prepareRoles();
        List<Role> rlist = new ArrayList<Role>();

        try {
            rlist = service.getRoles();
            Assert.assertNotNull(rlist);
            // CHECKSTYLE:OFF
            Assert.assertEquals(3, rlist.size());
            // CHECKSTYLE:ON
        } catch (TitaDAOException e) {
            fail();
        } finally {
            try {
                deleteRoles(roles);
            } catch (TitaDAOException e) {
                fail();
            }
        }
    }
}

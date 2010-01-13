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

package at.ac.tuwien.ifs.tita.test.service.user;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
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

    private List<Role> roleList;
    private List<TiTAUser> userList;

    /**
     * Prepare database for test -> insert 3 roles and users.
     *
     */
    @Before
    public void setUp() {
        // insert Roles
        roleList = new ArrayList<Role>();

        // CHECKSTYLE:OFF
        roleList.add(new Role(997L, "role1"));
        roleList.add(new Role(998L, "role1"));
        roleList.add(new Role(999L, "role1"));
        // CHECKSTYLE:ON

        try {
            for (Role r : roleList) {
                service.saveRole(r);
            }
        } catch (PersistenceException e) {
            fail();
        }

        // Insert Users
        userList = new ArrayList<TiTAUser>();

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

        user1.setRole(roleList.get(0));
        user1.setRole(roleList.get(1));
        user1.setRole(roleList.get(2));

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
    }

    /**
     * Test.
     */
    @Test
    public void testGetRoleById() {
        try {
            // CHECKSTYLE:OFF
            Role r = service.getRoleById(997L);
            // CHECKSTYLE:ON
            Assert.assertNotNull(r);
            Assert.assertEquals("role1", r.getDescription());
        } catch (PersistenceException e) {
            fail();
        }
    }

    /**
     * Test.
     */
    @Test
    public void testGetRoles() {
        List<Role> rolesList = null;
        try {
            rolesList = service.getRoles();

            Assert.assertNotNull(rolesList);
            Assert.assertFalse(rolesList.isEmpty());
        } catch (PersistenceException e) {
            fail();
        }
    }

    /**
     * Test.
     */
    @Test
    public void testSaveRole() {
        try {
            // CHECKSTYLE:OFF
            Role r = new Role(1000L, "role1000");
            service.saveRole(r);
            Assert.assertNotNull(service.getRoleById(1000L));
            Assert.assertEquals("role1000", service.getRoleById(1000L).getDescription());
            service.deleteRole(r);
            // CHECKSTYLE:ON
        } catch (PersistenceException e) {
            fail();
        }
    }

    /**
     * Test.
     */
    @Test
    public void testDeleteRole() {
        try {
            // CHECKSTYLE:OFF
            Role r = new Role(1000L, "role1000");
            service.saveRole(r);
            service.deleteRole(r);
            Assert.assertNull(service.getRoleById(1000L));
            // CHECKSTYLE:ON
        } catch (PersistenceException e) {
            fail();
        }
    }

    /**
     * Test.
     */
    @Test
    public void testSaveTiTAUser() {
        TiTAUser u = new TiTAUser();
        u.setDeleted(false);
        u.setUserName("newuser");
        u.setPassword("pwd1");
        u.setFirstName("John");
        u.setLastName("Doe");
        u.setEmail("john.doe@usa.com");

        try {
            // CHECKSTYLE:OFF
            Role role = service.getRoleById(999L);
            // CHECKSTYLE:ON
            u.setRole(role);
            service.saveUser(u);
            Assert.assertNotNull(u.getId());
            Assert.assertNotNull(u.getRole());
        } catch (PersistenceException e) {
            fail();
        } finally {
            service.deleteUser(u);
        }
    }

    /**
     * Test.
     */
    @Test
    public void testDeleteTiTAUser() {
        TiTAUser u = new TiTAUser();
        u.setDeleted(false);
        u.setUserName("newuser2");
        u.setPassword("pwd1");
        u.setFirstName("John");
        u.setLastName("Doe");
        u.setEmail("john.doe@usa.com");

        try {
            // CHECKSTYLE:OFF
            Role role = service.getRoleById(999L);
            // CHECKSTYLE:ON
            u.setRole(role);
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
        try {
            TiTAUser u = service.getUserByUsername("user1");
            Assert.assertNotNull(u);
            Assert.assertEquals("user1", u.getUserName());
            Assert.assertEquals("pwd1", u.getPassword());
            Assert.assertNotNull(u.getRole());
            Assert.assertEquals("role1", u.getRole().getDescription());
        } catch (PersistenceException e) {
            fail();
        }
    }

    /**
     * Test: Get all users.
     */
    @Test
    public void testGetTiTAUsers() {
        try {
            List<TiTAUser> ulist = service.getUndeletedUsers();
            Assert.assertNotNull(ulist);
            // CHECKSTYLE:OFF
            Assert.assertFalse(ulist.isEmpty());
            // CHECKSTYLE:ON
        } catch (PersistenceException e) {
            fail();
        }
    }

    /**
     * After Delete Roles and Users.
     */
    @After
    public void tearDown() {
        for (TiTAUser u : userList) {
            service.deleteUser(u);
        }

        for (Role r : roleList) {
            service.deleteRole(r);
        }
    }
}

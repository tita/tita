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

import javax.persistence.PersistenceException;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.entity.User;

/**
 * Tests the UserService with various CRUD-Operations.
 * 
 * @author ASE Group 10
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:datasourceContext-test.xml" })
@TransactionConfiguration
@Transactional
public class UserServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    private final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    private IUserService service;

    // global user object for tests
    private User user;

    /**
     * test saving a User.
     */
    @Test
    public void testSaveUser() {
        log.debug("##### Save User #####");
        user = new User();
        user.setDeleted(false);
        user.setEmail("test@person.at");
        user.setFirstName("test");
        user.setLastName("person");
        user.setPassword("testpassword");
        user.setUserName("testusername");

        try {
            user = service.saveUser(user);
            Assert.assertNotNull(user.getId());
            log.debug("##### Save User -> Success!! #####");
        } catch (PersistenceException e) {
            log.error("##### Save User -> Error!#####");
            Assert.fail();
        }
    }

    /**
     * test updating a User.
     */
    @Test
    public void testUpdateUser() {
        log.debug("##### Update User #####");
        user = new User();
        user.setDeleted(false);
        user.setEmail("test@person.at");
        user.setFirstName("test");
        user.setLastName("person");
        user.setPassword("testpassword");
        user.setUserName("testusername");

        try {
            User temp = service.saveUser(user);

            temp.setDeleted(true);
            temp.setEmail("other@person.at");
            temp.setFirstName("other");
            temp.setLastName("other");
            temp.setPassword("otherpassword");
            temp.setUserName("otherusername");

            user = service.saveUser(temp);

            Assert.assertEquals(temp.getId(), user.getId());
            Assert.assertEquals(temp.getEmail(), user.getEmail());
            Assert.assertEquals(temp.isDeleted(), user.isDeleted());
            Assert.assertEquals(temp.getFirstName(), user.getFirstName());
            Assert.assertEquals(temp.getLastName(), user.getLastName());
            Assert.assertEquals(temp.getPassword(), user.getPassword());
            Assert.assertEquals(temp.getUserName(), user.getUserName());

            log.debug("##### Update User -> Success!! #####");
        } catch (PersistenceException e) {
            log.error("##### Update User -> Error!#####");
            Assert.fail();
        }
    }

    /**
     * test deleting a User.
     */
    @Test
    public void testDeleteUser() {
        log.debug("##### Delete User #####");
        user = new User();
        user.setDeleted(false);
        user.setEmail("test@person.at");
        user.setFirstName("test");
        user.setLastName("person");
        user.setPassword("testpassword");
        user.setUserName("testusername");

        try {
            user = service.saveUser(user);
            Assert.assertNotNull(user.getId());
            service.deleteUser(user);
            Assert.assertNull(service.getUserById(user.getId()));
            log.debug("##### Delete User -> Success!! #####");
        } catch (PersistenceException e) {
            log.error("##### Delete User -> Failed!#####");
            Assert.fail();
        }
    }

    /**
     * test finding a User by Id.
     */
    @Test
    public void testFindUserById() {
        log.debug("##### Find User #####");
        user = new User();
        user.setDeleted(false);
        user.setEmail("test@person.at");
        user.setFirstName("test");
        user.setLastName("person");
        user.setPassword("testpassword");
        user.setUserName("testusername");

        try {
            user = service.saveUser(user);
            Assert.assertNotNull(user.getId());
            Assert.assertNotNull(service.getUserById(user.getId()));
            log.debug("##### Find User -> Success!! #####");
        } catch (PersistenceException e) {
            log.error("##### Find User -> Failed!#####");
            Assert.fail();
        }
    }

}

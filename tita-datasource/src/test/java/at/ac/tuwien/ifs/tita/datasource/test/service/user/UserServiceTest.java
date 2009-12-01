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

package at.ac.tuwien.ifs.tita.datasource.test.service.user;

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

import at.ac.tuwien.ifs.tita.datasource.entity.Role;
import at.ac.tuwien.ifs.tita.datasource.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.datasource.service.user.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:datasourceContext-test.xml" })
@TransactionConfiguration
public class UserServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    // @Autowired
    // private ApplicationContext ctx;

    @Autowired
    private IUserService service;

    @Test
    public void testSaveRole() {
        Role role1 = new Role();
        role1.setDescription("Das ist die Test Rolle 1");

        try {
            service.saveRole(role1);
            Assert.assertNotNull(role1.getId());
        } catch (TitaDAOException e) {
            e.printStackTrace();
        }
    }

	@Test
	public void testSearchRole() {
		Role role1 = new Role();
		role1.setDescription("Das ist die Test Rolle 1");

		try {
			service.saveRole(role1);
			Assert.assertNotNull(role1.getId());		
			Role role2 = service.getRoleById(role1.getId());
			Assert.assertNotNull(role2);
			Assert.assertEquals(role1.getId(),role2.getId());
		} catch (TitaDAOException e) {
			e.printStackTrace();
		}
	}
}

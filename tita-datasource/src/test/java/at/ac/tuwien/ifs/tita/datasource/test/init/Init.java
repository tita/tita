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

package at.ac.tuwien.ifs.tita.datasource.test.init;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.ac.tuwien.ifs.tita.datasource.domain.Role;
import at.ac.tuwien.ifs.tita.datasource.domain.User;
import at.ac.tuwien.ifs.tita.datasource.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.datasource.service.IUserService;

public class Init {

    /**
     * @param args
     *            no args
     */
    public static void main(String[] args) {

        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                "persistence-context.xml");

        IUserService service = (IUserService) ctx.getBean("userService");

        // init the db and save some values in it
        Role role1 = new Role();
        role1.setDescription("Developer - No it's Time Recorder");
        Role role2 = new Role();
        role2.setDescription("Project Leader - No it's Time Inspector");

        try {
            role1 = service.saveRole(role1);
            role2 = service.saveRole(role2);
        } catch (TitaDAOException e) {
            e.printStackTrace();
        }

        User u1 = new User();
        u1.setDeleted(false);
        u1.setEmail("pberger@tita.at");
        u1.setFirstName("Peter");
        u1.setLastName("Berger");
        u1.setPassword("secret");
        u1.setRole(role1);
        u1.setUserName("pberger");

        User u2 = new User();
        u2.setDeleted(false);
        u2.setEmail("turing_machine@tita.at");
        u2.setFirstName("Alain");
        u2.setLastName("Turing");
        u2.setPassword("secret");
        u2.setRole(role1);
        u2.setUserName("turing_machine");

        try {
            u1 = service.saveUser(u1);
            u2 = service.saveUser(u2);
        } catch (TitaDAOException e) {
            e.printStackTrace();
        }

    }

}

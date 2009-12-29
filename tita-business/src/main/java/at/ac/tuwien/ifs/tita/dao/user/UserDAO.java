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
package at.ac.tuwien.ifs.tita.dao.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import at.ac.tuwien.ifs.tita.dao.GenericHibernateDao;
import at.ac.tuwien.ifs.tita.entity.User;

/**
 * UserDAO encapsulates CRUD-Operations for the TiTAUser.
 * 
 * @author ASE Group 10
 */
@Repository
public class UserDAO extends GenericHibernateDao<User, Long> {

    /**
     * public constructor, needed for telling the generic EntityManager the
     * actual type.
     */
    public UserDAO() {
        super(User.class);
    }

    /**
     * Gets a user by username.
     * 
     * @param username username of user
     * @return a user
     */
    public User findByUserName(String username) {
        User u = new User();
        u.setDeleted(false);
        u.setUserName(username);

        List<User> users = findByExample(u);

        if (users.size() > 0) {
            u = users.get(0);
        }

        return u;
    }
}

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

import at.ac.tuwien.ifs.tita.entity.TiTAUser;

/**
 * Interface for UserDao.
 * @author herbert
 *
 */
public interface IUserDAO {
    
    /**
     * Gets a user by username.
     * 
     * @param username username of user
     * @return a user
     */
    TiTAUser findByUserName(String username);
    
    /**
     * Finds all tita users which belong to given tita projects.
     * @param projects List of String
     * @return List of TiTAUser
     */
    List<TiTAUser> findUsersForProjectNames(List<String> projects);
}

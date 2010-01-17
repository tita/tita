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
package at.ac.tuwien.ifs.tita.dao.interfaces;

import java.util.List;

import javax.persistence.PersistenceException;

import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;

/**
 * Interface for UserDao.
 * 
 * @author herbert
 * 
 */
public interface IUserDAO extends IGenericHibernateDao<TiTAUser, Long> {

    /**
     * Gets a user by username.
     * 
     * @param username username of user
     * @return a user
     */
    TiTAUser findByUserName(String username);

    /**
     * Finds all tita users which belong to given tita projects.
     * 
     * @param projects List of String
     * @return List of TiTAUser
     */
    List<TiTAUser> findUsersForProjectNames(List<String> projects);

    /**
     * Finds the targetHours for a tita project a user is added with a defined
     * value.
     * 
     * @param userId - the id of the tita user
     * @param projectId - the id of the tita project
     * @return the value for the target hours or null, if it is unlimited.
     */
    Long findTargetHoursForTiTAProjectAndTiTAUser(Long userId, Long projectId);

    /**
     * Finds all users for a tita project.
     * 
     * @param project - the given project
     * @return a list of tita user, that are included in that project.
     */
    List<TiTAUser> findUsersForTiTAProject(TiTAProject project);
    
    /**
     * Searches the Database for a List of Users.
     * 
     * @param maxResult the maximum size of List.
     * @throws PersistenceException if Exception was thrown before.
     * @return a List of TiTAUsers.
     */
    public List<TiTAUser> findUsersOrdered(int maxResult) throws PersistenceException;

}

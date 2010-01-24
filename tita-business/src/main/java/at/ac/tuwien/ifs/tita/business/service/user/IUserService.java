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
package at.ac.tuwien.ifs.tita.business.service.user;

import java.util.List;

import javax.persistence.PersistenceException;

import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;
import at.ac.tuwien.ifs.tita.entity.conv.Role;

/**
 * IUserService encapsulates all User-concerning Database operations.
 * 
 * @author ASE Group 10 - TiTA
 * 
 */
@Transactional
public interface IUserService {
    /**
     * Saves a new user or updates an existing one.
     * 
     * @param user the user to be saved
     * @throws PersistenceException if Parameter is null or another Exception is thrown
     * @return the saved User.
     */
    TiTAUser saveUser(TiTAUser user) throws PersistenceException;

    /**
     * deletes an existing user.
     * 
     * @param user the user to be deleted
     * @throws PersistenceException if Parameter is null or another Exception is thrown
     */
    void deleteUser(TiTAUser user) throws PersistenceException;

    /**
     * returns a specific User found to the id given.
     * 
     * @param id the unique identifier of an user
     * @throws PersistenceException if no user was found or another Exception is thrown
     * @return the specified User, if found.
     */
    TiTAUser getUserById(Long id) throws PersistenceException;

    /**
     * returns a specific User found to the username given.
     * 
     * @param username the unique identifier of an user
     * @throws PersistenceException if no user was found or another Exception is thrown
     * @return the specified User, if found.
     */
    TiTAUser getUserByUsername(String username) throws PersistenceException;

    /**
     * Saves a new Role or updates an existing one.
     * 
     * @param role the role to be saved
     * @throws PersistenceException if Parameter is null or another Exception is thrown
     * @return the saved Role.
     */
    Role saveRole(Role role) throws PersistenceException;

    /**
     * deletes an existing role.
     * 
     * @param role the role to be deleted
     * @throws PersistenceException if Parameter is null or another Exception is thrown
     */
    void deleteRole(Role role) throws PersistenceException;

    /**
     * returns a specific Role found to the id given.
     * 
     * @param id the unique identifier of an role
     * @throws PersistenceException if no role was found or another Exception is thrown
     * @return the specified Role if id was found
     */
    Role getRoleById(Long id) throws PersistenceException;

    /**
     * returns a specific Role found to the name given.
     * 
     * @param name of role
     * @return the specified Role if name was found, null otherwise
     */
    Role getRoleByName(String name);

    /**
     * returns all undeleted Users.
     * 
     * @throws PersistenceException if no user was found or another Exception is thrown
     * @return all Users if found
     */
    List<TiTAUser> getUndeletedUsers() throws PersistenceException;

    /**
     * returns all Roles.
     * 
     * @throws PersistenceException if no role was found or another Exception is thrown
     * @return all Roles if found
     */
    List<Role> getRoles() throws PersistenceException;

    /**
     * Returns a list of all existing tita users.
     * 
     * @param projects List of tita projects
     * @return List of TiTAUsers
     */
    List<TiTAUser> findAllTiTAUsersForProjects(List<String> projects);

    /**
     * Finds all users for a tita project.
     * 
     * @param project - the given project
     * @return a list of tita user, that are included in that project.
     */
    List<TiTAUser> findAllTiTAUsersForProject(TiTAProject project);

    /**
     * Finds all users for a tita project by role.
     * 
     * @param project - the given project
     * @param role - role of user
     * @return a list of tita user, that are included in that project and have the given role.
     */
    List<TiTAUser> findAllTiTAUsersForProjectByRole(TiTAProject project, Role role);

    /**
     * Finds the targetHours for a tita project a user is added with a defined value.
     * 
     * @param userId - the id of the tita user
     * @param projectId - the id of the tita project
     * @return the value for the target hours or null, if it is unlimited.
     */
    Long findTargetHoursForTiTAProjectAndTiTAUser(Long userId, Long projectId);

    /**
     * Returns a List of Users, where the size of the list can be declared.
     * 
     * @param maxResult the maximum size of list
     * @return a list of TiTAUsers
     * @throws PersistenceException if an Exception from the DAO was thrown.
     */
    List<TiTAUser> getOrderedUsers(int maxResult) throws PersistenceException;

    /**
     * Fetches all Role Descriptions.
     * 
     * @return a List of all available Role Descpriptions.
     * @throws PersistenceException if an Exception from DAO was thrown.
     */
    List<String> getRoleDescriptions() throws PersistenceException;

    /**
     * Fetches all available IssueTracker.
     * 
     * @return a List of all available IssueTracker.
     * @throws PersistenceException if an Exception from DAO was thrown.
     */
    List<IssueTracker> getAvailableIssueTracker() throws PersistenceException;

    /**
     * saves a Login to a specific user.
     * 
     * @param login the login to save
     * @param user the user for the public key
     * @return the saved login, with encrypted passwords
     * @throws PersistenceException if an Exception from DAO was thrown.
     */
    IssueTrackerLogin saveIssueTrackerLogin(IssueTrackerLogin login, TiTAUser user) throws PersistenceException;

}

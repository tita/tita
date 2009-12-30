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

import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
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
     * @throws PersistenceException if Parameter is null or another Exception is
     *         thrown
     * @return the saved User.
     */
    TiTAUser saveUser(TiTAUser user) throws PersistenceException;

    /**
     * deletes an existing user.
     * 
     * @param user the user to be deleted
     * @throws PersistenceException if Parameter is null or another Exception is
     *         thrown
     */
    void deleteUser(TiTAUser user) throws PersistenceException;

    /**
     * returns a specific User found to the id given.
     * 
     * @param id the unique identifier of an user
     * @throws PersistenceException if no user was found or another Exception is
     *         thrown
     * @return the specified User, if found.
     */
    TiTAUser getUserById(Long id) throws PersistenceException;

    /**
     * returns a specific User found to the username given.
     * 
     * @param username the unique identifier of an user
     * @throws TitaDAOException if no user was found or another Exception is
     *         thrown
     * @return the specified User, if found.
     */
    TiTAUser getUserByUsername(String username) throws TitaDAOException;

    /**
     * Saves a new Role or updates an existing one.
     * 
     * @param role the role to be saved
     * @throws PersistenceException if Parameter is null or another Exception is
     *         thrown
     * @return the saved Role.
     */
    Role saveRole(Role role) throws PersistenceException;

    /**
     * deletes an existing role.
     * 
     * @param role the role to be deleted
     * @throws PersistenceException if Parameter is null or another Exception is
     *         thrown
     */
    void deleteRole(Role role) throws PersistenceException;

    /**
     * returns a specific Role found to the id given.
     * 
     * @param id the unique identifier of an role
     * @throws PersistenceException if no role was found or another Exception is
     *         thrown
     * @return the specified Role if id was found
     */
    Role getRoleById(Long id) throws PersistenceException;

    /**
     * returns all undeleted Users.
     * 
     * @throws TitaDAOException if no user was found or another Exception is
     *         thrown
     * @return all Users if found
     */
    List<TiTAUser> getUndeletedUsers() throws TitaDAOException;

    /**
     * returns all Roles.
     * 
     * @throws TitaDAOException if no role was found or another Exception is
     *         thrown
     * @return all Roles if found
     */
    List<Role> getRoles() throws TitaDAOException;
}

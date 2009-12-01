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

package at.ac.tuwien.ifs.tita.datasource.service.user;

import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.ifs.tita.datasource.entity.Role;
import at.ac.tuwien.ifs.tita.datasource.entity.User;
import at.ac.tuwien.ifs.tita.datasource.exception.TitaDAOException;

/**
 * IUserService encapsulates all User-concerning Database operations
 * 
 * @author ASE Group 10 - TiTA
 * 
 */
@Transactional
public interface IUserService {
    /**
     * Saves a new user or updates an existing one
     * 
     * @param user
     *            the user to be saved
     * @throws TitaDAOException
     *             if Parameter is null or another Exception is thrown
     */
    void saveUser(User user) throws TitaDAOException;

    /**
     * Updates an exsiting User
     * 
     * @param user
     *            the user to be updated
     * @throws TitaDAOException
     *             if Parameter is null or another Exception is thrown
     */
    User updateUser(User user) throws TitaDAOException;

    /**
     * deletes an existing user
     * 
     * @param user
     *            the user to be deleted
     * @throws TitaDAOException
     *             if Parameter is null or another Exception is thrown
     */
    void deleteUser(User user) throws TitaDAOException;

    /**
     * returns a specific User found to the id given
     * 
     * @param id
     *            the unique identifier of an user
     * @throws TitaDAOException
     *             if no user was found or another Exception is thrown
     */
    User getUserById(Long id) throws TitaDAOException;

    /**
     * Saves a new Role or updates an existing one
     * 
     * @param role
     *            the role to be saved
     * @throws TitaDAOException
     *             if Parameter is null or another Exception is thrown
     */
    void saveRole(Role role) throws TitaDAOException;

    /**
     * Updates an exsiting Role
     * 
     * @param role
     *            the role to be updated
     * @throws TitaDAOException
     *             if Parameter is null or another Exception is thrown
     */
    Role updateRole(Role role) throws TitaDAOException;

    /**
     * deletes an existing role
     * 
     * @param role
     *            the role to be deleted
     * @throws TitaDAOException
     *             if Parameter is null or another Exception is thrown
     */
    void deleteRole(Role role) throws TitaDAOException;

    /**
     * returns a specific Role found to the id given
     * 
     * @param id
     *            the unique identifier of an role
     * @throws TitaDAOException
     *             if no role was found or another Exception is thrown
     */
    Role getRoleById(Long id) throws TitaDAOException;
}

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

package at.ac.tuwien.ifs.tita.datasource.service;

import at.ac.tuwien.ifs.tita.datasource.dao.RoleDAO;
import at.ac.tuwien.ifs.tita.datasource.dao.UserDAO;
import at.ac.tuwien.ifs.tita.datasource.domain.Role;
import at.ac.tuwien.ifs.tita.datasource.domain.User;
import at.ac.tuwien.ifs.tita.datasource.exception.TitaDAOException;

public class UserService implements IUserService {

    private UserDAO userDAO;
    private RoleDAO roleDAO;

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setRoleDAO(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    public void deleteRole(Role role) throws TitaDAOException {
        roleDAO.delete(role);
    }

    @Override
    public void deleteUser(User user) throws TitaDAOException {
        userDAO.delete(user);

    }

    @Override
    public Role getRoleById(Long id) throws TitaDAOException {
        return roleDAO.getById(Role.class, id);
    }

    @Override
    public User getUserById(Long id) throws TitaDAOException {
        return userDAO.getById(User.class, id);
    }

    @Override
    public Role saveRole(Role role) throws TitaDAOException {
        return roleDAO.save(role);
    }

    @Override
    public User saveUser(User user) throws TitaDAOException {
        return userDAO.save(user);
    }

    @Override
    public void updateRole(Role role) throws TitaDAOException {
        roleDAO.update(role);
    }

    @Override
    public void updateUser(User user) throws TitaDAOException {
        userDAO.update(user);
    }

}

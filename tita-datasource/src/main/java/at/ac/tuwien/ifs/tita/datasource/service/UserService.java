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

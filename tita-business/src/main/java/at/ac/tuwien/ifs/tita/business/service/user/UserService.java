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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import at.ac.tuwien.ifs.tita.business.security.TiTASecurity;
import at.ac.tuwien.ifs.tita.business.security.exception.TiTASecurityException;
import at.ac.tuwien.ifs.tita.dao.interfaces.IGenericHibernateDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.IUserDAO;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;
import at.ac.tuwien.ifs.tita.entity.conv.Role;

/**
 * Service for manipulating (insert, update, delete, search... ) users and roles in TiTA.
 * 
 * @author ASE Group 10 - TiTA
 * 
 */
public class UserService implements IUserService {

    private IUserDAO userDao;
    private IGenericHibernateDao<Role, Long> roleDao;
    private IGenericHibernateDao<IssueTracker, Long> issueTrackerDao;
    private IGenericHibernateDao<IssueTrackerLogin, Long> issueTrackerLoginDao;

    public void setUserDao(IUserDAO userDao) {
        this.userDao = userDao;
    }

    public void setRoleDao(IGenericHibernateDao<Role, Long> roleDao) {
        this.roleDao = roleDao;
    }

    public void setIssueTrackerDao(IGenericHibernateDao<IssueTracker, Long> issueTrackerDao) {
        this.issueTrackerDao = issueTrackerDao;
    }

    public void setIssueTrackerLoginDao(IGenericHibernateDao<IssueTrackerLogin, Long> issueTrackerLoginDao) {
        this.issueTrackerLoginDao = issueTrackerLoginDao;
    }

    /** {@inheritDoc} */
    @Override
    public void deleteRole(Role role) throws PersistenceException {
        roleDao.delete(role);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteUser(TiTAUser user) throws PersistenceException {
        userDao.delete(user);

    }

    /** {@inheritDoc} */
    @Override
    public Role getRoleById(Long id) throws PersistenceException {
        return roleDao.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public TiTAUser getUserById(Long id) throws PersistenceException {
        return userDao.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public Role saveRole(Role role) throws PersistenceException {
        return roleDao.save(role);
    }

    /** {@inheritDoc} */
    @Override
    public TiTAUser saveUser(TiTAUser user) throws PersistenceException {
        return userDao.save(user);
    }

    /** {@inheritDoc} */
    @Override
    public TiTAUser getUserByUsername(String username) throws PersistenceException {
        return userDao.findByUserName(username);
    }

    /** {@inheritDoc} */
    @Override
    public List<TiTAUser> getUndeletedUsers() throws PersistenceException {
        TiTAUser u = new TiTAUser();
        u.setDeleted(false);
        return userDao.findByExample(u);
    }

    /** {@inheritDoc} */
    @Override
    public List<Role> getRoles() throws PersistenceException {
        return roleDao.findAll();
    }

    /** {@inheritDoc} */
    @Override
    public List<TiTAUser> findAllTiTAUsersForProjects(List<String> projects) {
        return userDao.findUsersForProjectNames(projects);
    }

    /** {@inheritDoc} */
    @Override
    public List<TiTAUser> findAllTiTAUsersForProject(TiTAProject project) {
        return userDao.findUsersForTiTAProject(project);
    }

    /** {@inheritDoc} */
    @Override
    public Long findTargetHoursForTiTAProjectAndTiTAUser(Long userId, Long projectId) {
        return userDao.findTargetHoursForTiTAProjectAndTiTAUser(userId, projectId);
    }

    /** {@inheritDoc} */
    @Override
    public List<IssueTracker> getAvailableIssueTracker() {
        return issueTrackerDao.findAll();
    }

    /** {@inheritDoc} */
    @Override
    public List<TiTAUser> getOrderedUsers(int maxResult) throws PersistenceException {
        return userDao.findUsersOrdered(maxResult);
    }

    /** {@inheritDoc} */
    @Override
    public List<String> getRoleDescriptions() throws PersistenceException {
        List<String> descriptions = new ArrayList<String>();
        for (Role r : roleDao.findByExample(new Role())) {
            descriptions.add(r.getDescription());
        }

        return descriptions;
    }

    /** {@inheritDoc} */
    @Override
    public Role getRoleByName(String name) {
        Role r = new Role();
        r.setDescription(name);
        List<Role> roles = roleDao.findByExample(r);
        return (roles.size() > 0) ? roles.get(0) : null;
    }

    /** {@inheritDoc} */
    @Override
    public List<TiTAUser> findAllTiTAUsersForProjectByRole(TiTAProject project, Role role) {
        return userDao.findUsersForTiTAProjectByRole(project, role);
    }

    /** {@inheritDoc} */
    @Override
    public IssueTrackerLogin saveIssueTrackerLogin(IssueTrackerLogin login, TiTAUser user) throws PersistenceException {

        try {
            if (login.getId() == null) {
                login.setPassword(TiTASecurity.getEncryptedPassword(user.getUserName(), login.getPassword()));
            }
        } catch (TiTASecurityException e) {
            try {
                login.setPassword(TiTASecurity.getEncryptedPassword(user.getUserName(), user.getPassword(), login
                    .getPassword()));
            } catch (TiTASecurityException e1) {
                throw new PersistenceException(e1.getMessage());
            }
        }
        login.setUser(user);
        return issueTrackerLoginDao.save(login);
    }

    /** {@inheritDoc} */
    @Override
    public IssueTracker saveIssueTracker(IssueTracker issueTracker) throws PersistenceException {
        return issueTrackerDao.save(issueTracker);
    }
}

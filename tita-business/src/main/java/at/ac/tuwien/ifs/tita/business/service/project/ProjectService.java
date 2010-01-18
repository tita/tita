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

package at.ac.tuwien.ifs.tita.business.service.project;

import java.util.List;

import javax.persistence.PersistenceException;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;

import at.ac.tuwien.ifs.tita.dao.interfaces.IGenericHibernateDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.IIssueTrackerProjectDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.IIssueTrackerTaskDao;
import at.ac.tuwien.ifs.tita.dao.project.TiTAProjectDao;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;
import at.ac.tuwien.ifs.tita.entity.conv.ProjectStatus;

/**
 * Service for manipulating (insert, update, delete, search... ) tita projects
 * in TiTA.
 * 
 * @author herbert
 * 
 */
public class ProjectService implements IProjectService {

    private TiTAProjectDao titaProjectDao;
    private IIssueTrackerTaskDao issueTrackerTaskDao;
    private IIssueTrackerProjectDao issueTrackerProjectDao;
    private IGenericHibernateDao<TiTATask, Long> titaTaskDao;
    private IGenericHibernateDao<IssueTracker, Long> issueTrackerDao;

    public void setTitaProjectDao(TiTAProjectDao titaProjectDao) {
        this.titaProjectDao = titaProjectDao;
    }

    public void setIssueTrackerTaskDao(IIssueTrackerTaskDao issueTrackerTaskDao) {
        this.issueTrackerTaskDao = issueTrackerTaskDao;
    }

    public void setIssueTrackerProjectDao(IIssueTrackerProjectDao issueTrackerProjectDao) {
        this.issueTrackerProjectDao = issueTrackerProjectDao;
    }

    public void setTitaTaskDao(IGenericHibernateDao<TiTATask, Long> titaTaskDao) {
        this.titaTaskDao = titaTaskDao;
    }

    public void setIssueTrackerDao(IGenericHibernateDao<IssueTracker, Long> issueTrackerDao) {
        this.issueTrackerDao = issueTrackerDao;
    }

    /** {@inheritDoc} */
    @Override
    public void deleteProject(TiTAProject project) throws PersistenceException {
        titaProjectDao.delete(project);
    }

    /** {@inheritDoc} */
    @Override
    public TiTAProject getProjectById(Long id) throws PersistenceException {
        return titaProjectDao.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public IssueTrackerTask saveIssueTrackerTask(IssueTrackerTask itt) throws PersistenceException {
        return issueTrackerTaskDao.save(itt);
    }

    /** {@inheritDoc} */
    @Override
    public List<TiTAProject> findAllTiTAProjects() {
        return titaProjectDao.findAllOrdered(new Order[] { Property.forName("name").asc() });
    }

    /** {@inheritDoc} */
    @Override
    public List<TiTAProject> findTiTAProjectsForUser(TiTAUser user) {
        return titaProjectDao.findTiTAProjectsForUsername(user.getUserName());
    }

    /** {@inheritDoc} */
    @Override
    public IssueTrackerTask findIssueTrackerTaskForTiTAProject(Long tp, Long it, Long itp, Long itt) {
        return issueTrackerTaskDao.findIssueTrackerTask(tp, it, itp, itt);
    }

    /** {@inheritDoc} */
    @Override
    public IssueTrackerProject findIssueTrackerProjectForTiTAProject(Long tp, Long issueTrackerId, Long itp) {
        return issueTrackerProjectDao.findIssueTrackerProjectForTiTAProject(tp, issueTrackerId, itp);
    }

    /** {@inheritDoc} */
    @Override
    public TiTATask saveTiTATask(TiTATask task) {
        return titaTaskDao.save(task);
    }

    /** {@inheritDoc} */
    @Override
    public List<IssueTracker> getAvailableIssueTracker() {
        return issueTrackerDao.findAll();
    }

    /** {@inheritDoc} */
    @Override
    public List<ProjectStatus> getAvailableProjectStati() throws PersistenceException {
        return titaProjectDao.getAvailableProjectStati();
    }

    /** {@inheritDoc} */
    @Override
    public List<TiTAProject> getOrderedProjects(int maxResult, String orderBy) throws PersistenceException {
        try {
            return titaProjectDao.findProjectsOrderedByName(maxResult, orderBy);
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public TiTAProject saveProject(TiTAProject project) throws PersistenceException {
        return titaProjectDao.save(project);
    }

}

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
package at.ac.tuwien.ifs.tita.business.service.tasks;

import java.util.Map;
import java.util.TreeMap;

import javax.persistence.PersistenceException;

import at.ac.tuwien.ifs.tita.dao.IGenericHibernateDao;
import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.dao.issuetracker.IssueTrackerDao;
import at.ac.tuwien.ifs.tita.dao.issuetracker.task.IssueTrackerTaskDao;
import at.ac.tuwien.ifs.tita.dao.titatask.TiTATaskDao;
import at.ac.tuwien.ifs.tita.dao.user.UserDAO;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueStatus;
import at.ac.tuwien.ifs.tita.issuetracker.exceptions.ProjectNotFoundException;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IProjectTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.issue.service.IIssueTrackerService;
import at.ac.tuwien.ifs.tita.issuetracker.issue.service.IssueTrackerService;

/**
 * The TaskService combines the IssueTrackerService, which fetches and manage
 * the data from the issue trackers, and encapsulates all Task-concerning
 * Database operations.
 *
 * @author Christoph
 *
 */

public class TaskService implements ITaskService {

    private UserDAO titaUserDao;

    private IGenericHibernateDao<TiTAProject, Long> titaProjectDao;

    private TiTAProject titaProject;
    private TiTAUser titaUser;

    private IssueTrackerTaskDao issueTrackerTaskDao;
    private TiTATaskDao titaTaskDao;

    private IssueTrackerDao issueTrackerDao;

    private Map<Long, ITaskTrackable> mapOfTasksFromAllProjectsIncludedInTiTAProject = new TreeMap<Long, ITaskTrackable>();

    private IIssueTrackerService issueTrackerService;

    public TaskService() {

    }


    public void setIssueTrackerDao(IssueTrackerDao issueTrackerDao) {
        this.issueTrackerDao = issueTrackerDao;
    }

    /** {@inheritDoc} */
    @Override
    public Map<Long, TiTATask> getTiTATasks(TiTAProject pojectTita) throws TitaDAOException {
        return this.issueTrackerTaskDao.getTiTATasksForTitaProject(pojectTita);
    }

    /** {@inheritDoc} */
    @Override
    public IssueTrackerTask saveIssueTrackerTask(IssueTrackerTask issueTrackerTask)
            throws PersistenceException {
        return this.issueTrackerTaskDao.save(issueTrackerTask);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteIssueTrackerTask(IssueTrackerTask issueTrackerTask)
            throws PersistenceException {
        this.issueTrackerTaskDao.delete(issueTrackerTask);
    }

    /** {@inheritDoc} */
    @Override
    public IssueTrackerTask getIssueTrackerTaskById(Long id) throws PersistenceException {
        return this.issueTrackerTaskDao.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public TiTATask saveTiTATask(TiTATask titaTask) throws PersistenceException {
        return this.titaTaskDao.save(titaTask);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteTiTATask(TiTATask titaTask) throws PersistenceException {
        this.titaTaskDao.delete(titaTask);
    }

    /** {@inheritDoc} */
    @Override
    public TiTATask getTiTATaskById(Long id) throws PersistenceException {
        return this.titaTaskDao.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public IssueTracker saveIssueTracker(IssueTracker issueTracker) throws PersistenceException {
        return this.issueTrackerDao.save(issueTracker);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteIssueTracker(IssueTracker issueTracker) throws PersistenceException {
        this.issueTrackerDao.delete(issueTracker);
    }

    /** {@inheritDoc} */
    @Override
    public IssueTracker getIssueTrackerById(Long id) throws PersistenceException {
        return this.issueTrackerDao.findById(id);
    }


    /** {@inheritDoc} */
    @Override
    public void fetchTaskFromIssueTrackerProjects(Long projectTitaId, Long userTitaId)
            throws ProjectNotFoundException, InterruptedException {

        this.titaProject = this.titaProjectDao.findById(projectTitaId);

        if (this.titaProject == null) {
            throw new ProjectNotFoundException("Project not found.");
        }

        this.titaUser = this.titaUserDao.findById(userTitaId);

        Long key = 0L;

        for (IssueTrackerLogin login : this.titaUser.getIssueTrackerLogins()) {
            this.issueTrackerService = new IssueTrackerService(login);

            Map<Long, IProjectTrackable> mapOfProjects = this.issueTrackerService.getProjects();

            for (IssueTrackerProject issueTrackerProject : this.titaProject
                    .getIssueTrackerProjects()) {

                for (IProjectTrackable projectTrackable : mapOfProjects.values()) {
                    if (projectTrackable.getId().equals(issueTrackerProject.getIsstProjectId())) {
                        // TODO Flag setzen

                        this.issueTrackerService.updateProject(projectTrackable);

                        Map<Long, ITaskTrackable> mapOfTasks = this.issueTrackerService
                                .getIssueTrackerTasksByProjectId(issueTrackerProject
                                        .getIsstProjectId());


                        if (mapOfTasks != null) {

                            for (ITaskTrackable taskTrackable : mapOfTasks.values()) {
                                key++;
                                this.mapOfTasksFromAllProjectsIncludedInTiTAProject.put(key,
                                        taskTrackable);
                            }
                        }
                    }
                }
            }
        }
    }

    public Map<Long, ITaskTrackable> getMapOfTasksFromAllProjectsIncludedInTiTAProject() {
        return this.mapOfTasksFromAllProjectsIncludedInTiTAProject;
    }

    public void setTitaUserDao(UserDAO titaUserDao) {
        this.titaUserDao = titaUserDao;
    }

    public void setTitaProjectDao(IGenericHibernateDao<TiTAProject, Long> titaProjectDao) {
        this.titaProjectDao = titaProjectDao;
    }

    public void setIssueTrackerTaskDao(IssueTrackerTaskDao issueTrackerTaskDao) {
        this.issueTrackerTaskDao = issueTrackerTaskDao;
    }

    public void setTitaTaskDao(TiTATaskDao titaTaskDao) {
        this.titaTaskDao = titaTaskDao;
    }

    /** {@inheritDoc} */
    @Override
    public Map<Long, ITaskTrackable> getIssueTrackerTasksGroupByIssueStatus(IssueStatus status) {

        Map<Long, ITaskTrackable> taskGroupByStatus = new TreeMap<Long, ITaskTrackable>();
        Long key = 0L;

        for (ITaskTrackable task : this.mapOfTasksFromAllProjectsIncludedInTiTAProject.values()) {
            if (task.getIssueTrackerType().equals(status)) {
                key++;
                taskGroupByStatus.put(key, task);
            }
        }
        return taskGroupByStatus;
    }

    /** {@inheritDoc} */
    @Override
    public Map<Long, ITaskTrackable> getIssueTrackerTasksGroupByIssueTracker(String url) {

        Map<Long, ITaskTrackable> taskGroupByIssueTrackerUrl = new TreeMap<Long, ITaskTrackable>();
        Long key = 0L;

        for (ITaskTrackable task : this.mapOfTasksFromAllProjectsIncludedInTiTAProject.values()) {
            if (task.getProject().getUrl().equals(url)) {
                key++;
                taskGroupByIssueTrackerUrl.put(key, task);
            }
        }
        return taskGroupByIssueTrackerUrl;
    }

}

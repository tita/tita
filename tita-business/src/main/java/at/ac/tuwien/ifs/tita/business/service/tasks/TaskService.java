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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.dao.interfaces.IEffortDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.IGenericHibernateDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.IIssueTrackerTaskDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.ITiTAProjectDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.ITiTATaskDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.IUserDAO;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;
import at.ac.tuwien.ifs.tita.entity.util.UserProjectTaskEffort;
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

    private IUserDAO titaUserDao;

    private ITiTAProjectDao titaProjectDao;

    private TiTAProject titaProject;
    private TiTAUser titaUser;

    private IIssueTrackerTaskDao issueTrackerTaskDao;
    private ITiTATaskDao titaTaskDao;
    private IGenericHibernateDao<IssueTracker, Long> issueTrackerDao;

    private Map<Long, ITaskTrackable> mapOfTasksFromAllProjectsIncludedInTiTAProject = new TreeMap<Long, ITaskTrackable>();

    private IIssueTrackerService issueTrackerService;

    private IEffortDao timeEffortDao;

    // Logging is not really useful in the debug mode, because of the mantis
    // webservice logging.
    private final Logger log = LoggerFactory.getLogger(TaskService.class);

    public TaskService() {

    }

    /** {@inheritDoc} */
    @Override
    public IssueTrackerTask saveIssueTrackerTask(IssueTrackerTask issueTrackerTask) throws PersistenceException {
        return issueTrackerTaskDao.save(issueTrackerTask);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteIssueTrackerTask(IssueTrackerTask issueTrackerTask) throws PersistenceException {
        issueTrackerTaskDao.delete(issueTrackerTask);
    }

    /** {@inheritDoc} */
    @Override
    public IssueTrackerTask getIssueTrackerTaskById(Long id) throws PersistenceException {
        return issueTrackerTaskDao.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public TiTATask saveTiTATask(TiTATask titaTask) throws PersistenceException {
        return titaTaskDao.save(titaTask);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteTiTATask(TiTATask titaTask) throws PersistenceException {
        titaTaskDao.delete(titaTask);
    }

    /** {@inheritDoc} */
    @Override
    public TiTATask getTiTATaskById(Long id) throws PersistenceException {
        return titaTaskDao.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public IssueTracker saveIssueTracker(IssueTracker issueTracker) throws PersistenceException {
        return issueTrackerDao.save(issueTracker);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteIssueTracker(IssueTracker issueTracker) throws PersistenceException {
        issueTrackerDao.delete(issueTracker);
    }

    /** {@inheritDoc} */
    @Override
    public IssueTracker getIssueTrackerById(Long id) throws PersistenceException {
        return issueTrackerDao.findById(id);
    }
    
  
    /** {@inheritDoc} */
    public ITaskTrackable getIssueTrackerTaskById(Long taskId, Long projectId, Long issueTrackerId){
        //TODO: check for issueTrackerID too!
        for(Long i = 1L; i <= mapOfTasksFromAllProjectsIncludedInTiTAProject.size(); i++){
            ITaskTrackable t = mapOfTasksFromAllProjectsIncludedInTiTAProject.get(i);
            if(t.getProject().getId().equals(projectId) &&
                    t.getId().equals(taskId)){
                return t;
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void fetchTaskFromIssueTrackerProjects(Long projectTitaId, Long userTitaId) throws ProjectNotFoundException {

        titaProject = titaProjectDao.findById(projectTitaId);

        if (titaProject == null) {
            log.debug("TaskService - fetchTaskFromIssueTrackerProjects: Project not found.");
            throw new ProjectNotFoundException("Project not found.");
        }
        mapOfTasksFromAllProjectsIncludedInTiTAProject.clear();

        titaUser = titaUserDao.findById(userTitaId);

        Long key = 0L;

        for (IssueTrackerLogin login : titaUser.getIssueTrackerLogins()) {
            log.debug("Take issue tracker login: " + login.getId() + " to use the issue tracker service.");
            issueTrackerService = new IssueTrackerService(login);

            Map<Long, IProjectTrackable> mapOfProjects = issueTrackerService.getProjects();

            log.info("Check all accessible projects from the Issue Tracker: " + login.getIssueTracker().getUrl());
            if (this.titaProject.getIssueTrackerProjects() != null && mapOfProjects != null
                    && mapOfProjects.values() != null) {
                for (IssueTrackerProject issueTrackerProject : titaProject.getIssueTrackerProjects()) {

                    for (IProjectTrackable projectTrackable : mapOfProjects.values()) {

                        if (projectTrackable.getId().equals(issueTrackerProject.getIsstProjectId())) {

                            log.info("Updating the project before fetching: " + projectTrackable.getName());
                            issueTrackerService.updateProject(projectTrackable);

                            log.info("Fetching tasks for the project: " + projectTrackable.getName());
                            Map<Long, ITaskTrackable> mapOfTasks = issueTrackerService
                                    .getIssueTrackerTasksByProjectName(issueTrackerProject.getProjectName());

                            if (mapOfTasks != null) {

                                for (ITaskTrackable taskTrackable : mapOfTasks.values()) {
                                    key++;
                                    mapOfTasksFromAllProjectsIncludedInTiTAProject.put(key, taskTrackable);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public Map<Long, ITaskTrackable> getMapOfTasksFromAllProjectsIncludedInTiTAProject() {
        return mapOfTasksFromAllProjectsIncludedInTiTAProject;
    }

    /** {@inheritDoc} */
    @Override
    public Map<Long, ITaskTrackable> sortingTasksByIssueStatus(IssueStatus status) {

        Map<Long, ITaskTrackable> taskGroupByStatus = new TreeMap<Long, ITaskTrackable>();
        Long key = 0L;

        for (ITaskTrackable task : mapOfTasksFromAllProjectsIncludedInTiTAProject.values()) {
            if (task.getStatus().equals(status)) {
                key++;
                taskGroupByStatus.put(key, task);
            }
        }

        log.debug("Return all task with the specified issue status: " + status);
        return taskGroupByStatus;
    }

    /** {@inheritDoc} */
    @Override
    public Map<Long, ITaskTrackable> sortingTasksByIssueTracker(String url) {
        Map<Long, ITaskTrackable> taskGroupByIssueTrackerUrl = new TreeMap<Long, ITaskTrackable>();
        Long key = 0L;

        for (ITaskTrackable task : mapOfTasksFromAllProjectsIncludedInTiTAProject.values()) {
            // String url2 = task.getProject().getUrl().toString();

            if (task.getProject().getUrl().toString().startsWith(url)) {
                key++;
                taskGroupByIssueTrackerUrl.put(key, task);
            }
        }

        log.debug("Return all task with the specified issue tracker url: " + url);
        return taskGroupByIssueTrackerUrl;
    }

    /** {@inheritDoc} */
    @Override
    public void assignTask(Long taskId) {
        issueTrackerService.assignTask(taskId);
    }

    /** {@inheritDoc} */
    @Override
    public void closeTask(Long taskId) {
        issueTrackerService.closeTask(taskId);
    }

    /** {@inheritDoc} */
    @Override
    public List<UserProjectTaskEffort> getPerformanceOfPersonView(TiTAProject project, TiTAUser user,
            String loggedInUsername) throws ProjectNotFoundException {
        List<UserProjectTaskEffort> popView = new ArrayList<UserProjectTaskEffort>();

        List<IssueTrackerTask> issueTrackerTasks = issueTrackerTaskDao.findIssueTrackerTasksforUserProject(project
                .getId(), user.getId());
        for (IssueTrackerTask t : issueTrackerTasks) {
            popView.add(new UserProjectTaskEffort("#" + t.getIsstTaskId(), "", timeEffortDao
                    .findEffortsSumForIssueTrackerTasks(project.getId(), user.getId(), t.getId())));
        }

        // get all titatasks with their efforts sum
        List<TiTATask> titaTasks = titaTaskDao.findTiTATasksforUserProject(project, user);
        for (TiTATask t : titaTasks) {
            popView.add(new UserProjectTaskEffort("T" + t.getId(), t.getDescription(), timeEffortDao
                    .findEffortsSumForTiTATasks(project.getId(), user.getId(), t.getId())));
        }

        return popView;
    }

    public void setIssueTrackerDao(IGenericHibernateDao<IssueTracker, Long> issueTrackerDao) {
        this.issueTrackerDao = issueTrackerDao;
    }

    public void setTitaUserDao(IUserDAO titaUserDao) {
        this.titaUserDao = titaUserDao;
    }

    public void setTitaProjectDao(ITiTAProjectDao titaProjectDao) {
        this.titaProjectDao = titaProjectDao;
    }

    public void setIssueTrackerTaskDao(IIssueTrackerTaskDao issueTrackerTaskDao) {
        this.issueTrackerTaskDao = issueTrackerTaskDao;
    }

    public void setTitaTaskDao(ITiTATaskDao titaTaskDao) {
        this.titaTaskDao = titaTaskDao;
    }

    public IEffortDao getTimeEffortDao() {
        return timeEffortDao;
    }

    public void setTimeEffortDao(IEffortDao timeEffortDao) {
        this.timeEffortDao = timeEffortDao;
    }
}

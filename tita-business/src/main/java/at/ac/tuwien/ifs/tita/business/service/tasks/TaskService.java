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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.dao.issuetracker.IssueTrackerDao;
import at.ac.tuwien.ifs.tita.dao.issuetracker.task.IssueTrackerTaskDao;
import at.ac.tuwien.ifs.tita.dao.project.TiTAProjectDao;
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

    private TiTAProjectDao titaProjectDao;

    private TiTAProject titaProject;
    private TiTAUser titaUser;

    private IssueTrackerTaskDao issueTrackerTaskDao;
    private TiTATaskDao titaTaskDao;
    private IssueTrackerDao issueTrackerDao;

    private Map<Long, ITaskTrackable> mapOfTasksFromAllProjectsIncludedInTiTAProject = new TreeMap<Long, ITaskTrackable>();

    private IIssueTrackerService issueTrackerService;

    // Logging is not really useful in the debug mode, because of the mantis
    // webservice logging.
    private final Logger log = LoggerFactory.getLogger(TaskService.class);

    public TaskService() {

    }

    /** {@inheritDoc} */
    @Override
    public Map<Long, TiTATask> getTiTATasks(TiTAProject pojectTita)
            throws PersistenceException {
        return issueTrackerTaskDao.getTiTATasksForTitaProject(pojectTita);
    }

    /** {@inheritDoc} */
    @Override
    public IssueTrackerTask saveIssueTrackerTask(
            IssueTrackerTask issueTrackerTask) throws PersistenceException {
        return issueTrackerTaskDao.save(issueTrackerTask);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteIssueTrackerTask(IssueTrackerTask issueTrackerTask)
            throws PersistenceException {
        issueTrackerTaskDao.delete(issueTrackerTask);
    }

    /** {@inheritDoc} */
    @Override
    public IssueTrackerTask getIssueTrackerTaskById(Long id)
            throws PersistenceException {
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
    public IssueTracker saveIssueTracker(IssueTracker issueTracker)
            throws PersistenceException {
        return issueTrackerDao.save(issueTracker);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteIssueTracker(IssueTracker issueTracker)
            throws PersistenceException {
        issueTrackerDao.delete(issueTracker);
    }

    /** {@inheritDoc} */
    @Override
    public IssueTracker getIssueTrackerById(Long id)
            throws PersistenceException {
        return issueTrackerDao.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public void fetchTaskFromIssueTrackerProjects(Long projectTitaId,
            Long userTitaId) throws ProjectNotFoundException {

        titaProject = titaProjectDao.findById(projectTitaId);

        if (titaProject == null) {
            log
                    .debug("TaskService - fetchTaskFromIssueTrackerProjects: Project not found.");
            throw new ProjectNotFoundException("Project not found.");
        }
        mapOfTasksFromAllProjectsIncludedInTiTAProject.clear();

        titaUser = titaUserDao.findById(userTitaId);

        Long key = 0L;

        for (IssueTrackerLogin login : titaUser.getIssueTrackerLogins()) {
            log.debug("Take issue tracker login: " + login.getId()
                    + " to use the issue tracker service.");
            issueTrackerService = new IssueTrackerService(login);

            Map<Long, IProjectTrackable> mapOfProjects = issueTrackerService
                    .getProjects();

            log.info("Check all accessible projects from the Issue Tracker: "
                    + login.getIssueTracker().getUrl());
            if (this.titaProject.getIssueTrackerProjects() != null
                    && mapOfProjects != null && mapOfProjects.values() != null) {
                for (IssueTrackerProject issueTrackerProject : titaProject
                        .getIssueTrackerProjects()) {

                    for (IProjectTrackable projectTrackable : mapOfProjects
                            .values()) {

                        if (projectTrackable.getId().equals(
                                issueTrackerProject.getIsstProjectId())) {

                            log.info("Updating the project before fetching: "
                                    + projectTrackable.getName());
                            issueTrackerService.updateProject(projectTrackable);

                            log.info("Fetching tasks for the project: "
                                    + projectTrackable.getName());
                            Map<Long, ITaskTrackable> mapOfTasks = issueTrackerService
                                    .getIssueTrackerTasksByProjectName(issueTrackerProject
                                            .getProjectName());

                            if (mapOfTasks != null) {

                                for (ITaskTrackable taskTrackable : mapOfTasks
                                        .values()) {
                                    key++;
                                    mapOfTasksFromAllProjectsIncludedInTiTAProject
                                            .put(key, taskTrackable);
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
    public Map<Long, ITaskTrackable> sortingTasksByIssueStatus(
            IssueStatus status) {

        Map<Long, ITaskTrackable> taskGroupByStatus = new TreeMap<Long, ITaskTrackable>();
        Long key = 0L;

        for (ITaskTrackable task : mapOfTasksFromAllProjectsIncludedInTiTAProject
                .values()) {
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

        for (ITaskTrackable task : mapOfTasksFromAllProjectsIncludedInTiTAProject
                .values()) {
            // String url2 = task.getProject().getUrl().toString();

            if (task.getProject().getUrl().toString().startsWith(url)) {
                key++;
                taskGroupByIssueTrackerUrl.put(key, task);
            }
        }

        log.debug("Return all task with the specified issue tracker url: "
                + url);
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

    public void setIssueTrackerDao(IssueTrackerDao issueTrackerDao) {
        this.issueTrackerDao = issueTrackerDao;
    }

    public void setTitaUserDao(UserDAO titaUserDao) {
        this.titaUserDao = titaUserDao;
    }

    public void setTitaProjectDao(TiTAProjectDao titaProjectDao) {
        this.titaProjectDao = titaProjectDao;
    }

    public void setIssueTrackerTaskDao(IssueTrackerTaskDao issueTrackerTaskDao) {
        this.issueTrackerTaskDao = issueTrackerTaskDao;
    }

    public void setTitaTaskDao(TiTATaskDao titaTaskDao) {
        this.titaTaskDao = titaTaskDao;
    }

}

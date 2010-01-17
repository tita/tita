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
package at.ac.tuwien.ifs.tita.issuetracker.issue.service;

import java.util.Map;
import java.util.TreeMap;

import org.mantisbt.connect.MCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueStatus;
import at.ac.tuwien.ifs.tita.issuetracker.exceptions.ProjectNotFoundException;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IIssueTrackerDao;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IProjectTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.mantis.dao.IssueTrackerMantisDao;

/**
 * The IssueTrackerService manage the synchronization between mantis and TiTA.
 * Therefore are two methods that starts the update functions. In addition you
 * can fetch the updated tasks from a project an status.
 *
 * @author Christoph
 *
 */
public class IssueTrackerService implements IIssueTrackerService {
    private static DispatcherThread dispatcher = null;
    private static WorkerThread worker = null;

    private IIssueTrackerDao dao;
    private Map<Long, IProjectTrackable> projects;
    private final IssueTrackerLogin loggedUser;

    private final Logger log = LoggerFactory.getLogger(IssueTrackerService.class);

    public IssueTrackerService(IssueTrackerLogin login) {
        loggedUser = login;
        dao = new IssueTrackerMantisDao(login);
        projects = dao.findAccessibleProjects();
    }

    public IIssueTrackerDao getIssueTrackerDao() {
        return dao;
    }

    public IssueTrackerLogin getLoggedUser() {
        return loggedUser;
    }

    public void setProjects(Map<Long, IProjectTrackable> projects) {
        this.projects = projects;
    }

    public Map<Long, IProjectTrackable> getProjects() {
        return projects;
    }

    /** {@inheritDoc} */
    @Override
    public void updateAll() {
        dao = new IssueTrackerMantisDao(loggedUser);

        log.debug("Fetching all projects");
        projects = dao.findAccessibleProjects();

        dispatcher = new DispatcherThread(projects, loggedUser);
        dispatcher.run();
    }

    /** {@inheritDoc} */
    @Override
    public void updateProject(IProjectTrackable project) {

        log.debug("Fetching all tasks for the project: " + project.getName());
        Map<Long, ITaskTrackable> tasklist = dao.findAllTasksForProject(project.getId());

        for (ITaskTrackable task : tasklist.values()) {
            task.setProject(project);
        }

        project.setTasks(tasklist);

        projects.put(project.getId(), project);
    }

    /** {@inheritDoc} */
    @Override
    @Deprecated
    public void updateProject(Long projectId) {
        worker = new WorkerThread(projectId, loggedUser);
        worker.start();
    }

    /** {@inheritDoc} */
    @Override
    public Map<Long, ITaskTrackable> getIssueTrackerTasks(IProjectTrackable project,
            IssueStatus status) throws ProjectNotFoundException {

        if (project == null) {
            log.error("Project not found.");
            throw new ProjectNotFoundException("No project was set.");
        }

        Map<Long, ITaskTrackable> tasks = project.getTasks();
        Map<Long, ITaskTrackable> statusTasks = new TreeMap<Long, ITaskTrackable>();

        if (tasks == null) {
            return null;
        }

        for (ITaskTrackable task : tasks.values()) {

            // Maybe user check is also needed
            if (task.getStatus().equals(status)) {
                statusTasks.put(task.getId(), task);
            }
        }

        if (statusTasks.size() == 0) {
            return null;
        }

        return statusTasks;
    }

    /** {@inheritDoc} */
    @Override
    public Map<Long, ITaskTrackable> getIssueTrackerTasks(IProjectTrackable project)
        throws ProjectNotFoundException {

        if (project == null) {
            log.error("Project not found.");
            throw new ProjectNotFoundException("No project was set.");
        }

        log.debug("Fetching Tasks from project over the webservice: " + project.getName());
        return dao.findAllTasksForProject(project.getId());
    }

    /** {@inheritDoc} */
    @Override
    public Map<Long, ITaskTrackable> getIssueTrackerTasksByProjectId(Long projectId)
        throws ProjectNotFoundException {

        if (projectId == null) {
            log.error("Project not found.");
            throw new ProjectNotFoundException("No project was set.");
        }

        for (IProjectTrackable project : projects.values()) {
            if (project.getId().equals(projectId)) {
                log.debug("Fetching Tasks from project: " + project.getName());
                return project.getTasks();
            }
        }

        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Map<Long, ITaskTrackable> getIssueTrackerTasksByProjectName(String projectName)
        throws ProjectNotFoundException {

        if (projectName == null) {
            log.error("Project not found.");
            throw new ProjectNotFoundException("No project was set.");
        }

        for (IProjectTrackable project : projects.values()) {
            if (project.getName().equals(projectName)) {
                log.debug("Fetching Tasks from project: " + project.getName());
                return project.getTasks();
            }
        }

        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void assignTask(Long taskId) {
        try {
            dao.assignTask(taskId);
        } catch (MCException e) {
            log.error("Assign Task was not succesful.");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void closeTask(Long taskId) {
        try {
            dao.closeTask(taskId);
        } catch (MCException e) {
            log.error("Closing Task was not succesful.");
        }
    }
}

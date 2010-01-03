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


    public IssueTrackerService(IssueTrackerLogin login) {
        this.loggedUser = login;
        this.dao = new IssueTrackerMantisDao(login);
        this.projects = this.dao.findAccessibleProjects();
    }

    public IIssueTrackerDao getIssueTrackerDao() {
        return this.dao;
    }

    public void setProjects(Map<Long, IProjectTrackable> projects) {
        this.projects = projects;
    }

    public Map<Long, IProjectTrackable> getProjects() {
        return this.projects;
    }

    /** {@inheritDoc} */
    @Override
    public void updateAll() {
        this.dao = new IssueTrackerMantisDao(this.loggedUser);
        this.projects = this.dao.findAccessibleProjects();

        dispatcher = new DispatcherThread(this.projects, this.loggedUser);
        dispatcher.run();
    }

    /** {@inheritDoc} */
    @Override
    public void updateProject(IProjectTrackable project) {
        // worker = new WorkerThread(project, this.loggedUser);
        // worker.start();

        Map<Long, ITaskTrackable> tasklist = this.dao.findAllTasksForProject(project.getId());

        for (ITaskTrackable task : tasklist.values()) {
            task.setProject(project);
        }

        project.setTasks(tasklist);
    }

    /** {@inheritDoc} */
    @Override
    @Deprecated
    public void updateProject(Long projectId) {
        worker = new WorkerThread(projectId, this.loggedUser);
        worker.start();
    }

    /** {@inheritDoc} */
    @Override
    public Map<Long, ITaskTrackable> getIssueTrackerTasks(IProjectTrackable project,
            IssueStatus status)
            throws ProjectNotFoundException {

        if (project == null) {
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
            throw new ProjectNotFoundException("No project was set.");
        }

        return project.getTasks();
    }

    // /** {@inheritDoc} */
    // @Override
    // @Deprecated
    // public Map<Long, ITaskTrackable> getIssueTrackerTasksByProjectId(Long
    // projectId)
    // throws ProjectNotFoundException {
    //
    // if (projectId == null) {
    // throw new ProjectNotFoundException("No project was set.");
    // }
    //
    // return this.dao.findAllTasksForProject(projectId);
    // }

    /** {@inheritDoc} */
    @Override
    public Map<Long, ITaskTrackable> getIssueTrackerTasksByProjectId(Long projectId)
            throws ProjectNotFoundException {

        if (projectId == null) {
             throw new ProjectNotFoundException("No project was set.");
        }

        for (IProjectTrackable project : this.projects.values()) {
            if (project.getId().equals(projectId)) {
                return project.getTasks();
            }
        }

        return null;
    }
}

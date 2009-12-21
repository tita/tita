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

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.dao.issuetracker.task.IssueTrackerTaskDao;
import at.ac.tuwien.ifs.tita.dao.titatask.TiTATaskDao;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueStatus;
import at.ac.tuwien.ifs.tita.issuetracker.exceptions.ProjectNotFoundException;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.issue.service.IIssueTrackerService;

/**
 * The TaskService combines the IssueTrackerService, which fetches and manage
 * the data from the issue trackers, and encapsulates all Task-concerning
 * Database operations.
 *
 * @author Christoph
 *
 */
public class TaskService implements ITaskService {


    private IssueTrackerTaskDao issueTrackerTaskDao;
    private TiTATaskDao titaTaskDao;

    private Map<Long, ITaskTrackable> mapOfTasksFromAllProjectsIncludedInTiTAProject;
    private TiTAProject project;
    private List<IssueTrackerLogin> logins;

    private IIssueTrackerService issueTrackerService;

    /** {@inheritDoc} */
    @Override
    public Map<Long, TiTATask> getTiTATasks(TiTAProject titaProject) throws TitaDAOException {
        return this.issueTrackerTaskDao.getTiTATasksForTitaProject(titaProject);
    }

    /** {@inheritDoc} */
    @Override
    public IssueTrackerTask saveIssueTrackerTask(IssueTrackerTask issueTrackerTask)
            throws TitaDAOException {
        return this.issueTrackerTaskDao.save(issueTrackerTask);
    }

    /** {@inheritDoc} */
    @Override
    public TiTATask saveTiTATask(TiTATask titaTask) throws TitaDAOException {
        return this.titaTaskDao.save(titaTask);
    }

    /** {@inheritDoc} */
    @Override
    public Map<Long, ITaskTrackable> getIssueTrackerTasksGroupByIssueStatus(String url) {

        Map<Long, ITaskTrackable> taskGroupByIssueTrackerUrl = new TreeMap<Long, ITaskTrackable>();
        // Long key = 0L;
        //
        // for (ITaskTrackable task :
        // this.mapOfTasksFromAllProjectsIncludedInTiTAProject.values()) {
        // if (task.getProject().getUrl().equals(url)) {
        // key++;
        // taskGroupByIssueTrackerUrl.put(key, task);
        // }
        // }
        return taskGroupByIssueTrackerUrl;
    }

    /** {@inheritDoc} */
    @Override
    public Map<Long, ITaskTrackable> getIssueTrackerTasksGroupByIssueTracker(IssueStatus status) {

        Map<Long, ITaskTrackable> taskGroupByStatus = new TreeMap<Long, ITaskTrackable>();
        // Long key = 0L;
        //
        // for (ITaskTrackable task :
        // this.mapOfTasksFromAllProjectsIncludedInTiTAProject.values()) {
        // if (task.getIssueTrackerType().equals(status)) {
        // key++;
        // taskGroupByStatus.put(key, task);
        // }
        // }
        return taskGroupByStatus;
    }

    public TiTAProject getProject() {
        return this.project;
    }

    public void setProject(TiTAProject project) {
        this.project = project;
    }

    public void setTitaTaskDao(TiTATaskDao titaTaskDao) {
        this.titaTaskDao = titaTaskDao;
    }

    public void setIssueTrackerTaskDao(IssueTrackerTaskDao issueTrackerTaskDao) {
        this.issueTrackerTaskDao = issueTrackerTaskDao;
    }

    /**
     * Method to fetch all tasks for the tita project and the added issue
     * tracker projects.
     *
     * @throws ProjectNotFoundException
     *             pnfe - if a project is null
     */
    public void fetchTaskFromIssueTrackerProjects()
            throws ProjectNotFoundException {
        //
        // Long key = 0L;
        //
        // for (IssueTrackerLogin login : this.logins) {
        // this.issueTrackerService = new IssueTrackerService(login);
        //
        // Map<Long, IProjectTrackable> mapOfProjects =
        // this.issueTrackerService.getProjects();
        //
        // for (IssueTrackerProject issueTrackerProject :
        // this.project.getIssueTrackerProjects()) {
        // if (mapOfProjects.containsValue(issueTrackerProject)) {
        // // TODO Flag setzen
        // Map<Long, ITaskTrackable> mapOfTasks = this.issueTrackerService
        // .getIssueTrackerTasksByProjectId(issueTrackerProject.getProjectId());
        //
        // for (ITaskTrackable taskTrackable : mapOfTasks.values()) {
        // key++;
        // this.mapOfTasksFromAllProjectsIncludedInTiTAProject.put(key,
        // taskTrackable);
        // }
        // }
        // }
        // }
    }

    public void setLogins(List<IssueTrackerLogin> logins) {
        this.logins = logins;
    }

    public List<IssueTrackerLogin> getLogins() {
        return this.logins;
    }

}

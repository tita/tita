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

import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueStatus;
import at.ac.tuwien.ifs.tita.issuetracker.exceptions.ProjectNotFoundException;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IIssueTrackerDao;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IProjectTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.mantis.dao.IssueTrackerMantisDao;

public class TaskService implements ITaskService {

    private IIssueTrackerDao dao;
    private Map<Long, IProjectTrackable> projects;

    private static DispatcherThread dispatcher = null;
    private static WorkerThread worker = null;

    public TaskService() {
        this.dao = new IssueTrackerMantisDao();
        this.projects = this.dao.getAllAccessibleProjects();
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

    @Override
    public void updateAll() {
        this.dao = new IssueTrackerMantisDao();
        this.projects = this.dao.getAllAccessibleProjects();

        dispatcher = new DispatcherThread(this.projects);
        dispatcher.start();
        dispatcher.close();

    }

    @Override
    public void updateProject(IProjectTrackable project) {
        worker = new WorkerThread(project);
        worker.start();
    }

    @Override
    public Map<Long, ITaskTrackable> getTasks(IProjectTrackable project, IssueStatus status)
            throws ProjectNotFoundException {

        if (project == null)
            throw new ProjectNotFoundException("No project was set.");

        Map<Long, ITaskTrackable> tasks = project.getTasks();
        Map<Long, ITaskTrackable> statusTasks = new TreeMap<Long, ITaskTrackable>();

        for (ITaskTrackable task : tasks.values()) {

            // Maybe user check is also needed
            if (task.getStatus().equals(status)) {
                statusTasks.put(task.getId(), task);
            }
        }

        if (statusTasks.size() == 0)
            return null;

        return statusTasks;
    }
}

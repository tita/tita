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

import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueStatus;
import at.ac.tuwien.ifs.tita.issuetracker.exceptions.ProjectNotFoundException;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IProjectTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;

/**
 * The IssueTrackerService manage the synchronization between mantis and TiTA.
 * Therefore are two methods that starts the update functions. In addition you
 * can fetch the updated tasks from a project an status.
 *
 * @author Christoph
 *
 */
public interface IIssueTrackerService {

    /**
     * Method returns a map of all tasks for the chosen project and status. If
     * the method returns null, no elements found for the project or status.
     *
     * @param project - the project, from where the issues are wanted
     * @param status - defines the state of the issues, that are supplied.
     * @return a map of tasks from the chosen project and status.
     * @throws ProjectNotFoundException if project is null
     */
    Map<Long, ITaskTrackable> getIssueTrackerTasks(IProjectTrackable project,
            IssueStatus status) throws ProjectNotFoundException;

    /**
     * Method returns a map of all tasks for the chosen project. If the method
     * returns null, no elements found for the project.
     *
     * @param project
     *            - the project, from where the tasks are wanted
     * @return a map of tasks from the chosen project.
     * @throws ProjectNotFoundException
     *             if project is null
     */
    Map<Long, ITaskTrackable> getIssueTrackerTasks(IProjectTrackable project)
        throws ProjectNotFoundException;


    /**
     * Method returns a map of all tasks for the chosen project. If the method
     * returns null, no elements found for the project.
     *
     * @param projectId
     *            - the id of the project, from where the tasks are wanted
     * @return a map of tasks from the chosen project.
     * @throws ProjectNotFoundException
     *             if project is null
     */
    Map<Long, ITaskTrackable> getIssueTrackerTasksByProjectId(Long projectId)
        throws ProjectNotFoundException;

    /**
     * Method returns a map of all tasks for the chosen project. If the method
     * returns null, no elements found for the project.
     *
     * @param projectName
     *            - the name of the project, from where the tasks are wanted
     * @return a map of tasks from the chosen project.
     * @throws ProjectNotFoundException
     *             if project is null
     */
    Map<Long, ITaskTrackable> getIssueTrackerTasksByProjectName(String projectName)
        throws ProjectNotFoundException;

    /**
     * Method manages a update of a single project.
     *
     * @param project
     *            - the chosen project
     */
    void updateProject(IProjectTrackable project);

    /**
     * Method manages a update of a single project.
     *
     * @param projectId
     *            - the project of the chosen project.
     */
    void updateProject(Long projectId);

    /**
     * Method to update all projects.
     */
    void updateAll();

    /**
     * Method to fetch all accessible projects.
     *
     * @return projects
     */
    Map<Long, IProjectTrackable> getProjects();

    /**
     * Method to assign a task in the issue tracker.
     *
     * @param taskId
     *            - the id of the task that should be assigned.
     */
    void assignTask(Long taskId);

    /**
     * Method to close a task in the issue tracker.
     *
     * @param taskId
     *            - the id of the task that should be closed.
     */
    void closeTask(Long taskId);
    
    
    /**
     * Find Project By Projectname.
     * @param name - project name
     * @return found project
     */
    IProjectTrackable getProjectByProjectName(String name);



}

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

import javax.persistence.PersistenceException;

import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;
import at.ac.tuwien.ifs.tita.entity.util.UserProjectTaskEffort;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueStatus;
import at.ac.tuwien.ifs.tita.issuetracker.exceptions.ProjectNotFoundException;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;

/**
 * The TaskService combines the IssueTrackerService, which fetches and manage
 * the data from the issue trackers, and encapsulates all Task-concerning
 * Database operations.
 *
 * @author Christoph
 *
 */
@Transactional
public interface ITaskService {

    /**
     * Method to save a tita task that is created to identify the self defined
     * effort.
     *
     * @param titaTask - task for the effort
     * @return titaTask - the saved tita task.
     * @throws PersistenceException - if parameter is null or another Exception
     *         is thrown
     */
    TiTATask saveTiTATask(TiTATask titaTask) throws PersistenceException;

    /**
     * Method to delete a tita task.
     *
     * @param titaTask - the tita task to be deleted
     * @throws PersistenceException - if parameter is null or another Exception
     *         is thrown
     */
    void deleteTiTATask(TiTATask titaTask) throws PersistenceException;

    /**
     * Returns a specific tita task found to the id given.
     *
     * @param id the unique identifier of an tita task
     * @throws PersistenceException if no tita task was found or another
     *         Exception is thrown
     * @return the specified tita task if id was found
     */
    TiTATask getTiTATaskById(Long id) throws PersistenceException;

    /**
     * Method to save a issue tracker task to Tita.
     *
     * @param issueTrackerTask - issue tracker task
     * @return issueTrackerTask - the saved issue tracker task.
     * @throws PersistenceException - if parameter is null or another Exception
     *         is thrown
     */
    IssueTrackerTask saveIssueTrackerTask(IssueTrackerTask issueTrackerTask) throws PersistenceException;

    /**
     * Method to delete a issue tracker task.
     *
     * @param issueTrackerTask - the issue tracker task to be deleted
     * @throws PersistenceException - if parameter is null or another Exception
     *         is thrown
     */
    void deleteIssueTrackerTask(IssueTrackerTask issueTrackerTask) throws PersistenceException;

    /**
     * Returns a specific issue tracker task found to the id given.
     *
     * @param id the unique identifier of an issue tracker task
     * @throws PersistenceException if no issue tracker task was found or
     *         another Exception is thrown
     * @return the specified issue tracker task if id was found
     */
    IssueTrackerTask getIssueTrackerTaskById(Long id) throws PersistenceException;

    /**
     * Method to save a issue tracker.
     *
     * @param issueTracker - issue tracker configuration
     * @return the issue tracker to be saved.
     * @throws PersistenceException - if parameter is null or another Exception
     *         is thrown
     */
    IssueTracker saveIssueTracker(IssueTracker issueTracker) throws PersistenceException;

    /**
     * Method to delete a issue tracker.
     *
     * @param issueTracker - the issue tracker to be deleted
     * @throws PersistenceException - if parameter is null or another Exception
     *         is thrown
     */
    void deleteIssueTracker(IssueTracker issueTracker) throws PersistenceException;

    /**
     * Returns a specific issue tracker found to the id given.
     *
     * @param id the unique identifier of an issue tracker
     * @throws PersistenceException if no issue tracker was found or another
     *         Exception is thrown
     * @return the specified issue tracker if id was found
     */
    IssueTracker getIssueTrackerById(Long id) throws PersistenceException;

    // TODO: will the procedure be needed?
    // /**
    // * Method to get all Tasks that were produced for self defined time
    // efforts
    // * for a specific project.
    // *
    // * @param titaProject
    // * - a tita project
    // * @return a map of tita tasks
    // * @throws PersistenceException
    // * - if Parameter is null or another Exception is thrown
    // */
    // Map<Long, TiTATask> getTiTATasks(TiTAProject titaProject) throws
    // PersistenceException;

    /**
     * Method to fetch all tasks for the tita project and the added issue
     * tracker projects using a other thread.
     *
     * @param projectTitaId
     *            - id of the selected tita project
     * @param userTitaId
     *            - id of the logged in user
     * @throws ProjectNotFoundException
     *             pnfe - if a project is null
     */
    void fetchTaskFromIssueTrackerProjects(Long projectTitaId, Long userTitaId) throws ProjectNotFoundException;

    /**
     * Method to get all Tasks from all issue trakcer projects that are included
     * in a tita project.
     *
     * @return a map of tasks from all projects included in the tita project.
     */
    Map<Long, ITaskTrackable> getMapOfTasksFromAllProjectsIncludedInTiTAProject();

    /**
     * Method to fetch all tasks from issue tracker group by issue tracker.
     *
     * @param url - parameter for grouping
     * @return map of tasks for a issue tracking tool url
     */
    Map<Long, ITaskTrackable> sortingTasksByIssueTracker(String url);

    /**
     * Method to fetch all tasks from issue tracker tasks group by issue status.
     *
     * @param status - parameter for grouping
     * @return map of tasks for a issue tracker
     */
    Map<Long, ITaskTrackable> sortingTasksByIssueStatus(IssueStatus status);

    /**
     * Method to assign a task in the issue tracker. Tests are already done for
     * the issuetrackerservice.
     *
     * @param taskId - the id of the task that should be assigned.
     */
    void assignTask(Long taskId);

    /**
     * Method to close a task in the issue tracker. Tests are already done for
     * the issuetrackerservice.
     *
     * @param taskId - the id of the task that should be closed.
     */
    void closeTask(Long taskId);

    /**
     * Gets a IssueTrackerTask from the IssueTracker.
     *
     * @param taskId - issueTracker task Id
     * @param projectId - issueTracker project Id
     * @param issueTrackerId - issueTracker Id
     * @return found IssueTrackerTask
     */
    ITaskTrackable getIssueTrackerTaskById(Long taskId, Long projectId, Long issueTrackerId);

    /**
     * Returns the view for performance of performacne evaluation, with data
     * from issuetracker tasks and tita tasks and the sum of their efforts.
     *
     * @param project selected project
     * @param user selected user
     *
     * @return tasks with their sum of efforts.
     */
    List<UserProjectTaskEffort> getPerformanceOfPersonView(TiTAProject project, TiTAUser user);

    /**
     * Starts the update thread.
     */
    void startUpdate();

    /**
     * Stops the update thread.
     */
    void stopUpdate();
}

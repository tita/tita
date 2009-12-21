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

import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueStatus;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;

/**
 * The TaskService combines the IssueTrackerService, which fetches and manage
 * the data from the issue trackers, and encapsulates all Task-concerning
 * Database operations.
 *
 * @author Christoph
 *
 */
public interface ITaskService {

    /**
     * Method to save a issue tracker task to Tita.
     * 
     * @param issueTrackerTask
     *            - issue tracker task
     * @return issueTrackerTask - the saved issue tracker task.
     * @throws TitaDAOException
     *             - titaDao
     */
    IssueTrackerTask saveIssueTrackerTask(IssueTrackerTask issueTrackerTask)
            throws TitaDAOException;

    /**
     * Method to save a tita task that is created to identify the self defined
     * effort.
     * 
     * @param titaTask
     *            - task for the effort
     * @return titaTask - the saved tita task.
     * @throws TitaDAOException
     *             - titaDao
     */
    TiTATask saveTiTATask(TiTATask titaTask) throws TitaDAOException;

    /**
     * Method to get all Tasks that were produced for self defined time efforts
     * for a specific project.
     *
     * @param titaProject
     *            - a tita project
     * @return a map of tita tasks
     * @throws TitaDAOException
     *             - if Parameter is null or another Exception is thrown
     */
    Map<Long, TiTATask> getTiTATasks(TiTAProject titaProject)
            throws TitaDAOException;

    /**
     * Method to fetch all tasks from issue tracker group by issue tracker.
     *
     * @param url
     *            - parameter for grouping
     * @return map of tasks for a issue tracking tool url
     */
    Map<Long, ITaskTrackable> getIssueTrackerTasksGroupByIssueStatus(String url);

    /**
     * Method to fetch all tasks from issue tracker group by issue status.
     *
     * @param status
     *            - parameter for grouping
     * @return map of tasks for a issue tracker
     */
    Map<Long, ITaskTrackable> getIssueTrackerTasksGroupByIssueTracker(IssueStatus status);
}

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
package at.ac.tuwien.ifs.tita.issuetracker.interfaces;

import java.util.Map;

import org.mantisbt.connect.MCException;

/**
 * The interface describes the view on comment objects from the integrated issue
 * tracker. The data from the issue tracker is mapped in the implementation of
 * the interface.
 * 
 * @author Karin
 * 
 */
public interface IIssueTrackerDao {

    /**
     * Method to find all projects for a User.
     * 
     * @param username
     *            - name of the user, whos projects should be found
     * @return list of found projects
     */
    Map<Long, IProjectTrackable> findAccessibleProjects();

    /**
     * Method to find a trackableProject by the projectId.
     * 
     * @param projectId
     *            - id of the project to find
     * @return project, if one was found, null otherwise
     */
    IProjectTrackable findProject(Long projectId);

    /**
     * Method to find a TrackableProject by the projectName.
     * 
     * @param projectName
     *            - name of the Project to find
     * @return project, if one was found, null otherwise
     */
    IProjectTrackable findProject(String projectName);

    /**
     * Method to find all TrackableTaks of a project, specified by the
     * projectId.
     * 
     * @param projectId
     *            - id of the project, for which the tasks should be found
     * @return map of all found tasks for the project
     */
    Map<Long, ITaskTrackable> findAllTasksForProject(Long projectId);

    /**
     * Method to find a trackableTask by the taskId.
     * 
     * @param taskId
     *            - id of the task to find
     * @return task, if one was found, null otherwise
     */
    ITaskTrackable findTask(Long taskId);

    /**
     * Method to find all TrackableComments of a task, specified by the taskId.
     * 
     * @param taskId
     *            - id of the task, for which the comments should be found
     * @return map of all found comments
     */
    Map<Long, ICommentTrackable> findAllCommentsForTask(Long taskId);

    /**
     * Closes the Task with the specified taskId.
     * 
     * @param taskId
     *            - id of the task to close
     * @return true, if closing was successful, false if an error ocurred
     * @exception MCException
     *                - if closing fails
     */
    void closeTask(long taskId) throws MCException;
}

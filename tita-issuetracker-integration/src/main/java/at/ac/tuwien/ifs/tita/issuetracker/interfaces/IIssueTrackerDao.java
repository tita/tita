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

import java.util.List;

import org.mantisbt.connect.MCException;

/**
 * The interface describes the view on comment objects from the integrated issue tracker.
 * The data from the issue tracker is mapped in the implementation of the interface.
 * 
 * @author Karin
 *
 */
public interface IIssueTrackerDao {
    /**
     * Method to find a trackableProject by the projectId.
     * @param projectId - id of the project to find
     * @return project, if one was found, null otherwise
     */
    IIsProjectTrackable findProject(long projectId);
    
    /**
     * Method to find a TrackableProject by the projectName.
     * @param projectName - name of the Project to find
     * @return project, if one was found, null otherwise
     */
    IIsProjectTrackable findProject(String projectName);
    
    /**
     * Method to find all TrackableTaks of a project, specified by the projectId.
     * @param projectId - id of the project, for which the tasks should be found
     * @return list of all found tasks
     */
    List<IIsTaskTrackable> findAllTasksForProject(long projectId);
    
    /**
     * Method to find a trackableTask by the taskId.
     * @param taskId - id of the task to find
     * @return task, if one was found, null otherwise
     */
    IIsTaskTrackable findTask(long taskId);
    
    /**
     * Method to find all TrackableComments of a task, specified by the taskId.
     * @param taskId - id of the task, for which the comments should be found
     * @return list of all found comments
     */
    List<IIsCommentTrackable> findAllCommentsForTask(long taskId);
    
    /**
     * Closes the Task with the specified taskId.
     * @param taskId - id of the task to close
     * @return true, if closing was successful, false if an error ocurred
     * @exception MCException - if closing fails
     */
    void closeTask(long taskId) throws MCException;
    
}

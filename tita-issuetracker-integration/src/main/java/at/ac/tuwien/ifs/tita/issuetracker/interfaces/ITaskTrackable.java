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

import java.util.Date;
import java.util.Map;

import at.ac.tuwien.ifs.tita.issuetracker.enums.IssuePriority;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueResolution;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueSeverity;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueStatus;

/**
 * The interface describes the view on issue or bug objects from the integrated
 * issue tracker, how it is called in the different issue trackers. The data
 * from the issue tracker is mapped in the implementation of the interface.
 * 
 * @author Christoph
 * 
 */
public interface ITaskTrackable {

    /**
     * Supplies the identifier of the task.
     * 
     * @return id - identifier of the task
     */
    Long getId();

    /**
     * Sets the summary, a short description of the task.
     * 
     * @param summary
     *            - short description
     */
    void setSummary(String summary);

    /**
     * Returns the summary of the task.
     * 
     * @return summary - short task description
     */
    String getSummary();

    /**
     * Sets the detailed description of the task.
     * 
     * @param description
     *            - detailed description of the task
     */
    void setDescription(String description);

    /**
     * Return the detailed task description.
     * 
     * @return description - detailed description
     */
    String getDescription();

    /**
     * Sets the priority of the task.
     * 
     * @param priority
     *            - uses the enumeration values for setting
     * @see IssuePriority
     */
    void setPriority(IssuePriority priority);

    /**
     * Returns the priority of the task.
     * 
     * @return priority - the priority of the task
     * @see IssuePriority
     */
    IssuePriority getPriority();

    /**
     * Sets the severity of the task.
     * 
     * @param severity
     *            - uses the enumeration values for setting
     * @see IssueSeverity
     */
    void setSeverity(IssueSeverity severity);

    /**
     * Returns the severity of the task.
     * 
     * @return severity - the severity of the task
     * @see IssueSeverity
     */
    IssueSeverity getSeverity();

    /**
     * Sets the resolution of the task.
     * 
     * @param resolution
     *            - uses the enumeration values for setting
     * @see IssueResolution
     */
    void setResolution(IssueResolution resolution);

    /**
     * Returns the resolution of the task.
     * 
     * @return resolution - the resolution of the task
     * @see IssueResolution
     */
    IssueResolution getResolution();

    /**
     * Sets the status of the task.
     * 
     * @param status
     *            - uses the enumeration values for setting
     * @see IssueStatus
     */
    void setStatus(IssueStatus status);

    /**
     * Returns the status of the task.
     * 
     * @return status - the status of the task
     * @see IssueStatus
     */
    IssueStatus getStatus();

    /**
     * Sets the creation time of the task due to the issue tracker.
     * 
     * @param creationTime
     *            - time the task was created
     */
    void setCreationTime(Date creationTime);

    /**
     * Returns the creation time of the task.
     * 
     * @return creationTime - time the task was created
     */
    Date getCreationTime();

    /**
     * Sets the time the last change was updated.
     * 
     * @param lastChange
     *            - the time the last change was updated
     */
    void setLastChange(Date lastChange);

    /**
     * Returns the last change time.
     * 
     * @return lastChange - the time the last change was committed
     */
    Date getLastChange();

    /**
     * Sets the reporter(user of the issue tracker) of the task.
     * 
     * @param reporter
     *            - the author of the task
     */
    void setReporter(String reporter);

    /**
     * Returns the reporter of the task.
     * 
     * @return reporter - author of the task
     */
    String getReporter();

    /**
     * Sets the owner of the task.
     * 
     * @param owner
     *            - the person who has assigned to
     */
    void setOwner(String owner);

    /**
     * Return the owner of the task.
     * 
     * @return owner - person who has assigned to
     */
    String getOwner();

    /**
     * Sets the Id of the project of the task.
     * 
     * @param projectId
     *            - the Id of the project the task is being filed against
     */
    void setProjectId(long projectId);

    /**
     * Returns the projectId the task is being filed against.
     * 
     * @return project - the project the task is being filed against
     * @see IProjectTrackable
     */
    Long getProjectId();

    /**
     * Sets the comments that are added to the task. At the creation time of
     * course null
     * 
     * @param comments
     *            - a map of comments, default null
     * @see ICommentTrackable
     */
    void setComments(Map<Long, ICommentTrackable> comments);
    /**
     * Returns a list of comments added to the task.
     * 
     * @return comments - a map of comments
     * @see ICommentTrackable
     */
    Map<Long, ICommentTrackable> getComments();

}

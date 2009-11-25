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
package at.ac.tuwien.ifs.tita.issuetracker.container;

import java.util.Date;
import java.util.List;

import at.ac.tuwien.ifs.tita.issuetracker.enums.IssuePriority;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueResolution;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueSeverity;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueStatus;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IIsCommentTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IIsTaskTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.time.ITimedTask;

/**
 * The container class for task objects from the integrated issue tracker.
 * 
 * @author Karin
 *
 */
public class IssueTrackerTask implements IIsTaskTrackable, ITimedTask{
    
    private List<IIsCommentTrackable> comments;
    private Date creationTime;
    private String description;
    private Long id;
    private Date lastChange;
    private String owner;
    private IssuePriority priority;
    private Long projectId;
    private String reporter;
    private IssueResolution resolution;
    private IssueSeverity severity;
    private IssueStatus status;
    private String summary;
    private Long startTime;
    private Long duration;
    private Boolean started;
    
    public IssueTrackerTask(Long id, String description, String owner, Date creationTime,
            Date lastChange, IssuePriority priority, Long projectId,
            List<IIsCommentTrackable> comments, String reporter, IssueResolution resolution, 
            IssueSeverity severity,IssueStatus status ,String summary){
        
        this.id = id;
        this.description = description;
        this.owner = owner;
        this.creationTime = creationTime;
        this.lastChange = lastChange;
        this.priority = priority;
        this.projectId = projectId;
        this.comments = comments;
        this.reporter = reporter;
        this.resolution = resolution;
        this.severity = severity;
        this.status = status;
        this.summary = summary;
        this.started = false;
    }
    
    /** {@inheritDoc} */
    public List<IIsCommentTrackable> getComments() {
        return comments;
    }
    /** {@inheritDoc} */
    public Date getCreationTime() {
        return creationTime;
    }
    /** {@inheritDoc} */
    public String getDescription() {
        return description;
    }
    /** {@inheritDoc} */
    public Long getId() {
        return id;
    }
    /** {@inheritDoc} */
    public Date getLastChange() {
        return lastChange;
    }
    /** {@inheritDoc} */
    public String getOwner() {
        return owner;
    }
    /** {@inheritDoc} */
    public IssuePriority getPriority() {
        return priority;
    }
    /** {@inheritDoc} */
    public Long getProjectId() {
        return projectId;
    }
    /** {@inheritDoc} */
    public String getReporter() {
        return reporter;
    }
    /** {@inheritDoc} */
    public IssueResolution getResolution() {
        return resolution;
    }
    /** {@inheritDoc} */
    public IssueSeverity getSeverity() {
        return severity;
    }
    /** {@inheritDoc} */
    public IssueStatus getStatus() {
        return status;
    }
    /** {@inheritDoc} */
    public String getSummary() {
        return summary;
    }
    /** {@inheritDoc} */
    public void setComments(List<IIsCommentTrackable> comments) {
        this.comments = comments;
    }
    /** {@inheritDoc} */
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
    /** {@inheritDoc} */
    public void setDescription(String description) {
        this.description = description; 
    }
    /** {@inheritDoc} */
//    public void setId(Long id) {
//        this.id = id;
//        
//    }
    /** {@inheritDoc} */
    public void setLastChange(Date lastChange) {
        this.lastChange = lastChange;
    }
    /** {@inheritDoc} */
    public void setOwner(String owner) {
        this.owner = owner;
    }
    /** {@inheritDoc} */
    public void setPriority(IssuePriority priority) {
        this.priority = priority;
    }
    /** {@inheritDoc} */
    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
    /** {@inheritDoc} */
    public void setReporter(String reporter) {
        this.reporter = reporter;
    }
    /** {@inheritDoc} */
    public void setResolution(IssueResolution resolution) {
        this.resolution = resolution;
    }
    /** {@inheritDoc} */
    public void setSeverity(IssueSeverity severity) {
        this.severity = severity;
    }
    /** {@inheritDoc} */
    public void setStatus(IssueStatus status) {
        this.status = status;
    }
    /** {@inheritDoc} */
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    /** {@inheritDoc} */
    @Override
    public void start() {
        if(!started){
            startTime = System.currentTimeMillis();
            started = true;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void stop() {
        if(started){
            duration = System.currentTimeMillis() - startTime;
            started = false;
        }
    }

    @Override
    public Long getDuration() {
        return duration;
    }

    @Override
    public Integer getTimedTaskId() {
        return id.intValue();
    }

    /**{@inheritDoc}*/
    @Override
    public void splitTime(Integer countRunning) {
        duration /= countRunning;
    }

    @Override
    public Boolean isStopped() {
        return !started;
    }
}

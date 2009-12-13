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

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import at.ac.tuwien.ifs.tita.issuetracker.enums.IssuePriority;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueResolution;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueSeverity;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueStatus;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ICommentTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;

/**
 * The container class for task objects from the integrated issue tracker.
 * 
 * @author Karin
 * 
 */
public class IssueTrackerTask implements ITaskTrackable, Serializable {

    private Long id;
    private Map<Long, ICommentTrackable> comments;
    private Date creationTime;
    private String description;
    private Date lastChange;
    private String owner;
    private IssuePriority priority;
    private Long projectId;
    private String reporter;
    private IssueResolution resolution;
    private IssueSeverity severity;
    private IssueStatus status;
    private String summary;

    public IssueTrackerTask(Long id, String description, String owner,
            Date creationTime, Date lastChange, IssuePriority priority,
            Long projectId, Map<Long, ICommentTrackable> comments,
            String reporter, IssueResolution resolution,
            IssueSeverity severity, IssueStatus status, String summary) {
        super();
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
    }

    /** {@inheritDoc} */
    public Map<Long, ICommentTrackable> getComments() {
        return this.comments;
    }

    /** {@inheritDoc} */
    public Date getCreationTime() {
        return this.creationTime;
    }

    /** {@inheritDoc} */
    public String getDescription() {
        return this.description;
    }

    /** {@inheritDoc} */
    public Long getId() {
        return this.id;
    }

    /** {@inheritDoc} */
    public Date getLastChange() {
        return this.lastChange;
    }

    /** {@inheritDoc} */
    public String getOwner() {
        return this.owner;
    }

    /** {@inheritDoc} */
    public IssuePriority getPriority() {
        return this.priority;
    }

    /** {@inheritDoc} */
    public Long getProjectId() {
        return this.projectId;
    }

    /** {@inheritDoc} */
    public String getReporter() {
        return this.reporter;
    }

    /** {@inheritDoc} */
    public IssueResolution getResolution() {
        return this.resolution;
    }

    /** {@inheritDoc} */
    public IssueSeverity getSeverity() {
        return this.severity;
    }

    /** {@inheritDoc} */
    public IssueStatus getStatus() {
        return this.status;
    }

    /** {@inheritDoc} */
    public String getSummary() {
        return this.summary;
    }

    /** {@inheritDoc} */
    public void setComments(Map<Long, ICommentTrackable> comments) {
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
}

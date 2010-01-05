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
package at.ac.tuwien.ifs.tita.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity for storing time producer's effort of his/her assigned tasks.
 * 
 * @author herbert
 * 
 */
@Entity
@Table(name = "EFFORT")
@SequenceGenerator(name = "seq_effort", sequenceName = "EEFORT_ID_SEQ", allocationSize = 1)
public class Effort extends BaseEntity<Long> implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_effort")
    private Long id;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "TITA_TASK_ID")
    private TiTATask titaTask;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "ISSUET_TASK_ID")
    private IssueTrackerTask issueTTask;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "START_TIME")
    private Long startTime;
    
    @Column(name = "END_TIME")
    private Long endTime;
    
    @Column(name = "DURATION")
    private Long duration;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DELETED")
    private Boolean deleted;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "USER_ID")
    // , referencedColumnName = "ID")
    private TiTAUser user;

    public Effort() {
    }

    public Effort(Date date, Long duration, Boolean deleted,
            String description, TiTAUser user) {
        super();
        this.date = date;
        this.duration = duration;
        this.deleted = deleted;
        this.description = description;
        this.user = user;
    }

    public Effort(Long id, TiTATask titaTask, IssueTrackerTask issueTTask,
            String description) {
        super();
        this.id = id;
        this.titaTask = titaTask;
        this.issueTTask = issueTTask;
        this.description = description;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public TiTATask getTitaTaskId() {
        return this.titaTask;
    }

    public IssueTrackerTask getIssueTTaskId() {
        return this.issueTTask;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public TiTAUser getUser() {
        return this.user;
    }

    public void setUser(TiTAUser user) {
        this.user = user;
    }

    public void setTitaTask(TiTATask titaTask) {
        this.titaTask = titaTask;
    }

    public void setIssueTTask(IssueTrackerTask issueTTask) {
        this.issueTTask = issueTTask;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return this.date;
    }

    public Long getEndTime() {
        return endTime;
    }

    public Long getDuration() {
        return this.duration;
    }

    public Boolean isTiTAEffort() {
        return this.titaTask.getId() != null;
    }

    /**
     * filter function.
     * 
     * @param filterString
     *            - Filter string
     * @return true if it contains the string pattern.
     */
    public Boolean matchDescription(String filterString) {
        return this.description.toLowerCase().contains(
                filterString.toLowerCase());
    }

    /**
     * 
     * @param date1
     * @return
     */
    public Boolean matchDateFrom(Date date1) {
        return date.after(date1) || date.compareTo(date1) == 0;
    }

    /**
     * 
     * @param date1
     * @return
     */
    public Boolean matchDateUntil(Date date1) {
        return date.before(date1) || date.compareTo(date1) == 0;
    }

    public TiTATask getTitaTask() {
        return this.titaTask;
    }

    public IssueTrackerTask getIssueTTask() {
        return this.issueTTask;
    }

    
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    /**
     * Generates the Url to get to the Task of the Issuetracker. For example:
     * projectname: Tita issue nr: 43 �> www.mantis.com/tita-issue:43
     * 
     * @return generated URL-String
     */
    public String getUrlToIssueTrackerTask() {
        if (this.issueTTask != null) {
            String url = this.issueTTask.getIsstProject().getIssueTracker()
                    .getUrl();
            String projectname = this.issueTTask.getIsstProject()
                    .getProjectName();
            Long taskno = this.issueTTask.getIsstTaskId();
            return url + "/" + projectname + ":" + taskno;
        }
        return "";
    }
}

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
public class Effort extends BaseEntity<Long> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_effort")
    private Long id;

    @ManyToOne(cascade = { CascadeType.MERGE })
    @JoinColumn(name = "TITA_TASK_ID")
    private TiTATask titaTask;

    @ManyToOne(cascade = { CascadeType.MERGE })
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

    @Column(name = "COST_CENTER")
    private String costcenter = "W580";

    @ManyToOne(cascade = { CascadeType.MERGE })
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

    public Effort(Date date, String description, Long startTime, Long endTime,
            Long duration, Boolean deleted, TiTAUser user) {
        super();
        this.date = date;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.deleted = deleted;
        this.user = user;
    }

    public Effort(TiTATask titaTask, IssueTrackerTask issueTTask,
            String description) {
        super();
        this.titaTask = titaTask;
        this.issueTTask = issueTTask;
        this.description = description;
    }

    public Effort(TiTATask titaTask, IssueTrackerTask issueTTask, Date date,
            Long startTime, Long endTime, Long duration, String description,
            Boolean deleted, TiTAUser user) {
        super();
        this.titaTask = titaTask;
        this.issueTTask = issueTTask;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.description = description;
        this.deleted = deleted;
        this.user = user;
    }

    public Effort(TiTATask titaTask, IssueTrackerTask issueTTask, Date date,
            Long startTime, Long endTime, Long duration, String description,
            Boolean deleted, String costcenter, TiTAUser user) {
        super();
        this.titaTask = titaTask;
        this.issueTTask = issueTTask;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.description = description;
        this.deleted = deleted;
        this.costcenter = costcenter;
        this.user = user;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public TiTAUser getUser() {
        return user;
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
        return date;
    }

    public Long getEndTime() {
        return endTime;
    }

    public Long getDuration() {
        return duration;
    }

    public Boolean isTiTATaskEffort() {
        return titaTask.getId() != null;
    }

    /**
     * Filter out all matching Efforts with a specific description.
     * 
     * @param filterString
     *            - Filter string
     * @return true if it contains the string pattern.
     */
    public Boolean matchDescription(String filterString) {
        return description != null ? description.toLowerCase().contains(
                filterString.toLowerCase()) : false;
    }

    /**
     * Filter out all matching Efforts with a specific costcenter.
     * 
     * @param filterString
     *            - Filter string
     * @return true if it contains the string pattern.
     */
    public Boolean matchCostCenter(String filterString) {
        return costcenter != null ? costcenter.toLowerCase().contains(
                filterString.toLowerCase()) : false;
    }

    /**
     * Filter out all matching Efforts with a date after the parameter date.
     * 
     * @param date1
     *            - filter date
     * @return true if it contains the string pattern.
     */
    public Boolean matchDateFrom(Date date1) {
        return date.after(date1) || date.compareTo(date1) == 0;
    }

    /**
     * Filter out all matching Efforts with a date before the parameter date.
     * 
     * @param date1
     *            - filter date
     * @return true if it contains the string pattern.
     */
    public Boolean matchDateUntil(Date date1) {
        return date.before(date1) || date.compareTo(date1) == 0;
    }

    public TiTATask getTitaTask() {
        return titaTask;
    }

    public IssueTrackerTask getIssueTTask() {
        return issueTTask;
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

    public String getCostcenter() {
        return costcenter;
    }

    public void setCostcenter(String costcenter) {
        this.costcenter = costcenter;
    }

    /**
     * Adds time to current duration.
     * 
     * @param dur
     *            duration to add as long.
     */
    public void addDuration(Long dur) {
        duration += dur;
    }
}

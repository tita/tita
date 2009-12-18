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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import at.ac.tuwien.ifs.tita.entity.interfaces.BaseEntity;

/**
 * Entity for storing time producer's effort of his/her assigned tasks.
 * 
 * @author herbert
 * 
 */
@Entity
@Table(name = "EFFORT")
@SequenceGenerator(name = "seq_effort", sequenceName = "EEFORT_ID_SEQ", allocationSize = 1)
@NamedQueries( {
        @NamedQuery(name = "timeeffort.daily.view", query = "select te from Effort te where YEAR(te.date) = :year "
                + " and MONTH(te.date)= :month and DAY(te.date)= :day"),
        @NamedQuery(name = "timeeffort.monthly.view", query = "select te from Effort te where YEAR(te.date) = :year "
                + " and MONTH(te.date)= :month"),
        @NamedQuery(name = "timeeffort.actual.view", query = "select te from Effort te order by te.date desc") })
public class Effort extends BaseEntity<Long> implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_effort")
    private Long id;

    @Column(name = "TITA_TASK_ID")
    private Long titaTaskId;

    @Column(name = "ISSUET_TASK_ID")
    private Long issueTTaskId;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "START_TIME")
    private Long startTime;

    @Column(name = "DURATION")
    private Long duration;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DELETED")
    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;

    public Effort() {
    }

    public Effort(Long id, Long titaTaskId, Long issueTTaskId,
            String description) {
        super();
        this.id = id;
        this.titaTaskId = titaTaskId;
        this.issueTTaskId = issueTTaskId;
        this.description = description;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Long getTitaTaskId() {
        return titaTaskId;
    }

    public Long getIssueTTaskId() {
        return issueTTaskId;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTitaTaskId(Long titaTaskId) {
        this.titaTaskId = titaTaskId;
    }

    public void setIssueTTaskId(Long issueTTaskId) {
        this.issueTTaskId = issueTTaskId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return startTime + duration;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public Boolean isTiTAEffort() {
        return (titaTaskId != null);
    }

    public Boolean matchDescription(String filterString) {
        return this.description.toLowerCase().contains(
                filterString.toLowerCase());
    }

}

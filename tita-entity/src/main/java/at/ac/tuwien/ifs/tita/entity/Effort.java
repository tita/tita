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
import java.util.Calendar;
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

<<<<<<< HEAD:tita-entity/src/main/java/at/ac/tuwien/ifs/tita/entity/Effort.java
=======

>>>>>>> working on feature projekuebergreifende auswertungen:tita-entity/src/main/java/at/ac/tuwien/ifs/tita/entity/Effort.java
/**
 * Entity for storing time producer's effort of his/her assigned tasks.
 * 
 * @author herbert
 * 
 */
@Entity
@Table(name = "EFFORT")
@SequenceGenerator(name = "seq_effort", sequenceName = "EFFORT_ID_SEQ", allocationSize = 1)
@NamedQueries( {
        @NamedQuery(name = "effort.years", query = "select distinct YEAR(te.date) from Effort te where deleted=false"),
        @NamedQuery(name = "timeeffort.actual.view", query = "select te from Effort te order by te.date desc") })
public class Effort extends BaseEntity<Long> implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_effort")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TITA_TASK_ID")
    private TiTATask titaTask;

    @ManyToOne
    @JoinColumn(name = "ISSUET_TASK_ID")
    private IssueTrackerTask issueTTask;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "DURATION")
    private Long duration;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DELETED")
    private Boolean deleted;

    @ManyToOne

    @JoinColumn(name = "USER_ID") //, referencedColumnName = "ID")
    private User user;

    public Effort() {
    }

    public Effort( Date creationDate, Long startTime, Long duration, Boolean deleted,
                  String description) {
        super();
        this.date = creationDate;
        this.startTime = startTime;
        this.duration = duration;
        this.deleted = deleted;
        this.description = description;
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public TiTATask getTitaTaskId() {
        return titaTask;
    }

    public IssueTrackerTask getIssueTTaskId() {
        return issueTTask;
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
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        return (cal.getTimeInMillis() + duration);
    }

    public Long getDuration() {
        return duration;
    }

    public Boolean isTiTAEffort() {
        return (titaTask.getId() != null);
    }

    /**
     * filter function.
     * 
     * @param filterString - Filter string
     * @return true if it contains the string pattern.
     */
    public Boolean matchDescription(String filterString) {
        return description.toLowerCase().contains(filterString.toLowerCase());
    }

}

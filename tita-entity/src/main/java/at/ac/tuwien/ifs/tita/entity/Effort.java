package at.ac.tuwien.ifs.tita.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity for storing time producer's effort of his/her assigned tasks.
 * @author herbert
 *
 */
@Entity
@Table(name="EFFORT")
@SequenceGenerator(name = "seq_effort", sequenceName = "EEFORT_ID_SEQ", allocationSize = 1)
@NamedQueries( {
        @NamedQuery(name = "timeeffort.daily.view", 
                query = "select te from Effort te where YEAR(te.startTime) = :year "
                        + " and MONTH(te.startTime)= :month and DAY(te.startTime)= :day"),
        @NamedQuery(name = "timeeffort.monthly.view", 
                query = "select te from Effort te where YEAR(te.startTime) = :year "
                        + " and MONTH(te.startTime)= :month"),
        @NamedQuery(name = "timeeffort.actual.view", 
                query = "select te from Effort te order by te.startTime desc") })
public class Effort implements IBaseEntity<Long> {
    
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq_effort")
    private Long id;   
    
    @Column(name="TITA_TASK_ID")
    private Long titaTaskId;
    
    @Column(name="ISSUET_TASK_ID")
    private Long issueTTaskId;
    
    @Column(name="START_TIME")
    private Date startTime;
    
    @Column(name="END_TIME")
    private Date endTime;
    
    @Column(name="DURATION")
    private Long duration;
    
    @Column(name="DESCRIPTION")
    private String description;
    
    public Effort() {
    }
    
    public Effort(Long id, Long titaTaskId, Long issueTTaskId, Date startTime,
            Date endTime, Long duration, String description) {
        super();
        this.id = id;
        this.titaTaskId = titaTaskId;
        this.issueTTaskId = issueTTaskId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.description = description;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public Long getDuration() {
        return duration;
    }

    public Long getTitaTaskId() {
        return titaTaskId;
    }

    public Long getIssueTTaskId() {
        return issueTTaskId;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}

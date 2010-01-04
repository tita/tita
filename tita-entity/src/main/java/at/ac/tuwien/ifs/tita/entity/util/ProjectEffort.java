package at.ac.tuwien.ifs.tita.entity.util;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ContainerClass for sum of effort's duration for tita project.
 * @author herbert
 *
 */
@Table(name="DUMMY1")
@Entity
public class ProjectEffort {

    @Id
    @Column (name = "PROJECT")
    protected String project;
    
    @Column (name = "YEAR")
    protected Integer year;
    
    @Column (name = "MONTH")
    protected Integer month;
    
    @Column (name = "DAY")
    protected Integer day;
    
    @Column (name = "DURATION")
    protected Long duration;
       
    public ProjectEffort() {
    }

    public Long getDuration() {
        return duration;
    }

    public String getProject() {
        return project;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getDay() {
        return day;
    }
}

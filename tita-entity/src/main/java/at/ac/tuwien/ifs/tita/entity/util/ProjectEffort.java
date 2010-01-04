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

    @Column (name = "DURATION")
    private Long duration;
    
    @Id
    @Column (name = "PROJECT")
    private String project;
    
    @Column (name = "YEAR")
    private Integer year;
    
    @Column (name = "MONTH")
    private Integer month;
    
    @Column (name = "DAY")
    private Integer day;
    
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

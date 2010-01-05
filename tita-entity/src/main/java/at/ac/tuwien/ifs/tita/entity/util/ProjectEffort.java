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
package at.ac.tuwien.ifs.tita.entity.util;

import java.io.Serializable;

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
public class ProjectEffort implements Serializable{
    @Id
    @Column (name = "ID", insertable=false, updatable=false)
    protected Long id;
    
    @Column (name = "PROJECT", insertable=false, updatable=false)
    protected String project;
    
    @Column (name = "YEAR", insertable=false, updatable=false)
    protected Integer year;
    
    @Column (name = "MONTH", insertable=false, updatable=false)
    protected Integer month;
    
    @Column (name = "DAY", insertable=false, updatable=false)
    protected Integer day;
    
    @Column (name = "DURATION", insertable=false, updatable=false)
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

    public Long getId() {
        return id;
    }
    
    
}

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
 * Container class extending ProjectEffort class.
 * @author herbert
 *
 */
@Table(name="DUMMY1")
@Entity
public class UserProjectEffort implements Serializable{
    @Id
    @Column (name = "ID", insertable=false, updatable=false)
    private Long id;
    
    @Column (name = "PROJECT", insertable=false, updatable=false)
    private String project;
    
    @Column (name = "YEAR", insertable=false, updatable=false)
    private Integer year;
    
    @Column (name = "MONTH", insertable=false, updatable=false)
    private Integer month;
    
    @Column (name = "DAY", insertable=false, updatable=false)
    private Integer day;
    
    @Column (name = "DURATION", insertable=false, updatable=false)
    private Long duration;
       
    @Column(name = "USERNAME", insertable=false, updatable=false)
    private String username;
    
    public UserProjectEffort() {
    }

    public String getUsername() {
        return username;
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

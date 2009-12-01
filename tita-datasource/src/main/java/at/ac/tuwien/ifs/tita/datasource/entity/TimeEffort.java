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

package at.ac.tuwien.ifs.tita.datasource.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * TimeEffort Entity.
 * 
 * @author markus
 * @author rene
 * 
 */
@Entity
@Table(name = "TIME_EFFORT")
@NamedQueries( {
        @NamedQuery(name = "timeeffort.daily.view", query = "select te from TimeEffort te where YEAR(te.date) = :year "
                + " and MONTH(te.date)= :month and DAY(te.date)= :day"),
        @NamedQuery(name = "timeeffort.monthly.view", query = "select te from TimeEffort te where YEAR(te.date) = :year "
                + " and MONTH(te.date)= :month") })
public class TimeEffort extends BaseTimeEffort{

    @Column(name = "DATE")
    private Date date;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "DELETED")
    private boolean deleted;

    // @ManyToOne(cascade = CascadeType.ALL, targetEntity = Role.class)
    // private Role role;

    public TimeEffort(){
        super();
    }
    
    public Long getId() {
        return id;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}

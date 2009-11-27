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

package at.ac.tuwien.ifs.tita.timeeffort.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import at.ac.tuwien.ifs.tita.datasource.domain.BaseEntity;

@Entity
@Table(name = "TimeEffort")
@NamedQueries( {
        @NamedQuery(name = "timeffort.by.day", query = "select te from TimeEffort te where YEAR(te.date) = :year "
                + " and MONTH(te.date)= :month and DAY(te.date)= :day"),
        @NamedQuery(name = "timeffort.by.month", query = "select te from TimeEffort te where YEAR(te.date) = :year "
                + " and MONTH(te.date)= :month") })
public class TimeEffort extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_timeeffort_id")
    @SequenceGenerator(name = "seq_timeeffort_id", sequenceName = "seq_timeeffort_id")
    private Long id;
    private Date date;
    private Date startTime;
    private Date endTime;
    private String description;
    private boolean deleted;

    // @ManyToOne(cascade = CascadeType.ALL, targetEntity = Role.class)
    // private Role role;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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
     * @return the startTime
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

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

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import at.ac.tuwien.ifs.tita.issuetracker.time.ITimedTask;

@MappedSuperclass
public abstract class BaseTimeEffort implements ITimedTask{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_timeeffort_id")
    @SequenceGenerator(name = "seq_timeeffort_id", sequenceName = "seq_timeeffort_id")
    @Column(name = "ID")
    protected Long id;
    
    @Column(name = "START_TIME")
    protected Long startTime;
    
    @Column(name = "DURATION")
    protected Long duration;
    
    @NotFound(action=NotFoundAction.IGNORE)
    private Boolean started;
    
    public BaseTimeEffort(){
        super();
        started = false;
    }
    
    /** {@inheritDoc} */
    @Override
    public void start() {
        if (!this.started) {
            this.startTime = System.currentTimeMillis();
            this.started = true;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void stop() {
        if (this.started) {
            this.duration = System.currentTimeMillis() - this.startTime;
            this.started = false;
        }
    }

    @Override
    public Long getDuration() {
        return duration;
    }

    @Override
    public Integer getTimedTaskId() {
        return id.intValue();
    }

    /** {@inheritDoc} */
    @Override
    public void splitTime(Integer countRunning) {
        this.duration /= countRunning;
    }

    @Override
    public Boolean isStopped() {
        return !started;
    }

    public Long getStartTime() {
        return startTime;
    }
}

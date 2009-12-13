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
package at.ac.tuwien.ifs.tita.util;

import java.util.Date;

import at.ac.tuwien.ifs.tita.entity.ITimer;


/**
 * Class for implementing the general timer function of TiTA.
 * @author herbert
 *
 */
public class GeneralTimer implements ITimer {
    
    private Long duration;
    private Boolean started;
    private Long startTime;
    private Long endTime;
    
    public GeneralTimer() {
        super();
        this.duration = 0L;
        this.started = false;
    }

    @Override
    public Long getDuration() {
        return duration;
    }
    
    /**{@inheritDoc}*/
    @Override
    public Boolean isStopped() {
        return null;
    }

    /**{@inheritDoc}*/
    @Override
    public void splitTime(Integer countRunning) {
    }

    /**{@inheritDoc}*/
    @Override
    public void start() {
        if (!this.started) {
            this.startTime = System.currentTimeMillis();
            this.started = true;
        }
    }

    /**{@inheritDoc}*/
    @Override
    public void stop() {
        if (this.started) {
            this.endTime = System.currentTimeMillis();
            this.duration = endTime - this.startTime;
            this.started = false;
        }
    }

    @Override
    public Date getStartTime() {
        return new Date(this.startTime);
    }

    @Override
    public Date getEndTime() {
        return new Date(this.endTime);
    }
}

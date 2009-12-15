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

import java.util.Date;

import at.ac.tuwien.ifs.tita.entity.interfaces.ITimedBase;


/**
 * Class for implementing the general timer function of TiTA.
 * @author herbert
 *
 */
public class GeneralTimer implements ITimedBase {
    
    protected Long duration;
    protected Boolean started;
    protected Long creationTime;
    protected Long startTime;
    protected Long endTime;
    
    public GeneralTimer() {
        super();
        this.duration = 0L;
        this.started = false;
        // real start time, startTime only for starting and 
        // splitting task time during synchronisation
        this.creationTime = System.currentTimeMillis();
    }

    /**{@inheritDoc}*/
    @Override
    public Long getDuration() {
        return duration;
    }
    
    /**{@inheritDoc}*/
    @Override
    public Boolean isStopped() {
        return !started;
    }

    /**{@inheritDoc}*/
    @Override
    public void start() {
        if (!started) {
            startTime = System.currentTimeMillis();
            started = true;
        }
    }

    /**{@inheritDoc}*/
    @Override
    public void stop() {
        if (started) {
            endTime = System.currentTimeMillis();
            started = false;
        }
    }

    /**{@inheritDoc}*/
    @Override
    public Date getStartTime() {
        return new Date(creationTime);
    }

    /**{@inheritDoc}*/
    @Override
    public Date getEndTime() {
        return new Date(endTime);
    }
}

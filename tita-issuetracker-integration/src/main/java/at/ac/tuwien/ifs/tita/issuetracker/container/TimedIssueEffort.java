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
package at.ac.tuwien.ifs.tita.issuetracker.container;

import at.ac.tuwien.ifs.tita.entity.util.GeneralTimer;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITimedEffort;

/**
 * This class implements an effort efforted for a specific task.
 * @author herbert
 *
 */
public class TimedIssueEffort extends GeneralTimer implements ITimedEffort {
    
    private Boolean started;
    private final TimedIssueId id;
    
    public TimedIssueEffort(Long projectId, Long taskId) {
        super();
        id = new TimedIssueId(projectId, taskId);
    }

    /** {@inheritDoc} */
    @Override
    public TimedIssueId getTimedIssueId() {
        return id;
    }

    /** {@inheritDoc} */
    @Override
    public Boolean isStopped() {
        return !started;
    }

    /** {@inheritDoc} */
    @Override
    public void splitTime(Integer countRunning) {
        Long pseudoEndTime;
        
        if(startTime != null){
            pseudoEndTime = System.currentTimeMillis() - startTime;
            duration += (pseudoEndTime / countRunning);
        }
    }
}

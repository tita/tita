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

package at.ac.tuwien.ifs.tita.issuetracker.mantis.util.time;

import java.util.Map;
import java.util.TreeMap;

import at.ac.tuwien.ifs.tita.entity.interfaces.ITimedBase;
import at.ac.tuwien.ifs.tita.issuetracker.container.TimedIssueId;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITimedEffort;

/**
 * This coordinator handles all incomming task. It performs starting, messuring
 * and stopping time for each incomming task.
 * 
 * @author herbert
 * 
 */
public class TimedIssueEffortCoordinator implements ITimedIssueEffortCoordinator {
    private Map<TimedIssueId, ITimedEffort> tasks;

    public TimedIssueEffortCoordinator() {
        this.tasks = new TreeMap<TimedIssueId, ITimedEffort>();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void startTimedIssueEffort(ITimedEffort t) {
        if (t != null) {
            balanceTasks();
            tasks.put(t.getTimedIssueId(), t);
            startTasks();
        }
    }
    
    /**
     * Balances all current available Tasks in task list by splitting their consumed time.
     */
    private void balanceTasks(){
        Integer tasksCount = tasks.size();
      
        for(ITimedEffort t : tasks.values()){
            t.splitTime(tasksCount);
        }
    }
    
    /**
     * Starts all current available Tasks in task list.
     */
    private void startTasks() {
        for (ITimedBase task : tasks.values()) {
            task.start();
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void stopTimedIssueEffort(ITimedEffort t) {
        if (t != null) {
            if (tasks.containsKey(t.getTimedIssueId())){
                balanceTasks();
                tasks.get(t.getTimedIssueId()).stop();
                tasks.remove(t.getTimedIssueId());
                startTasks();
            }
        }
    }
}

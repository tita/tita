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

package at.ac.tuwien.ifs.tita.util.time;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.ac.tuwien.ifs.tita.entity.ITimer;

/**
 * This coordinator handles all incomming task. It performs starting, messuring
 * and stopping time for each incomming task.
 * 
 * @author herbert
 * 
 */
public class TimedTaskCoordinator implements ITimedTaskCoordinator {
    private List<ITimer> tasks;

    public TimedTaskCoordinator() {
        this.tasks = new ArrayList<ITimer>();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void startTimeableTask(ITimer t) {
        if (t != null) {
            stopTasks();
            balanceTasks();
            tasks.add(t);
            startTasks();
        }
    }
    
    /**
     * Stops all current available Tasks in task list.
     */
    private void stopTasks(){
        for (ITimer task : tasks) {
            task.stop();
        }        
    }
    
    /**
     * Balances all current available Tasks in task list by splitting their consumed time.
     */
    private void balanceTasks(){
        Integer tasksCount = tasks.size();
        Long durTask = null;
        Long splitTime = null;
        
        for(ITimer t : tasks){
            // TODO: persist here in Database
            // duration currently for test purposes stored in durationTime
            if (durationTime.containsKey(t.getTimedTaskId())) {
                durTask = durationTime.get(t.getTimedTaskId());
                t.splitTime(tasksCount);
                splitTime = t.getDuration();
                durationTime.put(t.getTimedTaskId(), (durTask + splitTime));
            } else {
                t.splitTime(tasksCount);
                durationTime.put(t.getTimedTaskId(), t.getDuration());
            }
        }
    }
    
    /**
     * Starts all current available Tasks in task list.
     */
    private void startTasks() {
        for (ITimer task : tasks) {
            task.start();
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void stopTimeableTask(ITimer t) {
        if (t != null) {
            if (tasks.contains(t)){
                stopTasks();
                balanceTasks();
                tasks.remove(t);
                startTasks();
            }
        }
    }
}

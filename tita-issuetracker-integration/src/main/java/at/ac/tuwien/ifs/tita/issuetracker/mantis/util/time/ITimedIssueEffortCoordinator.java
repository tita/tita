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

import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITimedEffort;

/**
 * Interface for TimerCoordinator.
 * 
 * @author herbert
 */
public interface ITimedIssueEffortCoordinator {

    /**
     * Add a timeable issue effort to coordinators task list for managing it and starts
     * this task.
     * 
     * @param t ITimeabletask
     */
    void startTimedIssueEffort(ITimedEffort t);

    /**
     * Removes a timeable issue effort from coordinators task list, but stops it before.
     * 
     * @param t ITimeabletask
     */
    void stopTimedIssueEffort(ITimedEffort t);
}
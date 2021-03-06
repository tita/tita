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
package at.ac.tuwien.ifs.tita.entity.interfaces;

import java.util.Date;

/**
 * Interface for general timer methods.
 * @author herbert
 *
 */
public interface ITimedBase {

    /**
     * Starts timer's function for messuring efforted time of task/issue.
     */
    void start();

    /**
     * Stops timer's messuring function.
     */
    void stop();

    /**
     * Decides, if task has been stopped or is running.
     * 
     * @return true, if stopped
     */
    Boolean isStopped();

    /**
     * Returns duration of task.
     * 
     * @return Long duration
     */
    Long getDuration();
    
    /**
     * Returns start time of task.
     * @return Date startTime
     */
    Date getStartTime();
    
    /**
     * Returns end time of task.
     * @return Date startTime
     */
    Date getEndTime();
}

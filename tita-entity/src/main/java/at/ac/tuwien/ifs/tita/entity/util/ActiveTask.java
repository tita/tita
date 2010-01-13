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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.ac.tuwien.ifs.tita.entity.Effort;

/**
 * Container class for all active issue tracker tasks per tita user.
 *
 * @author herbert
 *
 */
public class ActiveTask {
    private ActiveTaskId tId;
    private List<ActiveTaskEffort> tList;

    public ActiveTask(ActiveTaskId tId) {
        this.tId = tId;
        tList = new ArrayList<ActiveTaskEffort>();
    }

    /**
     * Adds an effort for a given tita user.
     *
     * @param userId
     *            Long
     * @param effort
     *            Long
     */
    public void addTaskEffort(Long userId, Long effort) {
        ActiveTaskEffort a = findUser(userId);

        if (a != null) {
            a.addDurationToEffort(effort);
        } else {
            tList.add(new ActiveTaskEffort(userId, new Effort(null, null, new Date(),
                                            System.currentTimeMillis(), null,
                                            0L, null, false, null)));
        }
    }


    /**
     * Calculates if any task has been started by a user.
     * @param userId Long
     * @return ActiveTaskEffort
     */
    private ActiveTaskEffort findUser(Long userId) {
        for (ActiveTaskEffort ate : tList) {
            if (ate.getUserId().equals(userId)) {
                return ate;
            }
        }
        return null;
    }

    /**
     * Equals implementation for 2 different tasks.
     * 
     * @param taskId
     *            ActiveTaskId
     * @return ActiveTaskId
     */
    public Boolean equalsTask(ActiveTaskId taskId) {
        return tId.compareTo(taskId) == 0 ? true : false;
    }

    /**
     * Returns an task effort for given user id.
     * @param userId Long
     * @return Effort
     */
    public Effort getEffortForUser(Long userId) {
        ActiveTaskEffort eff = findUser(userId);
        Effort effort = null;

        if (eff != null) {
            effort = eff.getEffort();
        }
        return effort;
    }

    /**
     * Removes user of task list.
     * @param userId Long
     */
    public void removeEffortForUser(Long userId) {
        ActiveTaskEffort ate = findUser(userId);
        tList.remove(ate);
    }

    /**
     * Returns the id of a task that a user maybe have started.
     * @param userId Long
     * @return ActiveTaskId or null
     */
    public ActiveTaskId getTaskIdForUser(Long userId) {
        if (findUser(userId) != null) {
            return tId;
        }
        return null;
    }
}

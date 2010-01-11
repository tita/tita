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
package at.ac.tuwien.ifs.tita.presentation.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.util.ActiveTask;
import at.ac.tuwien.ifs.tita.entity.util.ActiveTaskId;

/**
 * Coordinator class to start, stop and schedule time of many actives tasks
 * simultaneously.
 * 
 * @author herbert
 * 
 */
public class TimerCoordinator implements Runnable {
    private static final Integer C_DURATION_IN_MILLIS = 10000;
    private List<ActiveTask> activeTasks;
    private Map<Long, Effort> titaTasks;
    private Map<Long, Long> registeredUsers;
    private Boolean doRun;

    public TimerCoordinator() {
        activeTasks = new ArrayList<ActiveTask>();
        doRun = true;
        titaTasks = new TreeMap<Long, Effort>();
        registeredUsers = new TreeMap<Long, Long>();
    }

    @Override
    public void run() {
        try {
            Long user = null, taskCount = null;
            Effort eff = null;
            Iterator<Long> users = null;

            while (doRun) {
                synchronized (activeTasks) {
                    for (ActiveTask act : activeTasks) {
                        synchronized (registeredUsers) {
                            users = registeredUsers.keySet().iterator();
                            while (users.hasNext()) {
                                user = users.next();
                                taskCount = registeredUsers.get(user);
                                if (taskCount != null) {
                                    if (taskCount > 0) {
                                        act.addTaskEffort(user, C_DURATION_IN_MILLIS / taskCount);
                                        // then fetch a tita task, if user
                                        // registered one and
                                        // split its time too
                                        synchronized (titaTasks) {
                                            if (titaTasks.containsKey(user)) {
                                                eff = titaTasks.get(user);
                                                eff.addDuration(C_DURATION_IN_MILLIS / taskCount);
                                                titaTasks.put(user, eff);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Thread.sleep(C_DURATION_IN_MILLIS);
            }
        } catch (InterruptedException e) {
            // TODO: refactoring
        }
    }

    public void shutdown() {
        doRun = false;
    }

    private ActiveTask containsTask(ActiveTaskId id) {
        for (ActiveTask a : activeTasks) {
            if (a.equalsTask(id)) {
                return a;
            }
        }
        return null;
    }

    public synchronized void startIssueTimer(Long userId, ActiveTaskId tId) {
        ActiveTask active;
        Long count;

        if (registeredUsers.containsKey(userId)) {
            if ((active = containsTask(tId)) != null) {
                active.addTaskEffort(userId, 0L);
            } else {
                activeTasks.add(new ActiveTask(tId));
            }
            if (registeredUsers.containsKey(userId)) {
                count = registeredUsers.get(userId);
                count++;
                registeredUsers.put(userId, count);
            }
        }
    }

    public synchronized Effort stopIssueTimer(Long userId, ActiveTaskId tId) {
        Long count;
        ActiveTask active;
        Effort effort = null;

        if (registeredUsers.containsKey(userId)) {
            if ((active = containsTask(tId)) != null) {
                effort = active.getEffortForUser(userId);
                if (effort != null) {
                    effort.setEndTime(System.currentTimeMillis());
                    active.removeEffortForUser(userId);
                    // subtract active tasks by 1 for given user and tasks count
                    if (registeredUsers.containsKey(userId)) {
                        count = registeredUsers.get(userId);
                        count--;
                        registeredUsers.put(userId, count);
                    }
                }
            }
        }
        return effort;
    }

    public synchronized void startTiTATimer(Long userId) {
        if (!titaTasks.containsKey(userId)) {
            titaTasks.put(userId, new Effort(null, null, new Date(), 0L, null, null, null, false, null));
        }
    }

    public synchronized Effort stopTiTATimer(Long userId) {
        Effort effort = null;

        if (titaTasks.containsKey(userId)) {
            effort = titaTasks.get(userId);
            effort.setEndTime(System.currentTimeMillis());
            titaTasks.remove(userId);
        }
        return effort;
    }

    public synchronized void registerUser(Long userId) {
        if (!registeredUsers.containsKey(userId)) {
            registeredUsers.put(userId, 0L);
        }
    }

    public synchronized void unregisterUser(Long userId) {
        if (registeredUsers.containsKey(userId)) {
            removeAllUnsavedTasksOfUser(userId);
            registeredUsers.remove(userId);
        }
    }

    private void removeAllUnsavedTasksOfUser(Long userId) {
        for (ActiveTask at : activeTasks) {
            at.removeEffortForUser(userId);
        }
        if (titaTasks.containsKey(userId)) {
            titaTasks.remove(userId);
        }
    }

    public List<ActiveTaskId> getActiveTasks(Long userId) {
        List<ActiveTaskId> ids = new ArrayList<ActiveTaskId>();
        ActiveTaskId id = null;

        for (ActiveTask a : activeTasks) {
            if ((id = a.getTaskIdForUser(userId)) != null) {
                ids.add(id);
            }
        }
        return ids;
    }
}

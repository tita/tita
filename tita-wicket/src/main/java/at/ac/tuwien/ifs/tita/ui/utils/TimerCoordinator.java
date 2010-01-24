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
package at.ac.tuwien.ifs.tita.ui.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.util.ActiveTask;
import at.ac.tuwien.ifs.tita.entity.util.ActiveTaskEffort;
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
    private List<ActiveTaskEffort> titaTasks;
    private Map<Long, Long> registeredUsers;
    private Boolean doRun;

    public TimerCoordinator() {
        activeTasks = new ArrayList<ActiveTask>();
        doRun = true;
        titaTasks = new ArrayList<ActiveTaskEffort>();
        registeredUsers = new TreeMap<Long, Long>();
    }

    /** {@inheritDoc} */
    @Override
    public void run() {
        Long user = null, taskCount = null;
        Iterator<Long> users = null;

        while (doRun) {
            try {
                synchronized (activeTasks) {
                    for (ActiveTask act : activeTasks) {
                        synchronized (registeredUsers) {
                            users = registeredUsers.keySet().iterator();
                            while (users.hasNext()) {
                                user = users.next();
                                taskCount = registeredUsers.get(user);
                                if (taskCount != null) {
                                    if (taskCount > 0) {
                                        act.addTaskEffort(user, C_DURATION_IN_MILLIS/taskCount);
                                    }
                                }
                            }
                        }
                    }
                    // then fetch a tita task, if user
                    // registered one and
                    // split its time too
                    synchronized (titaTasks) {
                        users = registeredUsers.keySet().iterator();
                        while (users.hasNext()) {
                            user = users.next();
                            taskCount = registeredUsers.get(user);
                            if (taskCount != null) {
                                if (taskCount > 0) {
                                    for(ActiveTaskEffort ate : titaTasks){
                                        if(user.equals(ate.getUserId())){
                                            ate.addDurationToEffort(C_DURATION_IN_MILLIS
                                                    / taskCount);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Thread.sleep(C_DURATION_IN_MILLIS);
            } catch (InterruptedException e) {
                // TODO: refactoring
            }
        }
    }

//    when tita will be shut down?!?!
//    public void shutdown() {
//        doRun = false;
//    }

    /**
     * Checks if time coordinator contains a given task.
     * @param id ActiveTaskId
     * @return ActiveTask or null
     */
    private ActiveTask containsTask(ActiveTaskId id) {
        for (ActiveTask a : activeTasks) {
            if (a.equalsTask(id)) {
                return a;
            }
        }
        return null;
    }

    /**
     * Start time measuring of an issue tracker task.
     * @param userId Long
     * @param tId ActiveTaskId
     */
    public synchronized void startIssueTimer(Long userId, ActiveTaskId tId) {
        ActiveTask active;
        Long count;

        if (registeredUsers.containsKey(userId)) {
            active = containsTask(tId);
            if (active != null) {
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

    /**
     * Stop time measuring of an issue tracker task.
     * @param userId Long
     * @param tId ActiveTaskId
     * @return Effort
     */
    public synchronized Effort stopIssueTimer(Long userId, ActiveTaskId tId) {
        Long count;
        ActiveTask active;
        Effort effort = null;

        if (registeredUsers.containsKey(userId)) {
            active = containsTask(tId);
            if (active != null) {
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

    /**
     * Start time measuring of an tita task.
     * @param userId Long
     */
    public synchronized void startTiTATimer(Long userId) {
        Long count;

        if (registeredUsers.containsKey(userId)) {
            if (findTiTATaskForUser(userId) == null) {
                titaTasks.add(new ActiveTaskEffort(userId, new Effort(null, null, new Date(),
                                                    System.currentTimeMillis(), null,
                                                    0L, null, false, null)));
                if (registeredUsers.containsKey(userId)) {
                    count = registeredUsers.get(userId);
                    count++;
                    registeredUsers.put(userId, count);
                }
            }
        }
    }

    /**
     * Find a task effort for a tita task for a given user id.
     * @param userId Long
     * @return ActiveTaskEffort
     */
    public ActiveTaskEffort findTiTATaskForUser(Long userId){
        for(ActiveTaskEffort ate : titaTasks){
            if(ate.getUserId().equals(userId)){
                return ate;
            }
        }
        return null;
    }

    /**
     * Stop time measuring of an tita task.
     *
     * @param userId
     *            Long
     * @return effort
     */
    public synchronized Effort stopTiTATimer(Long userId) {
        Effort effort = null;
        ActiveTaskEffort ate = null;
        Long count;

        if (registeredUsers.containsKey(userId)) {
            ate = findTiTATaskForUser(userId);
            if (ate != null) {
                effort = ate.getEffort();
                effort.setEndTime(System.currentTimeMillis());
                titaTasks.remove(userId);
                if (registeredUsers.containsKey(userId)) {
                    count = registeredUsers.get(userId);
                    count--;
                    registeredUsers.put(userId, count);
                }
            }
        }
        return effort;
    }

    /**
     * Register user at time coordinator.
     * Only registered users can measure efforts of tasks.
     * @param userId Long
     */
    public synchronized void registerUser(Long userId) {
        if (!registeredUsers.containsKey(userId)) {
            registeredUsers.put(userId, 0L);
        }
    }

    /**
     * Unregister user from time coordinator.
     * @param userId Long
     */
    public synchronized void unregisterUser(Long userId) {
        if (registeredUsers.containsKey(userId)) {
            registeredUsers.remove(userId);
        }
    }
    
//    /**
//     * Removes all unstopped tasks of a user.
//     * @param userId Long
//     * @return list of efforts to save
//     */
//    private List<Effort> removeAllUnsavedTasksOfUser(Long userId) {
//        ActiveTaskEffort ate = null;
//        List<Effort> efforts = new ArrayList<Effort>();
//        for (ActiveTask at : activeTasks) {
//            efforts.add(stopIssueTimer(userId, at.getTaskIdForUser(userId)));
//        }
//
//        ate = findTiTATaskForUser(userId);
//        if (ate != null) {
//            efforts.add(stopTiTATimer(userId));
//        }
//        
//        return efforts;
//    }

    /**
     * Get all active tasks for a given user id.
     * @param userId Long
     * @return List of ActiveTaskId
     */
    public List<ActiveTaskId> getActiveTasks(Long userId) {
        List<ActiveTaskId> ids = new ArrayList<ActiveTaskId>();
        ActiveTaskId id = null;

        for (ActiveTask a : activeTasks) {
            id = a.getTaskIdForUser(userId);
            if (id != null) {
                ids.add(id);
            }
        }
        return ids;
    }
}

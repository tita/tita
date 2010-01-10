package at.ac.tuwien.ifs.tita.presentation.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import at.ac.tuwien.ifs.tita.presentation.tasklist.stopwatch.ActiveTaskId;

/**
 * Coordinator class to start, stop and schedule time of many actives tasks simultaneously.
 * @author herbert
 *
 */
public class TimerCoordinator implements Runnable{
    private static final Integer C_DURATION_IN_MILLIS = 10000;
    private Map<Long, Map<ActiveTaskId, Long>> activeTasks;
    private Map<Long, Long> titaTasks;
    private Map<Long, Long> registeredUsers; 
    private Boolean doRun;
    
    public TimerCoordinator() {
        activeTasks = new TreeMap<Long, Map<ActiveTaskId,Long>>();
        doRun = true;
        titaTasks = new TreeMap<Long, Long>();
        registeredUsers = new TreeMap<Long, Long>();
    }

    @Override
    public void run() {
        try {
            Long user = null, taskCount = null, taskTime = null;
            Map<ActiveTaskId, Long> tasks = null;
            Iterator<Long> users = null;
            Iterator<ActiveTaskId> itTask = null;
            ActiveTaskId task = null;
            
            while(doRun){
                synchronized (activeTasks) {
                    //fetch for every registered user all running issue tracker tasks
                    users = activeTasks.keySet().iterator();
                    itTask = null;
                    while(users.hasNext()){
                        user = users.next();
                        //fetch all running tasks of a single user
                        tasks = activeTasks.get(user);
                        synchronized (registeredUsers){
                            taskCount = registeredUsers.get(user);
                        }
                        //split time of tasks
                        itTask = tasks.keySet().iterator();
                        while(itTask.hasNext()){
                            task = itTask.next();
                            taskTime = tasks.get(task);
                            taskTime += (taskTime / taskCount);
                            //set updated task time for every user task
                            tasks.put(task, taskTime);
                        }
                        //then fetch a tita task, if user registered one and split its time too
                        synchronized (titaTasks) {
                            if(titaTasks.containsKey(user)){
                                taskTime = titaTasks.get(user);
                                taskTime += (taskTime / taskCount);
                                titaTasks.put(user, taskTime);
                            }
                        }
                    }
                }
                Thread.sleep(C_DURATION_IN_MILLIS);
            }
        } catch (InterruptedException e) {
            //TODO: refactoring
            e.printStackTrace();
        }
    }
    
    public void shutdown(){
        doRun = false;
    }
    
    public synchronized void startIssueTimer(Long userId, ActiveTaskId tId){
        Long count;
        Map<ActiveTaskId, Long> efforts;
        
        if(!activeTasks.containsKey(userId)){
            efforts = new TreeMap<ActiveTaskId, Long>();
            efforts.put(tId, 0L);
            if(registeredUsers.containsKey(userId)){
                count = registeredUsers.get(userId);
                count++;
                registeredUsers.put(userId, count);
            }
        }
    }
    
    public synchronized Long stopIssueTimer(Long userId, ActiveTaskId tId){
        Long count;
        Map<ActiveTaskId, Long> efforts;
        Long effort = null;
        
        if(activeTasks.containsKey(userId)){
            efforts = activeTasks.get(userId);
            effort = efforts.get(tId);
            //subtract active tasks by 1 for given user and tasks count
            if(registeredUsers.containsKey(userId)){
                count = registeredUsers.get(userId);
                count--;
                registeredUsers.put(userId, count);
            }
        }
        return effort;
    }
    
    public synchronized void startTiTATimer(Long userId){
        if(!titaTasks.containsKey(userId)){
            titaTasks.put(userId, 0L);
        }
    }
    
    public synchronized Long stopTiTATimer(Long userId){
        Long effort = null;
        
        if(titaTasks.containsKey(userId)){
            effort = titaTasks.get(userId);
            titaTasks.remove(userId);
        }
        return effort;
    }
    
    public synchronized void registerUser(Long userId){
        registeredUsers.put(userId, 0L);
    }
    
    public synchronized void unregisterUser(Long userId){        
        if(registeredUsers.containsKey(userId)){
            registeredUsers.remove(userId);
        }
    }
}

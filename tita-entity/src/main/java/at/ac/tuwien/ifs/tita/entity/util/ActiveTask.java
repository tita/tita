package at.ac.tuwien.ifs.tita.entity.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.ac.tuwien.ifs.tita.entity.Effort;

/**
 * Container class for all active issue tracker tasks per tita user.
 * @author herbert
 *
 */
public class ActiveTask {
    private ActiveTaskId tId;
    private List<ActiveTaskEffort> tList;
    
    public ActiveTask(ActiveTaskId tId) {
        this.tId = tId;
        this.tList = new ArrayList<ActiveTaskEffort>();
    }
    
    public void addTaskEffort(Long userId, Long effort){
       ActiveTaskEffort a;
       
       if((a = findUser(userId)) != null){
           a.addEffort(effort);
       }else{
           tList.add(new ActiveTaskEffort(userId, new Effort(null, null, new Date(), 
                                                             System.currentTimeMillis(),
                                                             null, 0L, null, false, null)));
       }
    }
    
    private ActiveTaskEffort findUser(Long userId){
        for(ActiveTaskEffort ate : tList){
            if(ate.getUserId().equals(userId)){
                return ate;
            }
        }
        return null;
    }
    
    public Boolean equalsTask(ActiveTaskId taskId){
       return (tId.compareTo(taskId) == 0 ? true : false);
    }
    
    public Effort getEffortForUser(Long userId){
        ActiveTaskEffort eff;
        Effort effort = null;
        
        if((eff = findUser(userId)) != null){
            effort = eff.getEffort();
        }
        return effort;
    }
    
    public void removeEffortForUser(Long userId){
        ActiveTaskEffort ate = findUser(userId);
        tList.remove(ate);
    }
    
    public ActiveTaskId getTaskIdForUser(Long userId){
        if(findUser(userId) != null){
            return tId;
        }
        return null;
    }
}

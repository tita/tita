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
    private TimedIssueId id;
    
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

package at.ac.tuwien.ifs.tita.issuetracker.time;

/**
  * Interface for TimerCoordinator.
  * @author herbert
  */
public interface ITimedTaskCoordinator {
    
    /**
     * Add a timeable task to coordinators task list for managing it and starts this task.
     * @param t ITimeabletask
     */
    void startTimeableTask(ITimedTask t);
    
    /**
     * Removes a timeable task from coordinators task list, but stops it before.
     * @param t ITimeabletask 
     */
    void  stopTimeableTask(ITimedTask t);
}

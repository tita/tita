package at.ac.tuwien.ifs.tita.issuetracker.time;

/**
 * Timer interface for proper defining timer's tasks start and stop.
 * @author herbert
 *
 */
public interface ITimedTask {
    
    /**
     * Starts timer's function for messuring efforted time of task/issue.
     */
    void start();
    
    /**
     * Stops timer's messuring function.
     */
    void stop();
    
    /**
     * Returns proper id of task.
     * @return Long TasksId
     */
    Integer getTimedTaskId();
    
    /**
     * Splits the time for a given number of currently running tasks.
     * @param countRunning Integer
     */
    void splitTime(Integer countRunning);
    
    /**
     * Decides, if task has been stopped or is running.
     * @return true, if stopped
     */
    Boolean isStopped();
    
    /**
     * Returns duration of task.
     * @return Long duration
     */
    Long getDuration();
}

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

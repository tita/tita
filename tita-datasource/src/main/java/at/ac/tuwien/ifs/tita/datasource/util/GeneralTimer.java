package at.ac.tuwien.ifs.tita.datasource.util;

import at.ac.tuwien.ifs.tita.datasource.time.ITimer;


/**
 * Class for implementing the general timer function of TiTA.
 * @author herbert
 *
 */
public class GeneralTimer implements ITimer {
    
    private Long duration;
    private Boolean started;
    private Long startTime;
    
    public GeneralTimer() {
        super();
        this.duration = 0L;
        this.started = false;
    }

    @Override
    public Long getDuration() {
        return duration;
    }

    /**{@inheritDoc}*/
    @Override
    public Integer getTimedTaskId() {
        return null;
    }
    
    /**{@inheritDoc}*/
    @Override
    public Boolean isStopped() {
        return null;
    }

    /**{@inheritDoc}*/
    @Override
    public void splitTime(Integer countRunning) {
    }

    /**{@inheritDoc}*/
    @Override
    public void start() {
        if (!this.started) {
            this.startTime = System.currentTimeMillis();
            this.started = true;
        }
    }

    /**{@inheritDoc}*/
    @Override
    public void stop() {
        if (this.started) {
            this.duration = System.currentTimeMillis() - this.startTime;
            this.started = false;
        }
    }

}

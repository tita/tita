package at.ac.tuwien.ifs.tita.entity.util;

import at.ac.tuwien.ifs.tita.entity.Effort;

/**
 * Container class for effort per issue tracker task and user.
 * @author herbert
 *
 */
public class ActiveTaskEffort {
    
    private Long userId;
    private Effort effort;
    
    public ActiveTaskEffort(Long userId, Effort effort) {
        super();
        this.userId = userId;
        this.effort = effort;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Effort getEffort() {
        return effort;
    }

    public void addEffort(Long duration) {
        effort.addDuration(duration);
    }
}

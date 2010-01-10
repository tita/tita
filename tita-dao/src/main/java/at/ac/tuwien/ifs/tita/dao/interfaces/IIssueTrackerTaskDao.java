package at.ac.tuwien.ifs.tita.dao.interfaces;

import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;

/**
 * Interface for IssueTrackerTaskDao
 * @author herbert
 *
 */
public interface IIssueTrackerTaskDao {
    /**
     * Find IssueTrackerTasks for given tita project.
     * @param tp
     * @param it
     * @param itp
     * @param itt
     * @return
     */
    IssueTrackerTask findIssueTrackerTask(Long tp, Long it, Long itp, Long itt);
}

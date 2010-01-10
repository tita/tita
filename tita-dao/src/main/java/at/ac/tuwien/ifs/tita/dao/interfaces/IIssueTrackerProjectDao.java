package at.ac.tuwien.ifs.tita.dao.interfaces;

import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;

/**
 * Interface for issue tracker project dao.
 * @author herbert
 *
 */
public interface IIssueTrackerProjectDao {

    /**
     * Finds an issue tracker project to given tita project
     * @param tp Long
     * @param issueTrackerId Long
     * @param itp Long
     */
    IssueTrackerProject findIssueTrackerProjectForTiTAProject(Long tp, Long issueTrackerId, 
                                                              Long itp);
}

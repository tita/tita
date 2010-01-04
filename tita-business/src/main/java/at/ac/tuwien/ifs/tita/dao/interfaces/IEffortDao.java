package at.ac.tuwien.ifs.tita.dao.interfaces;

import java.util.List;

import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.util.ProjectEffort;
import at.ac.tuwien.ifs.tita.entity.util.UserProjectEffort;

/**
 * 
 * Interface for EffortDao.
 * 
 * @author herbert
 */
public interface IEffortDao {
        
    /**
     * Gets a view for the last time efforts.
     * 
     * @param maxresults
     *            sets the max results value for the query
     * @return list of timefforts that match dates
     */
    List<Effort> getActualTimeEfforts(Integer maxresults);
    
    /**
     * Find all efforts for a specific tita-project.
     * @param projectIds List of Long
     * @param grouping String
     * @return List of Effort
     */
    List<ProjectEffort> findEffortsForTiTAProjectId(List<String> projectId, String grouping);
    
    /**
     * Find all effort of a time consumer in a specific tita-project.
     * @param projectIds List of Long
     * @param tIds List of Long
     * @param grouping String
     * @return List of Effort
     */
    List<UserProjectEffort> findEffortsForTiTAProjectAndTimeConsumerId(List<String> projectId, 
                                                            List<String> tIds,
                                                            String grouping);
    
    /**
     * Find all efforts of a specific time consumer in whole tita.
     * @param tcId Long
     * @return List of Effort
     */
    List<Effort> findEffortsForTimeConsumerId(Long tcId);
}

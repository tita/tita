package at.ac.tuwien.ifs.tita.dao.interfaces;

import java.util.List;

import at.ac.tuwien.ifs.tita.entity.Effort;

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
     * @param projectId Long
     * @return List of Effort
     */
    List<Effort> findEffortsForTiTAProjectId(Long projectId);
    
    /**
     * Find all effort of a time consumer in a specific tita-project.
     * @param projectId Long
     * @param tcId Long
     * @return List of Effort
     */
    List<Effort> findEffortsForTiTAProjectAndTimeConsumerId(Long projectId, Long tcId);
    
    /**
     * Find all efforts of a specific time consumer in whole tita.
     * @param tcId Long
     * @return List of Effort
     */
    List<Effort> findEffortsForTimeConsumerId(Long tcId);
}

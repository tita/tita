package at.ac.tuwien.ifs.tita.dao.interfaces;

import java.util.List;

import at.ac.tuwien.ifs.tita.entity.TiTAProject;

/**
 * Interface for TiTAProjectDao implementation.
 * @author herbert
 *
 */
public interface ITiTAProjectDao {
    
    /**
     * Returns a list of tita projects for a given username.
     * @param username String
     * @return List of TiTAProject
     */
    List<TiTAProject> findTiTAProjectsForUsername(String username);
}

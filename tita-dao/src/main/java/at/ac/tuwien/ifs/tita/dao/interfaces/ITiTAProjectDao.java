/**
   Copyright 2009 TiTA Project, Vienna University of Technology
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE\-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package at.ac.tuwien.ifs.tita.dao.interfaces;

import java.util.List;

import javax.persistence.PersistenceException;

import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.conv.ProjectStatus;

/**
 * Interface for TiTAProjectDao implementation.
 * @author herbert
 *
 */
public interface ITiTAProjectDao extends IGenericHibernateDao<TiTAProject, Long>{

    /**
     * Returns a list of tita projects for a given username.
     * @param username String
     * @return List of TiTAProject
     */
    List<TiTAProject> findTiTAProjectsForUsername(String username);
    
    /**
     * Searches the Database for a List of Projects, ordered by their name.
     * 
     * @param maxResult the maximum size of List.
     * @param orderBy the attribute to order the List.
     * @return a List of TiTAProject.
     * @throws PersistenceException if Exception was thrown before.
     */
    List<TiTAProject> findProjectsOrderedByName(int maxResult, String orderBy) throws PersistenceException;
    
    /**
     * Returns a List of all available Project Stati.
     * 
     * @return a List of all stored Project Stati.
     * @throws PersistenceException if Exception was thrown before.
     */
    List<ProjectStatus> getAvailableProjectStati() throws PersistenceException;

}

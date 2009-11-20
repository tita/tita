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

package at.ac.tuwien.ifs.tita.timeeffort.service;

import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.ifs.tita.datasource.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.timeeffort.domain.TimeEffort;

/**
 * ITimeEffortService encapsulates all TimeEffort-concerning Database operations
 * 
 * @author ASE Group 10 - TiTA
 * 
 */
@Transactional
public interface ITimeEffortService {

    /**
     * Saves a new timeEffort or updates an existing one
     * 
     * @param timeEffort
     *            the timeEffort to be saved
     * @throws TitaDAOException
     *             if Parameter is null or another Exception is thrown
     */
    TimeEffort saveTimeEffort(TimeEffort timeEffort) throws TitaDAOException;

    /**
     * Updates an existing TimeEffort
     * 
     * @param timeEffort
     *            the timeEffort to be updated
     * @throws TitaDAOException
     *             if Parameter is null or another Exception is thrown
     */
    void updateTimeEffort(TimeEffort timeEffort) throws TitaDAOException;

    /**
     * deletes an existing timeEffort
     * 
     * @param timeEffort
     *            the timeEffort to be deleted
     * @throws TitaDAOException
     *             if Parameter is null or another Exception is thrown
     */
    void deleteTimeEffort(TimeEffort timeEffort) throws TitaDAOException;

    /**
     * returns a specific TimeEffort found to the id given
     * 
     * @param id
     *            the unique identifier of an timeEffort
     * @throws TitaDAOException
     *             if no timeEffort was found or another Exception is thrown
     */
    TimeEffort getTimeEffortById(Long id) throws TitaDAOException;
}

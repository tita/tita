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

package at.ac.tuwien.ifs.tita.business.service.time;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.entity.Effort;

/**
 * ITimeEffortService encapsulates all TimeEffort-concerning Database
 * operations.
 * 
 * @author ASE Group 10 - TiTA
 * 
 */
@Transactional
public interface IEffortService {

    /**
     * Saves a new timeEffort or updates an existing one.
     * 
     * @param timeEffort the timeEffort to be saved
     * @throws TitaDAOException if Parameter is null or another Exception is
     *         thrown
     */
    void saveEffort(Effort timeEffort) throws TitaDAOException;

    /**
     * deletes an existing timeEffort.
     * 
     * @param timeEffort the timeEffort to be deleted
     * @throws TitaDAOException if Parameter is null or another Exception is
     *         thrown
     */
    void deleteEffort(Effort timeEffort) throws TitaDAOException;

    /**
     * returns a specific TimeEffort found to the id given.
     * 
     * @param id the unique identifier of an timeEffort
     * @throws TitaDAOException if no timeEffort was found or another Exception
     *         is thrown
     * @return Effort found
     */
    Effort getEffortById(Long id) throws TitaDAOException;

    /**
     * Gets a view for a day.
     * 
     * @param date dates which are selected
     * @return list of efforts that match dates
     * @throws TitaDAOException if anything goes wrong with db access.
     */
    List<Effort> getEffortsDailyView(Date date) throws TitaDAOException;

    /**
     * Gets a view for a month.
     * 
     * @param year year which is selected
     * @param month month which is selected
     * @return list of efforts that match dates
     * @throws TitaDAOException if anything goes wrong with db access.
     */
    List<Effort> getEffortsMonthlyView(Integer year, Integer month) throws TitaDAOException;

    /**
     * Gets all years in which efforts were saved.
     * 
     * @return list of years
     */
    List<Double> getEffortsYears();

    /**
     * Gets a view for the last time efforts.
     * 
     * @param maxresults sets max results
     * @return list of efforts that match dates
     * @throws TitaDAOException if anything goes wrong with db access.
     */
    List<Effort> getActualEfforts(int maxresults) throws TitaDAOException;
}

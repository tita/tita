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

import javax.persistence.PersistenceException;

import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.util.UserProjectEffort;

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
     * @param timeEffort
     *            the timeEffort to be saved
     * @throws PersistenceException
     *             if Parameter is null or another Exception is thrown
     */
    void saveEffort(Effort timeEffort) throws PersistenceException;

    /**
     * deletes an existing timeEffort.
     * 
     * @param timeEffort
     *            the timeEffort to be deleted
     * @throws PersistenceException
     *             if Parameter is null or another Exception is thrown
     */
    void deleteEffort(Effort timeEffort) throws PersistenceException;

    /**
     * returns a specific TimeEffort found to the id given.
     * 
     * @param id
     *            the unique identifier of an timeEffort
     * @throws PersistenceException
     *             if no timeEffort was found or another Exception is thrown
     * @return Effort found
     */
    Effort getEffortById(Long id) throws PersistenceException;

    /**
     * Gets a view for a day.
     * 
     * @param date
     *            dates which are selected
     * @return list of efforts that match dates
     * @throws PersistenceException
     *             if anything goes wrong with db access.
     */
    List<Effort> getEffortsDailyView(Date date) throws PersistenceException;

    /**
     * Gets a view for a month.
     * 
     * @param year
     *            year which is selected
     * @param month
     *            month which is selected
     * @return list of efforts that match dates
     * @throws PersistenceException
     *             if anything goes wrong with db access.
     */
    List<Effort> getEffortsMonthlyView(Integer year, Integer month)
            throws PersistenceException;

    /**
     * Gets all years in which efforts were saved.
     * 
     * @return list of years
     */
    List<Integer> getEffortsYears();

    /**
     * Gets a summary of Effort for List of user and projects - overall,
     * monthly, daily.
     * 
     * @param projects
     *            List of String
     * @param usernames
     *            List of String
     * @param grouping
     *            String
     * @return UserProjectEffort List
     */
    List<UserProjectEffort> getEffortsSummaryForProjectAndUserNames(
            List<String> projects, List<String> usernames, String grouping);

    /**
     * Gets a summary of Effort for and projects - overall, monthly, daily.
     * 
     * @param projects
     *            List of String
     * @param grouping
     *            String
     * @return ProjectEffort List
     */
    List<UserProjectEffort> getEffortsSummaryForProjectNames(
            List<String> projects, String grouping);

    /**
     * Returns sum of Efforts per tita project, tita user, issue tracker
     * project, issue tracker task id, issue tracker id.
     * 
     * @param tp
     *            TiTAProject
     * @param tu
     *            TiTAUser
     * @param issTProjectId
     *            Long
     * @param isstTTaskId
     *            Long
     * @param isstId
     *            Long
     * @return sum of duration of efforts
     */
    Long findEffortsSumForIssueTrackerTask(TiTAProject tp, TiTAUser tu,
            Long issTProjectId, Long isstTTaskId, Long isstId);

    /**
     * Totalize the efforts for a tita project and a tita user.
     * 
     * @param projectId
     *            - the id of the tita project
     * @param userId
     *            - the id of the tita user
     * @return the totalized effort as sum
     */
    Long totalizeEffortsForTiTAProjectAndTiTAUser(Long projectId, Long userId);

    /**
     * Lists all effort objects for a tita project and a tita user.
     * 
     * @param projectId
     *            - the id of the tita project
     * @param userId
     *            - the id of the tita user
     * @return a list of efforts
     */
    List<Effort> findEffortsForTiTAProjectAndTiTAUser(Long projectId,
            Long userId);

    /**
     * Lists all effort objects for a tita project and a tita user ordered by
     * end time.
     * 
     * @param projectId
     *            - the id of the tita project
     * @param userId
     *            - the id of the tita user
     * @return a list of efforts
     */
    List<Effort> findEffortsForTiTAProjectAndTiTAUserOrdered(Long projectId,
            Long userId);
}

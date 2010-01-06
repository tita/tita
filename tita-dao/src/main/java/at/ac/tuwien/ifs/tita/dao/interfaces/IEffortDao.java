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

import java.util.Date;
import java.util.List;

import at.ac.tuwien.ifs.tita.dao.IGenericHibernateDao;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.util.UserProjectEffort;

/**
 * 
 * Interface for EffortDao.
 * 
 * @author herbert
 */
public interface IEffortDao extends IGenericHibernateDao<Effort, Long> {

    /**
     * Gets a view for the last time efforts.
     * 
     * @param maxresults sets the max results value for the query
     * @return list of timefforts that match dates
     */
    List<Effort> getActualTimeEfforts(Integer maxresults);

    /**
     * Gets a view for a month.
     * 
     * @param year year which is selected
     * @param month month which is selected
     * @return list of efforts that match dates
     */
    List<Effort> getTimeEffortsMonthlyView(Integer year, Integer month);

    /**
     * Gets a view for a day.
     * 
     * @param date dates which are selected
     * @return list of efforts that match dates
     */
    List<Effort> getTimeEffortsDailyView(Date date);

    /**
     * Gets all years for which efforts are stored.
     * 
     * @return list of years as list of integer
     */
    List<Integer> getTimeEffortsYears();

    /**
     * Find all efforts for a specific tita-project.
     * 
     * @param projectIds List of Long
     * @param grouping String
     * @return List of Effort
     */
    List<UserProjectEffort> findEffortsForTiTAProjectId(List<String> projectIds, String grouping);

    /**
     * Find all effort of a time consumer in a specific tita-project.
     * 
     * @param projectIds List of Long
     * @param tIds List of Long
     * @param grouping String
     * @return List of Effort
     */
    List<UserProjectEffort> findEffortsForTiTAProjectAndTimeConsumerId(List<String> projectIds, List<String> tIds,
            String grouping);

    /**
     * Find all effort of a time consumer in a specific issue tracker project.
     * 
     * @param projectIds List of Long
     * @param tIds List of Long
     * @param grouping String
     * @return List of Effort
     */
    List<UserProjectEffort> findEffortsForIssueTrackerProjectAndTimeConsumerId(List<String> projectIds,
            List<String> tIds, String grouping);

    /**
     * Find all efforts of a specific time consumer in whole tita.
     * 
     * @param tcId Long
     * @return List of Effort
     */
    List<Effort> findEffortsForTimeConsumerId(Long tcId);
}

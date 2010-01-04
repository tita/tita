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
    List<Double> getTimeEffortsYears();

    /**
     * Find all efforts for a specific tita-project.
     * 
     * @param projectId List of Long
     * @return List of Effort
     */
    List<Effort> findEffortsForTiTAProjectId(List<Long> projectId);

    /**
     * Find all effort of a time consumer in a specific tita-project.
     * 
     * @param projectId List of Long
     * @param tcId Long
     * @return List of Effort
     */
    List<Effort> findEffortsForTiTAProjectAndTimeConsumerId(List<Long> projectId, Long tcId);

    /**
     * Find all efforts of a specific time consumer in whole tita.
     * 
     * @param tcId Long
     * @return List of Effort
     */
    List<Effort> findEffortsForTimeConsumerId(Long tcId);
}

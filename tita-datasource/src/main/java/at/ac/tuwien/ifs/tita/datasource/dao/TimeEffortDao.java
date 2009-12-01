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

package at.ac.tuwien.ifs.tita.datasource.dao;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;

import at.ac.tuwien.ifs.tita.datasource.entity.TimeEffort;

/**
 * TimeEffortDao.
 * 
 * @author markus
 * @author rene
 * 
 */
public class TimeEffortDao extends GenericHibernateDao<TimeEffort, Long> {

    public TimeEffortDao(){
        super(TimeEffort.class);
    }
    
    /**
     * Gets a view for a month.
     * 
     * @param cal calenderdates which are selected
     * @return list of timefforts that match dates
     */
    @SuppressWarnings("unchecked")
    public List<TimeEffort> getTimeEffortsMonthlyView(Calendar cal) {
        Query q = em.createNamedQuery("timeeffort.monthly.view");
        q.setParameter("year", cal.get(Calendar.YEAR));
        q.setParameter("month", cal.get(Calendar.MONTH) + 1);
        return (List<TimeEffort>) q.getResultList();
    }

    /**
     * Gets a view for a day.
     * 
     * @param cal calenderdates which are selected
     * @return list of timefforts that match dates
     */
    @SuppressWarnings("unchecked")
    public List<TimeEffort> getTimeEffortsDailyView(Calendar cal) {
        Query q = em.createNamedQuery("timeeffort.daily.view");
        q.setParameter("year", cal.get(Calendar.YEAR));
        q.setParameter("month", cal.get(Calendar.MONTH) + 1);
        q.setParameter("day", cal.get(Calendar.DAY_OF_MONTH));
        return q.getResultList();
    }
}

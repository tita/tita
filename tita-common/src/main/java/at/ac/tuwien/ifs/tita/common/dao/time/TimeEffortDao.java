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

package at.ac.tuwien.ifs.tita.common.dao.time;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;

import at.ac.tuwien.ifs.tita.common.dao.GenericHibernateDao;
import at.ac.tuwien.ifs.tita.entity.Effort;

/**
 * TimeEffortDao.
 * 
 * @author markus
 * @author rene
 * 
 */
public class TimeEffortDao extends GenericHibernateDao<Effort, Long> {

    public TimeEffortDao() {
        super(Effort.class);
    }

    /**
     * Gets a view for a month.
     * 
     * @param cal
     *            calenderdates which are selected
     * @return list of timefforts that match dates
     */
    @SuppressWarnings("unchecked")
    public List<Effort> getTimeEffortsMonthlyView(Calendar cal) {
        Query q = em.createNamedQuery("timeeffort.monthly.view");
        q.setParameter("year", cal.get(Calendar.YEAR));
        q.setParameter("month", cal.get(Calendar.MONTH) + 1);
        return (List<Effort>) q.getResultList();
    }

    /**
     * Gets a view for a day.
     * 
     * @param cal
     *            calenderdates which are selected
     * @return list of timefforts that match dates
     */
    @SuppressWarnings("unchecked")
    public List<Effort> getTimeEffortsDailyView(Calendar cal) {
        Query q = em.createNamedQuery("timeeffort.daily.view");
        q.setParameter("year", cal.get(Calendar.YEAR));
        q.setParameter("month", cal.get(Calendar.MONTH) + 1);
        q.setParameter("day", cal.get(Calendar.DAY_OF_MONTH));
        return q.getResultList();
    }

    /**
     * Gets a view for the last time efforts.
     * 
     * @param maxresults
     *            sets the max results value for the query
     * @return list of timefforts that match dates
     */
    public List<Effort> getActualTimeEfforts(int maxresults) {
        Query q = em.createNamedQuery("timeeffort.actual.view");
        q.setMaxResults(maxresults);
        return q.getResultList();
    }
}

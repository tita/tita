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

package at.ac.tuwien.ifs.tita.dao.time;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.ifs.tita.dao.GenericHibernateDao;
import at.ac.tuwien.ifs.tita.entity.Effort;

/**
 * TimeEffortDao.
 * 
 * @author markus
 * @author rene
 * 
 */
@Repository
public class EffortDao extends GenericHibernateDao<Effort, Long> {

    public EffortDao() {
        super(Effort.class);
    }

    /**
     * Gets a view for a month.
     * 
     * @param cal calenderdates which are selected
     * @return list of timefforts that match dates
     */
    public List<Effort> getTimeEffortsMonthlyView(Integer year, Integer month) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.set(year, month, 1);
        end.set(year, month, start.getActualMaximum(Calendar.DAY_OF_MONTH));

        return findByCriteria(Restrictions.between("date", start.getTime(), end.getTime()), Restrictions.eq("deleted",
                false));
    }

    /**
     * Gets a view for a day.
     * 
     * @param date dates which are selected
     * @return list of timefforts that match dates
     */
    public List<Effort> getTimeEffortsDailyView(Date date) {
        Effort e = new Effort();
        e.setDate(date);
        e.setDeleted(false);
        return findByExample(e);
    }

    /**
     * Gets all years in which efforts were saved.
     * 
     * @return list of years
     */
    @SuppressWarnings("unchecked")
    public List<Integer> getTimeEffortsYears() {
        // return
        // findByHqlQuery("select distinct YEAR(te.date) from Effort te where deleted=false");
        Query q = em.createNamedQuery("effort.years");
        return q.getResultList();
    }

    /**
     * Gets a view for the last time efforts.
     * 
     * @param maxresults sets the max results value for the query
     * @return list of timefforts that match dates
     */
    public List<Effort> getActualTimeEfforts(int maxresults) {
        Query q = em.createNamedQuery("timeeffort.actual.view");
        q.setMaxResults(maxresults);
        return q.getResultList();
    }
}

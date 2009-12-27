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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import at.ac.tuwien.ifs.tita.dao.GenericHibernateDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.IEffortDao;
import at.ac.tuwien.ifs.tita.entity.Effort;

/**
 * TimeEffortDao.
 * 
 * @author markus
 * @author rene
 * 
 */
public class EffortDao extends GenericHibernateDao<Effort, Long> implements IEffortDao {
    private static final String C_TITA_EFFORT_SQL = 
        "select e1.* from effort e1 " +
        "join tita_task tt on e1.tita_task_id = tt.id " +
        "join tita_project tp on tt.tita_project_id = tp.id ";
        
    private static final String C_ISSUE_TRACKER_EFFORT_SQL = 
        "select e2.* from effort e2 " +      
        "join issue_tracker_task it on e2.issuet_task_id = it.id " + 
        "join issue_tracker_project itp on it.issue_tracker_project_id =" +
        "itp.id join tita_project tp2 on tp2.id = itp.tita_project_id ";
    
    public EffortDao() {
        super(Effort.class);
    }
    /**
     * Gets a view for a month.
     * 
     * @param year year which is selected
     * @param month month whicht is selected
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
    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public List<Effort> getTimeEffortsMonthlyView(Calendar cal) {
        Query q = em.createNamedQuery("timeeffort.monthly.view");
        q.setParameter("year", cal.get(Calendar.YEAR));
        q.setParameter("month", cal.get(Calendar.MONTH) + 1);
        return q.getResultList();
    }

    /** {@inheritDoc} */
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
    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public List<Effort> getActualTimeEfforts(Integer maxresults) {
        Query q = em.createNamedQuery("timeeffort.actual.view");
        q.setMaxResults(maxresults);
        return q.getResultList();
    }

    /** {@inheritDoc} */
    @Override
    public List<Effort> findEffortsForTiTAProjectAndTimeConsumerId(Long projectId, Long tcId) {
        String queryString = C_TITA_EFFORT_SQL +
                             "where tp.id = ? and e1.user_id = ? union " +
                             C_ISSUE_TRACKER_EFFORT_SQL +
                             "where tp2.id = ? and e2.user_id = ? ";
        
        org.hibernate.SQLQuery q = getSession().createSQLQuery(queryString);
        q.setLong(0,projectId);
        q.setLong(1, tcId);
        q.setLong(2, projectId);
        q.setLong(3, tcId);
        
        return readEffortsFromDB(q);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Effort> findEffortsForTiTAProjectId(Long projectId) {
        String queryString = C_TITA_EFFORT_SQL +
                             "where tp.id = ? union " +
                             C_ISSUE_TRACKER_EFFORT_SQL +
                             "where tp2.id = ?";
        
        org.hibernate.SQLQuery q = getSession().createSQLQuery(queryString);
        q.setLong(0,projectId);
        q.setLong(1, projectId);
                
        return readEffortsFromDB(q);
    }

    /**
     * Reads all efforts specified in query and returns it to caller.
     * @param query org.hibernate.SQLQuery
     * @return List of Efforts
     */
    @SuppressWarnings("unchecked")
    private List<Effort> readEffortsFromDB(org.hibernate.SQLQuery query){
        List<Effort> efforts = new ArrayList<Effort>();
        
        query.addEntity(Effort.class);
        
        try {
            efforts = query.list();
        } catch (NoResultException e) {
            // nothing to do
        }
        return efforts;
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Effort> findEffortsForTimeConsumerId(Long tcId) {
        Criterion criterions[] = null;
        Order order[] = {Order.asc("date")};

        criterions = new Criterion [] { Restrictions.eq("user.id", tcId)};
        return findByCriteriaOrdered(criterions, order, null);
    }
}

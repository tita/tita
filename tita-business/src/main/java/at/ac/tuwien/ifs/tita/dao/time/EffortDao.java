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
import org.springframework.stereotype.Repository;

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
//        Criterion criterions[] = null;
//        
//        Order order[] = {Order.asc("date")};
//        
//      
//        criterions = new Criterion [] { Restrictions.eq("issueTTask.isstProject.projectId", 
//                                                        projectId),
//                                        Restrictions.eq("titaTask.titaProject.id", projectId),
//                                        Restrictions.eq("user.id", tcId)};
//        return findByCriteriaOrdered(criterions, order, null);
        
       return null;
    }
    
    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public List<Effort> findEffortsForTiTAProjectId(Long projectId) {
        List<Effort> efforts = new ArrayList<Effort>();
        
        
        String queryString = "select e.* from effort e " +
                             "join tita_task tt on e.tita_task_id = tt.id " +
                             "join tita_project tp on tt.tita_project_id = tp.id " +
                             "where tp.id = ? union " +
                             "select e.* from effort e " +      
                             "join issue_tracker_task it on e.issuet_task_id = it.id " + 
                             "join issue_tracker_project itp on it.issue_tracker_project_id =" +
                             "itp.id join tita_project tp2 on tp2.id = itp.tita_project_id " +
                             "where tp2.id = ?";
        
        org.hibernate.SQLQuery q = getSession().createSQLQuery(queryString);
        
        q.setLong(0,projectId);
        q.setLong(1, projectId);
        q.addEntity(Effort.class);
        
        try {
            efforts = q.list();
        } catch (NoResultException e) {
            // nothing to do
        }
        return efforts;
    }

    /** {@inheritDoc} */
    @Override
    public List<Effort> findEffortsForTimeConsumerId(Long tcId) {
        // TODO Auto-generated method stub
        return null;
    }
}

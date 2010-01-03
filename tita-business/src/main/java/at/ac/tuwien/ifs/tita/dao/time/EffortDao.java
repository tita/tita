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
import org.hibernate.criterion.Property;
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
     * @param year
     *            year which is selected
     * @param month
     *            month whicht is selected
     * @return list of timefforts that match dates
     */
    public List<Effort> getTimeEffortsMonthlyView(Integer year, Integer month) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.set(year, month, 1);
        end.set(year, month, start.getActualMaximum(Calendar.DAY_OF_MONTH));

        return findByCriteriaOrdered(new Criterion[] {
                Restrictions.between("date", start.getTime(), end.getTime()),
                Restrictions.eq("deleted", false) }, new Order[] {
                Property.forName("date").asc()}, new String[] {});
    }


    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public List<Effort> getTimeEffortsDailyView(Calendar cal) {
        Query q = em.createNamedQuery("timeeffort.daily.view");
        q.setParameter("year", cal.get(Calendar.YEAR));
        q.setParameter("month", cal.get(Calendar.MONTH) + 1);
        q.setParameter("day", cal.get(Calendar.DAY_OF_MONTH));
        return q.getResultList();
    }
    
    /**
     * Gets a view for a day.
     * 
     * @param date
     *            dates which are selected
     * @return list of timefforts that match dates
     */
    public List<Effort> getTimeEffortsDailyView(Date date) {
        return findByCriteriaOrdered(new Criterion[] {
                Restrictions.eq("date", date),
                Restrictions.eq("deleted", false) }, new Order[] { Property
                .forName("date").asc() }, new String[] {});
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public List<Integer> getTimeEffortsYears() {
        // return
        // findByHqlQuery("select distinct YEAR(te.date) from Effort te where deleted=false");
        Query q = em.createNamedQuery("effort.years");
        return q.getResultList();
    }

    /** {@inheritDoc} */
    @Override
    public List<Effort> findEffortsForTiTAProjectAndTimeConsumerId(List<Long> projectIds,Long tcId){
        String pIds = generateProjectIdList(projectIds);
        String queryString = C_TITA_EFFORT_SQL +
                             "where tp.id in (" + pIds + ") and e1.user_id = ? union " +
                             C_ISSUE_TRACKER_EFFORT_SQL +
                             "where tp2.id in (" + pIds + ") and e2.user_id = ? ";
        
        org.hibernate.SQLQuery q = getSession().createSQLQuery(queryString);
        
        q.setLong(0, tcId);
        q.setLong(1, tcId);

        return readEffortsFromDB(q);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Effort> findEffortsForTiTAProjectId(List<Long> projectIds) {
        String pIds = generateProjectIdList(projectIds);
        String queryString = C_TITA_EFFORT_SQL +
                             "where tp.id in (" + pIds + ") union " +
                             C_ISSUE_TRACKER_EFFORT_SQL +
                             "where tp2.id in (" + pIds + ")";
        
        org.hibernate.SQLQuery q = getSession().createSQLQuery(queryString);
     
        return readEffortsFromDB(q);
    }
        
    /**
     * Generates a list of strings of project ids.
     * @param projectIds List of Long
     * @return String
     */
    private String generateProjectIdList(List<Long> projectIds) {
        String pidList = "";
        
        for(Long id : projectIds){
            pidList += id + ",";
        }
        
        return pidList.substring(0, pidList.lastIndexOf(","));
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
    
    /** {@inheritDoc} */
    @Override
    public List<Effort> getActualTimeEfforts(Integer maxresults) {
        return findByCriteriaOrdered(new Criterion[] { Restrictions.eq("deleted", false) }, 
                new Order[] { Order.asc("date") }, null);
    }
}

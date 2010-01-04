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

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import at.ac.tuwien.ifs.tita.dao.GenericHibernateDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.IEffortDao;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.util.ProjectEffort;
import at.ac.tuwien.ifs.tita.entity.util.StringUtil;
import at.ac.tuwien.ifs.tita.entity.util.UserProjectEffort;

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

    /** {@inheritDoc} */
    public List<Effort> getTimeEffortsMonthlyView(Integer year, Integer month) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.set(year, month, 1);
        end.set(year, month, start.getActualMaximum(Calendar.DAY_OF_MONTH));

        return findByCriteriaOrdered(new Criterion[] { Restrictions.between("date", start.getTime(), end.getTime()),
                Restrictions.eq("deleted", false) }, new Order[] { Property.forName("date").asc() }, new String[] {});
    }

    /** {@inheritDoc} */
    public List<Effort> getTimeEffortsDailyView(Date date) {
        return findByCriteriaOrdered(
                new Criterion[] { Restrictions.eq("date", date), Restrictions.eq("deleted", false) },
                new Order[] { Property.forName("date").asc() }, new String[] {});
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public List<Double> getTimeEffortsYears() {
        String queryString = "select distinct date_part('year', te.date) from Effort te where deleted=false";
        org.hibernate.SQLQuery q = getSession().createSQLQuery(queryString);
        return q.list();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public List<UserProjectEffort> findEffortsForTiTAProjectAndTimeConsumerId(
                                                                   List<String> projectIds,
                                                                   List<String> tIds, 
                                                                   String grouping){
        String pIds = StringUtil.generateIdStringFromStringList(projectIds);
        String tcIds = StringUtil.generateIdStringFromStringList(tIds);
        
        String queryString =  "select sum(e1.duration) as DURATION, tu.username as USERNAME, " +
                              "tp.name as PROJECT ";
        
        if(grouping.equals("month")){
            queryString += ", date_part('year', e1.date) as YEAR, "+ 
                           " date_part('month', e1.date) as MONTH";
        }else if(grouping.equals("day")){
            queryString += ", date_part('year', e1.date) as YEAR, " +
                           " date_part('month', e1.date) as MONTH, date_part('day', e1.date) as DAY";
        }
        
        queryString += " from effort e1 " +
                              "join tita_task tt on e1.tita_task_id = tt.id " +
                              "join tita_project tp on tt.tita_project_id = tp.id " +
                              "join tita_user tu on tu.id = e1.user_id " +
                              "where tp.name in (" + pIds + ") and tu.username in (" + tcIds + 
                              ") ";
        
        if(grouping.equals("month")){
            queryString += " group by tp.name, tu.username, date_part('year', e1.date), "+ 
                           " date_part('month', e1.date) ";
        }else if(grouping.equals("day")){
            queryString += " group by tp.name, tu.username, date_part('year', e1.date), " +
                           " date_part('month', e1.date), date_part('day', e1.date) ";
        }else if(grouping.equals("all")){
            queryString += " group by tp.name, tu.username ";
        }
        
        queryString +=       " union all" +
                             " select sum(e2.duration) as DURATION, tu1.username as USERNAME, " +
                             " tp2.name as PROJECT ";
        
        if(grouping.equals("month")){
            queryString += ", date_part('year', e2.date) as YEAR, "+ 
                           " date_part('month', e2.date) as MONTH";
        }else if(grouping.equals("day")){
            queryString += ", date_part('year', e2.date) as YEAR, " +
                           " date_part('month', e2.date) as MONTH, date_part('day', e2.date) as DAY";
        }
        
        
        queryString +=       " from effort e2 " +      
                             "join issue_tracker_task it on e2.issuet_task_id = it.id " + 
                             "join issue_tracker_project itp on it.issue_tracker_project_id =" +
                             "itp.id join tita_project tp2 on tp2.id = itp.tita_project_id " +
                             "join tita_user tu1 on tu1.id = e2.user_id " +
                             "where tp2.name in (" + pIds + ") and tu1.username in (" + tcIds + ")";

         if(grouping.equals("month")){
             queryString += " group by tp2.name, tu1.username, date_part('year', e2.date), "+ 
                           " date_part('month', e2.date) ";
         }else if(grouping.equals("day")){
             queryString += " group by tp2.name, tu1.username, date_part('year', e2.date), " +
                           " date_part('month', e2.date), date_part('day', e2.date) ";
         }else if(grouping.equals("all")){
             queryString += " group by tp2.name, tu1.username";
         }
         
         org.hibernate.SQLQuery query = getSession().createSQLQuery(queryString);
         List<UserProjectEffort> efforts = new ArrayList<UserProjectEffort>();
             
         query.addEntity(UserProjectEffort.class);
         try {
             efforts = query.list();
         } catch (NoResultException e) {
             // nothing to do
         }
         return efforts;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public List<ProjectEffort> findEffortsForTiTAProjectId(List<String> projectIds, 
                                                               String grouping) {
        String pIds = StringUtil.generateIdStringFromStringList(projectIds);
        String queryString =  "select sum(e1.duration) as DURATION, tp.name as PROJECT ";
        
        if(grouping.equals("month")){
            queryString += ", date_part('year', e1.date) as YEAR, "+
                           " date_part('month', e1.date) as MONTH ";
        }else if(grouping.equals("day")){
            queryString += ", date_part('year', e1.date) as YEAR, " +
                           " date_part('month', e1.date) as MONTH, date_part('day',e1.date) as DAY";
        }
        
        queryString +=      " from effort e1 join tita_task tt on e1.tita_task_id = tt.id " +
                            "join tita_project tp on tt.tita_project_id = tp.id " +
                            "where tp.name in (" + pIds + ") ";
        
        if(grouping.equals("month")){
            queryString += " group by tp.name, date_part('year', e1.date), "+
                           " date_part('month', e1.date) ";
        }else if(grouping.equals("day")){
            queryString += " group by tp.name, date_part('year', e1.date), " +
                           " date_part('month', e1.date), date_part('day', e1.date) ";
        }else if(grouping.equals("all")){
            queryString += " group by tp.name ";
        }
        
        queryString +=       " union all" +
               " select sum(e2.duration) as DURATION, tp2.name as PROJECT ";
        
        if(grouping.equals("month")){
            queryString += ", date_part('year', e2.date) as YEAR, "+
                           " date_part('month', e2.date) as MONTH ";
        }else if(grouping.equals("day")){
            queryString += ", date_part('year', e2.date) as YEAR, " +
                           " date_part('month', e2.date) as MONTH, date_part('day',e2.date) as DAY";
        }
        
        queryString +=  " from effort e2 " +      
               "join issue_tracker_task it on e2.issuet_task_id = it.id " + 
               "join issue_tracker_project itp on it.issue_tracker_project_id =" +
               "itp.id join tita_project tp2 on tp2.id = itp.tita_project_id " +
               "where tp2.name in (" + pIds + ") ";
        
        if(grouping.equals("month")){
            queryString += " group by tp2.name, date_part('year', e2.date), "+ 
                           " date_part('month', e2.date) ";
        }else if(grouping.equals("day")){
            queryString += " group by tp2.name, date_part('year', e2.date), " +
                           " date_part('month', e2.date), date_part('day', e2.date) ";
        }else if(grouping.equals("all")){
            queryString += " group by tp2.name ";
        }
        
        Query query = em.createNativeQuery(queryString);
        List<ProjectEffort> efforts = new ArrayList<ProjectEffort>();
        
        try {
            efforts = query.getResultList();
        } catch (NoResultException e) {
            // nothing to do
        }
        return efforts;
    }

    /** {@inheritDoc} */
    @Override
    public List<Effort> findEffortsForTimeConsumerId(Long tcId) {
        Criterion criterions[] = null;
        Order order[] = { Order.asc("date") };

        criterions = new Criterion[] { Restrictions.eq("user.id", tcId) };
        return findByCriteriaOrdered(criterions, order, null);
    }

    /** {@inheritDoc} */
    @Override
    public List<Effort> getActualTimeEfforts(Integer maxresults) {
        return findByCriteriaOrdered(new Criterion[] { Restrictions.eq("deleted", false) }, new Order[] { Order
                .asc("date") }, null);
    }
}

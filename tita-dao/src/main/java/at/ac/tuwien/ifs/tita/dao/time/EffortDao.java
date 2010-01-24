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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import at.ac.tuwien.ifs.tita.dao.GenericHibernateDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.IEffortDao;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.util.StringUtil;
import at.ac.tuwien.ifs.tita.entity.util.UserProjectEffort;

/**
 * TimeEffortDao.
 * 
 * @author markus
 * 
 */
public class EffortDao extends GenericHibernateDao<Effort, Long> implements IEffortDao {
    private static final int C_FETCHSIZE = 1000;

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
    public List<Integer> getTimeEffortsYears() {
        String query = "select distinct year(te.date) from Effort te where deleted=false order by 1 desc";
        Query q = getSession().createQuery(query);
        return q.list();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public List<UserProjectEffort> findEffortsForTiTAProjectAndTimeConsumerId(List<String> projectIds,
            List<String> tIds, String grouping) {
        String pIds = StringUtil.generateIdStringFromStringList(projectIds);
        String tcIds = StringUtil.generateIdStringFromStringList(tIds);

        String queryString = "select nextval('USER_PROJECT_EFFORT_1_ID_SEQ') as ID, "
                + " sum(duration) as DURATION, username as USERNAME, project as PROJECT";

        if (grouping.equals("month")) {
            queryString += ", year as YEAR, month as MONTH, null as DAY";
        } else if (grouping.equals("day")) {
            queryString += ", year as YEAR, month as MONTH, day as DAY";
        } else if (grouping.equals("overall")) {
            queryString += ", null as YEAR, null as MONTH, null as DAY";
        }

        queryString += " from (select sum(e1.duration) as duration, tu.username as username," + " tp.name as project ";

        if (grouping.equals("month")) {
            queryString += ", date_part('year', e1.date) as YEAR, "
                    + " date_part('month', e1.date) as MONTH, null as DAY";
        } else if (grouping.equals("day")) {
            queryString += ", date_part('year', e1.date) as YEAR, " + " date_part('month', e1.date) as MONTH, "
                    + " date_part('day', e1.date) as DAY";
        } else if (grouping.equals("overall")) {
            queryString += ", null as YEAR, null as MONTH, null as DAY";
        }

        queryString += " from effort e1 " + "join tita_task tt on e1.tita_task_id = tt.id "
                + "join tita_project tp on tt.tita_project_id = tp.id " + "join tita_user tu on tu.id = e1.user_id "
                + "where tp.name in (" + pIds + ") and tu.username in (" + tcIds + ") ";

        if (grouping.equals("month")) {
            queryString += " group by tp.name, tu.username, date_part('year', e1.date), "
                    + " date_part('month', e1.date) ";
        } else if (grouping.equals("day")) {
            queryString += " group by tp.name, tu.username, date_part('year', e1.date), "
                    + " date_part('month', e1.date), date_part('day', e1.date) ";
        } else if (grouping.equals("overall")) {
            queryString += " group by tp.name, tu.username ";
        }

        queryString += " union all" + " select sum(e2.duration) as duration, tu1.username as username, "
                + " tp2.name as project ";

        if (grouping.equals("month")) {
            queryString += ", date_part('year', e2.date) as YEAR, "
                    + " date_part('month', e2.date) as MONTH, null as DAY";
        } else if (grouping.equals("day")) {
            queryString += ", date_part('year', e2.date) as YEAR, "
                    + " date_part('month', e2.date) as MONTH, date_part('day',e2.date) as DAY";
        } else if (grouping.equals("overall")) {
            queryString += ", null as YEAR, null as MONTH, null as DAY";
        }

        queryString += " from effort e2 " + "join issue_tracker_task it on e2.issuet_task_id = it.id "
                + "join issue_tracker_project itp on it.issue_tracker_project_id ="
                + "itp.id join tita_project tp2 on tp2.id = itp.tita_project_id "
                + "join tita_user tu1 on tu1.id = e2.user_id " + "where tp2.name in (" + pIds
                + ") and tu1.username in (" + tcIds + ")";

        if (grouping.equals("month")) {
            queryString += " group by tp2.name, tu1.username, date_part('year', e2.date), "
                    + " date_part('month', e2.date)";
        } else if (grouping.equals("day")) {
            queryString += " group by tp2.name, tu1.username, date_part('year', e2.date), "
                    + " date_part('month', e2.date), date_part('day', e2.date)";
        } else if (grouping.equals("overall")) {
            queryString += " group by tp2.name, tu1.username";
        }

        queryString += ") as U group by project, username, year, month, day "
                + " order by project, year, month, day, duration, username";

        org.hibernate.SQLQuery q = getSession().createSQLQuery(queryString);
        q.addEntity(UserProjectEffort.class);
        q.setFetchSize(C_FETCHSIZE);

        List<UserProjectEffort> efforts = null;

        try {
            efforts = q.list();
        } catch (NoResultException e) {
            // nothing to do
        }
        return efforts;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public List<UserProjectEffort> findEffortsForTiTAProjectId(List<String> projectIds, String grouping) {
        String pIds = StringUtil.generateIdStringFromStringList(projectIds);

        String queryString = "select nextval('USER_PROJECT_EFFORT_1_ID_SEQ') as ID, "
                + " sum(duration) as DURATION, project as PROJECT, null as USERNAME";

        if (grouping.equals("month")) {
            queryString += ", year as YEAR, month as MONTH, null as DAY";
        } else if (grouping.equals("day")) {
            queryString += ", year as YEAR, month as MONTH, day as DAY";
        } else if (grouping.equals("overall")) {
            queryString += ", null as YEAR, null as MONTH, null as DAY";
        }

        queryString += " from (select sum(e1.duration) as duration, tp.name as project ";

        if (grouping.equals("month")) {
            queryString += ", date_part('year', e1.date) as YEAR, "
                    + " date_part('month', e1.date) as MONTH, null as DAY ";
        } else if (grouping.equals("day")) {
            queryString += ", date_part('year', e1.date) as YEAR, "
                    + " date_part('month', e1.date) as MONTH, date_part('day',e1.date) as DAY";
        } else if (grouping.equals("overall")) {
            queryString += ", null as YEAR, null as MONTH, null as DAY";
        }

        queryString += " from effort e1 join tita_task tt on e1.tita_task_id = tt.id "
                + "join tita_project tp on tt.tita_project_id = tp.id " + "where tp.name in (" + pIds + ") ";

        if (grouping.equals("month")) {
            queryString += " group by tp.name, date_part('year', e1.date), " + " date_part('month', e1.date) ";
        } else if (grouping.equals("day")) {
            queryString += " group by tp.name, date_part('year', e1.date), "
                    + " date_part('month', e1.date), date_part('day', e1.date) ";
        } else if (grouping.equals("overall")) {
            queryString += " group by tp.name ";
        }

        queryString += " union all" + " select sum(e2.duration) as duration, tp2.name as project ";

        if (grouping.equals("month")) {
            queryString += ", date_part('year', e2.date) as year, "
                    + " date_part('month', e2.date) as month, null as day ";
        } else if (grouping.equals("day")) {
            queryString += ", date_part('year', e2.date) as year, "
                    + " date_part('month', e2.date) as month, date_part('day',e2.date) as day";
        } else if (grouping.equals("overall")) {
            queryString += ", null as year, null as month, null as day";
        }

        queryString += " from effort e2 " + "join issue_tracker_task it on e2.issuet_task_id = it.id "
                + "join issue_tracker_project itp on it.issue_tracker_project_id ="
                + "itp.id join tita_project tp2 on tp2.id = itp.tita_project_id " + "where tp2.name in (" + pIds + ") ";

        if (grouping.equals("month")) {
            queryString += " group by tp2.name, date_part('year', e2.date), " + " date_part('month', e2.date) ";
        } else if (grouping.equals("day")) {
            queryString += " group by tp2.name, date_part('year', e2.date), "
                    + " date_part('month', e2.date), date_part('day', e2.date) ";
        } else if (grouping.equals("overall")) {
            queryString += " group by tp2.name ";
        }

        queryString += ") as U group by project, year, month, day, username "
                + " order by project, year, month, day, duration";

        org.hibernate.SQLQuery q = getSession().createSQLQuery(queryString);
        q.addEntity(UserProjectEffort.class);
        q.setFetchSize(C_FETCHSIZE);
        List<UserProjectEffort> efforts = new ArrayList<UserProjectEffort>();

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
        Criterion criterions[] = null;
        Order order[] = { Order.asc("date") };

        criterions = new Criterion[] { Restrictions.eq("user.id", tcId) };
        return findByCriteriaOrdered(criterions, order, null);
    }

    /** {@inheritDoc} */
    @Override
    public Long findEffortsSumForIssueTrackerTask(Long tpId, String username, Long issTProjectId, Long isstTTaskId,
            Long isstId) {
        String queryString = "select sum(duration) as duration from effort e "
                + "join issue_tracker_task itt on e.issuet_task_id = itt.id "
                + "join issue_tracker_project itp on itt.issue_tracker_project_id = itp.id "
                + "join tita_project tp on itp.tita_project_id = tp.id " + "join tita_user tu on e.user_id = tu.id "
                + "where tp.id = ? and itp.isst_id = ? and itp.isst_project_id = ? "
                + "and itt.isst_task_id = ? and tu.username = ?";

        org.hibernate.SQLQuery q = getSession().createSQLQuery(queryString);
        // q.addEntity(Long.class);
        // CHECKSTYLE:OFF
        q.setParameter(0, tpId);
        q.setParameter(1, isstId);
        q.setParameter(2, issTProjectId);
        q.setParameter(3, isstTTaskId);
        q.setParameter(4, username);
        // CHECKSTYLE:ON

        Object obj = q.uniqueResult();

        if (obj != null) {
            return new Long(((BigDecimal) obj).longValue());
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Long findEffortsSumForTiTATasks(Long projectId, Long userId, Long taskId) {
        String queryString = "select sum(e.duration) as sum " + "from Effort e " + "join e.titaTask as tt "
                + "join tt.titaProject as tp " + "where e.user = " + userId + " and tp.id = " + projectId
                + " and tt.id=" + taskId + " and e.deleted != true";

        Query q = getSession().createQuery(queryString);

        Long result = (Long) q.uniqueResult();
        return (result != null) ? result : 0L;
    }

    /** {@inheritDoc} */
    @Override
    public Long findEffortsSumForIssueTrackerTasks(Long projectId, Long userId, Long taskId) {
        String queryString = "select sum(e.duration) as sum " + "from Effort e " + "join e.issueTTask as itt "
                + "join itt.isstProject as itp " + "join itp.titaProject as tp " + "where e.user = " + userId
                + " and tp.id = " + projectId + " and itt.id= " + taskId + " and e.deleted != true";

        Query q = getSession().createQuery(queryString);

        Long result = (Long) q.uniqueResult();
        return (result != null) ? result : 0L;
    }

    /** {@inheritDoc} */
    @Override
    public Long totalizeEffortsForTiTAProjectAndTiTAUser(Long projectId, Long userId) {

        String first = "select sum(e.duration) as sum " + "from Effort e " + "join e.titaTask as tt "
                + "join tt.titaProject as tp " + "where e.user = " + userId + " and tp.id = " + projectId
                + " and e.deleted != true";

        String second = "select sum(e.duration) as sum " + "from Effort e " + "join e.issueTTask as itt "
                + "join itt.isstProject as itp " + "join itp.titaProject as tp " + "where e.user = " + userId
                + " and tp.id = " + projectId + " and e.deleted != true";

        Query query1 = getSession().createQuery(first);
        Query query2 = getSession().createQuery(second);
        Long sumQuery1 = (Long) query1.uniqueResult();
        Long sumQuery2 = (Long) query2.uniqueResult();

        if (sumQuery1 != null && sumQuery2 != null) {
            return sumQuery1 + sumQuery2;
        } else if (sumQuery1 != null && sumQuery2 == null) {
            return sumQuery1;
        } else if (sumQuery1 == null && sumQuery2 != null) {
            return sumQuery2;
        } else {
            return 0L;
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<Effort> findEffortsForTiTAProjectAndTiTAUser(Long projectId, Long userId) {

        String first = "select e " + "from Effort e " + "join e.titaTask as tt " + "join tt.titaProject as tp "
                + "where e.user = " + userId + " and tp.id = " + projectId + " and e.deleted != true";

        String second = "select e " + "from Effort e " + "join e.issueTTask as itt " + "join itt.isstProject as itp "
                + "join itp.titaProject as tp " + "where e.user = " + userId + " and tp.id = " + projectId
                + " and e.deleted != true";

        Query query1 = getSession().createQuery(first);
        Query query2 = getSession().createQuery(second);

        List<Effort> list1 = query1.list();
        List<Effort> list2 = query2.list();
        list1.addAll(list2);

        List<Effort> effortList = list1;
        return effortList;
    }

    /** {@inheritDoc} */
    @Override
    public List<Effort> findEffortsForTiTAProjectAndTiTAUserOrdered(Long projectId, Long userId) {
        List<Effort> returnValue = new ArrayList<Effort>();

        String queryString = "select e1.ID, e1.DESCRIPTION, e1.TITA_TASK_ID, e1.ISSUET_TASK_ID, "
                + "e1.DATE, e1.START_TIME, e1.END_TIME, e1.DURATION, e1.DELETED, " + "e1.USER_ID from Effort e1 "
                + "join tita_task tt on e1.tita_task_id = tt.id "
                + "join tita_project tp on tt.tita_project_id = tp.id " + "join tita_user tu on tu.id = e1.user_id "
                + "where tu.id = " + userId + " and tp.id = " + projectId + " and e1.deleted != true " + "UNION "
                + "select e2.ID, e2.DESCRIPTION, e2.TITA_TASK_ID, e2.ISSUET_TASK_ID, e2.DATE, "
                + "e2.START_TIME, e2.END_TIME, e2.DURATION, e2.DELETED, " + "e2.USER_ID from Effort e2 "
                + "join issue_tracker_task itt on e2.issuet_task_id = itt.id "
                + "join issue_tracker_project itp on itt.issue_tracker_project_id = itp.id "
                + "join tita_project tp2 on tp2.id = itp.tita_project_id "
                + "join tita_user tu2 on tu2.id = e2.user_id " + "where tu2.id = " + userId + " and tp2.id = "
                + projectId + " and e2.deleted != true " + "order by date desc, end_time desc";

        org.hibernate.SQLQuery q = getSession().createSQLQuery(queryString);
        q.addEntity(Effort.class);
        q.setFetchSize(C_FETCHSIZE);

        try {
            returnValue = q.list();
        } catch (NoResultException e) {
            // no results
        }
        return returnValue;
    }
}

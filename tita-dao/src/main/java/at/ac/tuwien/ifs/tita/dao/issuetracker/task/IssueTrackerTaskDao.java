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
package at.ac.tuwien.ifs.tita.dao.issuetracker.task;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.ifs.tita.dao.GenericHibernateDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.IIssueTrackerTaskDao;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;

/**
 * IssueTrackerTask Data Access Object.
 * 
 * @author Christoph
 * 
 */
@Repository
public class IssueTrackerTaskDao extends GenericHibernateDao<IssueTrackerTask, Long> implements IIssueTrackerTaskDao {

    public IssueTrackerTaskDao() {
        super(IssueTrackerTask.class);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public IssueTrackerTask findIssueTrackerTask(Long tp, Long it, Long itp, Long itt) {
        String queryString = "select * from issue_tracker_task itt "
                + "join issue_tracker_project itp on itt.issue_tracker_project_id = itp.id "
                + "join tita_project tp on itp.tita_project_id = tp.id "
                + "where tp.id = ? and itp.isst_id = ? and itp.isst_project_id = ? " + "and itt.isst_task_id = ? ";

        org.hibernate.SQLQuery q = getSession().createSQLQuery(queryString);
        q.addEntity(IssueTrackerTask.class);
        // CHECKSTYLE:OFF
        q.setParameter(0, tp);
        q.setParameter(1, it);
        q.setParameter(2, itp);
        q.setParameter(3, itt);
        // CHECKSTYLE:ON

        List<IssueTrackerTask> task = q.list();

        if (task.size() != 0) {
            return task.get(0);
        } else {
            return null;
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public List<IssueTrackerTask> findIssueTrackerTasksforUserProject(Long projectId, Long userId) {
        String queryString = "select distinct itt from Effort e " + "join e.issueTTask as itt "
                + "join itt.isstProject as itp " + "join itp.titaProject as tp " + "where e.user = " + userId
                + " and tp.id = " + projectId + " and e.deleted != true";

        Query q = getSession().createQuery(queryString);

        return q.list();
    }
}

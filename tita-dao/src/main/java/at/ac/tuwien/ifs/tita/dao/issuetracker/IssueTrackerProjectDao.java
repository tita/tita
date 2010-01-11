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
package at.ac.tuwien.ifs.tita.dao.issuetracker;

import at.ac.tuwien.ifs.tita.dao.GenericHibernateDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.IIssueTrackerProjectDao;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;

/**
 * Dao for issue tracker projects.
 * 
 * @author herbert
 * 
 */
public class IssueTrackerProjectDao extends GenericHibernateDao<IssueTrackerProject, Long> implements
        IIssueTrackerProjectDao {

    public IssueTrackerProjectDao() {
        super(IssueTrackerProject.class);
    }

    /** {@inheritDoc} */
    @Override
    public IssueTrackerProject findIssueTrackerProjectForTiTAProject(Long tp, Long issueTrackerId, Long itp) {
        String queryString = "select * from issue_tracker_project itp "
                + "join tita_project tp on itp.tita_project_id = tp.id "
                + "where tp.id = ? and itp.isst_id = ? and itp.isst_project_id = ? ";

        org.hibernate.SQLQuery q = getSession().createSQLQuery(queryString);
        q.addEntity(IssueTrackerProject.class);
        // CHECKSTYLE:OFF
        q.setParameter(0, tp);
        q.setParameter(1, issueTrackerId);
        q.setParameter(2, itp);
        // CHECKSTYLE:ON

        return (IssueTrackerProject) q.list().get(0);
    }
}

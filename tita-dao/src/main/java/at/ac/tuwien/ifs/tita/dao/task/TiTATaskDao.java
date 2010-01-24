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
package at.ac.tuwien.ifs.tita.dao.task;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.ifs.tita.dao.GenericHibernateDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.ITiTATaskDao;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;

/**
 * TiTATask Data Access Object.
 * 
 * @author rene
 * 
 */
@Repository
public class TiTATaskDao extends GenericHibernateDao<TiTATask, Long> implements ITiTATaskDao {

    public TiTATaskDao() {
        super(TiTATask.class);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public List<TiTATask> findTiTATasksforUserProject(TiTAProject project, TiTAUser user) {
        return findByCriteria(new Criterion[] { Restrictions.eq("titaProject", project), Restrictions.eq("user", user) });
        // String queryString =
        // "select task from TiTATask tt where tt.titaProject = " + projectId +
        // " and tt.user = "
        // + userId;

        // Query q = getSession().createQuery(queryString);
        // return q.list();
    }
}

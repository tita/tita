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
package at.ac.tuwien.ifs.tita.dao.project;

import java.util.List;

import at.ac.tuwien.ifs.tita.dao.GenericHibernateDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.ITiTAProjectDao;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;

/**
 * Data acces object for TiTAProject.
 * 
 * @author herbert
 * 
 */
public class TiTAProjectDao extends GenericHibernateDao<TiTAProject, Long> 
    implements ITiTAProjectDao {

    public TiTAProjectDao() {
        super(TiTAProject.class);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public List<TiTAProject> findTiTAProjectsForUsername(String username) {
        String queryString = "select * from TITA_PROJECT tp, USER_PROJECT up, TITA_USER tu "
                + " where tp.id = up.tita_project_id and up.user_id = tu.id and tu.username = ? ";

        org.hibernate.SQLQuery query = getSession().createSQLQuery(queryString);
        query.setParameter(0, username);
        query.addEntity(TiTAProject.class);

        return query.list();
    }
}

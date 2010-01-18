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
package at.ac.tuwien.ifs.tita.dao.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.ifs.tita.dao.GenericHibernateDao;
import at.ac.tuwien.ifs.tita.dao.interfaces.IUserDAO;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.util.StringUtil;

/**
 * UserDAO encapsulates CRUD-Operations for the TiTAUser.
 * 
 * @author ASE Group 10
 */
@Repository
public class UserDAO extends GenericHibernateDao<TiTAUser, Long> implements IUserDAO {

    /**
     * public constructor, needed for telling the generic EntityManager the
     * actual type.
     */
    public UserDAO() {
        super(TiTAUser.class);
    }

    /** {@inheritDoc} */
    @Override
    public TiTAUser findByUserName(String username) {
        TiTAUser u = new TiTAUser();
        u.setDeleted(false);
        u.setUserName(username);

        List<TiTAUser> users = findByExample(u);

        if (users.size() > 0) {
            u = users.get(0);
        }

        return u;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public List<TiTAUser> findUsersForProjectNames(List<String> projects) {
        String names = StringUtil.generateIdStringFromStringList(projects);

        String queryString = "select u.* from tita_user u join user_project up on "
                + "u.id = up.user_id join tita_project tp on up.tita_project_id = tp.id " + 
                " where tp.name in ("
                + names + ")";

        org.hibernate.SQLQuery query = getSession().createSQLQuery(queryString);

        List<TiTAUser> users = new ArrayList<TiTAUser>();

        query.addEntity(TiTAUser.class);

        try {
            users = query.list();
        } catch (NoResultException e) {
            // nothing to do
        }
        return users;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public List<TiTAUser> findUsersForTiTAProject(TiTAProject project) {

        String queryString = "select u.* from tita_user u join user_project up on "
                + "u.id = up.user_id join tita_project tp on up.tita_project_id = tp.id " +
                " where tp.name in (" + ")";

        // String first =
        // "select e.id, e.description, e.date, e.startTime, e.endTime, e.duration, e.deleted "
        // + "from Effort e "
        // + "join e.titaTask as tt "
        // + "join tt.titaProject as tp "
        // + "where e.user = "
        // + userId
        // + " and tp.id = "
        // + projectId
        // + " and e.deleted != true";

        queryString = "select u " + "from TiTAUserProject tup," + " TiTAUser as u "
                + "where tup.user = u.id and tup.project = " + project.getId();

        // u.id = r.id and - , Role as r

        Query query = getSession().createQuery(queryString);
        List<TiTAUser> users = new ArrayList<TiTAUser>();

        try {
            users = query.list();
        } catch (NoResultException e) {
            // nothing to do
        }

        // List<TiTAUser> newUsers = new ArrayList<TiTAUser>();
        //
        // for (int i = 0; i < users.size(); i++) {
        // newUsers.add(this.findById(users.get(0).getId()));
        // }

        return users;
    }

    /** {@inheritDoc} */
    @Override
    public Long findTargetHoursForTiTAProjectAndTiTAUser(Long userId, Long projectId) {

        String queryString = "select tup.targetHours " + "from TiTAUserProject tup " + 
                "where tup.user = " + userId
                + " and tup.project = " + projectId;

        Query query = getSession().createQuery(queryString);
        Long targetHours = (Long) query.uniqueResult();

        if (targetHours == null || targetHours == -1) {
            return null;
        } else {
            return targetHours;
        }
    }
    
    /** {@inheritDoc} **/
    @Override
    @SuppressWarnings("unchecked")
    public List<TiTAUser> findUsersOrdered(int maxResult) throws PersistenceException {
        Criteria crit = getSession().createCriteria(TiTAUser.class);

        if (maxResult > 0) {
            crit.setMaxResults(maxResult);
        }

        crit.addOrder(Order.asc("userName"));
        crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        return crit.list();
    }
}

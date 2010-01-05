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

import org.springframework.stereotype.Repository;

import at.ac.tuwien.ifs.tita.dao.GenericHibernateDao;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.util.StringUtil;

/**
 * UserDAO encapsulates CRUD-Operations for the TiTAUser.
 *
 * @author ASE Group 10
 */
@Repository
public class UserDAO extends GenericHibernateDao<TiTAUser, Long> implements IUserDAO{

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
    @Override
    public List<TiTAUser> findUsersForProjectNames(List<String> projects) {
        String names = StringUtil.generateIdStringFromStringList(projects);

        String queryString = "select u.* from tita_user u join user_project up on " +
                             "u.id = up.user_id join tita_project tp on up.project_id = tp.id " +
                             " where tp.name in (" + names + ")";

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
}

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
                            implements ITiTAProjectDao{

    public TiTAProjectDao() {
        super(TiTAProject.class);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public List<TiTAProject> findTiTAProjectsForUsername(String username) {
        String queryString = "select * from TITA_PROJECT tp, USER_PROJECT up, TITA_USER tu "+
                             " where tp.id = up.project_id and up.user_id = tu.id and "+
                             " tu.username = ? ";

        org.hibernate.SQLQuery query = getSession().createSQLQuery(queryString);
        query.setParameter(0, username);
        query.addEntity(TiTAProject.class);

        return query.list();
    }
}

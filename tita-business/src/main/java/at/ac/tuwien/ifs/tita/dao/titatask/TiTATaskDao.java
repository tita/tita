package at.ac.tuwien.ifs.tita.dao.titatask;

import org.springframework.stereotype.Repository;

import at.ac.tuwien.ifs.tita.dao.GenericHibernateDao;
import at.ac.tuwien.ifs.tita.entity.TiTATask;

/**
 * TiTATask Data Access Object.
 * 
 * @author Christoph
 * 
 */
@Repository
public class TiTATaskDao extends GenericHibernateDao<TiTATask, Long> {

}

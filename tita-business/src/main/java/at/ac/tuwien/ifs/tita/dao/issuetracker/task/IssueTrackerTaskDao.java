package at.ac.tuwien.ifs.tita.dao.issuetracker.task;

import org.springframework.stereotype.Repository;

import at.ac.tuwien.ifs.tita.dao.GenericHibernateDao;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;

/**
 * IssueTrackerTask Data Access Object.
 * 
 * @author Christoph
 * 
 */
@Repository
public class IssueTrackerTaskDao extends GenericHibernateDao<IssueTrackerTask, Long> {

    public IssueTrackerTaskDao() {
        super(IssueTrackerTask.class);
    }

}

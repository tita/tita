package at.ac.tuwien.ifs.tita.dao.issuetracker;

import at.ac.tuwien.ifs.tita.dao.GenericHibernateDao;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;

/**
 * Issue Tracker Data Access Object.
 * 
 * @author Christoph
 * 
 */
public class IssueTrackerDao extends GenericHibernateDao<IssueTracker, Long> {

    public IssueTrackerDao() {
        super(IssueTracker.class);
    }
}

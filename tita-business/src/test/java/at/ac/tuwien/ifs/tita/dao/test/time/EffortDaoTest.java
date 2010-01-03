package at.ac.tuwien.ifs.tita.dao.test.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.ifs.tita.dao.IGenericHibernateDao;
import at.ac.tuwien.ifs.tita.dao.time.EffortDao;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.TiTAUserProject;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;
import at.ac.tuwien.ifs.tita.entity.conv.Role;

/**
 * Test for EffortDao.
 *
 * @author herbert
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:datasourceContext-test.xml" })
@TransactionConfiguration
@Transactional
public class EffortDaoTest { //extends AbstractJpaTests {
    private TiTAProject tip1;
    private TiTAUser us1, us2;
    private Role r1;

    @Autowired
    private EffortDao timeEffortDAO;

    @Qualifier("titaProjectDAO")
    @Autowired
    private IGenericHibernateDao<TiTAProject, Long> titaProjectDAO;

    @Qualifier("userTitaDAO")
    @Autowired
    private IGenericHibernateDao<TiTAUserProject, Long> utpDao;

    @Qualifier("roleDAO")
    @Autowired
    private IGenericHibernateDao<Role, Long> roleDao;

    public EffortDaoTest() {

    }

    /**
     * Prepare on TiTA Project for testing effort dao.
     */
    @Before
    public void prepareProjects(){
//        Long id, Date creationDate, Long startTime, Long duration, Boolean deleted,
//        String description
        //CHECKSTYLE:OFF

        IssueTracker it = new IssueTracker(1L,"issue tracker 1",null);

        this.r1 = new Role(1L, "role 1");
        this.roleDao.save(this.r1);

        Effort et1 = null, et2 = null, et3 = null, et4 = null,
               ei1 = null, ei2 = null, ei3 = null, ei4 = null;

        this.us1 = new TiTAUser(null, null, null, null, null, null, this.r1, null, null);
        this.us2 = new TiTAUser(null, null, null, null, null, null, this.r1, null, null);

        et1 = new Effort(new Date(System.currentTimeMillis()), 1000L, false,
                "tita task 1 - effort 1",this.us1);
        et2 = new Effort(new Date(System.currentTimeMillis()),2000L, false,
                "tita task 1 - effort 2",this.us1);
        et3 = new Effort(new Date(System.currentTimeMillis()),5000L, false,
                "tita task 2 - effort 1",this.us2);
        et4 = new Effort(new Date(System.currentTimeMillis()),3000L, false,
                "tita task 2 - effort 2",this.us1);
        ei1 = new Effort(new Date(System.currentTimeMillis()),2000L, false,
                "issuetracker task 1 - effort 1",this.us2);
        ei2 = new Effort(new Date(System.currentTimeMillis()),8000L, false,
                "issuetracker task 1 - effort 2",this.us1);
        ei3 = new Effort(new Date(System.currentTimeMillis()),1000L, false,
                "issuetracker task 2 - effort 1",this.us2);
        ei4 = new Effort(new Date(System.currentTimeMillis()),7000L, false,
                "issuetracker task 2 - effort 2",this.us1);

        Set<Effort> se1 = new HashSet<Effort>();
        se1.add(et1);
        se1.add(et2);

        Set<Effort> se2 = new HashSet<Effort>();
        se2.add(et3);
        se2.add(et4);

        Set<Effort> se3 = new HashSet<Effort>();
        se3.add(ei1);
        se3.add(ei2);

        Set<Effort> se4 = new HashSet<Effort>();
        se4.add(ei3);
        se4.add(ei4);

        TiTATask tit1 = new TiTATask(this.us1,se1);
        TiTATask tit2 = new TiTATask(this.us2,se2);

        et1.setTitaTask(tit1);
        et2.setTitaTask(tit1);
        et3.setTitaTask(tit2);
        et4.setTitaTask(tit2);

        Set<TiTATask> sa1 = new HashSet<TiTATask>();
        sa1.add(tit1);
        sa1.add(tit2);

        IssueTrackerTask itt1 = new IssueTrackerTask(se3);
        IssueTrackerTask itt2 = new IssueTrackerTask(se4);

        ei1.setIssueTTask(itt1);
        ei2.setIssueTTask(itt1);
        ei3.setIssueTTask(itt2);
        ei4.setIssueTTask(itt2);

        Set<IssueTrackerTask> si1 = new HashSet<IssueTrackerTask>();
        si1.add(itt1);

        Set<IssueTrackerTask> si2 = new HashSet<IssueTrackerTask>();
        si2.add(itt2);

        IssueTrackerProject ip1 = new IssueTrackerProject(it,97L,si1);

        itt1.setIsstProject(ip1);
        itt2.setIsstProject(ip1);

        IssueTrackerProject ip2 = new IssueTrackerProject(it,98L,si2);

        Set<IssueTrackerProject> sip = new HashSet<IssueTrackerProject>();
        sip.add(ip1);
        sip.add(ip2);


        this.tip1 = new TiTAProject("bla", "bla", false, null,
                sa1,sip);

        ip1.setTitaProject(this.tip1);
        ip2.setTitaProject(this.tip1);

        tit2.setTitaProject(this.tip1);
        tit1.setTitaProject(this.tip1);

        this.titaProjectDAO.save(this.tip1);
        this.titaProjectDAO.flushnClear();

        TiTAUserProject utp1 = new TiTAUserProject(this.us1, this.tip1, null);
        TiTAUserProject utp2 = new TiTAUserProject(this.us2, this.tip1, null);

        this.utpDao.save(utp1);
        this.utpDao.save(utp2);
        this.utpDao.flush();
        //CHECKSTYLE:ON
    }

    @After
    public void tearDown() {
        this.roleDao.delete(this.roleDao.findById(this.r1.getId()));
    }

    /**
     * Test - returns 8 efforts for one tita project.
     */
    @Test
    public void testfindEffortsForTiTAProjectIdShouldSucceed(){
        List<Long> li = new ArrayList<Long>();
        li.add(tip1.getId());
        
        //CHECKSTYLE:OFF     
        List<Effort> leff = timeEffortDAO.findEffortsForTiTAProjectId(li);

        assertNotNull(leff);
        assertEquals(8, leff.size());
        //CHECKSTYLE:ON
    }

    /**
     * Test - returns 5 efforts for tita user 1 and 3 for tita user 2 project.
     */
    @Test
    public void testfindEffortsForTimeConsumerIdShouldSucceed(){
        //CHECKSTYLE:OFF
        List<Effort> leff = this.timeEffortDAO.findEffortsForTimeConsumerId(this.us1.getId());

        assertNotNull(leff);
        assertEquals(5, leff.size());

        leff = this.timeEffortDAO.findEffortsForTimeConsumerId(this.us2.getId());

        assertNotNull(leff);
        assertEquals(3, leff.size());
        //CHECKSTYLE:ON
    }

    /**
     * Test - returns 3 for tita user 2 project.
     */
    @Test
    public void testfindEffortsForTiTAProjectIdAndTimeConsumerIdShouldSucceed(){
        List<Long> li = new ArrayList<Long>();
        li.add(tip1.getId());
        //CHECKSTYLE:OFF     
        List<Effort> leff = timeEffortDAO.findEffortsForTiTAProjectAndTimeConsumerId(
                                                                                    li,us2.getId());

        assertNotNull(leff);
        assertEquals(3, leff.size());
        //CHECKSTYLE:ON
    }
}

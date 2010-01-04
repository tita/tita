package at.ac.tuwien.ifs.tita.dao.test.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import at.ac.tuwien.ifs.tita.entity.util.ProjectEffort;
import at.ac.tuwien.ifs.tita.entity.util.UserProjectEffort;

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
    
    @Autowired
    private EffortDao timeEffortDAO;
    
    @Qualifier("titaProjectDAO")
    @Autowired
    private IGenericHibernateDao<TiTAProject, Long> titaProjectDAO;

    @Qualifier("userTitaDAO")
    @Autowired
    private IGenericHibernateDao<TiTAUserProject, Long> utpDao;
        
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
        
        Role r1 = new Role(1L,"role 1");
        
        Effort et1 = null, et2 = null, et3 = null, et4 = null, 
               ei1 = null, ei2 = null, ei3 = null, ei4 = null; 
        
        us1 = new TiTAUser("user1", null, null, null, null, null, r1, null, null); 
        us2 = new TiTAUser("user2", null, null, null, null, null, r1, null, null);
        
        et1 = new Effort(new Date(System.currentTimeMillis()), 1000L, false,
                "tita task 1 - effort 1",us1);
        et2 = new Effort(new Date(System.currentTimeMillis()),2000L, false,
                "tita task 1 - effort 2",us1);
        et3 = new Effort(new Date(System.currentTimeMillis()),5000L, false,
                "tita task 2 - effort 1",us2);
        et4 = new Effort(new Date(System.currentTimeMillis()),3000L, false,
                "tita task 2 - effort 2",us1);
        ei1 = new Effort(new Date(System.currentTimeMillis()),2000L, false,
                "issuetracker task 1 - effort 1",us2);
        ei2 = new Effort(new Date(System.currentTimeMillis()),8000L, false,
                "issuetracker task 1 - effort 2",us1);
        ei3 = new Effort(new Date(System.currentTimeMillis()),1000L, false,
                "issuetracker task 2 - effort 1",us2);
        ei4 = new Effort(new Date(System.currentTimeMillis()),7000L, false,
                "issuetracker task 2 - effort 2",us1);
        
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
        
        TiTATask tit1 = new TiTATask(us1,se1);
        TiTATask tit2 = new TiTATask(us2,se2);
        
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
        
        
        tip1 = new TiTAProject("bla", "bla", false, null,
                sa1,sip);

        ip1.setTitaProject(tip1);
        ip2.setTitaProject(tip1);
        
        tit2.setTitaProject(tip1);
        tit1.setTitaProject(tip1);
        
        titaProjectDAO.save(tip1);
        titaProjectDAO.flush();
        
        TiTAUserProject utp1 = new TiTAUserProject(us1,tip1);
        TiTAUserProject utp2 = new TiTAUserProject(us2,tip1);
              
        utpDao.save(utp1);
        utpDao.save(utp2);
        utpDao.flush();
        //CHECKSTYLE:ON
    }
    
    /**
     * Test - returns 8 efforts for one tita project.
     */
    @Test
    public void testfindEffortsForTiTAProjectIdShouldSucceed(){
        List<String> li = new ArrayList<String>();
        li.add("bla");
        
        //CHECKSTYLE:OFF     
        List<ProjectEffort> leff = timeEffortDAO.findEffortsForTiTAProjectId(li, "month");
          
        assertNotNull(leff);
        assertEquals(2, leff.size());
        //CHECKSTYLE:ON
    }
    
    /**
     * Test - returns 5 efforts for tita user 1 and 3 for tita user 2 project.
     */
    @Test
    public void testfindEffortsForTimeConsumerIdShouldSucceed(){
        //CHECKSTYLE:OFF     
        List<Effort> leff = timeEffortDAO.findEffortsForTimeConsumerId(us1.getId());
          
        assertNotNull(leff);
        assertEquals(5, leff.size());
        
        leff = timeEffortDAO.findEffortsForTimeConsumerId(us2.getId());
        
        assertNotNull(leff);
        assertEquals(3, leff.size());
        //CHECKSTYLE:ON
    }
    
    /**
     * Test - returns 3 for tita user 2 project.
     */
    @Test
    public void testfindEffortsForTiTAProjectIdAndTimeConsumerIdShouldSucceed(){
        List<String> li = new ArrayList<String>();
        li.add(tip1.getName());
        
        List<String> ti = new ArrayList<String>();
        ti.add(us2.getUserName());
        //CHECKSTYLE:OFF     
        List<UserProjectEffort> leff = timeEffortDAO.findEffortsForTiTAProjectAndTimeConsumerId(
                                                                                       li,ti,"day");
          
        assertNotNull(leff);
        assertEquals(2, leff.size());
        //CHECKSTYLE:ON
    }
}

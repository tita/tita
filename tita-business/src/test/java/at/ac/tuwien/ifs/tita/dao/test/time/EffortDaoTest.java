package at.ac.tuwien.ifs.tita.dao.test.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import at.ac.tuwien.ifs.tita.entity.User;
import at.ac.tuwien.ifs.tita.entity.UserTitaProject;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;

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
    
    @Autowired
    private EffortDao timeEffortDAO;
    
    @Qualifier("titaProjectDAO")
    @Autowired
    private IGenericHibernateDao<TiTAProject, Long> titaProjectDAO;
    
    @Qualifier("issueTrackerDAO")
    @Autowired
    private IGenericHibernateDao<IssueTracker, Long> itDao;
    
    @Qualifier("userTitaDAO")
    @Autowired
    private IGenericHibernateDao<UserTitaProject, Long> utpDao;
    
    @Qualifier("issueTrackerProjectDAO")
    @Autowired
    private IGenericHibernateDao<IssueTrackerProject, Long> itpDao;
    
    @Qualifier("userDAO")
    @Autowired
    private IGenericHibernateDao<User, Long> userDao;
    
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
        
        IssueTracker it = new IssueTracker(1L,"issue tracker 1");
        
        itDao.save(it);
        itDao.flushnClear();
        
        Effort et1 = null, et2 = null, et3 = null, et4 = null, 
               ei1 = null, ei2 = null, ei3 = null, ei4 = null; 
        
        User us1 = new User(), us2 = new User();
        
        userDao.save(us1);
        userDao.save(us2);
        userDao.flushnClear();
                               
        tip1 = new TiTAProject(null, "bla", "bla", false, null,
                               null,null);
       
        titaProjectDAO.save(tip1);
        titaProjectDAO.flushnClear();
        
        UserTitaProject utp1 = new UserTitaProject(us1,tip1);
        UserTitaProject utp2 = new UserTitaProject(us2,tip1);
              
        utpDao.save(utp1);
        utpDao.save(utp2);
        utpDao.flush();
        
        IssueTrackerProject ip1 = new IssueTrackerProject(null,tip1,it,97L,null);
        IssueTrackerProject ip2 = new IssueTrackerProject(null,tip1,it,98L,null);
        
//        itpDao.save(ip1);
//        itpDao.save(ip2);
//        itpDao.flushnClear();
        
        et1 = new Effort(null,new Date(System.currentTimeMillis()), 1000L, false,
                "tita task 1 - effort 1");
        et2 = new Effort(null,new Date(System.currentTimeMillis()),2000L, false,"tita task 1 - effort 2");
        et3 = new Effort(null,new Date(System.currentTimeMillis()),5000L, false,"tita task 2 - effort 1");
        et4 = new Effort(null,new Date(System.currentTimeMillis()),3000L, false,"tita task 2 - effort 2");
        ei1 = new Effort(null,new Date(System.currentTimeMillis()),2000L, false,
                "issuetracker task 1 - effort 1");
        ei2 = new Effort(null,new Date(System.currentTimeMillis()),8000L, false,
                "issuetracker task 1 - effort 2");
        ei3 = new Effort(null,new Date(System.currentTimeMillis()),1000L, false,
                "issuetracker task 2 - effort 1");
        ei4 = new Effort(null,new Date(System.currentTimeMillis()),7000L, false,
                "issuetracker task 2 - effort 2");
        
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
        
        TiTATask tit1 = new TiTATask(null,us1,tip1,se1);
        TiTATask tit2 = new TiTATask(null,us2,tip1,se2);
        
        Set<TiTATask> sa1 = new HashSet<TiTATask>();
        sa1.add(tit1);
        sa1.add(tit2);
        
        IssueTrackerTask itt1 = new IssueTrackerTask(null,ip1,se3);
        IssueTrackerTask itt2 = new IssueTrackerTask(null,ip2,se4);
        
        Set<IssueTrackerTask> si1 = new HashSet<IssueTrackerTask>();
        si1.add(itt1);
        
        Set<IssueTrackerTask> si2 = new HashSet<IssueTrackerTask>();
        si2.add(itt2); 
        
        titaProjectDAO.save(tip1);
        titaProjectDAO.flushnClear();
        //CHECKSTYLE:ON
    }
    
    /**
     * Test.
     */
    @Test
    public void testfindEffortsForTiTAProjectIdShouldSucceed(){
        //CHECKSTYLE:OFF
        List<Effort> leff = timeEffortDAO.findEffortsForTiTAProjectId(1L);
          
          
        assertNotNull(leff);
        assertEquals(8, leff.size());
        //CHECKSTYLE:ON
    }
    
//    @After
//    public void cleanProjects(){
//        
//    }
}

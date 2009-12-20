package at.ac.tuwien.ifs.tita.dao.test.time;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import at.ac.tuwien.ifs.tita.dao.GenericHibernateDao;
import at.ac.tuwien.ifs.tita.dao.time.EffortDao;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;

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
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    @Autowired
    private EffortDao timeEffortDAO;
    
    @Qualifier("titaProjectDAO")
    @Autowired
    private GenericHibernateDao<TiTAProject, Long> titaProjectDAO;
    
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
        Effort et1 = null, et2 = null, et3 = null, et4 = null, 
               ei1 = null, ei2 = null, ei3 = null, ei4 = null;      
        try {
            et1 = new Effort(null,sdf.parse(sdf.format(new Date())),
                             null, 1000L, false,
                             "tita task 1 - effort 1");
            et2 = new Effort(null,sdf.parse(sdf.format(new Date())),
                    null,2000L, false,"tita task 1 - effort 2");
            et3 = new Effort(null,sdf.parse(sdf.format(new Date())),
                    null,5000L, false,"tita task 2 - effort 1");
            et4 = new Effort(null,sdf.parse(sdf.format(new Date())),
                    null,3000L, false,"tita task 2 - effort 2");
            ei1 = new Effort(null,sdf.parse(sdf.format(new Date())),
                    null,2000L, false,
                             "issuetracker task 1 - effort 1");
            ei2 = new Effort(null,sdf.parse(sdf.format(new Date())),
                    null,8000L, false,
                             "issuetracker task 1 - effort 2");
            ei3 = new Effort(null,sdf.parse(sdf.format(new Date())),
                    null,1000L, false,
                             "issuetracker task 2 - effort 1");
            ei4 = new Effort(null,sdf.parse(sdf.format(new Date())),
                    null,7000L, false,
                             "issuetracker task 2 - effort 2");
            
            System.out.println(sdf.format(et1.getDate()));
        } catch (ParseException e) {
            fail();
        }
               
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
        
        TiTATask tit1 = new TiTATask(null,se1);
        TiTATask tit2 = new TiTATask(null,se2);
        
        Set<TiTATask> sa1 = new HashSet<TiTATask>();
        sa1.add(tit1);
        sa1.add(tit2);
        
        IssueTrackerTask itt1 = new IssueTrackerTask(null,se3);
        IssueTrackerTask itt2 = new IssueTrackerTask(null,se4);
        
        Set<IssueTrackerTask> si1 = new HashSet<IssueTrackerTask>();
        si1.add(itt1);
        
        Set<IssueTrackerTask> si2 = new HashSet<IssueTrackerTask>();
        si2.add(itt2);
        
        IssueTrackerProject ip1 = new IssueTrackerProject(null,97L,si1);
        IssueTrackerProject ip2 = new IssueTrackerProject(null,98L,si2);
        
        Set<IssueTrackerProject> sip1 = new HashSet<IssueTrackerProject>();
        sip1.add(ip1);
        sip1.add(ip2);
        
        tip1 = new TiTAProject(null,sa1,sip1);
        
        //save object tree in database
        titaProjectDAO.save(tip1);
        //CHECKSTYLE:ON
    }
    
    /**
     * Test.
     */
    @Test
    public void testfindEffortsForTiTAProjectIdShouldSucceed(){
        //CHECKSTYLE:OFF
        List<Effort> leff = timeEffortDAO.findEffortsForTiTAProjectId(tip1.getId());
          
          
        assertNotNull(leff);
        assertEquals(8, leff.size());
        //CHECKSTYLE:ON
    }
    
//    @After
//    public void cleanProjects(){
//        
//    }
}

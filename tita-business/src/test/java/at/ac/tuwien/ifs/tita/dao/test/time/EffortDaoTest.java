package at.ac.tuwien.ifs.tita.dao.test.time;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.ifs.tita.dao.time.EffortDao;

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
public class EffortDaoTest {

    @Autowired
    private EffortDao timeEffortDAO;
    
    public EffortDaoTest() {
        
    }  
    
    @Before
    public void prepareProjects(){
        
    }
    
    @Test
    public void testTest(){
        
    }
    
    @After
    public void cleanProjects(){
        
    }
}

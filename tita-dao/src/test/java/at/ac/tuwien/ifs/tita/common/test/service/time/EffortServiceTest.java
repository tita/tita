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
package at.ac.tuwien.ifs.tita.common.test.service.time;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.ifs.tita.business.service.time.IEffortService;
import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.entity.Effort;

/**
 * Effort Service Test.
 * @author herbert
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:datasourceContext-test.xml" })
@TransactionConfiguration
@Transactional
public class EffortServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
    
//    private final Logger log = LoggerFactory.getLogger(EffortServiceTest.class);

    @Autowired
    private IEffortService service;

    /**
     * Test.
     */
    @Test
    public void testSaveTimeEffort() {
        Effort timeEffort = new Effort(null, null, null, "Das ist die Test TimeEffort 1");
 
        try {
            service.saveEffort(timeEffort);
        } catch (TitaDAOException e) {
            fail();
        }
        Assert.assertNotNull(timeEffort.getId());
    }

    /**
     * Test.
     */
    @Test
    public void testDeleteTimeEffort() {
        Effort timeEffort = new Effort(null, null, null, "Das ist die Test TimeEffort 2");
        try {
            service.saveEffort(timeEffort);
            Assert.assertNotNull(timeEffort.getId());

            service.deleteEffort(timeEffort);
            Assert.assertNull(service.getEffortById(timeEffort.getId()));
        } catch (TitaDAOException e) {
            fail();
        }
    }
    
    /**
     * Test.
     */
    @Test
    public void testUpdateTimeEffort() {
        Effort timeEffort = new Effort(null, null, null,"Das ist die Test TimeEffort 3");
        try {
            service.saveEffort(timeEffort);
            Assert.assertNotNull(timeEffort.getId());

            Date time = new Date();
            timeEffort.setStartTime(time);
            service.saveEffort(timeEffort);
            Assert.assertEquals(service.getEffortById(timeEffort.getId()).getStartTime(), time);
        } catch (TitaDAOException e) {
            fail();
        }
    }

    /**
     * Test.
     */
    @Test
    public void testSearchTimeEffort() {
        Effort timeEffort = new Effort(null, null, null, "Das ist die Test TimeEffort 4");
        try {
            service.saveEffort(timeEffort);
            Assert.assertNotNull(timeEffort.getId());
            Effort te2 = service.getEffortById(timeEffort.getId());
            Assert.assertNotNull(te2);
            Assert.assertEquals(timeEffort.getId(),te2.getId());
        } catch (TitaDAOException e) {
            fail();
        }
    }

    /**
     * Prepare database for test -> insert 3 efforts.
     * @return List of efforts
     */
    private List<Effort> prepareEfforts(){
        String strdate1 = "18.10.2009";
        String strdate2 = "25.05.2009";
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date1 = null;
        Date date2 = null;
        
        List<Effort> effortList = new ArrayList<Effort>();
        
        try {
            date1 = formatter.parse(strdate1);
            date2 = formatter.parse(strdate2);
        } catch (ParseException e1) {
            assertTrue(false);
        }
        Effort timeEffort1 = new Effort(null, null, null, "Das ist die Test TimeEffort 5");
        Effort timeEffort2 = new Effort(null, null, null, "Das ist die Test TimeEffort 6");
        Effort timeEffort3 = new Effort(null, null, null, "Das ist die Test TimeEffort 7");
        
        effortList.add(timeEffort1);
        effortList.add(timeEffort2);
        effortList.add(timeEffort3);
        try {
            for(Effort eff : effortList){
                service.saveEffort(eff);
            }
        } catch (TitaDAOException e) {
            fail();
        }
        return effortList;
    }
    
    /**
     * Test: Get TimeEffort by day with named query.
     * 
     * EXCEPTION: DEBUG BaseDAO:67 - Trying to persist Domain with Id: null.
     * DEBUG BaseDAO:82 - Exception catched while trying to save Entity with Id:
     * null
     */

    @Test
    public void testGetTimeEffortByDay() {
        List<Effort> efforts = prepareEfforts();
        List<Effort> list = null;
        GregorianCalendar cal1 = new GregorianCalendar();
        //CHECKSTYLE:OFF
        cal1.set(2009, 9, 18);
        //CHECKSTYLE:ON
        try {    
            list = service.getEffortsDailyView(cal1);
            Assert.assertNotNull(list);
            Assert.assertEquals(2, list.size());
            deleteEfforts(efforts);
        } catch (TitaDAOException e) {
            fail();
        }
    }

    /**
     * Delete all inserted efforts.
     * @param efforts List
     * @throws TitaDAOException e
     */
    private void deleteEfforts(List<Effort> efforts) throws TitaDAOException{
        for(Effort eff : efforts){
            service.deleteEffort(eff);
        }
    }
    
    /**
     * Test: Get TimeEffort by month with named query. EXCEPTION: DEBUG
     * BaseDAO:67 - Trying to persist Domain with Id: null. DEBUG BaseDAO:82 -
     * Exception catched while trying to save Entity with Id: null
     */

    @Test
    public void testGetTimeEffortByMonth() {
        List<Effort> efforts = prepareEfforts();
        List<Effort> list = null;
        GregorianCalendar cal1 = new GregorianCalendar();
        //CHECKSTYLE:OFF
        cal1.set(2009, 9, 18);
        //CHECKSTYLE:ON
        try {
            list = service.getEffortsMonthlyView(cal1);
            Assert.assertNotNull(list);
            //CHECKSTYLE:OFF
            Assert.assertEquals(2, list.size());
            //CHECKSTYLE:ON
            deleteEfforts(efforts);
        } catch (TitaDAOException e) {
            fail();
        }
    }
}

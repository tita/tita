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
package at.ac.tuwien.ifs.tita.test.service.time;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 *
 * @author herbert
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:datasourceContext-test.xml" })
@TransactionConfiguration
@Transactional
public class EffortServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    private static final long C_THREE_HUNDRED = 300L;

    private static final long C_TWO_HUNDRED = 200L;

    private static final long C_HUNDRED = 100L;

    private final Logger log = LoggerFactory.getLogger(EffortServiceTest.class);

    @Autowired
    private IEffortService service;

    /**
     * Test.
     */
    @Test
    public void testSaveTimeEffort() {

        String strdate = "18.10.2009";
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date = null;

        List<Effort> effortList = new ArrayList<Effort>();

        try {
            date = formatter.parse(strdate);
        } catch (ParseException e1) {
            assertTrue(false);
        }

        Effort timeEffort = new Effort(date, "Test-Description", C_HUNDRED, C_TWO_HUNDRED, C_THREE_HUNDRED,
                false, null);

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
        Effort timeEffort = new Effort(null, null, null, "Das ist die Test TimeEffort 3");
        try {
            service.saveEffort(timeEffort);
            Assert.assertNotNull(timeEffort.getId());

            Date date = new Date();
            timeEffort.setDate(date);
            service.saveEffort(timeEffort);
            Assert.assertEquals(service.getEffortById(timeEffort.getId()).getDate(), date);
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
            Assert.assertEquals(timeEffort.getId(), te2.getId());
        } catch (TitaDAOException e) {
            fail();
        }
    }

    /**
     * Prepare database for test -> insert 3 efforts.
     *
     * @return List of efforts
     */
    private List<Effort> prepareEfforts() {
        String strdate1 = "18.10.2009";
        String strdate2 = "25.05.2008";
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

        Effort timeEffort1 = new Effort(date1, 0L, false, "Das ist die Test TimeEffort 5", null);
        Effort timeEffort2 = new Effort(date2, 0L, false, "Das ist die Test TimeEffort 6", null);
        Effort timeEffort3 = new Effort(date1, 0L, false, "Das ist die Test TimeEffort 7", null);

        effortList.add(timeEffort1);
        effortList.add(timeEffort2);
        effortList.add(timeEffort3);
        try {
            for (Effort eff : effortList) {
                service.saveEffort(eff);
            }
        } catch (TitaDAOException e) {
            fail();
        }
        return effortList;
    }

    /**
     * Delete all inserted efforts.
     *
     * @param efforts
     *            List
     * @throws TitaDAOException
     *             e
     */
    private void deleteEfforts(List<Effort> efforts) throws TitaDAOException {
        for (Effort eff : efforts) {
            service.deleteEffort(eff);
        }
    }

    /**
     * Test: Get TimeEffort by day.
     */
    @Test
    public void testGetTimeEffortByDay() {
        List<Effort> efforts = prepareEfforts();
        List<Effort> list = null;

        String strdate1 = "18.10.2009";
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date1 = null;

        try {
            date1 = formatter.parse(strdate1);
        } catch (ParseException e1) {
            assertTrue(false);
        }

        try {
            list = service.getEffortsDailyView(date1);
            Assert.assertNotNull(list);
            Assert.assertEquals(2, list.size());
            deleteEfforts(efforts);
        } catch (TitaDAOException e) {
            fail();
        }
    }



    /**
     * Test: Get TimeEffort by month.
     */

    @Test
    public void testGetTimeEffortByMonth() {
        List<Effort> efforts = prepareEfforts();
        List<Effort> list = null;

        try {
            // CHECKSTYLE:OFF
            list = service.getEffortsMonthlyView(2009, 9);
            // CHECKSTYLE:ON
            Assert.assertNotNull(list);
            Assert.assertFalse(list.isEmpty());
            deleteEfforts(efforts);
        } catch (TitaDAOException e) {
            fail();
        }
    }

    /**
     * Test: Get all years of TimeEfforts.
     */

    @Test
    public void testGetTimeEffortYears() {
        List<Effort> efforts = prepareEfforts();
        List<Integer> list = null;

        try {
            list = service.getEffortsYears();
            Assert.assertNotNull(list);
            // Assert.assertEquals(2, list.size());
            deleteEfforts(efforts);
        } catch (TitaDAOException e) {
            fail();
        }
    }
}

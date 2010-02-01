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
package at.ac.tuwien.ifs.tita.dao.test.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.ifs.tita.dao.interfaces.IEffortDao;
import at.ac.tuwien.ifs.tita.dao.test.util.INativeSqlExecutorDao;
import at.ac.tuwien.ifs.tita.entity.Effort;
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
public class EffortDaoTest { // extends AbstractJpaTests {
    @Autowired
    private IEffortDao timeEffortDAO;

    @Autowired
    private INativeSqlExecutorDao<Object, Long> executor;

    public EffortDaoTest() {

    }

    /**
     * Prepare on TiTA Project for testing effort dao.
     * 
     * @throws Exception
     *             ex
     */
    @Before
    public void prepareDB() throws Exception {
        executor.executeSqlFile(this);
    }

    /**
     * Clean db after each test.
     */
    @After
    public void cleanDB() {
        executor.executeSql("delete from EFFORT;");
        executor.executeSql("delete from TITA_TASK;");
        executor.executeSql("delete from ISSUE_TRACKER_TASK;");
        executor.executeSql("delete from ISSUE_TRACKER_PROJECT;");
        executor.executeSql("delete from USER_PROJECT;");
        executor.executeSql("delete from TITA_PROJECT;");
        executor.executeSql("delete from CONV_PROJECT_STATUS;");
        executor.executeSql("delete from CONV_ISSUE_TRACKER;");
        executor.executeSql("delete from ISST_LOGIN;");
        executor.executeSql("delete from TITA_USER;");
        executor.executeSql("delete from CONV_ROLE;");
        executor.executeSql("commit;");
    }

    /**
     * Test - returns 8 efforts for one tita project.
     */
    @Test
    public void findEffortsForTiTAProjectIdShouldSucceed() {
        List<String> li = new ArrayList<String>();
        li.add("tita_test");

        // CHECKSTYLE:OFF
        List<UserProjectEffort> leff = timeEffortDAO
                .findEffortsForTiTAProjectId(li, "month");

        assertNotNull(leff);
        assertEquals(10, leff.size());
        // CHECKSTYLE:ON
    }

    /**
     * Test - returns 5 efforts for tita user 1 and 3 for tita user 2 project.
     */
    @Test
    public void findEffortsForTimeConsumerIdShouldSucceed() {
        // CHECKSTYLE:OFF
        List<Effort> leff = timeEffortDAO.findEffortsForTimeConsumerId(1L);

        assertNotNull(leff);
        assertEquals(21, leff.size());

        leff = timeEffortDAO.findEffortsForTimeConsumerId(2L);

        assertNotNull(leff);
        assertEquals(4, leff.size());
        // CHECKSTYLE:ON
    }

    /**
     * Test - returns 1 for tita user 1 project and project tita_test.
     */
    @Test
    public void findEffortsForTiTAProjectIdAndTimeConsumerIdShouldSucceed() {
        List<String> li = new ArrayList<String>();
        li.add("tita_test");

        List<String> ti = new ArrayList<String>();
        ti.add("hans");
        // CHECKSTYLE:OFF
        List<UserProjectEffort> leff = timeEffortDAO
                .findEffortsForTiTAProjectAndTimeConsumerId(li, ti, "overall");

        assertNotNull(leff);
        assertEquals(1, leff.size());
        // CHECKSTYLE:ON
    }

    /**
     * Method.
     */
    @Test
    public void findEffortsForTiTAProjectAndTiTAUser() {

        List<Effort> list = timeEffortDAO.findEffortsForTiTAProjectAndTiTAUser(
                1L, 2L);
        // CHECKSTYLE:OFF
        assertNotNull(list);
        assertEquals(4, list.size());
        // CHECKSTYLE:ON
    }

    /**
     * Method.
     */
    @Test
    public void findEffortsForTiTAProjectAndTiTAUserOrdered() {

        List<Effort> list = timeEffortDAO
                .findEffortsForTiTAProjectAndTiTAUserOrdered(1L, 2L);
        // CHECKSTYLE:OFF
        assertNotNull(list);
        assertEquals(4, list.size());
        // CHECKSTYLE:ON
    }

    /**
     * Method.
     */
    @Test
    public void totalizeEffortsForTiTAProjectAndTiTAUser() {

        Long sumOfEfforts = timeEffortDAO
                .totalizeEffortsForTiTAProjectAndTiTAUser(1L, 1L);
        // CHECKSTYLE:OFF
        assertNotNull(sumOfEfforts);
        assertEquals(311500.0, sumOfEfforts, 0.0);
        // CHECKSTYLE:ON
    }

    /**
     * Method.
     */
    @Test
    public void testEffortsForIssueTrackerTaskShouldSucceed() {
        // CHECKSTYLE:OFF
        Long effort = timeEffortDAO.findEffortsSumForIssueTrackerTask(1L,
                "hans", 96L, 24L, 1L);
        assertNotNull(effort);
        assertEquals(new Long(2000), effort);
        // CHECKSTYLE:ON
    }

    /**
     * Test: Get TimeEffort by day.
     */
    @Test
    public void testGetTimeEffortByDay() {
        List<Effort> list = null;

        String strdate1 = "01.01.2010";
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date1 = null;

        try {
            date1 = formatter.parse(strdate1);
        } catch (ParseException e1) {
            fail();
        }

        try {
            list = timeEffortDAO.getTimeEffortsDailyView(date1);
            Assert.assertNotNull(list);
            Assert.assertFalse(list.isEmpty());
        } catch (PersistenceException e) {
            fail();
        }
    }

    /**
     * Test: Get TimeEffort by month.
     */

    @Test
    public void testGetTimeEffortByMonth() {
        List<Effort> list = null;

        try {
            // CHECKSTYLE:OFF
            list = timeEffortDAO.getTimeEffortsMonthlyView(2010, 0);
            // CHECKSTYLE:ON
            Assert.assertNotNull(list);
            Assert.assertFalse(list.isEmpty());
        } catch (PersistenceException e) {
            fail();
        }
    }

    /**
     * Test: Get all years of TimeEfforts.
     */

    @Test
    public void testGetTimeEffortYears() {
        List<Integer> list = null;

        try {
            list = timeEffortDAO.getTimeEffortsYears();
            Assert.assertNotNull(list);
        } catch (PersistenceException e) {
            fail();
        }
    }

    /**
     * Test: Get duration sum for titaTasks.
     */
    @Test
    public void findEffortsSumForTiTATasks() {
        // CHECKSTYLE:OFF
        Assert.assertEquals(24000L, (long) timeEffortDAO
                .findEffortsSumForTiTATasks(1L, 1L, 3L));
        // CHECKSTYLE:ON
    }

    /**
     * Test: Get duration sum for issueTrackerTasks.
     */
    @Test
    public void findEffortsSumForIssueTrackerTasks() {
        // CHECKSTYLE:OFF
        Assert.assertEquals(9000L, (long) timeEffortDAO
                .findEffortsSumForIssueTrackerTasks(1L, 1L, 4L));
        // CHECKSTYLE:ON
    }
}

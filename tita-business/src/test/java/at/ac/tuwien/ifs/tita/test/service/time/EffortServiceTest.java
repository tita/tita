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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.PersistenceException;

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

    private static final long C_MOCKING_VALUE_1 = 311500L;

    private static IEffortService effortService;

    private static final long C_THREE_HUNDRED = 300L;
    private static final long C_TWO_HUNDRED = 200L;
    private static final long C_HUNDRED = 100L;
    private final Logger log = LoggerFactory.getLogger(EffortServiceTest.class);

    private DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
    @Autowired
    private IEffortService service;

    /**
     * Test.
     */
    @Test
    public void testSaveTimeEffort() {

        String strdate = "18.10.2009";

        Date date = null;

        try {
            date = formatter.parse(strdate);
        } catch (ParseException e1) {
            assertTrue(false);
        }

        Effort timeEffort = new Effort(date, "Test-Description", C_HUNDRED, C_TWO_HUNDRED, C_THREE_HUNDRED, false, null);
        Effort savedEffort = null;
        try {
            service.saveEffort(timeEffort);
            savedEffort = service.getEffortById(timeEffort.getId());
            assertEquals(timeEffort.getDate(), savedEffort.getDate());
            assertEquals(timeEffort.getDescription(), savedEffort.getDescription());
            assertEquals(timeEffort.getStartTime(), savedEffort.getStartTime());
            assertEquals(timeEffort.getEndTime(), savedEffort.getEndTime());
            assertEquals(timeEffort.getDuration(), savedEffort.getDuration());

            Assert.assertNotNull(timeEffort.getId());

        } catch (PersistenceException e) {
            fail();
        } finally {
            service.deleteEffort(savedEffort);
        }

    }

    /**
     * Test.
     * 
     * @throws PersistenceException - ex
     */
    @Test
    public void testDeleteTimeEffort() throws PersistenceException {
        Effort timeEffort = new Effort(null, null, "Das ist die Test TimeEffort 2");
        service.saveEffort(timeEffort);
        Assert.assertNotNull(timeEffort.getId());
        timeEffort = service.getEffortById(timeEffort.getId());

        service.deleteEffort(timeEffort);
        Assert.assertNull(service.getEffortById(timeEffort.getId()));

    }

    /**
     * Test.
     */
    @Test
    public void testUpdateTimeEffort() {
        Effort timeEffort = new Effort(null, null, "Das ist die Test TimeEffort 3");
        try {
            service.saveEffort(timeEffort);
            Assert.assertNotNull(timeEffort.getId());

            Date date = new Date();
            timeEffort.setDate(date);
            service.saveEffort(timeEffort);
            Assert.assertEquals(formatter.format(date), formatter.format(service.getEffortById(timeEffort.getId())
                    .getDate()));
        } catch (PersistenceException e) {
            fail();
        }
    }

    /**
     * Test.
     */
    @Test
    public void testSearchTimeEffort() {
        Effort timeEffort = new Effort(null, null, "Das ist die Test TimeEffort 4");
        try {
            service.saveEffort(timeEffort);
            Assert.assertNotNull(timeEffort.getId());
            Effort te2 = service.getEffortById(timeEffort.getId());
            Assert.assertNotNull(te2);
            Assert.assertEquals(timeEffort.getId(), te2.getId());
        } catch (PersistenceException e) {
            fail();
        }
    }

    /**
     * Mocking the <tt>EffortService</tt> to show the integration of the
     * <tt>EffortDAO</tt> in the service layer for <tt>Effort</tt> objects.
     * 
     * The test case shows that the dao returns the values fetched from the
     * database and is provided from the service.
     * 
     * In this case the sum of efforts from a project a user has worked for, is
     * expected.
     */
    @Test
    public void totalizeEffortsForTiTAProjectAndTiTAUserMocking() {
        effortService = mock(IEffortService.class);
        doReturn(C_MOCKING_VALUE_1).when(effortService).totalizeEffortsForTiTAProjectAndTiTAUser(1L, 1L);
    }

    /**
     * Mocking the <tt>EffortService</tt> to show the integration of the
     * <tt>EffortDAO</tt> in the service layer for <tt>Effort</tt> objects.
     * 
     * The test case shows that the dao returns the values fetched from the
     * database and is provided from the service.
     * 
     * In this case a list of efforts is expected.
     */
    @Test
    public void findEffortsForTiTAProjectAndTiTAUserMocking() {
        effortService = mock(IEffortService.class);
        doReturn(new ArrayList<Effort>()).when(effortService).findEffortsForTiTAProjectAndTiTAUser(1L, 1L);
    }

    // @Test
    // public void testSaveEffortForTiTATask(){
    // Effort e = new Effort();
    //        
    // e = service.saveEffortForTiTATask(e, "abc", mock(TiTAUser.class),
    // mock(TiTAProject.class));
    //        
    // assertEquals("abc", e.getDescription());
    // }

    // public void saveEffortForTiTATask(Effort effort, String description,
    // TiTAUser user, TiTAProject project) {
    // if (effort != null) {
    // effort.setDescription(description);
    // effort.setUser(user);
    //
    // TiTATask tt = new TiTATask(description, user, project, new
    // HashSet<Effort>());
    // tt.getTitaEfforts().add(effort);
    // projectService.saveTiTATask(tt);
    //            
    // effort.setTitaTask(tt);
    // return saveEffort(effort);
    // // timeEffortDao.flushnClear();
    // }
    // }

    /**
     * Prepare database for test -> insert 3 efforts.
     * 
     * @return List of efforts
     */
    private List<Effort> prepareEfforts() {
        String strdate1 = "18.10.2009";
        String strdate2 = "25.05.2008";
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
        } catch (PersistenceException e) {
            fail();
        }
        return effortList;
    }

    /**
     * Delete all inserted efforts.
     * 
     * @param efforts List
     * @throws PersistenceException titaDao
     */
    private void deleteEfforts(List<Effort> efforts) throws PersistenceException {
        for (Effort eff : efforts) {
            service.deleteEffort(eff);
        }
    }

}

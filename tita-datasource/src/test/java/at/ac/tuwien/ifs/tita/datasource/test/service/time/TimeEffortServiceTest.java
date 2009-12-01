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
package at.ac.tuwien.ifs.tita.datasource.test.service.time;


import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
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

import at.ac.tuwien.ifs.tita.datasource.entity.TimeEffort;
import at.ac.tuwien.ifs.tita.datasource.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.datasource.service.time.ITimeEffortService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:datasourceContext-test.xml" })
@TransactionConfiguration
@Transactional
public class TimeEffortServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    final Logger log = LoggerFactory.getLogger(TimeEffortServiceTest.class);

    // @Autowired
    // private ApplicationContext ctx;

    @Autowired
    private ITimeEffortService service;

    @Test
    public void testSaveTimeEffort() {
        TimeEffort timeEffort = new TimeEffort();
        timeEffort.setDate(new Date());
        timeEffort.setDeleted(false);
        timeEffort.setDescription("Das ist die Test TimeEffort 1");

        try {
            service.saveTimeEffort(timeEffort);
            Assert.assertNotNull(timeEffort.getId());
        } catch (TitaDAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteTimeEffort() {
        TimeEffort timeEffort = new TimeEffort();
        timeEffort.setDate(new Date());
        timeEffort.setDeleted(false);
        timeEffort.setDescription("Das ist die Test TimeEffort 1");

        try {
            service.saveTimeEffort(timeEffort);
            Assert.assertNotNull(timeEffort.getId());

            service.deleteTimeEffort(timeEffort);
            Assert.assertNull(service.getTimeEffortById(timeEffort.getId()));
        } catch (TitaDAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateTimeEffort() {
        TimeEffort timeEffort = new TimeEffort();
        timeEffort.setDate(new Date());
        timeEffort.setDeleted(false);
        timeEffort.setDescription("Das ist die Test TimeEffort 1");

        try {
            service.saveTimeEffort(timeEffort);
            Assert.assertNotNull(timeEffort.getId());

            Date newDate = new Date();
            timeEffort.setDate(newDate);
            service.updateTimeEffort(timeEffort);
            Assert.assertEquals(service.getTimeEffortById(timeEffort.getId()).getDate(), newDate);
        } catch (TitaDAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSearchTimeEffort() {
        TimeEffort timeEffort = new TimeEffort();
        timeEffort.setDate(new Date());
        timeEffort.setDeleted(false);
        timeEffort.setDescription("Das ist die Test TimeEffort 1");

        try {
            service.saveTimeEffort(timeEffort);
            Assert.assertNotNull(timeEffort.getId());
            TimeEffort te2 = service.getTimeEffortById(timeEffort.getId());
            Assert.assertNotNull(te2);
            Assert.assertEquals(timeEffort.getId(),te2.getId());
        } catch (TitaDAOException e) {
            e.printStackTrace();
        }
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
        String strdate1 = "18.10.2009";
        String strdate2 = "25.05.2009";
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = formatter.parse(strdate1);
            date2 = formatter.parse(strdate2);
        } catch (ParseException e1) {
            assertTrue(false);
        }

        GregorianCalendar cal1 = new GregorianCalendar();
        cal1.set(2009, 9, 18);

        GregorianCalendar cal2 = new GregorianCalendar();
        cal2.set(2009, 4, 25);

        TimeEffort timeEffort1 = new TimeEffort();
        timeEffort1.setDate(date1);
        timeEffort1.setDeleted(false);
        timeEffort1.setDescription("Das ist die Test TimeEffort by date");

        TimeEffort timeEffort2 = new TimeEffort();
        timeEffort2.setDate(date2);
        timeEffort2.setDeleted(false);
        timeEffort2.setDescription("Das ist die Test TimeEffort by date");

        TimeEffort timeEffort3 = new TimeEffort();
        timeEffort3.setDate(date1);
        timeEffort3.setDeleted(false);
        timeEffort3.setDescription("Das ist die Test TimeEffort by date");

        List<TimeEffort> list = null;
        try {
            service.saveTimeEffort(timeEffort1);
            service.saveTimeEffort(timeEffort2);
            service.saveTimeEffort(timeEffort3);
            System.out.println(timeEffort1.getId());
            list = service.getTimeEffortsDailyView(cal1);
            Assert.assertNotNull(list);
            Assert.assertEquals(2, list.size());

            service.deleteTimeEffort(timeEffort1);
            service.deleteTimeEffort(timeEffort2);
            service.deleteTimeEffort(timeEffort3);
        } catch (TitaDAOException e) {
            assertTrue(false);
        }

    }

    /**
     * Test: Get TimeEffort by month with named query. EXCEPTION: DEBUG
     * BaseDAO:67 - Trying to persist Domain with Id: null. DEBUG BaseDAO:82 -
     * Exception catched while trying to save Entity with Id: null
     */

    @Test
    public void testGetTimeEffortByMonth() {
        String strdate1 = "18.10.2009";
        String strdate2 = "25.10.2009";
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = formatter.parse(strdate1);
            date2 = formatter.parse(strdate2);
        } catch (ParseException e1) {
            assertTrue(false);
        }

        GregorianCalendar cal1 = new GregorianCalendar();
        cal1.set(2009, 9, 18);

        GregorianCalendar cal2 = new GregorianCalendar();
        cal2.set(2009, 9, 25);

        TimeEffort timeEffort1 = new TimeEffort();
        timeEffort1.setDate(date1);
        timeEffort1.setDeleted(false);

        timeEffort1.setDescription("Das ist die Test TimeEffort by date");

        TimeEffort timeEffort2 = new TimeEffort();
        timeEffort2.setDate(date2);
        timeEffort2.setDeleted(false);
        timeEffort2.setDescription("Das ist die Test TimeEffort by date");

        TimeEffort timeEffort3 = new TimeEffort();
        timeEffort3.setDate(date1);
        timeEffort3.setDeleted(false);
        timeEffort3.setDescription("Das ist die Test TimeEffort by date");

        List<TimeEffort> list = null;
        try {
            service.saveTimeEffort(timeEffort1);
            service.saveTimeEffort(timeEffort2);
            service.saveTimeEffort(timeEffort3);

            list = service.getTimeEffortsMonthlyView(cal1);
            Assert.assertNotNull(list);
            Assert.assertEquals(3, list.size());

            service.deleteTimeEffort(timeEffort1);
            service.deleteTimeEffort(timeEffort2);
            service.deleteTimeEffort(timeEffort3);
        } catch (TitaDAOException e) {
            assertTrue(false);
        }

    }

}

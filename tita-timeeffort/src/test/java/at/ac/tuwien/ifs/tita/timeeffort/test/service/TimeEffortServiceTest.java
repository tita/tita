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

package at.ac.tuwien.ifs.tita.timeeffort.test.service;

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

import at.ac.tuwien.ifs.tita.datasource.criteria.IBaseCriteria;
import at.ac.tuwien.ifs.tita.datasource.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.timeeffort.domain.TimeEffort;
import at.ac.tuwien.ifs.tita.timeeffort.service.ITimeEffortService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/timeeffort-context-test.xml" })
@TransactionConfiguration
@Transactional
public class TimeEffortServiceTest extends
        AbstractTransactionalJUnit4SpringContextTests {

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
        timeEffort.setEndTime(new Date());
        timeEffort.setStartTime(new Date());
        timeEffort.setDescription("Das ist die Test TimeEffort 1");

        try {
            timeEffort = service.saveTimeEffort(timeEffort);
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
        timeEffort.setEndTime(new Date());
        timeEffort.setStartTime(new Date());
        timeEffort.setDescription("Das ist die Test TimeEffort 1");

        try {
            timeEffort = service.saveTimeEffort(timeEffort);
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
        timeEffort.setEndTime(new Date());
        timeEffort.setStartTime(new Date());
        timeEffort.setDescription("Das ist die Test TimeEffort 1");

        try {
            timeEffort = service.saveTimeEffort(timeEffort);
            Assert.assertNotNull(timeEffort.getId());

            Date newDate = new Date();
            timeEffort.setDate(newDate);
            service.updateTimeEffort(timeEffort);
            Assert.assertEquals(service.getTimeEffortById(timeEffort.getId())
                    .getDate(), newDate);
        } catch (TitaDAOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSearchTimeEffort() {
        TimeEffort timeEffort = new TimeEffort();
        timeEffort.setDate(new Date());
        timeEffort.setDeleted(false);
        timeEffort.setEndTime(new Date());
        timeEffort.setStartTime(new Date());
        timeEffort.setDescription("Das ist die Test TimeEffort 1");

        try {
            timeEffort = service.saveTimeEffort(timeEffort);
            Assert.assertNotNull(timeEffort.getId());
            TimeEffort te2 = new TimeEffort();
            te2.setId(timeEffort.getId());
            IBaseCriteria<TimeEffort> tecrit = service.createCriteria(te2);
            tecrit.getCriteria().setMaxResults(10);
            tecrit.setOrderAscBy("id");
            List<TimeEffort> list = service.searchTimeEffort(tecrit);
            Assert.assertNotNull(list);
            Assert.assertEquals(list.size(), 1);
            Assert.assertEquals(list.get(0).getDescription(), timeEffort
                    .getDescription());
        } catch (TitaDAOException e) {
            e.printStackTrace();
        }
    }
}

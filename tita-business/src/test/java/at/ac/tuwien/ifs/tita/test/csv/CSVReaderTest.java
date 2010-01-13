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
package at.ac.tuwien.ifs.tita.test.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.PersistenceException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.ift.CellProcessor;

import at.ac.tuwien.ifs.tita.business.csv.CSVReader;
import at.ac.tuwien.ifs.tita.business.csv.IImportReader;
import at.ac.tuwien.ifs.tita.business.service.time.IEffortService;
import at.ac.tuwien.ifs.tita.dao.interfaces.IGenericHibernateDao;
import at.ac.tuwien.ifs.tita.dao.issuetracker.IssueTrackerDao;
import at.ac.tuwien.ifs.tita.dao.project.TiTAProjectDao;
import at.ac.tuwien.ifs.tita.dao.user.RoleDAO;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.TiTAUserProject;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;
import at.ac.tuwien.ifs.tita.entity.conv.Role;

/**
 * CSV Reader Testcases.
 *
 * @author karin
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:datasourceContext-test.xml" })
@TransactionConfiguration
@Transactional
public class CSVReaderTest extends AbstractTransactionalJUnit4SpringContextTests {

    private static final int C_TWENTY_ONE = 21;
    private static final int C_THREE = 3;
    private static final int C_TWO = 2;
    private static final int C_TEN = 10;

    private TiTAProject tip1;
    private TiTAUser us1, us2;
    private TiTATask tit1;
    private Role r11;

    @Autowired
    private IEffortService service;

    @Autowired
    private TiTAProjectDao titaProjectDAO;

    @Qualifier("userTitaDAO")
    @Autowired
    private IGenericHibernateDao<TiTAUserProject, Long> utpDao;

    @Autowired
    private RoleDAO roleDao;

    @Autowired
    private IssueTrackerDao issueTrackerDAO;

    /**
     * Prepare on TiTA Project for testing effort dao.
     */
    @Before
    public void prepareProjects() {
        // CHECKSTYLE:OFF
        IssueTracker it = new IssueTracker(100L, "issue tracker 1", null);
        r11 = new Role(100L, "role 1");

        // save conv data
        issueTrackerDAO.save(it);
        roleDao.save(r11);

        Effort et1 = null, et2 = null, et3 = null, et4 = null, ei1 = null, ei2 = null, ei3 = null, ei4 = null;

        us1 = new TiTAUser("user1", null, null, null, null, null, r11, null, null);
        us2 = new TiTAUser("user2", null, null, null, null, null, r11, null, null);

        et1 = new Effort(new Date(System.currentTimeMillis()), 1000L, false,
                "tita task 1 - effort 1", us1);
        et2 = new Effort(new Date(System.currentTimeMillis()), 2000L, false,
                "tita task 1 - effort 2", us1);
        et3 = new Effort(new Date(System.currentTimeMillis()), 5000L, false,
                "tita task 2 - effort 1", us2);
        et4 = new Effort(new Date(System.currentTimeMillis()), 3000L, false,
                "tita task 2 - effort 2", us1);
        ei1 = new Effort(new Date(System.currentTimeMillis()), 2000L, false,
                "issuetracker task 1 - effort 1", us2);
        ei2 = new Effort(new Date(System.currentTimeMillis()), 8000L, false,
                "issuetracker task 1 - effort 2", us1);
        ei3 = new Effort(new Date(System.currentTimeMillis()), 1000L, false,
                "issuetracker task 2 - effort 1", us2);
        ei4 = new Effort(new Date(System.currentTimeMillis()), 7000L, false,
                "issuetracker task 2 - effort 2", us1);

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

        tit1 = new TiTATask(us1, se1);
        TiTATask tit2 = new TiTATask(us2, se2);

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

        IssueTrackerProject ip1 = new IssueTrackerProject(it, 97L, si1);

        itt1.setIsstProject(ip1);
        itt2.setIsstProject(ip1);

        IssueTrackerProject ip2 = new IssueTrackerProject(it, 98L, si2);

        Set<IssueTrackerProject> sip = new HashSet<IssueTrackerProject>();
        sip.add(ip1);
        sip.add(ip2);

        tip1 = new TiTAProject("bla", "bla", false, null, sa1, sip);

        ip1.setTitaProject(tip1);
        ip2.setTitaProject(tip1);

        tit2.setTitaProject(tip1);
        tit1.setTitaProject(tip1);

        titaProjectDAO.save(tip1);
        titaProjectDAO.flush();

        TiTAUserProject utp1 = new TiTAUserProject(us1, tip1, 0L);
        TiTAUserProject utp2 = new TiTAUserProject(us2, tip1, 0L);

        utpDao.save(utp1);
        utpDao.save(utp2);
        utpDao.flush();
        // CHECKSTYLE:ON
    }

    /**
     * Test.
     *
     * @throws IOException
     *             ioe
     */
    @Test
    public void testImportCSV() throws IOException {
        CellProcessor[] processors = new CellProcessor[] { new ParseDate("dd.MM.yyyy"), null,
                new ParseLong(), new ParseDate("HH:mm:ss"), new ParseDate("HH:mm:ss") };

        IImportReader reader = new CSVReader(service);

        ClassPathResource res = new ClassPathResource(
                "../test-classes/at/ac/tuwien/ifs/tita/test/csv");
        File file = res.getFile();
        String path = file.getAbsolutePath() + "/CSVReaderTest.csv";

        String[] header = new String[] { "date", "description", "duration", "startTime", "endTime" };

        assertEquals("Before importing", C_TWO, tit1.getTitaEfforts().size());
        try {
            assertEquals("Before importing", C_TWENTY_ONE, service.getActualEfforts(C_TEN).size());
        } catch (PersistenceException e2) {
            fail("");
        }

        try {
            reader.importEffortData(path, header, processors, tit1, us1);
        } catch (PersistenceException e1) {
            fail("");
        }

        assertEquals("Three Efforts were imported", C_TWO + C_THREE, tit1.getTitaEfforts().size());

        try {
            assertEquals("Three Efforts were imported", C_TWENTY_ONE + C_THREE, service
                    .getActualEfforts(C_TEN).size());
        } catch (PersistenceException e) {
            fail("");
        }
    }
}

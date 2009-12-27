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

package at.ac.tuwien.ifs.tita.issuetracker.mantis.time;

import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mantisbt.connect.MCException;

import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;
import at.ac.tuwien.ifs.tita.issuetracker.mantis.base.MantisBaseTest;
import at.ac.tuwien.ifs.tita.issuetracker.mantis.dao.IssueTrackerMantisDao;
import at.ac.tuwien.ifs.tita.issuetracker.mantis.util.time.TimedIssueEffortCoordinator;

/**
 * Test for class TimedTaskCoordinator.
 * 
 * @author herbert
 * 
 */
public class IssueTrackerTaskCoordinatorTest extends MantisBaseTest {

    private final IssueTrackerLogin defaultLogin = new IssueTrackerLogin(1L, "administrator",
            "root", new IssueTracker(1L, "test-mantis", "http://localhost/mantisbt-1.1.8"));

    private Long projectId = null;
    private Long taskId1, taskId2, taskId3 = null;
    private final IssueTrackerMantisDao mantisDao = new IssueTrackerMantisDao(defaultLogin);
    private TimedIssueEffortCoordinator tiCo;

    /**
     * Prepare mantis connection and project for all tests.
     */
    @Override
    @Before
    public void setUp() {
        super.setUp();
        tiCo = new TimedIssueEffortCoordinator();
        try {
            projectId = createTestProject("tita_test", "tita_test_description",
                    true, false);
            taskId1 = createTestTask("task1", "task1_summary", "tita_test");
            taskId2 = createTestTask("task1", "task1_summary", "tita_test");
            taskId3 = createTestTask("task1", "task1_summary", "tita_test");
        } catch (MCException e) {
            fail("Mantis connection error.");
        }
    }

    /**
     * Delete mantis projects for all tests.
     */
    @After
    public void tearDown() {
        deleteTestTask(taskId1);
        deleteTestTask(taskId2);
        deleteTestTask(taskId3);
        deleteTestProject("tita_test");
    }

    /**
     * Messures time for task t and return messured time of 4 sec.
     */
    @Test
    public void messureTimeForOneTaskShouldSucceed() {

        /*
         * ITaskTrackable task; TimedIssueEffort issueEff1;
         * 
         * task = mantisDao.findTask(taskId1);
         * 
         * issueEff1 = new TimedIssueEffort(task.getProjectId(), task.getId());
         * 
         * tiCo.startTimedIssueEffort(issueEff1); try { // CHECKSTYLE:OFF
         * Thread.sleep(4000); } catch (InterruptedException e) {
         * fail("InterruptedException should never be reached."); }
         * tiCo.stopTimedIssueEffort(issueEff1);
         * 
         * assertEquals(new Integer(4), TiTATimeConverter
         * .getSeconds((issueEff1).getDuration()));
         */
        // CHECKSTYLE:ON

    }

    /**
     * Messures time for more tasks parallel and return messured time of 7, 3
     * and 1 sec. for them.
     */
    @Test
    public void messureTimeForMoreTaskRunningParallelShouldSucceed() {
        // CHECKSTYLE:OFF
        Map<Long, ITaskTrackable> tasks;
        TimedIssueEffort tiff1, tiff2, tiff3;
       
        tasks = mantisDao.findAllTasksForProject(projectId);
        
        tiff1 = new TimedIssueEffort(tasks.get(taskId1).getProjectId(), tasks.get(taskId1).getId());
        tiff2 = new TimedIssueEffort(tasks.get(taskId2).getProjectId(), tasks.get(taskId2).getId());
        tiff3 = new TimedIssueEffort(tasks.get(taskId3).getProjectId(), tasks.get(taskId3).getId());
        
        this.tiCo.startTimedIssueEffort(tiff1);
        try {
            Thread.sleep(4000);
            this.tiCo.startTimedIssueEffort(tiff2);
            Thread.sleep(4000);
            this.tiCo.startTimedIssueEffort(tiff3);
            Thread.sleep(4000);
            // stop all tasks immediately
            this.tiCo.stopTimedIssueEffort(tiff1);
            this.tiCo.stopTimedIssueEffort(tiff2);
            this.tiCo.stopTimedIssueEffort(tiff3);
        } catch (InterruptedException e) {
            fail("InterruptedException should never be reached.");
        }

        assertEquals(new Integer(7), TiTATimeConverter.getSeconds(tiff1.getDuration()));
        assertEquals(new Integer(3), TiTATimeConverter.getSeconds(tiff2.getDuration()));
        assertEquals(new Integer(1), TiTATimeConverter.getSeconds(tiff3.getDuration()));
        // CHECKSTYLE:ON
    }

    /**
     * Messures time for more tasks parallel and return messured time of 3 and 4
     * sec. for them.
     * 
     */
    @Test
    public void messureTimeForMoreTasksFirstStoppedBeforeSecondShouldSucceed() {
        // CHECKSTYLE:OFF
        Map<Long, ITaskTrackable> tasks;
        TimedIssueEffort tiff1, tiff2;

        tasks = mantisDao.findAllTasksForProject(projectId);

        tiff1 = new TimedIssueEffort(tasks.get(taskId1).getProjectId(), tasks.get(taskId1).getId());
        tiff2 = new TimedIssueEffort(tasks.get(taskId2).getProjectId(), tasks.get(taskId2).getId());
        
        this.tiCo.startTimedIssueEffort(tiff1);
        try {
            Thread.sleep(2000);
            this.tiCo.startTimedIssueEffort(tiff2);
            Thread.sleep(2000);
            this.tiCo.stopTimedIssueEffort(tiff1);
            Thread.sleep(3000);
            // stop task 1 immediately
            this.tiCo.stopTimedIssueEffort(tiff2);
        } catch (InterruptedException e) {
            fail("InterruptedException should never be reached.");
        }

        assertEquals(new Integer(3), TiTATimeConverter.getSeconds(tiff1.getDuration()));
        assertEquals(new Integer(4), TiTATimeConverter.getSeconds(tiff2.getDuration()));
        // CHECKSTYLE:ON
    }
}

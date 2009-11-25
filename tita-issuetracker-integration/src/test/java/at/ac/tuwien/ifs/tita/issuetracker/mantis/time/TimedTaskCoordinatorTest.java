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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mantisbt.connect.MCException;

import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IIsTaskTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.mantis.base.MantisBaseTest;
import at.ac.tuwien.ifs.tita.issuetracker.mantis.dao.IssueTrackerMantisDao;
import at.ac.tuwien.ifs.tita.issuetracker.time.ITimedTask;
import at.ac.tuwien.ifs.tita.issuetracker.time.TiTATimeConverter;
import at.ac.tuwien.ifs.tita.issuetracker.time.TimedTaskCoordinator;

/**
 * Test for class TimedTaskCoordinator.
 * 
 * @author herbert
 * 
 */
public class TimedTaskCoordinatorTest extends MantisBaseTest {
    private static Long projectId = null;
    private static Long taskId1, taskId2, taskId3 = null;
    private final IssueTrackerMantisDao mantisDao = new IssueTrackerMantisDao();
    private TimedTaskCoordinator tiCo;

    /**
     * Prepare mantis connection and project for all tests.
     */
    @Override
    @Before
    public void setUp() {
        super.setUp();
        tiCo = new TimedTaskCoordinator();
        try {
            projectId = createTestProject("tita_test", "tita_test_description", true, false);
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
        IIsTaskTrackable task;

        task = mantisDao.findTask(taskId1);
        tiCo.startTimeableTask((ITimedTask) task);
        try {
            // CHECKSTYLE:OFF
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            fail("InterruptedException should never be reached.");
        }
        tiCo.stopTimeableTask((ITimedTask) task);

        assertEquals(new Integer(4), TiTATimeConverter.getSeconds(((ITimedTask) task).getDuration()));
        // CHECKSTYLE:ON

    }

    /**
     * Messures time for more tasks parallel and return messured time of 2 sec.
     * for each.
     */
    @Test
    public void messureTimeForMoreTaskShouldSucceed() {
        // CHECKSTYLE:OFF
        List<IIsTaskTrackable> tasks;

        tasks = mantisDao.findAllTasksForProject(projectId);
        for (IIsTaskTrackable trackable : tasks) {
            System.out.println("TaskId: " + trackable.getId());
        }

        tiCo.startTimeableTask(((ITimedTask) (tasks.get(0))));
        try {
            Thread.sleep(4000);
            tiCo.startTimeableTask(((ITimedTask) (tasks.get(1))));
            Thread.sleep(4000);
            tiCo.startTimeableTask(((ITimedTask) (tasks.get(2))));
            Thread.sleep(4000);
            // stop all tasks immediately
            tiCo.stopTimeableTask(((ITimedTask) (tasks.get(0))));
            tiCo.stopTimeableTask(((ITimedTask) (tasks.get(1))));
            tiCo.stopTimeableTask(((ITimedTask) (tasks.get(2))));

            for (Integer tm : tiCo.getTaskDurations().keySet()) {
                System.out.println("TaskId: " + tm + " - Duration: "
                        + TiTATimeConverter.getSeconds(tiCo.getTaskDurations().get(tm)));
            }

        } catch (InterruptedException e) {
            fail("InterruptedException should never be reached.");
        }

        assertEquals(new Integer(7), TiTATimeConverter.getSeconds(tiCo.getTaskDurations().get(
                ((ITimedTask) (tasks.get(0))).getTimedTaskId())));
        assertEquals(new Integer(3), TiTATimeConverter.getSeconds(tiCo.getTaskDurations().get(
                ((ITimedTask) (tasks.get(1))).getTimedTaskId())));
        assertEquals(new Integer(1), TiTATimeConverter.getSeconds(tiCo.getTaskDurations().get(
                ((ITimedTask) (tasks.get(2))).getTimedTaskId())));
        // CHECKSTYLE:ON
    }
}

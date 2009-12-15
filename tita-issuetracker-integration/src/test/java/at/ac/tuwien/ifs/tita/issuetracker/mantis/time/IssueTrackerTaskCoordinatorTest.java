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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mantisbt.connect.MCException;

import at.ac.tuwien.ifs.tita.business.util.TiTATimeConverter;
import at.ac.tuwien.ifs.tita.issuetracker.container.TimedIssueEffort;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;
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
    private static Long projectId = null;
    private static Long taskId1, taskId2, taskId3 = null;
    private final IssueTrackerMantisDao mantisDao = new IssueTrackerMantisDao();
    private TimedIssueEffortCoordinator tiCo;

    /**
     * Prepare mantis connection and project for all tests.
     */
    @Override
    @Before
    public void setUp() {
        super.setUp();
        this.tiCo = new TimedIssueEffortCoordinator();
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
        ITaskTrackable task;
        TimedIssueEffort issueEff1;
        
        task = this.mantisDao.findTask(taskId1);
       
        issueEff1 = new TimedIssueEffort(task.getProjectId(), task.getId());
        
        this.tiCo.startTimedIssueEffort(issueEff1);
        try {
            // CHECKSTYLE:OFF
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            fail("InterruptedException should never be reached.");
        }
        this.tiCo.stopTimedIssueEffort(issueEff1);

        assertEquals(new Integer(4), TiTATimeConverter
                .getSeconds((issueEff1).getDuration()));
        // CHECKSTYLE:ON

    }

    /**
     * Messures time for more tasks parallel and return messured time of 7, 3
     * and 1 sec. for them.
     */
//    @Test
//    public void messureTimeForMoreTaskRunningParallelShouldSucceed() {
//        // CHECKSTYLE:OFF
//        Map<Long, ITaskTrackable> tasks;
//        Long[] ids = new Long[3];
//        int i = 0;
//
//        tasks = mantisDao.findAllTasksForProject(projectId);
//        for (ITaskTrackable trackable : tasks.values()) {
//            System.out.println("TaskId: " + trackable.getId());
//            ids[i] = trackable.getId();
//            i++;
//        }
//
//        this.tiCo.startTimeableTask(((ITimedEffort) (tasks.get(ids[0]))));
//        try {
//            Thread.sleep(4000);
//            this.tiCo.startTimeableTask(((ITimedEffort) (tasks.get(ids[1]))));
//            Thread.sleep(4000);
//            this.tiCo.startTimeableTask(((ITimedEffort) (tasks.get(ids[2]))));
//            Thread.sleep(4000);
//            // stop all tasks immediately
//            this.tiCo.stopTimeableTask(((ITimedEffort) (tasks.get(ids[0]))));
//            this.tiCo.stopTimeableTask(((ITimedEffort) (tasks.get(ids[1]))));
//            this.tiCo.stopTimeableTask(((ITimedEffort) (tasks.get(ids[2]))));
//
//            for (Integer tm : this.tiCo.getTaskDurations().keySet()) {
//                System.out.println("TaskId: "
//                        + tm
//                        + " - Duration: "
//                        + TiTATimeConverter.getSeconds(this.tiCo
//                                .getTaskDurations().get(tm)));
//            }
//
//        } catch (InterruptedException e) {
//            fail("InterruptedException should never be reached.");
//        }
//
//        assertEquals(new Integer(7), TiTATimeConverter.getSeconds(this.tiCo
//                .getTaskDurations().get(
//                        ((ITimedEffort) (tasks.get(ids[0]))).getTimedTaskId())));
//        assertEquals(new Integer(3), TiTATimeConverter.getSeconds(this.tiCo
//                .getTaskDurations().get(
//                        ((ITimedEffort) (tasks.get(ids[1]))).getTimedTaskId())));
//        assertEquals(new Integer(1), TiTATimeConverter.getSeconds(this.tiCo
//                .getTaskDurations().get(
//                        ((ITimedEffort) (tasks.get(ids[2]))).getTimedTaskId())));
//        // CHECKSTYLE:ON
//    }
//
//    /**
//     * Messures time for more tasks parallel and return messured time of 3 and 4
//     * sec. for them.
//     */
//    @Test
//    public void messureTimeForMoreTasksFirstStoppedBeforeSecondShouldSucceed() {
//        // CHECKSTYLE:OFF
//        Map<Long, ITaskTrackable> tasks;
//        Long[] ids = new Long[3];
//        int i = 0;
//
//        tasks = mantisDao.findAllTasksForProject(projectId);
//        for (ITaskTrackable trackable : tasks.values()) {
//            System.out.println("TaskId: " + trackable.getId());
//            ids[i] = trackable.getId();
//            i++;
//        }
//
//        this.tiCo.startTimeableTask(((ITimedEffort) (tasks.get(ids[0]))));
//        try {
//            Thread.sleep(2000);
//            this.tiCo.startTimeableTask(((ITimedEffort) (tasks.get(ids[1]))));
//            Thread.sleep(2000);
//            this.tiCo.stopTimeableTask(((ITimedEffort) (tasks.get(ids[0]))));
//            Thread.sleep(3000);
//            // stop task 1 immediately
//            this.tiCo.stopTimeableTask(((ITimedEffort) (tasks.get(ids[1]))));
//
//            for (Integer tm : this.tiCo.getTaskDurations().keySet()) {
//                System.out.println("TaskId: "
//                        + tm
//                        + " - Duration: "
//                        + TiTATimeConverter.getSeconds(this.tiCo
//                                .getTaskDurations().get(tm)));
//            }
//
//        } catch (InterruptedException e) {
//            fail("InterruptedException should never be reached.");
//        }
//
//        assertEquals(new Integer(3), TiTATimeConverter.getSeconds(this.tiCo
//                .getTaskDurations().get(
//                        ((ITimedEffort) (tasks.get(ids[0]))).getTimedTaskId())));
//        assertEquals(new Integer(4), TiTATimeConverter.getSeconds(this.tiCo
//                .getTaskDurations().get(
//                        ((ITimedEffort) (tasks.get(ids[1]))).getTimedTaskId())));
//        // CHECKSTYLE:ON
//    }
}

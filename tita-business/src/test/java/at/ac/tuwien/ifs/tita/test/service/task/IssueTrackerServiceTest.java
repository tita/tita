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

package at.ac.tuwien.ifs.tita.test.service.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.mantisbt.connect.MCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.NotTransactional;

import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueStatus;
import at.ac.tuwien.ifs.tita.issuetracker.exceptions.ProjectNotFoundException;
import at.ac.tuwien.ifs.tita.issuetracker.issue.service.IssueTrackerService;

/**
 * Test for testing task service that imports all tasks comming from an issue
 * tracker.
 *
 * @author herbert
 *
 */
public abstract class IssueTrackerServiceTest extends MantisBaseTest {

    protected IssueTrackerService issueTrackerService = new IssueTrackerService(defaultLogin);

    protected final Integer numberOfProjects = 3;
    protected final int numberOfTasksForEachProject = 2;

    protected List<Long> taskIds = new ArrayList<Long>();
    protected List<Long> projectIds = new ArrayList<Long>();

    private final Logger log = LoggerFactory.getLogger(IssueTrackerServiceTest.class);

    /**
     * Prepare mantis connection and create a setup in mantis with projects and
     * tasks.
     */
    public void prepareSetup() {
        super.connectToMantis();

        try {
            createSetup(numberOfProjects, numberOfTasksForEachProject);
        } catch (MCException e) {
            fail("Mantis connection error.");
        } catch (ProjectNotFoundException e) {
            fail("Project must be set.");
        } catch (InterruptedException e) {
            fail("Interruption Failure.");
        }
    }

    /**
     * Delete mantis projects for all tests.
     *
     * @throws InterruptedException
     *             e
     */
    public void deleteSetup() throws InterruptedException {
        deleteSetupAndChanges();
    }

    /**
     * The method creates a setup in mantis to evaluate the performance and the
     * update mechanism from TiTA.
     *
     * @param amountOfProjects
     *            p
     * @param amountOfTasksForEachProject
     *            p
     * @throws MCException
     *             e
     * @throws ProjectNotFoundException
     *             ef
     * @throws InterruptedException
     *             ie
     */
    @NotTransactional
    protected void createSetup(int amountOfProjects, int amountOfTasksForEachProject)
            throws MCException, ProjectNotFoundException, InterruptedException {
        List<Long> taskids = new ArrayList<Long>();
        List<Long> projectids = new ArrayList<Long>();

        startTimer("Starting setup:");

        Long id = 1L;
        Long taskid = 1L;

        for (int i = 0; i < amountOfProjects; i++) {
            id = createTestProject("projectName" + i, "description" + i, true, false);
            projectids.add(id);

            for (int j = 0; j < amountOfTasksForEachProject; j++) {
                taskid = createTestTask("tita_test_issue" + i + j, "issue_summary" + i + j,
                        "projectName" + i);
                taskids.add(taskid);
            }
        }
        taskIds = taskids;
        projectIds = projectids;

        assertEquals(0, issueTrackerService.getProjects().size());

        issueTrackerService = new IssueTrackerService(defaultLogin);
        issueTrackerService.updateAll();

        // CHECKSTYLE:OFF
        // Sleeping because the updating is made on a other thread. This
        // thread will be quicker.
        Thread.sleep(15000);
        // CHECKSTYLE:ON

        assertEquals(amountOfProjects, issueTrackerService.getProjects().size());
        assertEquals(amountOfTasksForEachProject, issueTrackerService.getIssueTrackerTasks(
                issueTrackerService.getProjects().get(id), IssueStatus.NEW).size());

        stopTimer("End of the setup.");
        System.out.print(getPerformanceOutput());
    }

    /**
     * The method undo the setup for mantis.
     *
     * @throws InterruptedException
     *             ie
     */
    @NotTransactional
    protected void deleteSetupAndChanges() throws InterruptedException {
        // CHECKSTYLE:OFF
        Thread.sleep(2000);

        startTimer("Starting deleting:");

        // delete tasks
        for (int i = 0; i < taskIds.size(); i++) {
            deleteTestTask(taskIds.get(i));
        }
        // delete projects
        for (int i = 0; i < numberOfProjects; i++) {
            deleteTestProject("projectName" + i);
        }

        stopTimer("End of deleting.");
        System.out.print(getPerformanceOutput());

        issueTrackerService = null;
    }
}

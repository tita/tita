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
package at.ac.tuwien.ifs.tita.issutracker.mantis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueResolution;
import at.ac.tuwien.ifs.tita.issuetracker.enums.ProjectStatus;
import at.ac.tuwien.ifs.tita.issuetracker.enums.ViewState;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IProjectTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.mantis.base.MantisBaseTest;
import at.ac.tuwien.ifs.tita.issuetracker.mantis.dao.IssueTrackerMantisDao;

/**
 * Class for Testing the Mantis Dao.
 * 
 * @author Karin
 * 
 */
public class IssueTrackerMantisDaoTest extends MantisBaseTest {

    /**
     * Test: find Project by ProjectName.
     */
    @Test
    public void findProjectByName() {
        try {

            createTestProject("tita_test", "tita_test_description", true, false);

            IssueTrackerMantisDao dao = new IssueTrackerMantisDao();
            IProjectTrackable mantisProject = dao.findProject("tita_test");
            assertEquals("tita_test", mantisProject.getName());
            assertEquals("tita_test_description", mantisProject
                    .getDescription());
            assertEquals(ProjectStatus.OPEN, mantisProject.getStatus());
            assertEquals(ViewState.PUBLIC, mantisProject.getViewState());

        } catch (Exception e) {
            assertTrue(false);
        } finally {
            deleteTestProject("tita_test");
        }
    }

    /**
     * Test: find Task by TaskId.
     */
    @Test
    public void findTaskById() {
        Long taskId = 0L;
        Long projectId = 0L;

        try {
            projectId = createTestProject("tita_test", "tita_test_description",
                    true, false);
            taskId = createTestTask("tita_test_issue1", "issue_summary1",
                    "tita_test");

            IssueTrackerMantisDao dao = new IssueTrackerMantisDao();
            ITaskTrackable mantisTask = dao.findTask(taskId);
            assertEquals("tita_test_issue1", mantisTask.getDescription());
            assertEquals("issue_summary1", mantisTask.getSummary());
            assertEquals(IssueResolution.OPEN, mantisTask.getResolution());
            assertEquals(projectId, mantisTask.getProjectId());

        } catch (Exception e) {
            assertTrue(false);
        } finally {
            deleteTestTask(taskId);
            deleteTestProject("tita_test");
        }
    }

    /**
     * Test: find comment by commentId.
     */
    @Test
    public void findCommentById() {
        Long taskId = 0L;
        Long projectId = 0L;
        Long commentId = 0L;
        try {
            projectId = createTestProject("tita_test", "tita_test_description",
                    true, false);
            taskId = createTestTask("tita_test_issue1", "issue_summary1",
                    "tita_test");
            commentId = createTestComment("test_comment_text", true, taskId);

            IssueTrackerMantisDao dao = new IssueTrackerMantisDao();
            ITaskTrackable mantisTask = dao.findTask(taskId);
            assertEquals("tita_test_issue1", mantisTask.getDescription());
            assertEquals("issue_summary1", mantisTask.getSummary());
            assertEquals(IssueResolution.OPEN, mantisTask.getResolution());
            assertEquals(projectId, mantisTask.getProjectId());

        } catch (Exception e) {
            assertTrue(false);
        } finally {
            deleteTestComment(commentId);
            deleteTestTask(taskId);
            deleteTestProject("tita_test");
        }
    }

    /**
     * The test case should fetch the correct amount of accessible projects from
     * mantis. The connection close and refresh should be done starting a new
     * one.
     */
    @Test
    public void getAllAccessibleProjects_shouldFindCorrectAmountOfProjects() {
        Long projectId = 4L;

        try {
            projectId = createTestProject("tita_test1",
                    "tita_test_description", true, false);

            // Create connection
            IssueTrackerMantisDao dao = new IssueTrackerMantisDao();
            assertEquals(1, dao.getAllAccessibleProjects().size());

            projectId = createTestProject("tita_test2",
                    "tita_test_description", true, false);

            // Close the session because it is already running
            IssueTrackerMantisDao dao_current = new IssueTrackerMantisDao();
            assertEquals(2, dao_current.getAllAccessibleProjects().size());

            deleteTestProject("tita_test1");
            deleteTestProject("tita_test2");

            // Explicit close for the connection
            dao_current.disconnect();
            IssueTrackerMantisDao dao_current2 = new IssueTrackerMantisDao();
            assertEquals(0, dao_current2.getAllAccessibleProjects().size());
        } catch (Exception e) {
            fail("Creating projects or creating session failed!");
        }
    }
}

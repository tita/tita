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

import org.junit.Test;
import org.mantisbt.connect.model.IIssue;

import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueResolution;
import at.ac.tuwien.ifs.tita.issuetracker.enums.ProjectStatus;
import at.ac.tuwien.ifs.tita.issuetracker.enums.ViewState;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IIsProjectTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IIsTaskTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.mantis.base.MantisBaseTest;
import at.ac.tuwien.ifs.tita.issuetracker.mantis.dao.IssueTrackerMantisDao;
/**
 * Class for Testing the Mantis Dao.
 * @author Karin
 *
 */
public class IssueTrackerMantisDaoTest extends MantisBaseTest{
    
    /**
     * Test: find Project by ProjectName.
     */
    @Test
    public void findProjectByName() {
        try {
            
            createTestProject("tita_test", "tita_test_description", true, false);

            IssueTrackerMantisDao dao = new IssueTrackerMantisDao();
            IIsProjectTrackable mantisProject = dao.findProject("tita_test");
            assertEquals("tita_test", mantisProject.getName());
            assertEquals("tita_test_description", mantisProject.getDescription());
            assertEquals(ProjectStatus.OPEN, mantisProject.getStatus());
            assertEquals(ViewState.PUBLIC, mantisProject.getViewState());
        
        } catch (Exception e) {
            assertTrue(false);
        } finally{
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
            projectId = createTestProject("tita_test", "tita_test_description", true, false);
            taskId = createTestTask("tita_test_issue1", "issue_summary1","tita_test" );

            IssueTrackerMantisDao dao = new IssueTrackerMantisDao();
            IIsTaskTrackable mantisTask = dao.findTask(taskId);
            assertEquals("tita_test_issue1", mantisTask.getDescription());
            assertEquals("issue_summary1", mantisTask.getSummary());
            assertEquals(IssueResolution.OPEN, mantisTask.getResolution());
            assertEquals(projectId, mantisTask.getProjectId());
        
        } catch (Exception e) {
            assertTrue(false);
        } finally{
            deleteTestTask(taskId);
            deleteTestProject("tita_test");   
        }
    }
    
    /**
     * Test: close task
     */
    @Test
    public void closeTask() {
        long taskId = 0;
        try {
            createTestProject("tita_test", "tita_test_description", true, false);
            taskId = createTestTask("tita_test_issue1", "issue_summary1","tita_test" );

            //close task
            IssueTrackerMantisDao dao = new IssueTrackerMantisDao();
            dao.closeTask(taskId);      
            
            //check
            IIssue issue = session.getIssue(taskId);
            assertEquals("fixed", issue.getResolution().getName());
            
        
        } catch (Exception e) {
            assertTrue(false);
        } finally{
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
            projectId = createTestProject("tita_test", "tita_test_description", true, false);
            taskId = createTestTask("tita_test_issue1", "issue_summary1","tita_test" );
            commentId = createTestComment("test_comment_text", true, taskId);
            
            IssueTrackerMantisDao dao = new IssueTrackerMantisDao();
            IIsTaskTrackable mantisTask = dao.findTask(taskId);
            assertEquals("tita_test_issue1", mantisTask.getDescription());
            assertEquals("issue_summary1", mantisTask.getSummary());
            assertEquals(IssueResolution.OPEN, mantisTask.getResolution());
            assertEquals(projectId, mantisTask.getProjectId());
        
        } catch (Exception e) {
            assertTrue(false);
        } finally{
            deleteTestComment(commentId);
            deleteTestTask(taskId);
            deleteTestProject("tita_test");  
        }
    }
}

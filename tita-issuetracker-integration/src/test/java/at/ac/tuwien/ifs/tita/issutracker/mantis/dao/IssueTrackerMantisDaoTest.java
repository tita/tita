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

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.mantisbt.connect.AccessLevel;
import org.mantisbt.connect.MCException;
import org.mantisbt.connect.axis.MCSession;
import org.mantisbt.connect.model.IIssue;
import org.mantisbt.connect.model.INote;
import org.mantisbt.connect.model.IProject;
import org.mantisbt.connect.model.Issue;
import org.mantisbt.connect.model.MCAttribute;
import org.mantisbt.connect.model.Note;
import org.mantisbt.connect.model.Project;

import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueResolution;
import at.ac.tuwien.ifs.tita.issuetracker.enums.ProjectStatus;
import at.ac.tuwien.ifs.tita.issuetracker.enums.ViewState;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IIsProjectTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IIsTaskTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.mantis.dao.IssueTrackerMantisDao;
/**
 * Class for Testing the Mantis Dao.
 * @author Karin
 *
 */
public class IssueTrackerMantisDaoTest {
    private MCSession session;
    private String url = "http://localhost/mantisbt-1.1.8/api/soap/mantisconnect.php";
    private String user = "administrator";
    private String pwd = "root";
    
    /**
     * Connects to Mantis-Server.
     */
    @Before
    public void setUp() {
        try {
            URL u = new URL(url);
            session = new MCSession(u, user, pwd);
        } catch (MCException e) {
            assertTrue(false);
        } catch (MalformedURLException e) {
            assertTrue(false);
        }
    }

    
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
        long taskId = 0;
        long projectId = 0;
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
        long taskId = 0;
        long projectId = 0;
        long commentId = 0;
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
    
    /**
     * Creates a Project on the Mantis-Server.
     * @param projectName - name of the project
     * @param description - description of the project
     * @param enabled - status of the project
     * @param viewStatePrivate - private or public
     * @return id of the created project
     * @throws MCException - if error occurs, when project is added
     */
    private long createTestProject(String projectName, String description, boolean enabled,
            boolean viewStatePrivate) throws MCException{
        
        IProject newProject = new Project();
        newProject.setName(projectName);
        newProject.setAccessLevelMin(AccessLevel.DEVELOPER);
        newProject.setDesription(description);
        newProject.setEnabled(enabled); //ProjectStatus: Open
        newProject.setPrivate(viewStatePrivate); //ViewState:Public
        long id = session.addProject(newProject);
        session.flush();
        return id;
    }
    
    /**
     * Creates a task on the Mantis-Server.
     * @param description - description of the project
     * @param summary - summary of the project
     * @param projectName - name of the project of the task
     * @return id of the created task
     * @throws MCException - if error occurs, when task is added
     */
    private long createTestTask(String description, String summary, String projectName)
        throws MCException{
        
        IIssue newIssue = new Issue();
        newIssue.setDescription(description);
        //newIssue.setHandler(new Account(100, "test", "test", "test@test"));
        newIssue.setPriority(session.getDefaultIssuePriority());
        newIssue.setSummary(summary);
        newIssue.setSeverity(session.getDefaultIssueSeverity());
        //newIssue.setReporter(new Account(101, "rep1", "rep1", "rep@rep"));  
        IProject p = session.getProject(projectName);
        newIssue.setProject(new MCAttribute(p.getId(), p.getName()));
        long id = session.addIssue(newIssue);
        session.flush();
        return id;
    }
    
    /**
     * Creates a comment on the Mantis-Server.
     * @param text - text of the comment
     * @param isPrivate - if true, comment is private, else public
     * @param issueId - id of the issue, the comment is linked to
     * @return id of the created comment
     * @throws MCException - MCException - if error occurs, when comment is added
     */
    private long createTestComment(String text, boolean isPrivate, long issueId) throws MCException{
        
        INote newNote = new Note();
        newNote.setText(text);
        newNote.setPrivate(isPrivate);
        long id = session.addNote(issueId, newNote);
        session.flush();
        return id;
    }
    
    /**
     * Deletes project on the Mantis-Server.
     * @param projectName - name of the project to delete
     */
    private void deleteTestProject(String projectName){
        IProject old;
        try {
            old = session.getProject(projectName);
            if(old != null){
                session.deleteProject(old.getId());
                session.flush();
            }
        } catch (MCException e) {
            assertTrue(false);
        }
    }
    /**
     * Deletes task on the Mantis-Server.
     * @param taskId - id of the task to delete
     */
    private void deleteTestTask(long taskId) {
        try {
            session.deleteIssue(taskId);
            session.flush();
        } catch (MCException e) {
            assertTrue(false);
        }
    }
    
    /**
     * Deletes comment on the Mantis-Server.
     * @param commentId - id of the comment to delete
     */
    private void deleteTestComment(long commentId) {
        try {
            session.deleteNote(commentId);
            session.flush();
        } catch (MCException e) {
            assertTrue(false);
        }
    }
}

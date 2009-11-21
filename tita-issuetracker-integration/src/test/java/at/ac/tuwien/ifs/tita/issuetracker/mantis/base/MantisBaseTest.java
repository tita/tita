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

package at.ac.tuwien.ifs.tita.issuetracker.mantis.base;

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
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

/**
 * Base class for all local mantis tests (connecting, etc.).
 * 
 * @author herbert
 * 
 */
public class MantisBaseTest {
    protected MCSession session;
    private final String url = "http://localhost/mantisbt-1.1.8/api/soap/mantisconnect.php";
    private final String user = "administrator";
    private final String pwd = "root";

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
     * Creates a Project on the Mantis-Server.
     * 
     * @param projectName - name of the project
     * @param description - description of the project
     * @param enabled - status of the project
     * @param viewStatePrivate - private or public
     * @return id of the created project
     * @throws MCException - if error occurs, when project is added
     */
    protected Long createTestProject(String projectName, String description, Boolean enabled, Boolean viewStatePrivate)
            throws MCException {

        IProject newProject = new Project();
        newProject.setName(projectName);
        newProject.setAccessLevelMin(AccessLevel.DEVELOPER);
        newProject.setDesription(description);
        newProject.setEnabled(enabled); // ProjectStatus: Open
        newProject.setPrivate(viewStatePrivate); // ViewState:Public
        Long id = session.addProject(newProject);
        session.flush();
        return id;
    }

    /**
     * Creates a task on the Mantis-Server.
     * 
     * @param description - description of the project
     * @param summary - summary of the project
     * @param projectName - name of the project of the task
     * @return id of the created task
     * @throws MCException - if error occurs, when task is added
     */
    protected Long createTestTask(String description, String summary, String projectName) throws MCException {

        IIssue newIssue = new Issue();
        newIssue.setDescription(description);
        // newIssue.setHandler(new Account(100, "test", "test", "test@test"));
        newIssue.setPriority(session.getDefaultIssuePriority());
        newIssue.setSummary(summary);
        newIssue.setSeverity(session.getDefaultIssueSeverity());
        // newIssue.setReporter(new Account(101, "rep1", "rep1", "rep@rep"));
        IProject p = session.getProject(projectName);
        newIssue.setProject(new MCAttribute(p.getId(), p.getName()));
        long id = session.addIssue(newIssue);
        session.flush();
        return id;
    }

    /**
     * Creates a comment on the Mantis-Server.
     * 
     * @param text - text of the comment
     * @param isPrivate - if true, comment is private, else public
     * @param issueId - id of the issue, the comment is linked to
     * @return id of the created comment
     * @throws MCException - MCException - if error occurs, when comment is
     *         added
     */
    protected Long createTestComment(String text, boolean isPrivate, long issueId) throws MCException {
        INote newNote = new Note();
        newNote.setText(text);
        newNote.setPrivate(isPrivate);
        Long id = session.addNote(issueId, newNote);
        session.flush();
        return id;
    }

    /**
     * Deletes project on the Mantis-Server.
     * 
     * @param projectName - name of the project to delete
     */
    protected void deleteTestProject(String projectName) {
        IProject old;
        try {
            old = session.getProject(projectName);
            if (old != null) {
                session.deleteProject(old.getId());
                session.flush();
            }
        } catch (MCException e) {
            assertTrue(false);
        }
    }

    /**
     * Deletes task on the Mantis-Server.
     * 
     * @param taskId - id of the task to delete
     */
    protected void deleteTestTask(long taskId) {
        try {
            session.deleteIssue(taskId);
            session.flush();
        } catch (MCException e) {
            assertTrue(false);
        }
    }

    /**
     * Deletes comment on the Mantis-Server.
     * 
     * @param commentId - id of the comment to delete
     */
    protected void deleteTestComment(long commentId) {
        try {
            session.deleteNote(commentId);
            session.flush();
        } catch (MCException e) {
            assertTrue(false);
        }
    }
}

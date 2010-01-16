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

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Ignore;
import org.junit.runner.RunWith;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.NotTransactional;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;

/**
 * Base class for all local mantis tests (connecting, etc.).
 *
 * @author herbert
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:datasourceContext-test.xml" })
@TransactionConfiguration
@Transactional
public abstract class MantisBaseTest {

    protected MCSession session;
    //TODO: delete
    protected final IssueTrackerLogin defaultLogin = new IssueTrackerLogin("administrator",
            "root", new IssueTracker(1L, "test-mantis", "http://localhost/mantisbt-1.1.8"),null);

    private final String url = defaultLogin.getIssueTracker().getUrl() + "/api/soap/mantisconnect.php";
    private final String user = defaultLogin.getUserName();
    private final String pwd = defaultLogin.getPassword();

    private Long startTime;
    private Long endTime;
    private String performanceOutput = "";

    private final Logger log = LoggerFactory.getLogger(MantisBaseTest.class);

    /**
     * Connects to Mantis-Server.
     */
    public void connectToMantis() {
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
     * @param projectName
     *            - name of the project
     * @param description
     *            - description of the project
     * @param enabled
     *            - status of the project
     * @param viewStatePrivate
     *            - private or public
     * @return id of the created project
     * @throws MCException
     *             - if error occurs, when project is added
     */
    @NotTransactional
    protected Long createTestProject(String projectName, String description,
            Boolean enabled, Boolean viewStatePrivate) throws MCException {

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
     * @param description
     *            - description of the project
     * @param summary
     *            - summary of the project
     * @param projectName
     *            - name of the project of the task
     * @return id of the created task
     * @throws MCException
     *             - if error occurs, when task is added
     */
    @NotTransactional
    protected Long createTestTask(String description, String summary,
            String projectName) throws MCException {

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
     * @param text
     *            - text of the comment
     * @param isPrivate
     *            - if true, comment is private, else public
     * @param issueId
     *            - id of the issue, the comment is linked to
     * @return id of the created comment
     * @throws MCException
     *             - MCException - if error occurs, when comment is added
     */
    @NotTransactional
    protected Long createTestComment(String text, boolean isPrivate,
            long issueId) throws MCException {
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
     * @param projectName
     *            - name of the project to delete
     */
    @NotTransactional
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
     * @param taskId
     *            - id of the task to delete
     */
    @NotTransactional
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
     * @param commentId
     *            - id of the comment to delete
     */
    @NotTransactional
    protected void deleteTestComment(long commentId) {
        try {
            session.deleteNote(commentId);
            session.flush();
        } catch (MCException e) {
            assertTrue(false);
        }
    }

    /**
     * Starts the timer.
     *
     * @param description
     *            - It describes the measured situation.
     */
    @NotTransactional
    protected void startTimer(String description) {
        log.debug(description);
        performanceOutput = "";
        performanceOutput += description + "\n";
        startTime = System.currentTimeMillis();
    }

    /**
     * Stops the timer.
     *
     * @param description
     *            - It describes the measured situation.
     */
    @NotTransactional
    protected void stopTimer(String description) {
        log.debug(description);
        performanceOutput += description + "\n";
        endTime = System.currentTimeMillis();
        showDuration();
    }

    /**
     * Shows the duration that was measured.
     */
    @NotTransactional
    private void showDuration() {
        // CHECKSTYLE:OFF
        log.debug("Duration:" + (getEndTime() - getStartTime()) / 1000 + " sec.");
        performanceOutput += "Duration:" + (getEndTime() - getStartTime()) / 1000 + " sec." + "\n";
        // CHECKSTYLE:ON
    }

    /**
     * Returns the startTime.
     *
     * @return startTime of the measured activity.
     */
    @NotTransactional
    private Long getStartTime() {
        return startTime;
    }

    /**
     * Returns the endTime.
     *
     * @return endTime of the measured activity.
     */
    @NotTransactional
    private Long getEndTime() {
        return endTime;
    }

    @NotTransactional
    protected String getPerformanceOutput() {
        return performanceOutput;
    }

}

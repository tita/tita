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
package at.ac.tuwien.ifs.tita.issuetracker.mantis.dao;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.mantisbt.connect.AccessLevel;
import org.mantisbt.connect.Enumeration;
import org.mantisbt.connect.IMCSession;
import org.mantisbt.connect.MCException;
import org.mantisbt.connect.axis.MCSession;
import org.mantisbt.connect.model.IIssue;
import org.mantisbt.connect.model.IMCAttribute;
import org.mantisbt.connect.model.INote;
import org.mantisbt.connect.model.IProject;
import org.mantisbt.connect.model.Issue;
import org.mantisbt.connect.model.MCAttribute;
import org.mantisbt.connect.model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.issuetracker.container.IssueTrackerCommentTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.container.IssueTrackerProjectTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.container.IssueTrackerTaskTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssuePriority;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueResolution;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueSeverity;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueStatus;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueTrackingTool;
import at.ac.tuwien.ifs.tita.issuetracker.enums.ProjectStatus;
import at.ac.tuwien.ifs.tita.issuetracker.enums.ViewState;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ICommentTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IIssueTrackerDao;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IProjectTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;

/**
 * The dao for reading Data (Projects/Issues/Notes) from the mantis server.
 *
 * @author Karin
 *
 */
public class IssueTrackerMantisDao implements IIssueTrackerDao {


    private static final int C_ASSIGEND = 4;
    private static final int C_CLOSED = 6;

    private final Logger log = LoggerFactory.getLogger(IssueTrackerMantisDao.class);

    private URL url;
    private String urlAsString;
    private String user;
    private String pwd;
    private IMCSession session;


    public IssueTrackerMantisDao(IssueTrackerLogin login) {
        // TODO: read values from DB
        this.user = login.getUserName();
        this.pwd = login.getPassword();
        this.urlAsString = login.getIssueTracker().getUrl();

        if (this.session != null) {
            disconnect();
        }
        connect();
    }

    public IssueTrackerMantisDao(){
    }

    /**
     * Opens a Session to the Mantis-Server.
     */
    private void connect() {
        try {
            // TODO: read values from DB
            this.url = new URL(this.urlAsString + "/api/soap/mantisconnect.php");
            this.session = new MCSession(this.url, this.user, this.pwd);
        } catch (MCException e) {
            this.log.error("Connection to Mantis-Server failed!");
        } catch (MalformedURLException e) {
            this.log.error("URL " + this.url.getPath() + " is malformed!");
        }
    }

    /**
     * Close a Session to the Mantis-Server.
     */
    public void disconnect() {
        this.session = null;
    }

    /** {@inheritDoc} */
    @Override
    public IProjectTrackable findProject(Long id) {

        IProject project;
        try {
            project = this.session.getProject(id);
            IProjectTrackable mantisProject = createMantisProject(project);
            return mantisProject;

        } catch (MCException e) {
            this.log.error("Reading Project with Id " + id + " from Mantis-Server failed!");
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Map<Long, ITaskTrackable> findAllTasksForProject(Long projectId) {
        Map<Long, ITaskTrackable> taskList = new TreeMap<Long, ITaskTrackable>();
        try {
            IIssue[] issues = this.session.getProjectIssues(projectId);
            for (IIssue issue : issues) {
                IssueTrackerTaskTrackable task = createMantisTask(issue, this.session.getProject(projectId));
                taskList.put(task.getId(), task);
            }
            return taskList;
        } catch (MCException e) {
            this.log.error("Reading Project Issues with projectId " + projectId + " from Mantis-Server failed!");
        }

        return null;

    }

    /** {@inheritDoc} */
    public Map<Long, ICommentTrackable> findAllCommentsForTask(Long taskId) {
        Map<Long, ICommentTrackable> commentList = new TreeMap<Long, ICommentTrackable>();

        try {
            INote[] notes = this.session.getIssue(taskId).getNotes();
            for (INote note : notes) {
                IssueTrackerCommentTrackable comment = createMantisComment(note);
                commentList.put(comment.getId(), comment);
            }
            return commentList;
        } catch (MCException e) {
            this.log.error("Reading Comments from Task with taskId " + taskId + " from Mantis-Server failed!");
        }

        return null;

    }

    /** {@inheritDoc} */
    public ITaskTrackable findTask(Long taskId, Long projectId) {
        IIssue issue;
        IProject project;
        try {
            issue = this.session.getIssue(taskId);
            project = this.session.getProject(projectId);
            IssueTrackerTaskTrackable mantisTask = createMantisTask(issue, project);
            return mantisTask;

        } catch (MCException e) {
            this.log.error("Reading Task with taskId " + taskId + " from Mantis-Server failed!");
        }
        return null;
    }

    /** {@inheritDoc} */
    public IProjectTrackable findProject(String projectName) {
        IProject project;
        try {
            project = this.session.getProject(projectName);
            IProjectTrackable mantisProject = createMantisProject(project);
            return mantisProject;

        } catch (MCException e) {
            this.log.error("Reading project with projectName " + projectName + " from Mantis-Server failed!");
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Map<Long, IProjectTrackable> findAccessibleProjects() {
        IProject[] projects;
        Map<Long, IProjectTrackable> mapOfProjects = new TreeMap<Long, IProjectTrackable>();

        try {
            projects = this.session.getAccessibleProjects();
            for (IProject project : projects) {
                IProjectTrackable mantisProject = createMantisProject(project);
                mapOfProjects.put(project.getId(), mantisProject);
            }
            return mapOfProjects;

        } catch (MCException e) {
            this.log.error("Reading accessible projects from Mantis-Server failed!");
        }
        return null;
    }

    /** {@inheritDoc} */
    public void assignTask(Long taskId) throws MCException {
        IIssue issue = this.session.getIssue(taskId);
        IMCAttribute[] status = this.session.getEnum(Enumeration.STATUS);
        issue.setStatus(status[C_ASSIGEND]); // 4...assigned
        this.session.updateIssue(issue);
        this.session.flush();
    }

    /** {@inheritDoc} */
    public void closeTask(Long taskId) throws MCException {
        IIssue issue = this.session.getIssue(taskId);
        IMCAttribute[] resolutions = this.session.getEnum(Enumeration.RESOLUTIONS);
        IMCAttribute[] status = this.session.getEnum(Enumeration.STATUS);
        issue.setResolution(resolutions[1]);// fixed
        issue.setStatus(status[C_CLOSED]); // 6...closed
        this.session.updateIssue(issue);
        this.session.flush();
    }

    /**
     * Copies all values from a Mantis-Project-Object to an IssueTrackerProject.
     *
     * @param project - Project to copy
     * @return new IssueTrackerProject
     */
    public IProjectTrackable createMantisProject(IProject project) {
        Long id = project.getId();
        String name = project.getName();
        String description = project.getDescription();
        ProjectStatus status = IssueTrackerMantisEnum.extractProjectStatus(project);
        // Map<Long, ITaskTrackable> tasks = findAllTasksForProject(id);
        ViewState viewState = IssueTrackerMantisEnum.extractViewState(project);
        return new IssueTrackerProjectTrackable(id, name, description, status, this.url, viewState);
    }

    /**
     * Copies all values from a Mantis-Issue-Object to an IssueTrackerTask.
     *
     * @param issue
     *            - Issue to copy
     * @param project
     *            - Project to find
     * @return new IssueTrackerTask
     */
    public IssueTrackerTaskTrackable createMantisTask(IIssue issue, IProject project) {
        Long id = issue.getId();
        String description = issue.getDescription();
        String owner = "";
        if (issue.getHandler() != null) {
            owner = issue.getHandler().getName();
        }
        Date creationTime = issue.getDateSubmitted();
        Date lastChange = issue.getDateLastUpdated();
        IssuePriority priority = IssueTrackerMantisEnum.extractIssuePriority(issue);
        IProjectTrackable projectTrackable = null;

        Map<Long, ICommentTrackable> comments = findAllCommentsForTask(issue.getId());
        String reporter = "";
        if (issue.getReporter() != null) {
            reporter = issue.getReporter().getName();
        }
        IssueResolution resolution = IssueTrackerMantisEnum.extractIssueResolution(issue);
        IssueSeverity severity = IssueTrackerMantisEnum.extractIssueSeverity(issue);
        IssueStatus status = IssueTrackerMantisEnum.extractIssueStatus(issue);
        String summary = issue.getSummary();
        IssueTrackingTool tool = IssueTrackingTool.MANTIS;

        return new IssueTrackerTaskTrackable(id, description, owner, creationTime, lastChange, priority,
                projectTrackable, comments, reporter, resolution, severity, status, summary, tool);
    }

    /**
     * Copies all values from a Mantis-Note-Object to an IssueTrackerComment.
     *
     * @param note - note to copy
     * @return new IssueTrackerComment
     */
    public IssueTrackerCommentTrackable createMantisComment(INote note) {
        Long id = note.getId();
        Date creationTime = note.getDateSubmitted();
        Date lastChange = note.getDateLastModified();
        String reporter = "";
        if (note.getReporter() != null) {
            reporter = note.getReporter().toString();
        }
        String text = note.getText();
        ViewState viewState = IssueTrackerMantisEnum.extractViewState(note);

        return new IssueTrackerCommentTrackable(id, creationTime, lastChange, reporter, text, viewState);
    }

    /**
     * Creates a test project in mantis.
     *
     * @param projectName String
     * @param description String
     * @param enabled Boolean
     * @param viewStatePrivate Boolean
     * @return Long id
     * @throws MCException mce
     */
    public Long createProject(String projectName, String description, Boolean enabled,
            Boolean viewStatePrivate)
            throws MCException {

        IProject newProject = new Project();
        newProject.setName(projectName);
        newProject.setAccessLevelMin(AccessLevel.DEVELOPER);
        newProject.setDesription(description);
        newProject.setEnabled(enabled); // ProjectStatus: Open
        newProject.setPrivate(viewStatePrivate); // ViewState:Public
        Long id = this.session.addProject(newProject);
        this.session.flush();
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
    public Long createTask(String description, String summary, String projectName)
            throws MCException {

        IIssue newIssue = new Issue();
        newIssue.setDescription(description);
        // newIssue.setHandler(new Account(100, "test", "test", "test@test"));
        newIssue.setPriority(this.session.getDefaultIssuePriority());
        newIssue.setSummary(summary);
        newIssue.setSeverity(this.session.getDefaultIssueSeverity());
        // newIssue.setReporter(new Account(101, "rep1", "rep1", "rep@rep"));
        IProject p = this.session.getProject(projectName);
        newIssue.setProject(new MCAttribute(p.getId(), p.getName()));
        long id = this.session.addIssue(newIssue);
        this.session.flush();
        return id;
    }
}

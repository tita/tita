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
import at.ac.tuwien.ifs.tita.issuetracker.container.IssueTrackerComment;
import at.ac.tuwien.ifs.tita.issuetracker.container.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.issuetracker.container.IssueTrackerTask;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssuePriority;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueResolution;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueSeverity;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueStatus;
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

    private final Logger log = LoggerFactory.getLogger(IssueTrackerMantisDao.class);

    private URL url;
    private final String urlAsString;
    private final String user;
    private final String pwd;
    private IMCSession session;


    public IssueTrackerMantisDao(IssueTrackerLogin login) {
        // TODO: read values from DB
        user = login.getUserName();
        pwd = login.getPassword();
        urlAsString = login.getIssueTracker().getUrl();

        if (session != null) {
            disconnect();
        }
        connect();
    }

    /**
     * Opens a Session to the Mantis-Server.
     */
    private void connect() {
        try {
            // TODO: read values from DB
            url = new URL(urlAsString + "/api/soap/mantisconnect.php");
            session = new MCSession(url, user, pwd);
        } catch (MCException e) {
            log.error("Connection to Mantis-Server failed!");
        } catch (MalformedURLException e) {
            log.error("URL " + url.getPath() + " is malformed!");
        }

    }

    /**
     * Close a Session to the Mantis-Server.
     */
    public void disconnect() {
        session = null;
    }

    /** {@inheritDoc} */
    @Override
    public IProjectTrackable findProject(Long id) {

        IProject project;
        try {
            project = session.getProject(id);
            IssueTrackerProject mantisProject = createMantisProject(project);
            return mantisProject;

        } catch (MCException e) {
            log.error("Reading Project with Id " + id + " from Mantis-Server failed!");
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Map<Long, ITaskTrackable> findAllTasksForProject(Long projectId) {
        Map<Long, ITaskTrackable> taskList = new TreeMap<Long, ITaskTrackable>();
        try {
            IIssue[] issues = session.getProjectIssues(projectId);
            for (IIssue issue : issues) {
                IssueTrackerTask task = createMantisTask(issue);
                taskList.put(task.getId(), task);
            }
            return taskList;
        } catch (MCException e) {
            log.error("Reading Project Issues with projectId " + projectId + " from Mantis-Server failed!");
        }

        return null;

    }

    /** {@inheritDoc} */
    public Map<Long, ICommentTrackable> findAllCommentsForTask(Long taskId) {
        Map<Long, ICommentTrackable> commentList = new TreeMap<Long, ICommentTrackable>();

        try {
            INote[] notes = session.getIssue(taskId).getNotes();
            for (INote note : notes) {
                IssueTrackerComment comment = createMantisComment(note);
                commentList.put(comment.getId(), comment);
            }
            return commentList;
        } catch (MCException e) {
            log.error("Reading Comments from Task with taskId " + taskId + " from Mantis-Server failed!");
        }

        return null;

    }

    /** {@inheritDoc} */
    public ITaskTrackable findTask(Long taskId) {
        IIssue issue;
        try {
            issue = session.getIssue(taskId);
            IssueTrackerTask mantisTask = createMantisTask(issue);
            return mantisTask;

        } catch (MCException e) {
            log.error("Reading Task with taskId " + taskId + " from Mantis-Server failed!");
        }
        return null;
    }

    /** {@inheritDoc} */
    public IProjectTrackable findProject(String projectName) {
        IProject project;
        try {
            project = session.getProject(projectName);
            IssueTrackerProject mantisProject = createMantisProject(project);
            return mantisProject;

        } catch (MCException e) {
            log.error("Reading project with projectName " + projectName + " from Mantis-Server failed!");
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Map<Long, IProjectTrackable> findAccessibleProjects() {
        IProject[] projects;
        Map<Long, IProjectTrackable> mapOfProjects = new TreeMap<Long, IProjectTrackable>();

        try {
            projects = session.getAccessibleProjects();
            for (IProject project : projects) {
                IssueTrackerProject mantisProject = createMantisProject(project);
                mapOfProjects.put(project.getId(), mantisProject);
            }
            return mapOfProjects;

        } catch (MCException e) {
            log.error("Reading accessible projects from Mantis-Server failed!");
        }
        return null;
    }

    /** {@inheritDoc} */
    public void closeTask(long taskId) throws MCException {
        IIssue issue = session.getIssue(taskId);
        IMCAttribute[] resolutions = session.getEnum(Enumeration.RESOLUTIONS);
        issue.setResolution(resolutions[1]);// fixed
        session.updateIssue(issue);
        session.flush();
    }

    /**
     * Copies all values from a Mantis-Project-Object to an IssueTrackerProject.
     * 
     * @param project - Project to copy
     * @return new IssueTrackerProject
     */
    public IssueTrackerProject createMantisProject(IProject project) {
        Long id = project.getId();
        String name = project.getName();
        String description = project.getDescription();
        ProjectStatus status = IssueTrackerMantisEnum.extractProjectStatus(project);
        Map<Long, ITaskTrackable> tasks = findAllTasksForProject(id);
        ViewState viewState = IssueTrackerMantisEnum.extractViewState(project);
        return new IssueTrackerProject(id, name, description, status, tasks, url, viewState);

    }

    /**
     * Copies all values from a Mantis-Issue-Object to an IssueTrackerTask.
     * 
     * @param issue - Issue to copy
     * @return new IssueTrackerTask
     */
    public IssueTrackerTask createMantisTask(IIssue issue) {
        Long id = issue.getId();
        String description = issue.getDescription();
        String owner = "";
        if (issue.getHandler() != null) {
            owner = issue.getHandler().getName();
        }
        Date creationTime = issue.getDateSubmitted();
        Date lastChange = issue.getDateLastUpdated();
        IssuePriority priority = IssueTrackerMantisEnum.extractIssuePriority(issue);
        Long projectId = null;
        if (issue.getProject() != null) {
            projectId = issue.getProject().getId();
        }
        Map<Long, ICommentTrackable> comments = findAllCommentsForTask(issue.getId());
        String reporter = "";
        if (issue.getReporter() != null) {
            reporter = issue.getReporter().getName();
        }
        IssueResolution resolution = IssueTrackerMantisEnum.extractIssueResolution(issue);
        IssueSeverity severity = IssueTrackerMantisEnum.extractIssueSeverity(issue);
        IssueStatus status = IssueTrackerMantisEnum.extractIssueStatus(issue);
        String summary = issue.getSummary();

        return new IssueTrackerTask(id, description, owner, creationTime, lastChange, priority, projectId, comments,
                reporter, resolution, severity, status, summary);
    }

    /**
     * Copies all values from a Mantis-Note-Object to an IssueTrackerComment.
     * 
     * @param note - note to copy
     * @return new IssueTrackerComment
     */
    public IssueTrackerComment createMantisComment(INote note) {
        Long id = note.getId();
        Date creationTime = note.getDateSubmitted();
        Date lastChange = note.getDateLastModified();
        String reporter = "";
        if (note.getReporter() != null) {
            reporter = note.getReporter().toString();
        }
        String text = note.getText();
        ViewState viewState = IssueTrackerMantisEnum.extractViewState(note);

        return new IssueTrackerComment(id, creationTime, lastChange, reporter, text, viewState);
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
    public Long createTask(String description, String summary, String projectName)
            throws MCException {

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
}

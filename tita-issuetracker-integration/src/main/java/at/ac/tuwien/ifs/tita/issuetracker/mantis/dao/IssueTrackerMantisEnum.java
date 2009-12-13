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

import org.mantisbt.connect.model.IIssue;
import org.mantisbt.connect.model.INote;
import org.mantisbt.connect.model.IProject;

import at.ac.tuwien.ifs.tita.issuetracker.enums.IssuePriority;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueResolution;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueSeverity;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueStatus;
import at.ac.tuwien.ifs.tita.issuetracker.enums.ProjectStatus;
import at.ac.tuwien.ifs.tita.issuetracker.enums.ViewState;

/**
 * Class for mapping the Mantis-specific values (like priority or status) to the
 * defined Enums used in IssueTrackerComment, IssueTrackerProject and
 * IssueTrackerTask.
 * 
 * @author Karin
 * 
 */
public final class IssueTrackerMantisEnum {

    public static final String PRIORITY_NONE = "none";
    public static final String PRIORITY_LOW = "low";
    public static final String PRIORITY_NORMAL = "normal";
    public static final String PRIORITY_HIGH = "high";
    public static final String PRIORITY_URGENT = "urgent";
    public static final String PRIORITY_IMMEDIATE = "immediate";

    public static final String RESOLUTION_OPEN = "open";
    public static final String RESOLUTION_REOPENED = "reopened";
    public static final String RESOLUTION_FIXED = "fixed";
    public static final String RESOLUTION_INVALID = "invalid";
    public static final String RESOLUTION_WONTFIX = "won't fix";
    public static final String RESOLUTION_DUPLICATE = "duplicate";
    public static final String RESOLUTION_WORKSFORM = "worksform";
    public static final String RESOLUTION_UNABLETOPRODUCE = "unable to reproduce";
    public static final String RESOLUTION_NOTFIXABLE = "not fixable";
    public static final String RESOLUTION_NOCHANGEREQUIRED = "no change required";
    public static final String RESOLUTION_SUSPENDED = "suspended";

    public static final String SEVERITY_FEATURE = "feature";
    public static final String SEVERITY_TRIVIAL = "trivial";
    public static final String SEVERITY_TEXT = "text";
    public static final String SEVERITY_TWEAK = "tweak";
    public static final String SEVERITY_MINOR = "minor";
    public static final String SEVERITY_MAJOR = "major";
    public static final String SEVERITY_CRASH = "crash";
    public static final String SEVERITY_BLOCK = "block";

    public static final String ISSUE_STATUS_NEW = "new";
    public static final String ISSUE_STATUS_ASSIGNED = "assigned";
    public static final String ISSUE_STATUS_CLOSED = "closed";

    /**
     * Creates an IssuePriority out of the Mantis-Issue.
     * 
     * @param issue
     *            - specific issue
     * @return - extracted IssuePriority.
     */
    public static IssuePriority extractIssuePriority(IIssue issue) {
        String priority = issue.getPriority().getName();

        if (priority.equalsIgnoreCase(PRIORITY_NONE)) {
            return IssuePriority.NONE;
        } else if (priority.equalsIgnoreCase(PRIORITY_LOW)) {
            return IssuePriority.LOW;
        } else if (priority.equalsIgnoreCase(PRIORITY_NORMAL)) {
            return IssuePriority.NORMAL;
        } else if (priority.equalsIgnoreCase(PRIORITY_HIGH)) {
            return IssuePriority.HIGH;
        } else if (priority.equalsIgnoreCase(PRIORITY_URGENT)) {
            return IssuePriority.URGENT;
        } else if (priority.equalsIgnoreCase(PRIORITY_IMMEDIATE)) {
            return IssuePriority.IMMEDIATE;
        }

        return null;
    }

    /**
     * Creates an IssueResolution out of the Mantis-Issue.
     * 
     * @param issue
     *            - specific issue
     * @return - extracted IssueResolution.
     */
    public static IssueResolution extractIssueResolution(IIssue issue) {
        String resolution = issue.getResolution().getName();

        if (resolution.equalsIgnoreCase(RESOLUTION_OPEN)) {
            return IssueResolution.OPEN;
        } else if (resolution.equalsIgnoreCase(RESOLUTION_REOPENED)) {
            return IssueResolution.REOPENED;
        } else if (resolution.equalsIgnoreCase(RESOLUTION_FIXED)) {
            return IssueResolution.FIXED;
        } else if (resolution.equalsIgnoreCase(RESOLUTION_INVALID)) {
            return IssueResolution.INVALID;
        } else if (resolution.equalsIgnoreCase(RESOLUTION_WONTFIX)) {
            return IssueResolution.WONTFIX;
        } else if (resolution.equalsIgnoreCase(RESOLUTION_DUPLICATE)) {
            return IssueResolution.DUPLICATE;
        } else if (resolution.equalsIgnoreCase(RESOLUTION_WORKSFORM)) {
            return IssueResolution.WORKSFORM;
        } else if (resolution.equalsIgnoreCase(RESOLUTION_UNABLETOPRODUCE)) {
            return IssueResolution.UNABLETOPRODUCE;
        } else if (resolution.equalsIgnoreCase(RESOLUTION_NOTFIXABLE)) {
            return IssueResolution.NOTFIXABLE;
        } else if (resolution.equalsIgnoreCase(RESOLUTION_NOCHANGEREQUIRED)) {
            return IssueResolution.NOCHANGEREQUIRED;
        } else if (resolution.equalsIgnoreCase(RESOLUTION_SUSPENDED)) {
            return IssueResolution.SUSPENDED;
        }
        return null;
    }

    /**
     * Creates an IssueSeverity out of the Mantis-Issue.
     * 
     * @param issue
     *            - specific issue
     * @return - extracted IssueSeverity.
     */
    public static IssueSeverity extractIssueSeverity(IIssue issue) {
        String severity = issue.getSeverity().getName();

        if (severity.equalsIgnoreCase(SEVERITY_FEATURE)) {
            return IssueSeverity.FEATURE;
        } else if (severity.equalsIgnoreCase(SEVERITY_TRIVIAL)) {
            return IssueSeverity.TRIVIAL;
        } else if (severity.equalsIgnoreCase(SEVERITY_TEXT)) {
            return IssueSeverity.TEXT;
        } else if (severity.equalsIgnoreCase(SEVERITY_TWEAK)) {
            return IssueSeverity.TWEAK;
        } else if (severity.equalsIgnoreCase(SEVERITY_MINOR)) {
            return IssueSeverity.MINOR;
        } else if (severity.equalsIgnoreCase(SEVERITY_MAJOR)) {
            return IssueSeverity.MAJOR;
        } else if (severity.equalsIgnoreCase(SEVERITY_CRASH)) {
            return IssueSeverity.CRASH;
        } else if (severity.equalsIgnoreCase(SEVERITY_BLOCK)) {
            return IssueSeverity.BLOCK;
        }
        return null;
    }

    /**
     * Creates an IssueStatus out of the Mantis-Issue.
     * 
     * @param issue
     *            - specific issue
     * @return - extracted IssueStatus.
     */
    public static IssueStatus extractIssueStatus(IIssue issue) {
        String status = issue.getStatus().getName();

        if (status.equalsIgnoreCase(ISSUE_STATUS_NEW)) {
            return IssueStatus.NEW;
        } else if (status.equalsIgnoreCase(ISSUE_STATUS_ASSIGNED)) {
            return IssueStatus.ASSIGNED;
        } else if (status.equalsIgnoreCase(ISSUE_STATUS_CLOSED)) {
            return IssueStatus.CLOSED;
        }
        return null;
    }

    /**
     * Creates a ProjectStatus out of the Mantis-Project.
     * 
     * @param project
     *            - specific project
     * @return - extracted ProjectStatus.
     */
    public static ProjectStatus extractProjectStatus(IProject project) {
        if (project.isEnabled()) {
            return ProjectStatus.OPEN;
        } else {
            return ProjectStatus.CLOSED;
        }
    }

    /**
     * Creates a ViewState out of the Mantis-Project.
     * 
     * @param project
     *            - specific project
     * @return - extracted ViewState.
     */
    public static ViewState extractViewState(IProject project) {
        if (project.isPrivate()) {
            return ViewState.PRIVATE;
        } else {
            return ViewState.PUBLIC;
        }
    }

    /**
     * Creates a ViewState out of the Mantis-Note.
     * 
     * @param note
     *            - specific note
     * @return - extracted ViewState.
     */
    public static ViewState extractViewState(INote note) {
        if (note.isPrivate()) {
            return ViewState.PRIVATE;
        } else {
            return ViewState.PUBLIC;
        }
    }

}

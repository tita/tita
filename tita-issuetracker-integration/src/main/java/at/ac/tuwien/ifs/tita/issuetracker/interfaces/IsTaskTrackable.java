package at.ac.tuwien.ifs.tita.issuetracker.interfaces;

import java.util.Date;
import java.util.List;

import at.ac.tuwien.ifs.tita.issuetracker.enums.IssuePriority;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueResolution;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueSeverity;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueStatus;

/**
 * The interface describes the view on issue or bug objects from the integrated issue tracker, 
 * how it is called in the different issue trackers.
 * The data from the issue tracker is mapped in the implementation of the interface.
 * @author Christoph
 *
 */
public interface IsTaskTrackable {
	
	/**
	 * Sets the id of the task to identify it.
	 * @param id - the id identifies the task
	 */
	public void setId(Long id);
	
	/**
	 * Supplies the identifier of the task
	 * @return id - identifier of the task
	 */
	public Long getId();
	
	/**
	 * Sets the summary, a short description of the task
	 * @param summary - short description
	 */
	public void setSummary(String summary);
	
	/**
	 * Returns the summary of the task
	 * @return summary - short task description
	 */
	public String getSummary();
	
	/**
	 * Sets the detailed description of the task
	 * @param description - detailed description of the task
	 */
	public void setDescription(String description);
	
	/**
	 * Return the detailed task description
	 * @return description - detailed description
	 */
	public String getDescription();
	
	/**
	 * Sets the priority of the task
	 * @param priority - uses the enumeration values for setting
	 * @see IssuePriority
	 */
	public void setPriority(IssuePriority priority);
	
	/**
	 * Returns the priority of the task
	 * @return priority - the priority of the task
	 * @see IssuePriority
	 */
	public IssuePriority getPriority();
	
	/**
	 * Sets the severity of the task
	 * @param severity - uses the enumeration values for setting
	 * @see IssueSeverity
	 */
	public void setSeverity(IssueSeverity severity);
	
	/**
	 * Returns the severity of the task
	 * @return severity - the severity of the task
	 * @see IssueSeverity
	 */
	public IssueSeverity getSeverity();
	
	/**
	 * Sets the resolution of the task
	 * @param resolution - uses the enumeration values for setting
	 * @see IssueResolution
	 */
	public void setResolution(IssueResolution resolution);
	
	/**
	 * Returns the resolution of the task
	 * @return resolution - the resolution of the task
	 * @see IssueResolution
	 */
	public IssueResolution getResolution();
	
	/**
	 * Sets the status of the task
	 * @param status - uses the enumeration values for setting
	 * @see IssueStatus
	 */
	public void setStatus(IssueStatus status);
	
	/**
	 * Returns the status of the task
	 * @return status - the status of the task
	 * @see IssueStatus
	 */
	public IssueStatus getStatus();
	
	/**
	 * Sets the creation time of the task due to the issue tracker
	 * @param creationTime - time the task was created
	 */
	public void setCreationTime(Date creationTime);
	
	/**
	 * Returns the creation time of the task
	 * @return creationTime - time the task was created
	 */
	public Date getCreationTime();
	
	/**
	 * Sets the time the last change was updated
	 * @param lastChange - the time the last change was updated
	 */
	public void setLastChange(Date lastChange);
	
	/**
	 * Returns the last change time
	 * @return lastChange - the time the last change was committed
	 */
	public Date getLastChange();
	
	/**
	 * Sets the reporter(user of the issue tracker) of the task
	 * @param reporter - the author of the task
	 */
	public void setReporter(String reporter);
	
	/**
	 * Returns the reporter of the task
	 * @return reporter - author of the task
	 */
	public String getReporter();
	
	/**
	 * Sets the owner of the task
	 * @param owner - the person who has assigned to
	 */
	public void setOwner(String owner);
	
	/**
	 * Return the owner of the task
	 * @return owner - person who has assigned to
	 */
	public String getOwner();
	
	/**
	 * Sets the project of task
	 * @param project - the project the task is being filed against
	 * @see IsProjectTrackable
	 */
	public void setProject(IsProjectTrackable project);
	
	/**
	 * Returns the project the task is being filed against
	 * @return project - the project the task is being filed against
	 * @see IsProjectTrackable
	 */
	public IsProjectTrackable getProject();
	
	/**
	 * Sets the comments that are added to the task
	 * At the creation time of course null
	 * @param comments - a list of comments, default null
	 * @see IsCommentTrackable
	 */
	public void setComments(List<IsCommentTrackable> comments);
	
	/**
	 * Returns a list of comments added to the task
	 * @return comments - a list of comments
	 * @see IsCommentTrackable
	 */
	public List<IsCommentTrackable> getComments();
	
	

}

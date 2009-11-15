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
package at.ac.tuwien.ifs.tita.issuetracker.interfaces;

import java.util.Date;

import at.ac.tuwien.ifs.tita.issuetracker.enums.ViewState;

/**
 * The interface describes the view on comment objects from the integrated issue tracker.
 * The data from the issue tracker is mapped in the implementation of the interface.
 * 
 * @author Christoph
 *
 */
public interface IsCommentTrackable {
	
	/**
	 * Sets the id of the comment to identify it.
	 * @param id - the id identifies the comment
	 */
	public void setId(Long id);
	
	/**
	 * Supplies the identifier of the comment
	 * @return id - identifier of the comment
	 */
	public Long getId();
	
	/**
	 * Sets the reporter of the comment
	 * @param reporter - author of the comment
	 */
	public void setReporter(String reporter);
	
	/**
	 * Returns the reporter of the comment
	 * @return reporter - author of the comment
	 */
	public String getReporter();
	
	/**
	 * Sets the text of the comment
	 * @param text - the content of the comment
	 */
	public void setText(String text);
	
	/**
	 * Returns the text of the comment
	 * @return text - content of the comment
	 */
	public String getText();
	
	/**
	 * Sets the state of the view for the comment
	 * It decides if everybody is allowed to read it.
	 * The default value is public.
	 * @param viewState - uses the enumeration for setting
	 * @see ViewState
	 */
	public void setViewState(ViewState viewState);
	
	/**
	 * Returns the view state of the comment
	 * @return viewState - public, private or any
	 * @see ViewState
	 */
	public ViewState getViewState();
	
	/**
	 * Sets the creation time of the comment due to the issue tracker
	 * @param creationTime - time the comment was created
	 */
	public void setCreationTime(Date creationTime);
	
	/**
	 * Returns the creation time of the comment
	 * @return creationTime - time the comment was created
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
	
}

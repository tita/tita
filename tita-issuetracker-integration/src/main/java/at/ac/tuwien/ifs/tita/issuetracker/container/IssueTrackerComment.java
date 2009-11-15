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


package at.ac.tuwien.ifs.tita.issuetracker.container;

import java.util.Date;

import at.ac.tuwien.ifs.tita.issuetracker.enums.ViewState;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IIsCommentTrackable;

/**
 * The container class for comment objects from the integrated issue tracker.
 * 
 * @author Karin
 *
 */
public class IssueTrackerComment implements IIsCommentTrackable {
    private Date creationTime;
    private long id;
    private Date lastChange;
    private String reporter;
    private String text;
    private ViewState viewState;
    
    public IssueTrackerComment(long id, Date creationTime, Date lastChange, 
            String reporter, String text, ViewState viewState){
        this.id =id;
        this.creationTime = creationTime;
        this.lastChange = lastChange;
        this.reporter = reporter;
        this.text = text;
        this.viewState = viewState;
    }
    
    /** {@inheritDoc} */
    public Date getCreationTime() {
        return this.creationTime;
    }

    /** {@inheritDoc} */
    public Long getId() {
        return this.id;
    }
    /** {@inheritDoc} */
    public Date getLastChange() {
        return this.lastChange;
    }
    /** {@inheritDoc} */
    public String getReporter() {
        return this.reporter;
    }
    /** {@inheritDoc} */
    public String getText() {
        return this.text;
    }
    /** {@inheritDoc} */
    public ViewState getViewState() {
        return this.viewState;
    }
    /** {@inheritDoc} */
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
    /** {@inheritDoc} */
    public void setId(Long id) {
        this.id = id;
    }
    /** {@inheritDoc} */
    public void setLastChange(Date lastChange) {
        this.lastChange = lastChange;
    }
    /** {@inheritDoc} */
    public void setReporter(String reporter) {
        this.reporter = reporter;
    }
    /** {@inheritDoc} */
    public void setText(String text) {
        this.text = text;
    }
    /** {@inheritDoc} */
    public void setViewState(ViewState viewState) {
        this.viewState = viewState;
    }
}

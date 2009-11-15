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

import java.net.URL;
import java.util.List;

import at.ac.tuwien.ifs.tita.issuetracker.enums.ProjectStatus;
import at.ac.tuwien.ifs.tita.issuetracker.enums.ViewState;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IIsProjectTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IIsTaskTrackable;

/**
 * The container class for project objects from the integrated issue tracker.
 * 
 * @author Karin
 *
 */
public class IssueTrackerProject implements IIsProjectTrackable {
    private long id;
    private String name;
    private String description; 
    private ProjectStatus status;
    private List<IIsTaskTrackable> tasks;
    private URL url; 
    private ViewState viewState;
    
    public IssueTrackerProject(long id, String name, String description, 
            ProjectStatus status, List<IIsTaskTrackable> tasks, URL url, 
            ViewState viewState){
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.tasks = tasks;
        this.url = url;
        this.viewState = viewState;
    }

    /** {@inheritDoc} */
    public String getDescription() {
        return description;
    }
    /** {@inheritDoc} */
    public Long getId() {
        return id;
    }
    
    /** {@inheritDoc} */
    public String getName() {
        return name;
    }
    /** {@inheritDoc} */
    public ProjectStatus getStatus() {
        return status;
    }

    public List<IIsTaskTrackable> getTasks() {
        return tasks;
    }
    /** {@inheritDoc} */
    public URL getUrl() {
        return url;
    }
    /** {@inheritDoc} */
    public ViewState getViewState() {
        return viewState;
    }
    /** {@inheritDoc} */
    public void setDescription(String description) {
        this.description = description;
    }
    /** {@inheritDoc} */
    public void setId(Long id) {
        this.id = id;
    }
    /** {@inheritDoc} */
    public void setName(String name) {
        this.name = name;
    }
    /** {@inheritDoc} */
    public void setStatus(ProjectStatus projectStatus) {
        this.status = projectStatus;
    }
    /** {@inheritDoc} */
    public void setTasks(List<IIsTaskTrackable> tasks) {
        this.tasks = tasks;
    }
    /** {@inheritDoc} */
    public void setUrl(URL url) {
        this.url = url;
    }
    /** {@inheritDoc} */
    public void setViewState(ViewState viewState) {
        this.viewState = viewState;
    }

}

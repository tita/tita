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

import java.net.URL;
import java.util.List;

import at.ac.tuwien.ifs.tita.issuetracker.enums.ProjectStatus;
import at.ac.tuwien.ifs.tita.issuetracker.enums.ViewState;
/**
 * The interface describes the view on project objects from the integrated issue tracker.
 * The data from the issue tracker is mapped in the implementation of the interface.
 * 
 * @author Christoph
 *
 */
public interface IIsProjectTrackable {
    
    /**
     * Sets the id of the project to identify it.
     * @param id - the id identifies the project
     */
    void setId(Long id);
    
    /**
     * Supplies the identifier of the project.
     * @return id - identifier of the project
     */
    Long getId();
    
    /**
     * Sets the name of the project in the form of a short.
     * @param name - short description
     */
    void setName(String name);
    
    /**
     * Supplies the name of the project.
     * @return name - short description of the project
     */
    String getName();
    
    /**
     * Sets the status of the project.
     * @param projectStatus - use the enumaration values for setting
     * @see ProjectStatus
     */
    void setStatus(ProjectStatus projectStatus);
    
    /**
     * Supplies the topically status of the project.
     * @return status - the topically status of the project
     * @see ProjectStatus
     */
    ProjectStatus getStatus();
    
    /**
     * Sets the description of the project.
     * @param description - a detailed description of the project
     */
    void setDescription(String description);
    
    /**
     * Supplies the detailed description of the project.
     * @return description - in detailed form
     */
    String getDescription();
    
    /**
     * Sets the URL of the project .
     * @param url - place on the file system(remote in most cases)
     */
    void setUrl(URL url);
    
    /**
     * Supplies the URL of the project.
     * @return url - place on the file system
     */
    URL getUrl();
    
    /**
     * Sets the tasks for a project from the issue tracker.
     * @param tasks - a list of tasks
     * @see IIsTaskTrackable
     */
    void setTasks(List<IIsTaskTrackable> tasks);
    
    /**
     * Returns the tasks of the project.
     * @return tasks - reported tasks of the project
     * @see IIsTaskTrackable
     */
    List<IIsTaskTrackable> getTasks();
    
    /**
     * Sets the viewState of the project.
     * @param viewState - public, private or any
     */
    void setViewState(ViewState viewState);
    
    
    /**
     * Returns the viewState of the project.
     * @return viewState - using the enumeration options
     */
    ViewState getViewState();

}

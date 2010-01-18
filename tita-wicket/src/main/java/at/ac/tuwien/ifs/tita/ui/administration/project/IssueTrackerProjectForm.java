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
package at.ac.tuwien.ifs.tita.ui.administration.project;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;

/**
 * Form to save or update a IssueTrackerProject Entity.
 * 
 * @author ASE Group 10
 */
public class IssueTrackerProjectForm extends Form<IssueTrackerProject> {

    // the object
    private final IssueTrackerProject issueTrackerProject;

    // the actual List
    private final List<IssueTracker> issueTracker;

    // Logger
    private final Logger log = LoggerFactory
            .getLogger(IssueTrackerProjectForm.class);

    /**
     * public constructor.
     * 
     * @param id
     *            the unique ID that is declared in the HTML-File for this
     *            Panel.
     * @param project
     *            the Project to which the IssueTrackerProjects are added.
     * @param parent
     *            the Parent Panel for Returning a Project on Close.
     */
    public IssueTrackerProjectForm(String id,
            IssueTrackerProject issueTrackerProject,
            List<IssueTracker> issueTracker, final TiTAProject project) {
        super(id);

        log.info("Creating Form for Adding a new IssueTrackerProject.");

        if (issueTrackerProject == null) {
            this.issueTrackerProject = new IssueTrackerProject();
        } else {
            this.issueTrackerProject = issueTrackerProject;
        }

        if (issueTracker == null) {
            this.issueTracker = new ArrayList<IssueTracker>();
        } else {
            this.issueTracker = issueTracker;
        }

        this.issueTrackerProject.setTitaProject(project);

        addComponents();
        addLabels();
    }

    /**
     * displays the Table of issueTrackerProjects.
     */
    private void addComponents() {
        addOrReplace(new TextField<String>("tfProjectName",
                new PropertyModel<String>(issueTrackerProject, "projectName")));
        addOrReplace(new DropDownChoice<IssueTracker>("dropDownIssueTracker",
                new PropertyModel<IssueTracker>(issueTrackerProject,
                        "issueTracker"), issueTracker));
        // addOrReplace(new CheckBox("checkBoxDeletedIssueTrackerProject", new
        // PropertyModel<Boolean>(issueTrackerProject,
        // "deleted")));
    }

    /**
     * adds labels to the form.
     */
    private void addLabels() {
        addOrReplace(new Label("lbProjectName", "Project Name: "));
        addOrReplace(new Label("lbIssueTracker", "Issue Tracker: "));
        // addOrReplace(new Label("lbDeleted", "Deleted: "));
    }

    /**
     * returns the current edited IssueTrackerProject.
     * 
     * @return the issueTrackerProject to return.
     */
    public IssueTrackerProject getIssueTrackerProject() {
        return this.issueTrackerProject;
    }
}

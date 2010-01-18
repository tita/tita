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
import java.util.HashSet;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.table.SelectableListItem;
import org.wicketstuff.table.Table;
import org.wicketstuff.table.cell.CellEditor;
import org.wicketstuff.table.cell.CellRender;

import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.conv.ProjectStatus;
import at.ac.tuwien.ifs.tita.ui.models.TableModelIssueTrackerProject;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonDelete;
import at.ac.tuwien.ifs.tita.ui.uihelper.LenientAjaxButton;

/**
 * Form to save or update a Project Entity.
 * 
 * @author ASE Group 10
 */
public class ProjectForm extends Form<TiTAProject> {
    // the object to save
    private final TiTAProject project;

    // List for all available ProjectStati
    private final List<ProjectStatus> projectStati;

    // List needed for Table
    private final List<IssueTrackerProject> issueTrackerProjects;
    private Table issueTrackerProjectTable;
    private TableModelIssueTrackerProject issueTrackerProjectTM;

    // the Parent panel to add Projects on submit
    private final ProjectAdministrationPanel parent;

    // the Logger
    private final Logger log = LoggerFactory.getLogger(ProjectForm.class);

    /**
     * public constructor for creating a new Project.
     * 
     * @param id the Components name.
     * @param issueTrackerProjects the List of available IssueTrackerProjects to
     *        this Project.
     * @param parent the parent panel to add the Project when saving Form.
     * @param project the Project to create or modify.
     */
    public ProjectForm(String id, ProjectAdministrationPanel parent, List<ProjectStatus> projectStati,
            TiTAProject project) {
        super(id);

        log.info("Opening Form for creating a NEW User");

        this.parent = parent;

        if (project == null) {
            this.project = new TiTAProject("", "", new Boolean(false), new ProjectStatus(), new HashSet<TiTATask>(),
                    new HashSet<IssueTrackerProject>());
        } else {
            this.project = project;
        }
        this.projectStati = projectStati;

        this.issueTrackerProjects = new ArrayList<IssueTrackerProject>();
        for (IssueTrackerProject pr : this.project.getIssueTrackerProjects()) {
            this.issueTrackerProjects.add(pr);
        }

        init();
    }

    /**
     * inits other components for both constructors.
     */
    private void init() {
        addComponents();
        addLabels();
    }

    /**
     * add all controls.
     */
    private void addComponents() {

        log.info("Adding Controls to the Form");

        addOrReplace(new TextField<String>("tfDescription", new PropertyModel<String>(project, "description")));
        addOrReplace(new TextField<String>("tfName", new PropertyModel<String>(project, "name")));
        addOrReplace(new CheckBox("checkBoxDeleted", new PropertyModel<Boolean>(project, "deleted")));
        addOrReplace(new DropDownChoice<ProjectStatus>("dropDownProjectStatus", new PropertyModel<ProjectStatus>(
                project, "projectStatus"), projectStati));

        issueTrackerProjectTM = new TableModelIssueTrackerProject(this.issueTrackerProjects);
        issueTrackerProjectTable = new Table("issueTrackerProjectTable", issueTrackerProjectTM);

        DeleteRenderer deleteRenderer = new DeleteRenderer();
        issueTrackerProjectTable.setDefaultRenderer(ButtonDelete.class, deleteRenderer);
        issueTrackerProjectTable.setDefaultEditor(ButtonDelete.class, deleteRenderer);

        addOrReplace(issueTrackerProjectTable);

        // other components will not be displayed, they are not part of the
        // ProjectAdministration
    }

    /**
     * add all Labels for controls.
     */
    private void addLabels() {

        log.info("Adding Labels to the Form");

        addOrReplace(new Label("lbDescription", "Description: "));
        addOrReplace(new Label("lbName", "Name of the Project: "));
        addOrReplace(new Label("lbDeleted", "Deleted: "));
        addOrReplace(new Label("lbProjectStatus", "Project Status: "));
        addOrReplace(new Label("lbIssueTrackerProjects", "Issue Tracker Projects: "));
    }

    /** {@inheritDoc} **/
    @Override
    public final void onSubmit() {
        log.info("Submitting Form for Project " + project.getName());
        parent.saveEntity(project);
        parent.displayCurrentList();
    }

    /**
     * returns the Current Project in its state.
     * 
     * @return the current Project.
     */
    public TiTAProject getProject() {
        return this.project;
    }

    /**
     * deletes the current IssueTrackerProject from table.
     * 
     * @param target the Ajax Request Target.
     */
    public void deleteCurrentIssueTrackerProject(AjaxRequestTarget target) {
        if (issueTrackerProjectTM.getSelectedRow() > -1) {
            issueTrackerProjectTM.deleteAt(issueTrackerProjectTM.getSelectedRow());
            issueTrackerProjectTM.reload();
            target.addComponent(this);
        }
    }

    /******************** INNER CLASS FOR BUTTON RENDER. ***************/

    private class DeleteRenderer implements CellRender, CellEditor {

        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("unchecked")
        @Override
        public Component getRenderComponent(String id, IModel model, SelectableListItem nparent, int row, int column) {
            return new Label(id, model);
        }

        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("unchecked")
        @Override
        public Component getEditorComponent(String id, IModel model, SelectableListItem nparent, int row, int column) {

            return new LenientAjaxButton(id) {
                @Override
                public void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                    deleteCurrentIssueTrackerProject(target);
                }

                @Override
                protected void onComponentTag(ComponentTag tag) {
                    super.onComponentTag(tag);
                    tag.put("class", "buttonDelete");
                }
            };
        }
    }
}

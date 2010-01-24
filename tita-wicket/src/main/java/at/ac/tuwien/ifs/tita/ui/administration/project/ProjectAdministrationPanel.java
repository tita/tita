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
import java.util.Set;

import javax.persistence.PersistenceException;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.table.Table;

import at.ac.tuwien.ifs.tita.business.service.project.IProjectService;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.conv.ProjectStatus;
import at.ac.tuwien.ifs.tita.ui.models.TableModelProject;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonDelete;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonDeleteRenderer;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonEdit;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonEditRenderer;
import at.ac.tuwien.ifs.tita.ui.uihelper.IAdministrationPanel;
import at.ac.tuwien.ifs.tita.ui.utils.EffortUtils;

/**
 * ProjectAdministrationPanel allows to manage Projects over the GUI.
 * 
 * @author ASE Group 10
 */
public class ProjectAdministrationPanel extends Panel implements IAdministrationPanel {

    // the service for DB-Operations
    @SpringBean(name = "titaProjectService")
    private IProjectService titaProjectService;

    // the containers
    private final WebMarkupContainer listContainer;
    private final WebMarkupContainer detailContainer;
    private final WebMarkupContainer issueTrackerContainer;

    // ////////////// LIST VIEW ////////////////
    // The Table Model with Table
    private Table table;
    private TableModelProject tm;

    // the Entity List
    private List<ProjectStatus> projectStati;

    // the form for sending data
    private ProjectForm form;
    private IssueTrackerProjectForm issueTrackerForm;

    // the current Project
    private TiTAProject currentProject;

    // Logger
    private final Logger log = LoggerFactory.getLogger(ProjectAdministrationPanel.class);

    /**
     * public constructor.
     * 
     * @param id the name of the Panel
     */
    public ProjectAdministrationPanel(String id) {
        super(id);

        try {
            projectStati = titaProjectService.getAvailableProjectStati();
        } catch (PersistenceException e) {
            log.error("Could not load Project Stati, empty list will be initialized");
            projectStati = new ArrayList<ProjectStatus>();
        }

        // init the two WebmarkupContainer
        // Note, they have the same id for switching between them
        listContainer = new WebMarkupContainer("listContainer");
        listContainer.setOutputMarkupId(true);
        listContainer.setOutputMarkupPlaceholderTag(true);
        addOrReplace(listContainer);

        detailContainer = new WebMarkupContainer("detailContainer");
        detailContainer.setOutputMarkupId(true);
        detailContainer.setOutputMarkupPlaceholderTag(true);
        addOrReplace(detailContainer);

        issueTrackerContainer = new WebMarkupContainer("issueTrackerContainer");
        issueTrackerContainer.setOutputMarkupId(true);
        issueTrackerContainer.setOutputMarkupPlaceholderTag(true);
        addOrReplace(issueTrackerContainer);

        loadListEntities();
        this.setOutputMarkupId(true);
        this.setOutputMarkupPlaceholderTag(true);
    }

    /**
     * Method for displaying a List of Projects. Also hides the details page if null value is given, just switch without
     * refreshing data.
     * 
     * @param projectList the list of Projects.
     */
    private void displayTable(final List<TiTAProject> projectList) {
        // hide details
        detailContainer.setVisible(false);
        listContainer.setVisible(true);
        issueTrackerContainer.setVisible(false);

        if (projectList == null) {
            tm = new TableModelProject(new ArrayList<TiTAProject>());
        } else {
            tm = new TableModelProject(projectList);
        }
        table = new Table("projectTable", tm);
        table.setWidths(EffortUtils.WIDTHS);

        ButtonEditRenderer editRenderer = new ButtonEditRenderer(this);
        table.setDefaultRenderer(ButtonEdit.class, editRenderer);
        table.setDefaultEditor(ButtonEdit.class, editRenderer);

        ButtonDeleteRenderer deleteRenderer = new ButtonDeleteRenderer(this);
        table.setDefaultRenderer(ButtonDelete.class, deleteRenderer);
        table.setDefaultEditor(ButtonDelete.class, deleteRenderer);

        Form<TiTAProject> listForm = new Form<TiTAProject>("listForm");
        listForm.addOrReplace(table);
        listForm.addOrReplace(new Button("newProjectButton") {
            @Override
            public void onSubmit() {
                displayDetailsPage(null);
            }
        });

        listContainer.addOrReplace(listForm);
        addOrReplace(listContainer);
    }

    /**
     * Method for displaying the details site of a specific Project. Also hides the list page.
     * 
     * @param project the specific Project to show.
     */
    private void displayDetailsPage(final TiTAProject project) {

        this.currentProject = project;
        // hide list container
        listContainer.setVisible(false);
        detailContainer.setVisible(true);
        issueTrackerContainer.setVisible(false);

        form = new ProjectForm("projectForm", this, projectStati, project);

        form.add(new Button("save") {
            @Override
            public void onSubmit() {
                log.debug("Submitting Form.");
                form.setSubmitMode(ProjectForm.C_PERMANENT_SUBMIT);
            }
        });

        Button addIssueTrackerProjectButton = new Button("addIssueTrackerProjectButton") {
            @Override
            public void onSubmit() {
                log.debug("Opening Panel to Add IssueTrackerProject");
                form.setSubmitMode(ProjectForm.C_TEMPORARY_SUBMIT_THEN_SHOW_ISSUE_TRACKER_PROJECT_FORM);
            }
        };
        form.add(addIssueTrackerProjectButton);

        Button cancelButton = new Button("cancelAddProjectButton") {
            @Override
            public void onSubmit() {
                loadListEntities();
            }
        };
        cancelButton.setDefaultFormProcessing(false);
        form.add(cancelButton);

        detailContainer.addOrReplace(form);

        addOrReplace(detailContainer);
    }

    /**
     * displays the Panel to add IssueTrackerProjects.
     * 
     * @param issTProject the project to update
     */
    public void displayIssueTrackerProjectForm(IssueTrackerProject issTProject) {
        listContainer.setVisible(false);
        issueTrackerContainer.setVisible(true);
        detailContainer.setVisible(true);

        if (issTProject == null) {
            issueTrackerForm = new IssueTrackerProjectForm("issueTrackerProjectForm", 
                    new IssueTrackerProject(),
                titaProjectService.getAvailableIssueTracker(), currentProject);
        }
        issueTrackerForm = new IssueTrackerProjectForm("issueTrackerProjectForm", 
                issTProject, titaProjectService.getAvailableIssueTracker(), currentProject);

        issueTrackerForm.add(new Button("saveIssueTrackerProjectForm") {
            @Override
            public void onSubmit() {
                log.debug("Saving IssueTrackerProjectForm");

                Set<IssueTrackerProject> tmpSet = currentProject.getIssueTrackerProjects();
                tmpSet.add(issueTrackerForm.getIssueTrackerProject());
                currentProject.setIssueTrackerProjects(tmpSet);

                displayDetailsPage(currentProject);
            }
        });

        Button cancelIssueTrackerProjectForm = new Button("cancelIssueTrackerProjectForm") {
            @Override
            public void onSubmit() {
                displayDetailsPage(form.getProject());
            }
        };
        cancelIssueTrackerProjectForm.setDefaultFormProcessing(false);
        issueTrackerForm.add(cancelIssueTrackerProjectForm);

        issueTrackerContainer.addOrReplace(issueTrackerForm);
        addOrReplace(issueTrackerContainer);
    }

    /**
     * Sets the current Project to Submit.
     * 
     * @param project the Project to submit
     */
    public void setCurrentProject(TiTAProject project) {
        this.currentProject = project;
    }

    /**
     * Persists a Project and displays it in the current Project Table.
     * 
     * @param project the Project to display.
     */
    public void saveEntity(TiTAProject project) {
        try {
            titaProjectService.saveProject(project);
            tm.addEntity(project);
            tm.reload();
        } catch (PersistenceException e) {
            log.error("Could not save Project");
            log.error(e.getMessage());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void deleteListEntity(AjaxRequestTarget target) {
        TiTAProject project = null;

        int index = -1;
        try {
            index = table.getSelectedRows()[0];
        } catch (IndexOutOfBoundsException e) {
            log.debug("No Row From UserTable Selected.");
        }

        try {
            log.debug("trying to delete Project From Row " + index);
            project = tm.getProjectAt(index);
            project.setDeleted(true);
            titaProjectService.saveProject(project);
            tm.removeProjectAtIndex(index);
            tm.reload();
            target.addComponent(table);
        } catch (PersistenceException e) {
            log.error("Deleting Project failed!");
            log.error("Cause: " + e.getMessage());
        } catch (NullPointerException e) {
            log.error("Fatal Error, Table Model does not work correctly");
            log.error("Either an index out of bounds or a NULL Entity was returned");
        }
    }

    /** {@inheritDoc} */
    public List<TiTAProject> getEntityList(int maxsize) {
        List<TiTAProject> projectList = null;
        try {
            projectList = titaProjectService.getOrderedProjects(maxsize, "name");
        } catch (PersistenceException e) {
            log.error("Exception occured while fetching ProjectList of size: " + maxsize);
            log.error("Reason: " + e.getMessage());
            projectList = new ArrayList<TiTAProject>();
        }
        return projectList;
    }

    /** {@inheritDoc} */
    @Override
    public void reloadTable(AjaxRequestTarget target) {
        tm.reload(getEntityList(0));
        target.addComponent(table);
    }

    /** {@inheritDoc} */
    @Override
    public void saveListEntity(AjaxRequestTarget target) {
        // not implemented
    }

    /** {@inheritDoc} */
    @Override
    public void updateListEntity(AjaxRequestTarget target) {
        int index = -1;
        try {
            index = table.getSelectedRows()[0];
        } catch (IndexOutOfBoundsException e) {
            log.debug("No Row From UserTable Selected.");
        }

        log.debug("trying to update Project in Row " + index);
        if (index > -1) {
            TiTAProject project = tm.getProjectAt(index);
            displayDetailsPage(project);
            target.addComponent(this);
        }
    }

    @Override
    public List<Effort> getEntityList() {
        // not implemented
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void loadListEntities() {
        List<TiTAProject> list;
        try {
            // find all
            list = titaProjectService.getOrderedProjects(0, "name");
        } catch (PersistenceException e) {
            list = new ArrayList<TiTAProject>();
        }

        displayTable(list);
    }
}

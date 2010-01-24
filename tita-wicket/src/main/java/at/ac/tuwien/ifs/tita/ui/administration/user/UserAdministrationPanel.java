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
package at.ac.tuwien.ifs.tita.ui.administration.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.PersistenceException;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.table.Table;

import at.ac.tuwien.ifs.tita.business.service.project.IProjectService;
import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.TiTAUserProject;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;
import at.ac.tuwien.ifs.tita.entity.conv.Role;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IProjectTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.issue.service.IIssueTrackerService;
import at.ac.tuwien.ifs.tita.issuetracker.issue.service.IssueTrackerService;
import at.ac.tuwien.ifs.tita.ui.models.TableModelUser;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonDelete;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonDeleteRenderer;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonEdit;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonEditRenderer;
import at.ac.tuwien.ifs.tita.ui.uihelper.IAdministrationPanel;
import at.ac.tuwien.ifs.tita.ui.utils.EffortUtils;

/**
 * UserAdministrationPanel allows to manage Users over the GUI.
 * 
 * @author ASE Group 10
 */
public class UserAdministrationPanel extends Panel implements IAdministrationPanel {

    // the service for DB-Operations
    @SpringBean(name = "userService")
    private IUserService service;
    @SpringBean(name = "titaProjectService")
    private IProjectService titaProjectService;

    // the containers
    private final WebMarkupContainer listContainer;
    private final WebMarkupContainer detailContainer;

    // the panel to add Projects to the user
    private UserProjectPanel userProjectPanel;

    // the panel to add IssueTrackerLogins to the user
    private UserIssueTrackerLoginPanel userIssueTrackerPanel;

    // ////////////// LIST VIEW ////////////////
    // The Table Model with Table
    private Table table;
    private TableModelUser tm;

    // the form for sending data
    private UserForm form;

    // the current User Object
    private TiTAUser currentUser;

    // Logger
    private final Logger log = LoggerFactory.getLogger(UserAdministrationPanel.class);

    /**
     * public constructor.
     * 
     * @param id the name of the Panel
     */
    public UserAdministrationPanel(String id) {
        super(id);

        // init the two WebmarkupContainer
        // Note, they have the same id for switching between them
        listContainer = new WebMarkupContainer("listContainer");
        listContainer.setOutputMarkupId(true);
        listContainer.setOutputMarkupPlaceholderTag(true);

        detailContainer = new WebMarkupContainer("detailContainer");
        detailContainer.setOutputMarkupId(true);
        detailContainer.setOutputMarkupPlaceholderTag(true);

        addOrReplace(listContainer);
        addOrReplace(detailContainer);
        userProjectPanel = new UserProjectPanel("userProjectPanel", new TiTAUser(), this);
        userIssueTrackerPanel = new UserIssueTrackerLoginPanel("userIssueTrackerLoginPanel", new TiTAUser(), this);
        addOrReplace(userProjectPanel);
        addOrReplace(userIssueTrackerPanel);

        add(new FeedbackPanel("feedback").setOutputMarkupId(true));

        // displayDetailsPage(new TiTAUser());
        // displayProjectPanel();
        // displayIssueTrackerLoginPanel();

        loadListEntities();

        this.setOutputMarkupId(true);
        this.setOutputMarkupPlaceholderTag(true);
    }

    /**
     * Method for displaying a List of Users. Also hides the details page if null value is given, just switch without
     * refreshing data.
     * 
     * @param userList the list of users.
     */
    public void displayTable(final List<TiTAUser> userList) {
        // hide details
        detailContainer.setVisible(false);
        if (userProjectPanel != null) {
            userProjectPanel.setVisible(false);
        }
        if (userIssueTrackerPanel != null) {
            userIssueTrackerPanel.setVisible(false);
        }
        listContainer.setVisible(true);

        if (userList == null) {
            tm = new TableModelUser(new ArrayList<TiTAUser>());
        } else {
            tm = new TableModelUser(userList);
        }
        table = new Table("userTable", tm);
        table.setWidths(EffortUtils.WIDTHS);

        ButtonEditRenderer editRenderer = new ButtonEditRenderer(this);
        table.setDefaultRenderer(ButtonEdit.class, editRenderer);
        table.setDefaultEditor(ButtonEdit.class, editRenderer);

        ButtonDeleteRenderer deleteRenderer = new ButtonDeleteRenderer(this);
        table.setDefaultRenderer(ButtonDelete.class, deleteRenderer);
        table.setDefaultEditor(ButtonDelete.class, deleteRenderer);

        Form<TiTAUser> listForm = new Form<TiTAUser>("listForm");
        listForm.addOrReplace(table);
        listForm.addOrReplace(new Button("newUserButton") {
            @Override
            public void onSubmit() {
                displayDetailsPage(null);
            }
        });
        listContainer.addOrReplace(listForm);
        addOrReplace(listContainer);
    }

    /**
     * Method for displaying the details site of a specific User. Also hides the list page.
     * 
     * @param user the specific User to show.
     */
    public void displayDetailsPage(final TiTAUser user) {
        // hide list container
        listContainer.setVisible(false);
        detailContainer.setVisible(true);
        if (userProjectPanel != null) {
            userProjectPanel.setVisible(false);
        }
        if (userIssueTrackerPanel != null) {
            userIssueTrackerPanel.setVisible(false);
        }

        List<Role> roles = null;
        try {
            roles = service.getRoles();
        } catch (PersistenceException e) {
            log.error("Roles could not be loaded!");
            log.error("Reason: " + e.getMessage());
            roles = new ArrayList<Role>();
        }

        form = new UserForm("form", user, roles, this);

        addButtonsForDetailsPage();

        detailContainer.addOrReplace(form);

        addOrReplace(detailContainer);
    }

    /**
     * adds necessary buttons to the details page.
     */
    private void addButtonsForDetailsPage() {
        form.addOrReplace(new Button("save") {
            @Override
            public void onSubmit() {
                form.setSubmitMode(UserForm.C_PERMANENT_SUBMIT);
            }
        });

        Button cancelButton = new Button("cancel") {
            @Override
            public void onSubmit() {
                log.debug("Cancel the Form");
                loadListEntities();
            }
        };
        cancelButton.setDefaultFormProcessing(false);
        form.addOrReplace(cancelButton);

        Button addProjectButton = new Button("addProjectButton") {
            @Override
            public void onSubmit() {
                log.debug("Opening ProjectPanel to Add Project to User");
                form.setSubmitMode(UserForm.C_TEMPORARY_SUBMIT_THEN_SHOW_PROJECT_PANEL);
            }
        };
        addProjectButton.setDefaultFormProcessing(true);

        Button addLoginButton = new Button("addLoginButton") {
            @Override
            public void onSubmit() {
                log.debug("Opening IssueTrackerLoginPanel to Add IssueTrackerLogin to User");
                form.setSubmitMode(UserForm.C_TEMPORARY_SUBMIT_THEN_SHOW_LOGIN_PANEL);
            }
        };
        addLoginButton.setDefaultFormProcessing(true);

        form.addOrReplace(addProjectButton);
        form.addOrReplace(addLoginButton);
    }

    /**
     * displays the Panel to add Projects to the user. only possible if detailView has been initialized.
     */
    public void displayProjectPanel() {
        if (form != null) {
            detailContainer.setVisible(true);
            listContainer.setVisible(false);
            userIssueTrackerPanel.setVisible(false);
            userProjectPanel = new UserProjectPanel("userProjectPanel", currentUser, this);
            userProjectPanel.setVisible(true);
            this.addOrReplace(userProjectPanel);
        }
    }

    /**
     * displays the Panel to add IssueTrackerLogins to the user. only possible if detailView has been initialized.
     */
    public void displayIssueTrackerLoginPanel() {
        if (form != null) {
            detailContainer.setVisible(true);
            listContainer.setVisible(false);
            userProjectPanel.setVisible(false);
            userIssueTrackerPanel = new UserIssueTrackerLoginPanel("userIssueTrackerLoginPanel", currentUser, this);
            userIssueTrackerPanel.setVisible(true);
            this.addOrReplace(userIssueTrackerPanel);
        } else {
            log.error("Form is null!");
        }
    }

    /**
     * returns an IssueTrackerLogin for a specific IssueTracker if a Login for current User exists
     * 
     * @return a corresponding IssueTrackerLogin if existant.
     */
    public IssueTrackerLogin getLoginForIssueTracker(IssueTracker issueTracker) {
        Set<IssueTrackerLogin> logins = form.getUser().getIssueTrackerLogins();
        for (IssueTrackerLogin login : logins) {
            if (login.getIssueTracker().getDescription().equals(issueTracker.getDescription())) {
                return login;
            }
        }
        return null;
    }

    /**
     * returns a List of All available IssueTracker.
     * 
     * @return all available issueTrackers.
     */
    public List<IssueTracker> getAvailableIssueTracker() {
        try {
            return service.getAvailableIssueTracker();
        } catch (PersistenceException e) {
            log.error("Could not fetch IssueTracker, Exception from DAO was thrown");
            log.error(e.getMessage());
            return new ArrayList<IssueTracker>();
        }
    }

    /**
     * sets the current User to edit.
     * 
     * @param user the user to edit
     */
    public void setCurrentUser(TiTAUser user) {
        this.currentUser = user;
    }

    /** {@inheritDoc} */
    @Override
    public void deleteListEntity(AjaxRequestTarget target) {
        log.debug("deleting User entity from User Table");
        TiTAUser user = null;
        int index = -1;
        try {
            index = table.getSelectedRows()[0];
        } catch (IndexOutOfBoundsException e) {
            log.debug("No Row From UserTable Selected.");
        }

        try {
            user = (TiTAUser) tm.getValueAt(index, -1);
            user.setDeleted(true);
            service.saveUser(user);
            reloadTable(target);
        } catch (PersistenceException e) {
            log.error("Deleting User failed!");
            log.error("Cause: " + e.getMessage());
        } catch (NullPointerException e) {
            log.error("Fatal Error, Table Model does not work correctly");
            log.error("Either an index out of bounds or a NULL Entity was returned");
        }
    }

    /** {@inheritDoc} */
    public List<TiTAUser> getListEntities(int maxsize) {
        List<TiTAUser> userList = null;
        try {
            userList = service.getOrderedUsers(maxsize);
        } catch (PersistenceException e) {
            log.error("Exception occured while fetching UserList of size: " + maxsize);
            log.error("Reason: " + e.getMessage());
            userList = new ArrayList<TiTAUser>();
        }
        return userList;
    }

    /** {@inheritDoc} */
    @Override
    public void reloadTable(AjaxRequestTarget target) {
        tm.reload(getListEntities(0));
        target.addComponent(table);
    }

    /**
     * Persists a User and displays it in the current User Table.
     * 
     * @param user the User to display.
     */
    public void saveEntity(TiTAUser user) {
        try {
            this.currentUser = user;
            tm.addEntity(user);

            TiTAUser savedUser = service.saveUser(user);

            for (IssueTrackerLogin login : user.getIssueTrackerLogins()) {
                service.saveIssueTrackerLogin(login, savedUser);
            }
            for (TiTAUserProject userProject : user.getTitaUserProjects()) {
                for (IssueTrackerProject issProject : userProject.getProject().getIssueTrackerProjects()) {
                    IssueTrackerLogin login = getLoginForIssueTracker(issProject.getIssueTracker());
                    if (login != null) {
                        IIssueTrackerService issueTrackerService = new IssueTrackerService(login);
                        IProjectTrackable project = issueTrackerService.getProjectByProjectName(issProject
                            .getProjectName());
                        if (project != null) {
                            issProject.setIsstProjectId(project.getId());
                        }
                    }
                }
                userProject.setUser(savedUser);
                titaProjectService.saveUserProject(userProject);
            }
            tm.reload();
        } catch (PersistenceException e) {
            e.printStackTrace();
            log.error("Could not save User");
            log.error(e.getMessage());
        }
    }

    /**
     * Looks for existence of a specific User.
     * 
     * @param user The user to look for.
     * @return true if user already exists.
     */
    public boolean userExists(TiTAUser user) {
        try {
            if (service.getUserByUsername(user.getUserName()) != null) {
                return true;
            }
        } catch (Exception e) {
            log.error("Exception occured while finding a User.");
        }
        return false;
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

        if (index > -1) {
            try {
                TiTAUser user = (TiTAUser) tm.getValueAt(index, -1);
                displayDetailsPage(user);
                target.addComponent(this);
            } catch (ClassCastException e) {
                log.error("Could not edit Current User, ClassCastException occured.");
                log.error(e.getMessage());
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void loadListEntities() {
        List<TiTAUser> list;
        try {
            list = service.getOrderedUsers(0);
        } catch (PersistenceException e) {
            list = new ArrayList<TiTAUser>();
        }
        displayTable(list);
    }

    @Override
    public List<Effort> getEntityList() {
        // not implemented
        return null;
    }

}

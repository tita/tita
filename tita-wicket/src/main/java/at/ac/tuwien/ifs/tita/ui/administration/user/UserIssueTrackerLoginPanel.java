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
import java.util.HashSet;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.table.Table;

import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.ui.models.TableModelIssueTrackerLogin;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonDelete;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonDeleteRenderer;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonEdit;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonEditRenderer;
import at.ac.tuwien.ifs.tita.ui.uihelper.IAdministrationPanel;
import at.ac.tuwien.ifs.tita.ui.utils.EffortUtils;

/**
 * UserIssueTrackerLoginPanel allows to add IssueTrackerLogins to a User.
 * 
 * @author ASE Group 10
 */
public class UserIssueTrackerLoginPanel extends Panel implements
        IAdministrationPanel {
    // WebMarkupContainer
    private final WebMarkupContainer container;

    // Entity List
    private final List<IssueTrackerLogin> list;

    // the Table Model and Table
    private Table table;
    private TableModelIssueTrackerLogin tm;

    // the Parent Panel
    private final UserAdministrationPanel parent;

    // the user
    private TiTAUser user;

    // the form
    private IssueTrackerLoginForm form;

    // Logger
    private final Logger log = LoggerFactory
            .getLogger(UserIssueTrackerLoginPanel.class);

    /**
     * public Contructor.
     * 
     * @param id
     *            the unique ID that is declared in the HTML-File for this
     *            Panel.
     * @param user
     *            the User to add the IssueTrackerLogins.
     * @param parent
     *            the Parent Panel for closing and returning a User.
     */
    public UserIssueTrackerLoginPanel(final String id, final TiTAUser user,
            final UserAdministrationPanel parent) {
        super(id);

        this.parent = parent;

        if (user == null) {
            this.user = new TiTAUser();
        } else {
            this.user = user;
        }

        list = new ArrayList<IssueTrackerLogin>();
        if (user != null) {
            if (user.getIssueTrackerLogins() != null) {
                for (IssueTrackerLogin login : user.getIssueTrackerLogins()) {
                    list.add(login);
                }
            }
        }

        container = new WebMarkupContainer("userIssueTrackerLoginContainer");
        container.setOutputMarkupId(true);
        container.setOutputMarkupPlaceholderTag(true);

        addOrReplace(container);

        displayDetailsPage(new IssueTrackerLogin());
        displayTable(list);
    }

    /**
     * displays the table of IssueTrackerLogins.
     * 
     * @param issueTrackerList
     *            the list of IssueTracker.
     */
    @SuppressWarnings("unchecked")
    public void displayTable(final List<IssueTrackerLogin> issueTrackerList) {

        Form listForm = new Form("listFormIssueTrackerLogin");

        tm = new TableModelIssueTrackerLogin(issueTrackerList);
        table = new Table("userIssueTrackerLoginTable", tm);

        table.setWidths(EffortUtils.WIDTHS);

        ButtonEditRenderer editRenderer = new ButtonEditRenderer(this);
        table.setDefaultRenderer(ButtonEdit.class, editRenderer);
        table.setDefaultEditor(ButtonEdit.class, editRenderer);

        ButtonDeleteRenderer deleteRenderer = new ButtonDeleteRenderer(this);
        table.setDefaultRenderer(ButtonDelete.class, deleteRenderer);
        table.setDefaultEditor(ButtonDelete.class, deleteRenderer);

        // add components to the container
        listForm.addOrReplace(table);
        container.addOrReplace(listForm);
    }

    /**
     * shows the details page for IssueTrackerLogin.
     * 
     * @param login
     *            the login
     */
    public void displayDetailsPage(IssueTrackerLogin login) {
        if (login == null) {
            form = new IssueTrackerLoginForm("userIssueTrackerLoginForm",
                    parent.getAvailableIssueTracker(), this);
        } else {
            form = new IssueTrackerLoginForm("userIssueTrackerLoginForm",
                    parent.getAvailableIssueTracker(), login, this);
        }

        form.addOrReplace(new Button("userIssueTrackerLoginAddButton"));

        Button userIssueTrackerLoginSaveButton = new Button(
                "userIssueTrackerLoginSaveButton") {
            @Override
            public void onSubmit() {
                log.info("Returning to UserForm.");
                parent.displayDetailsPage(getUser());
            }
        };
        userIssueTrackerLoginSaveButton.setDefaultFormProcessing(false);
        form.addOrReplace(userIssueTrackerLoginSaveButton);

        container.addOrReplace(form);
    }

    /**
     * returns the current user.
     * 
     * @return the user
     */
    public TiTAUser getUser() {
        return this.user;
    }

    /**
     * sets a new User Object to the Panel.
     * 
     * @param user
     *            the new User
     */
    public void setUser(TiTAUser user) {
        this.user = user;
    }

    /** {@inheritDoc} */
    @Override
    public void deleteListEntity(AjaxRequestTarget target) {
        int index = -1;
        try {
            index = table.getSelectedRows()[0];
        } catch (IndexOutOfBoundsException e) {
            log.debug("No Row From UserTable Selected.");
        }

        if (index > -1) {
            try {
                log.debug("deleting IssueTrackerLogin in Row " + index);
                list.remove(index);
                tm.reload(list);
            } catch (IndexOutOfBoundsException e) {
                log.error("Error, Index out of Bounds Exception");
            } catch (ClassCastException e) {
                log
                        .error("Error, ClassCast of IssueTrackerLogin from Table Model failed.");
            }
        }
    }

    /**
     * adds an entity to the current in memory list.
     * 
     * @param login
     *            the IssueTrackerLogin.
     */
    public void addEntityToList(IssueTrackerLogin login) {
        log.debug("Adding IssueTrackerLogin to List");
        int index = -1;
        try {
            index = table.getSelectedRows()[0];
        } catch (IndexOutOfBoundsException e) {
            log.debug("No Row From UserTable Selected.");
        }

        if (index > -1) {
            // replace
            list.remove(index);
            list.add(index, login);
        } else {
            // else just add
            list.add(login);
        }

        setUser(new TiTAUser(getUser().getUserName(), getUser().getPassword(),
                getUser().getFirstName(), getUser().getLastName(), getUser()
                        .getEmail(), getUser().isDeleted(),
                getUser().getRole(), getUser().getTitaUserProjects(),
                new HashSet<IssueTrackerLogin>(list)));

        displayTable(list);
    }

    /** {@inheritDoc} */
    @Override
    public void reloadTable(AjaxRequestTarget target) {
        tm.reload(list);
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
        // not implemented
    }

    /** {@inheritDoc} */
    @Override
    public void loadListEntities() {
        // not implemented
    }

    @Override
    public List<Effort> getEntityList() {
        // not implemented
        return null;
    }
}

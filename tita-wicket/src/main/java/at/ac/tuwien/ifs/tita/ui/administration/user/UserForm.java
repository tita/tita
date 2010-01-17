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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.table.Table;

import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.TiTAUserProject;
import at.ac.tuwien.ifs.tita.entity.conv.Role;
import at.ac.tuwien.ifs.tita.ui.models.AbstractTitaTableModel;
import at.ac.tuwien.ifs.tita.ui.models.TableModelUserProject;
import at.ac.tuwien.ifs.tita.ui.utils.IntegerConstants;

/**
 * Form to save or update a User Entity.
 * 
 * @author ASE Group 10
 */
public class UserForm extends Form<TiTAUser> {

    private static final String C_ISSUE_TRACKER_TABLE_NAME = "issueTrackerTable";
    private static final String C_USER_PROJECT_TABLE_NAME = "userProjectTable";

    // The Object to save
    private TiTAUser user;
    // password fields required for authentitcation
    private String passwordRepetition;

    // roles needed for Dropdownbox
    private final List<Role> roles;

    // Lists needed for Tables
    private final List<TiTAUserProject> titaProjects;
    private Table userProjectTable;
    private TableModelUserProject userProjectTM;

    private final List<IssueTrackerLogin> titaIssueTracker;
    private Table issueTrackerLoginTable;
    private TableModelIssueTrackerLoginWithoutButtons issueTrackerLoginTM;

    // the Parent panel to add Users on Submit
    private final UserAdministrationPanel parent;

    // Logger
    private final Logger log = LoggerFactory.getLogger(UserForm.class);

    /**
     * public Constructor for updating existing Users.
     * 
     * @param id the Components name.
     * @param user the User to update
     * @param the roles, that a user can obtain.
     * @param parent the Parent Panel to add the User when saving the Form.
     */
    public UserForm(final String id, final TiTAUser u, final List<Role> roles, UserAdministrationPanel parent) {
        super(id);
        if (u == null) {
            this.user = new TiTAUser("", "", "", "", "", false, null, new HashSet<TiTAUserProject>(),
                    new HashSet<IssueTrackerLogin>());

        } else {
            this.user = u;
        }

        this.parent = parent;
        this.roles = roles;
        if (user.getIssueTrackerLogins() == null) {
            this.titaIssueTracker = new ArrayList<IssueTrackerLogin>();
        } else {
            this.titaIssueTracker = new ArrayList<IssueTrackerLogin>(user.getIssueTrackerLogins());
        }

        if (user.getTitaUserProjects() == null) {
            this.titaProjects = new ArrayList<TiTAUserProject>();
        } else {
            this.titaProjects = new ArrayList<TiTAUserProject>(user.getTitaUserProjects());
        }

        addComponents();
        addLabel();
    }

    /**
     * Method for Adding the Fields to the Form.
     */
    private void addComponents() {

        addOrReplace(new TextField<String>("tfUserName", new PropertyModel<String>(user, "userName")));
        addOrReplace(new TextField<String>("tfFirstName", new PropertyModel<String>(user, "firstName")));
        addOrReplace(new TextField<String>("tfLastName", new PropertyModel<String>(user, "lastName")));
        addOrReplace(new TextField<String>("tfEmail", new PropertyModel<String>(user, "email")));
        addOrReplace(new PasswordTextField("ptfPassword", new PropertyModel<String>(user, "password")));
        addOrReplace(new PasswordTextField("ptfPasswordRepetition", new PropertyModel<String>(this,
                "passwordRepetition")));

        addOrReplace(new CheckBox("checkBoxDeleted", new PropertyModel<Boolean>(user, "deleted")));
        addOrReplace(new DropDownChoice<Role>("dropDownRole", new PropertyModel<Role>(user, "role"), roles));

        userProjectTM = new TableModelUserProject(titaProjects);
        userProjectTable = new Table(C_USER_PROJECT_TABLE_NAME, userProjectTM);

        issueTrackerLoginTM = new TableModelIssueTrackerLoginWithoutButtons(titaIssueTracker);
        issueTrackerLoginTable = new Table(C_ISSUE_TRACKER_TABLE_NAME, issueTrackerLoginTM);

        addOrReplace(issueTrackerLoginTable);
        addOrReplace(userProjectTable);
    }

    /**
     * Method for adding Labels to the Form.
     */
    private void addLabel() {
        addOrReplace(new Label("lbUserName", "Username: "));
        addOrReplace(new Label("lbFirstName", "First Name: "));
        addOrReplace(new Label("lbLastName", "Last Name: "));
        addOrReplace(new Label("lbEmail", "Email: "));
        addOrReplace(new Label("lbPassword", "Password: "));
        addOrReplace(new Label("lbPasswordRepetition", "Retype Password: "));
        addOrReplace(new Label("lbDeleted", "Deleted: "));
        addOrReplace(new Label("lbRole", "Role: "));
        addOrReplace(new Label("lbProjects", "Projects: "));
        addOrReplace(new Label("lbIssueTracker", "Issue Tracker: "));
    }

    /** {@inheritDoc} **/
    @Override
    public final void onSubmit() {
        log.debug("trying to save user " + getUser());
        log.debug("checking if repetition: " + getPasswordRepetition() + " equals the password " + user.getPassword());

        // if form should be submitted, but data should not be saved
        if (getPasswordRepetition().equals(user.getPassword())) {
            parent.saveEntity(user);
            parent.displayCurrentList();
        } else {
            this.error("Entered Password does not equal Password Repetition.");
        }

    }

    /**
     * Method for getting the user.
     * 
     * @return the user
     */
    public TiTAUser getUser() {
        return user;
    }

    /**
     * Method for getting the passwordRepetition.
     * 
     * @return the passwordRepetition
     */
    public String getPasswordRepetition() {
        if (passwordRepetition == null) {
            return "";
        }
        return passwordRepetition;
    }

    /**
     * Method for setting the passwordRepetition.
     * 
     * @param passwordRepetition the passwordRepetition to set
     */
    public void setPasswordRepetition(String passwordRepetition) {
        this.passwordRepetition = passwordRepetition;
    }

    /**
     * Adds a new UserProject to the Form List and Refreshes the
     * UserProjectTable.
     * 
     * @param userProject the userProject to Add.
     */
    public void addUserProject(TiTAUserProject userProject) {
        titaProjects.add(userProject);
        userProjectTM.reload(titaProjects);
        userProjectTable = new Table(C_USER_PROJECT_TABLE_NAME, userProjectTM);
        this.addOrReplace(userProjectTable);
    }

    /**
     * Returns the currently selected UserProject of the UserProjectTable.
     * 
     * @return the currently selected UserProject, if no project is selected
     *         null.
     */
    public TiTAUserProject getCurrentUserProject() {
        if (userProjectTM.getSelectedRow() > 1) {
            return (TiTAUserProject) userProjectTM.getValueAt(userProjectTM.getSelectedRow(), -1);
        }
        return null;
    }

    /**
     * Adds a new IssueTrackerLogin to the Form List and Refreshes the
     * IssueTrackerLoginTable.
     * 
     * @param issueTrackerLogin the userProject to Add.
     */
    public void addIssueTrackerLogin(IssueTrackerLogin issueTrackerLogin) {
        titaIssueTracker.add(issueTrackerLogin);
        issueTrackerLoginTM.reload(titaIssueTracker);
        issueTrackerLoginTable = new Table(C_ISSUE_TRACKER_TABLE_NAME, issueTrackerLoginTM);
        this.addOrReplace(issueTrackerLoginTable);
    }

    /**
     * Returns the currently selected IssueTrackerLogin of the
     * IssueTrackerLoginTable.
     * 
     * @return the currently selected IssueTrackerLogin, if no login is selected
     *         null.
     */
    public IssueTrackerLogin getCurrentIssueTrackerLogin() {
        if (issueTrackerLoginTM.getSelectedRow() > 1) {
            return (IssueTrackerLogin) issueTrackerLoginTM.getValueAt(issueTrackerLoginTM.getSelectedRow(), -1);
        }
        return null;
    }

    /**
     * Removes the current IssueTrackerLogin from the IssueTrackerLogin Table.
     */
    public void removeCurrentUserProject() {
        if (userProjectTM.getSelectedRow() > 1) {
            this.titaProjects.remove(userProjectTM.getSelectedRow());
            userProjectTM.reload(titaProjects);
            userProjectTable = new Table(C_USER_PROJECT_TABLE_NAME, userProjectTM);
            this.addOrReplace(userProjectTable);
        }
    }

    /**
     * Removes the current IssueTrackerLogin from the IssueTrackerLogin Table.
     */
    public void removeCurrentIssueTrackerLogin() {
        if (issueTrackerLoginTM.getSelectedRow() > 1) {
            this.titaIssueTracker.remove(issueTrackerLoginTM.getSelectedRow());
            issueTrackerLoginTM.reload(titaIssueTracker);
            issueTrackerLoginTable = new Table(C_ISSUE_TRACKER_TABLE_NAME, issueTrackerLoginTM);
            this.addOrReplace(issueTrackerLoginTable);
        }
    }

    /********************** INNER CLASS FOR TM. *********************/
    public class TableModelIssueTrackerLoginWithoutButtons extends AbstractTitaTableModel {

        // Logger
        private final Logger log = LoggerFactory.getLogger(TableModelIssueTrackerLoginWithoutButtons.class);

        public TableModelIssueTrackerLoginWithoutButtons(List<IssueTrackerLogin> list) {
            super(list);
            columnNames = new String[] { "IssueTracker", "Username" };
        }

        /** {@inheritDoc} **/
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return super.getColumnClass(columnIndex);
        }

        /** {@inheritDoc} **/
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            IssueTrackerLogin login = null;
            try {
                login = (IssueTrackerLogin) list.get(rowIndex);

                if (columnIndex == IntegerConstants.ZERO) {
                    if (login.getIssueTracker() != null) {
                        return login.getIssueTracker().getDescription();
                    } else {
                        return null;
                    }
                } else if (columnIndex == IntegerConstants.ONE) {
                    return login.getUserName();
                } else {
                    return login;
                }

            } catch (IndexOutOfBoundsException e) {
                log.error("Error IndexOutOfBoundsException occured: " + e.getMessage());
            } catch (ClassCastException e) {
                log.error("Error ClassCastException occured: " + e.getMessage());
            } catch (NullPointerException e) {
                log.error("Error NullPointerException occured: " + e.getMessage());
            }
            return null;
        }

        /** {@inheritDoc} **/
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        /**
         * Gets the Current List of Entities in the Table.
         * 
         * @return the list of entites showed in the table.
         */
        @SuppressWarnings("unchecked")
        public List<IssueTrackerLogin> getList() {
            return (List<IssueTrackerLogin>) list;
        }
    }

}

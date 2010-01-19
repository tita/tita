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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;

/**
 * Form to save or update an IssueTrackerLogin Entity.
 * 
 * @author ASE Group 10
 */
public class IssueTrackerLoginForm extends Form<IssueTrackerLogin> {

    // the login Entity
    private final IssueTrackerLogin issueTrackerLogin;

    private final List<IssueTracker> availableIssueTracker;

    private final UserIssueTrackerLoginPanel parent;

    // Logger
    private final Logger log = LoggerFactory.getLogger(IssueTrackerLoginForm.class);

    /**
     * public constructor for creating new IssueTrackerLogins.
     * 
     * @param id the unique ID that is declared in the HTML-File for this Form.
     * @param availableIssT a List of available IssueTracker for
     *        Dropdown Box.
     * @param parent the parent panel to add the IssueTrackerLogin.
     */
    public IssueTrackerLoginForm(String id, List<IssueTracker> availableIssT, UserIssueTrackerLoginPanel parent) {
        super(id);

        this.parent = parent;

        if (availableIssT != null) {
            this.availableIssueTracker = availableIssT;
        } else {
            this.availableIssueTracker = new ArrayList<IssueTracker>();
        }

        this.issueTrackerLogin = new IssueTrackerLogin();

        init();
    }

    /**
     * public constructor for updating an existing IssueTrackerLogin.
     * 
     * @param id the unique ID that is declared in the HTML-File for this Form.
     * @param availableIssueTracker a List of available IssueTracker for
     *        Dropdown Box.
     * @param issueTrackerLogin the IssueTrackerLogin to update.
     * @param parent the parent panel to add the IssueTrackerLogin.
     */
    public IssueTrackerLoginForm(String id, List<IssueTracker> availableIssueTracker,
            IssueTrackerLogin issueTrackerLogin, UserIssueTrackerLoginPanel parent) {
        super(id);

        this.parent = parent;

        if (availableIssueTracker != null) {
            this.availableIssueTracker = availableIssueTracker;
        } else {
            this.availableIssueTracker = new ArrayList<IssueTracker>();
        }

        if (issueTrackerLogin == null) {
            this.issueTrackerLogin = new IssueTrackerLogin();
        } else {
            this.issueTrackerLogin = issueTrackerLogin;
        }

        init();
    }

    /**
     * init method calls all important methods.
     */
    private void init() {
        addComponents();
        addLabels();
    }

    /**
     * Method for Adding the Fields to the Form.
     */
    private void addComponents() {
        addOrReplace(new TextField<String>("tfIssueTrackerUserName", new PropertyModel<String>(issueTrackerLogin,
                "userName")));
        addOrReplace(new PasswordTextField("ptfIssueTrackerPassword", new PropertyModel<String>(issueTrackerLogin,
                "password")));

        addOrReplace(new DropDownChoice<IssueTracker>("dropDownIssueTracker", new PropertyModel<IssueTracker>(
                issueTrackerLogin, "issueTracker"), availableIssueTracker));
    }

    /**
     * Method for adding Labels to the Form.
     */
    private void addLabels() {
        addOrReplace(new Label("lbUserName", "Username: "));
        addOrReplace(new Label("lbPassword", "Password: "));
        addOrReplace(new Label("lbIssueTracker", "IssueTracker: "));
    }

    /**
     * returns the currently submitted IssueTrackerLogin.
     * 
     * @return the issueTrackerLogin
     */
    public IssueTrackerLogin getIssueTrackerLogin() {
        return this.issueTrackerLogin;
    }

    /** {@inheritDoc} **/
    @Override
    public void onSubmit() {
        log.debug("Submitting IssueTrackerLogin Form");
        if (issueTrackerLogin != null) {
            if (issueTrackerLogin.getIssueTracker() != null) {
                log.debug("IssueTrackerLogin and Corresponding IssueTracker found.");
                parent.addEntityToList(issueTrackerLogin);
            }
        }
        parent.displayDetailsPage(new IssueTrackerLogin());
    }
}

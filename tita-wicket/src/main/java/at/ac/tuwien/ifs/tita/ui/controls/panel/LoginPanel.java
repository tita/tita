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
package at.ac.tuwien.ifs.tita.ui.controls.panel;

import java.util.MissingResourceException;

import org.apache.wicket.Application;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.security.authentication.LoginException;
import org.apache.wicket.util.value.ValueMap;

import at.ac.tuwien.ifs.tita.ui.login.TitaSession;
import at.ac.tuwien.ifs.tita.ui.startpages.AdminPage;
import at.ac.tuwien.ifs.tita.ui.startpages.EffortsPage;
import at.ac.tuwien.ifs.tita.ui.startpages.ProjectsPage;

/**
 * Panel to authenticate user.
 *
 * @author Karin
 *
 */
public abstract class LoginPanel extends Panel {

    public LoginPanel(final String id) {
        super(id);
        add(new SignInForm("loginForm").setOutputMarkupId(false));
    }

    /**
     * The actual login process.
     * 
     * @param username
     *            - name
     * @param password
     *            - pwd
     * @return true, if the login was successful, false otherwise
     * @throws LoginException
     *             login - if a user with role timeconsumer has no assign tita
     *             project
     */
    public abstract boolean signIn(String username, String password) throws LoginException;

    /**
     * Sign in form.
     */
    @SuppressWarnings("unchecked")
    public final class SignInForm extends StatelessForm {
        private static final long serialVersionUID = 1L;

        /**
         * Constructor.
         *
         * @param id id of the form component
         */
        public SignInForm(final String id) {
            // sets a compound model on this form, every component without an
            // explicit model will use this model too
            super(id, new CompoundPropertyModel(new ValueMap()));

            // only remember username, not passwords
            add(new TextField("username").setOutputMarkupId(false));
            add(new PasswordTextField("password").setOutputMarkupId(false));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSubmit() {

            // delete persistent data
            getPage().removePersistedFormData(SignInForm.class, true);

            ValueMap values = (ValueMap) getModelObject();
            String username = values.getString("username");
            String password = values.getString("password");

            try {
                if (signIn(username, password)) {
                    TitaSession session = TitaSession.getSession();
                    if (session != null) {
                        if (session.getRole().equals("admin")) {
                            setResponsePage(AdminPage.class);
                        } else if (session.getRole().equals("timecontroller")) {
                            setResponsePage(ProjectsPage.class);
                        } else if (session.getRole().equals("timeconsumer")) {
                            setResponsePage(EffortsPage.class);
                        }
                    } else {
                        // continue or homepage?
                        if (!getPage().continueToOriginalDestination()) {
                            setResponsePage(Application.get().getHomePage());
                        }
                    }
                } else {
                    // Try the component based localizer first. If not found try
                    // the
                    // application localizer. Else use the default
                    error(getLocalizer().getString("exception.login", this,
                            "Illegal username password combo"));
                }
            } catch (MissingResourceException e) {
                error("No resources is available");
            } catch (LoginException e) {
                error(e.getMessage());
            }
        }

    }
}

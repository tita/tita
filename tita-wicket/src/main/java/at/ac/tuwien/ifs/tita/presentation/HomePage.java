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
package at.ac.tuwien.ifs.tita.presentation;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.security.WaspSession;
import org.apache.wicket.security.authentication.LoginException;

import at.ac.tuwien.ifs.tita.presentation.login.LoginPanel;
import at.ac.tuwien.ifs.tita.presentation.login.TitaLoginContext;

/**
 * Homepage of Hello World Application.
 */
public class HomePage extends WebPage {

    public HomePage() {
        // stateless so the login page will not throw a timeout exception
        // note that is only a hint we need to have stateless components on the
        // page for this to work, like a statelessform
        setStatelessHint(true);
        add(new FeedbackPanel("feedback") {
            private static final long serialVersionUID = 1L;

            /**
             * @see org.apache.wicket.Component#isVisible()
             */
            @Override
            public boolean isVisible() {
                return anyMessage();
            }
        });
        newUserPasswordSignInPanel("loginPanel");
    }

    /**
     * Creates a sign in panel with a username and a password field.
     * 
     * @param panelId - id
     */
    protected void newUserPasswordSignInPanel(String panelId) {
        add(new LoginPanel(panelId) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean signIn(String username, String password) {
                TitaLoginContext ctx = new TitaLoginContext(username, password);
                try {
                    ((WaspSession) getSession()).login(ctx);
                } catch (LoginException e) {
                    return false;
                }
                return true;
            }
        });
    }
}

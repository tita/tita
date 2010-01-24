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
package at.ac.tuwien.ifs.tita.ui;

import javax.persistence.PersistenceException;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.security.WaspSession;
import org.apache.wicket.security.authentication.LoginException;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.ui.controls.panel.LoginPanel;
import at.ac.tuwien.ifs.tita.ui.login.TitaLoginContext;
import at.ac.tuwien.ifs.tita.ui.login.TitaSession;
import at.ac.tuwien.ifs.tita.ui.utils.TimerCoordinator;

/**
 * Homepage of Hello World Application.
 */
public class HomePage extends WebPage {
    private final Logger log = LoggerFactory.getLogger(HomePage.class);

    @SpringBean(name = "timerCoordinator")
    private TimerCoordinator timerCoordinator;

    @SpringBean(name = "userService")
    private IUserService userService;

    public HomePage() {
        new Thread(timerCoordinator).start();
        setStatelessHint(true);
        add(new FeedbackPanel("feedback"));
        newLoginPanel("loginPanel");
    }

    /**
     * Creates a sign in panel with a username and a password field.
     * 
     * @param panelId - id
     */
    protected void newLoginPanel(String panelId) {
        add(new LoginPanel(panelId) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean signIn(String username, String password) {
                TiTAUser titaUser;

                TitaLoginContext ctx = new TitaLoginContext(username, password, userService);

                try {
                    ((WaspSession) getSession()).login(ctx);
                    TitaSession.getSession().setUsername(username);
                    titaUser = userService.getUserByUsername(username);
                    // register tita user with 0 active tasks
                    timerCoordinator.registerUser(titaUser.getId());
                } catch (PersistenceException ex) {
                    log.error("Could find user in db " + username, ex);
                    return false;
                } catch (LoginException e) {
                    log.error("Could not login " + username, e);
                    return false;
                }
                return true;
            }
        });
    }
}

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

import org.apache.wicket.Application;
import org.apache.wicket.PageParameters;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.WaspSession;
import org.apache.wicket.security.components.SecureWebPage;
import org.apache.wicket.security.hive.authentication.LoginContext;
import org.apache.wicket.security.swarm.SwarmWebApplication;

import at.ac.tuwien.ifs.tita.presentation.tasklist.TaskListPanel;

/**
 * BaseClass for sites.
 * 
 * @author rene
 * 
 */
public class BasePage extends SecureWebPage {
    
    public BasePage() {
        init();
        String username = "User1";
        add(new Label("showUser", "Signed in as " + username));
        
        final WebMarkupContainer timeConsumergroup = new WebMarkupContainer(
            "timeConsumerGroup");
        final WebMarkupContainer timeControllergroup = new WebMarkupContainer(
            "timeControllerGroup");
        final WebMarkupContainer administratorGroup = new WebMarkupContainer(
            "administratorGroup");
        add(timeConsumergroup);
        add(timeControllergroup);
        add(administratorGroup);
        add(new TaskListPanel("taskList"));
    }

    public BasePage(PageParameters parameters)  {
        super(parameters);
        init();
    }

    public BasePage(IModel model) {
        super(model);
        init();
    }
    
    /**
     * initialize the page.
     */
    protected void init() {
        // not a secure link because everyone can logoff.
        Link logoff = new Link("logoff")
        {

            private static final long serialVersionUID = 1L;

            public void onClick() {
                WaspSession waspSession = ((WaspSession)getSession());
                if (waspSession.logoff(getLogoffContext())) {
                    // homepage is not allowed anymore so we end up at the
                    // loginpage
                    setResponsePage(Application.get().getHomePage());
                    waspSession.invalidate();
                } else{
                    error("A problem occured during the logoff process, please " +
                            "try again or contact support");
                }
            }
        };
        add(logoff);
    }

    /**
     * shortcut to the {@link WaspSession}.
     * 
     * @return the session.
     */
    protected final WaspSession getSecureSession() {
        return (WaspSession)Session.get();
    }

    /**
     * Shortcut to the application.
     * 
     * @return the application
     */
    protected final SwarmWebApplication getSecureApplication() {
        return (SwarmWebApplication)Application.get();
    }

    /**
     * Allows subclasses to specify which context should be used when logging
     * off.
     * 
     * @return the context
     */
    protected final LoginContext getLogoffContext(){
        Application app = Application.get();
        if (app instanceof TiTAApplication){
            return ((TiTAApplication)app).getLogoffContext();
        }
        throw new WicketRuntimeException("Application is not a subclass of "
                + TiTAApplication.class);
    }
}
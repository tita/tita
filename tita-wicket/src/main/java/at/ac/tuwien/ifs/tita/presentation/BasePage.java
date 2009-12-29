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
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.security.WaspSession;
import org.apache.wicket.security.components.SecureWebPage;
import org.apache.wicket.security.components.markup.html.links.SecurePageLink;
import org.apache.wicket.security.hive.authentication.LoginContext;

import at.ac.tuwien.ifs.tita.presentation.effort.EffortsPage;
import at.ac.tuwien.ifs.tita.presentation.evaluation.timeconsumer.DailyViewPage;
import at.ac.tuwien.ifs.tita.presentation.evaluation.timeconsumer.MonthlyViewPage;
import at.ac.tuwien.ifs.tita.presentation.evaluation.timecontroller.MultipleProjectsView;
import at.ac.tuwien.ifs.tita.presentation.login.TitaSession;
import at.ac.tuwien.ifs.tita.presentation.tasklist.TaskListPanel;

/**
 * BaseClass for sites.
 * 
 * @author rene
 * 
 */
public class BasePage extends SecureWebPage {

    public BasePage() {
        initLogoutLink();

        String username = TitaSession.getSession().getUsername();
        add(new Label("showUser", "Signed in as " + username));

        final WebMarkupContainer timeConsumergroup = 
            new WebMarkupContainer("timeConsumerGroup");
        final WebMarkupContainer timeControllergroup = 
            new WebMarkupContainer("timeControllerGroup");
        final WebMarkupContainer administratorGroup = 
            new WebMarkupContainer("administratorGroup");
        
        addAdminLinks(administratorGroup);
        addTimeConsumerLinks(timeConsumergroup);
        addTimeControllerLinks(timeControllergroup);
        
        add(timeConsumergroup);
        add(timeControllergroup);
        add(administratorGroup);
        add(new TaskListPanel("taskList"));
    }

    /**
     * Adds the secure Links for TimeController.
     * @param timeControllergroup - container
     */
    private void addTimeControllerLinks(WebMarkupContainer timeControllergroup) {
        timeControllergroup.add(
                new SecurePageLink("multipleProjectsViewLink", MultipleProjectsView.class));
        
    }

    /**
     * Adds the secure Links for TimeConsumers.
     * @param timeConsumergroup - container
     */
    private void addTimeConsumerLinks(WebMarkupContainer timeConsumergroup) {
        timeConsumergroup.add(new SecurePageLink("effortsPageLink", EffortsPage.class));
        timeConsumergroup.add(new SecurePageLink("dailyViewPageLink", DailyViewPage.class));
        timeConsumergroup.add(new SecurePageLink("monthlyViewPageLink", MonthlyViewPage.class));
        
    }

    /**
     * Adds the secure Links for TimeController.
     * @param administratorGroup - container
     */
    private void addAdminLinks(WebMarkupContainer administratorGroup) {
        // TODO Auto-generated method stub
        
    }

    /**
     * initialize the page.
     */
    protected void initLogoutLink() {
        // not a secure link because everyone can logoff.
        Link logoff = new Link("logoff") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                WaspSession waspSession = (WaspSession) getSession();
                if (waspSession.logoff(getLogoffContext())) {
                    // goto login page
                    setResponsePage(Application.get().getHomePage());
                    waspSession.invalidate();
                } else {
                    error("A problem occured during the logoff process, please " 
                            + "try again or contact support");
                }
            }
        };
        add(logoff);
    }

    /**
     * Allows subclasses to specify which context should be used when logging
     * off.
     * 
     * @return the context
     */
    protected final LoginContext getLogoffContext() {
        Application app = Application.get();
        if (app instanceof TiTAApplication) {
            return ((TiTAApplication) app).getLogoffContext();
        }
        throw new WicketRuntimeException("Application is not a subclass of " 
                + TiTAApplication.class);
    }
}
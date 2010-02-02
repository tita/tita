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

import java.util.List;

import javax.annotation.Resource;

import org.apache.wicket.Application;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.security.components.SecureWebPage;
import org.apache.wicket.security.components.markup.html.links.SecurePageLink;
import org.apache.wicket.security.hive.authentication.LoginContext;
import org.apache.wicket.spring.injection.annot.SpringBean;

import at.ac.tuwien.ifs.tita.business.service.project.IProjectService;
import at.ac.tuwien.ifs.tita.business.service.tasks.ITaskService;
import at.ac.tuwien.ifs.tita.business.service.time.IEffortService;
import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.util.ActiveTaskId;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;
import at.ac.tuwien.ifs.tita.ui.administration.project.ProjectAdministrationPage;
import at.ac.tuwien.ifs.tita.ui.administration.user.UserAdministrationPage;
import at.ac.tuwien.ifs.tita.ui.evaluation.timeconsumer.DailyViewPage;
import at.ac.tuwien.ifs.tita.ui.evaluation.timeconsumer.MonthlyViewPage;
import at.ac.tuwien.ifs.tita.ui.evaluation.timecontroller.MultipleProjectsView;
import at.ac.tuwien.ifs.tita.ui.evaluation.timecontroller.PerformanceOfPersonView;
import at.ac.tuwien.ifs.tita.ui.evaluation.timecontroller.TargetActualView;
import at.ac.tuwien.ifs.tita.ui.importing.effort.csv.EffortImportCSVPage;
import at.ac.tuwien.ifs.tita.ui.login.TitaSession;
import at.ac.tuwien.ifs.tita.ui.startpages.AdminPage;
import at.ac.tuwien.ifs.tita.ui.startpages.EffortsPage;
import at.ac.tuwien.ifs.tita.ui.startpages.ProjectsPage;
import at.ac.tuwien.ifs.tita.ui.tasklist.TaskListPanel;
import at.ac.tuwien.ifs.tita.ui.utils.TimerCoordinator;

/**
 * BaseClass for sites.
 * 
 * @author rene
 * 
 */
public class BasePage extends SecureWebPage {

    @SpringBean(name = "userService")
    private IUserService userService;

    @SpringBean(name = "timerCoordinator")
    private TimerCoordinator timerCoordinator;

    @SpringBean(name = "titaEffortService")
    private IEffortService effortService;

    @SpringBean(name = "taskService")
    private ITaskService taskService;

    @Resource(name = "projectService")
    private IProjectService projectService;

    private TaskListPanel panel;

    public BasePage() {
        initLogoutLink();

        String username = TitaSession.getSession().getUsername();
        add(new Label("showUser", "Signed in as " + username));

        final WebMarkupContainer timeConsumergroup = new WebMarkupContainer("timeConsumerGroup");
        final WebMarkupContainer timeControllergroup = new WebMarkupContainer("timeControllerGroup");
        final WebMarkupContainer administratorGroup = new WebMarkupContainer("administratorGroup");

        addAdminLinks(administratorGroup);
        addTimeConsumerLinks(timeConsumergroup);
        addTimeControllerLinks(timeControllergroup);

        add(timeConsumergroup);
        add(timeControllergroup);
        add(administratorGroup);
    }

    public void setPanel(TaskListPanel panel) {
        this.panel = panel;
    }

    /**
     * Adds the secure Links for TimeController.
     * 
     * @param timeControllergroup - container
     */
    private void addTimeControllerLinks(WebMarkupContainer timeControllergroup) {
        timeControllergroup.add(new SecurePageLink("projectsPageLink", ProjectsPage.class));
        timeControllergroup.add(new SecurePageLink("multipleProjectsViewLink", MultipleProjectsView.class));
        timeControllergroup.add(new SecurePageLink("targetActualViewLink", TargetActualView.class));
        timeControllergroup.add(new SecurePageLink("performanceOfPersonViewLink", PerformanceOfPersonView.class));
    }

    /**
     * Adds the secure Links for TimeConsumers.
     * 
     * @param timeConsumergroup - container
     */
    private void addTimeConsumerLinks(WebMarkupContainer timeConsumergroup) {
        timeConsumergroup.add(new SecurePageLink("effortsPageLink", EffortsPage.class));
        timeConsumergroup.add(new SecurePageLink("dailyViewPageLink", DailyViewPage.class));
        timeConsumergroup.add(new SecurePageLink("monthlyViewPageLink", MonthlyViewPage.class));
        timeConsumergroup.add(new SecurePageLink("effortsImportCSVPageLink", EffortImportCSVPage.class));

    }

    /**
     * Adds the secure Links for TimeController.
     * 
     * @param administratorGroup - container
     */
    private void addAdminLinks(WebMarkupContainer administratorGroup) {
        administratorGroup.add(new SecurePageLink("adminPageLink", AdminPage.class));
        administratorGroup.add(new SecurePageLink("userAdministrationLink", UserAdministrationPage.class));
        administratorGroup.add(new SecurePageLink("projectAdministrationLink", ProjectAdministrationPage.class));

    }

    /**
     * initialize the page.
     */
    @SuppressWarnings("unchecked")
    protected void initLogoutLink() {
        // not a secure link because everyone can logoff.
        Link logoff = new Link("logoff") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                TitaSession titaSession = TitaSession.getSession();
                if (titaSession.logoff(getLogoffContext())) {
                    try {
                        TiTAUser user = userService.getUserByUsername(TitaSession.getSession().getUsername());
                        List<ActiveTaskId> activeTasks = timerCoordinator.getActiveTasks(user.getId());

                        for (ActiveTaskId at : activeTasks) {
                            Effort e = timerCoordinator.stopIssueTimer(user.getId(), at);
                            ITaskTrackable task = taskService.getIssueTrackerTaskById(at.getIssueId(), at
                                    .getIssueTProjetId(), at.getIssueTrackerId());

                            effortService.saveIssueTrackerTaskEfforts(e, at.getIssueId(), at.getIssueTProjetId(), task
                                    .getDescription(), user, at.getTitaProjectId());

                        }

                        // ActiveTaskEffort ate =
                        // timerCoordinator.findTiTATaskForUser(user.getId());
                        // if (ate != null && panel != null) {
                        // Effort e =
                        // timerCoordinator.stopTiTATimer(user.getId());
                        // effortService.saveEffortForTiTATask(e,
                        // panel.getGeneralTimer().getDescription(), user,
                        // panel.getProject());
                        // }

                        timerCoordinator.unregisterUser(user.getId());
                        activeTasks = timerCoordinator.getActiveTasks(user.getId());

                    } catch (Exception e) {
                        error("couldn't log out - user didn't exist in database");
                    }
                    setResponsePage(Application.get().getHomePage());
                    titaSession.invalidate();
                } else {
                    error("A problem occured during the logoff process, please " + "try again or contact support");
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
        throw new WicketRuntimeException("Application is not a subclass of " + TiTAApplication.class);
    }
}
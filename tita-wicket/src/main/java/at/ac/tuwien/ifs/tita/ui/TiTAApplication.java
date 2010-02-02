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

import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.security.hive.HiveMind;
import org.apache.wicket.security.hive.authentication.LoginContext;
import org.apache.wicket.security.hive.config.PolicyFileHiveFactory;
import org.apache.wicket.security.hive.config.SwarmPolicyFileHiveFactory;
import org.apache.wicket.security.swarm.SwarmWebApplication;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.file.Folder;

import at.ac.tuwien.ifs.tita.business.security.TiTASecurity;
import at.ac.tuwien.ifs.tita.business.service.project.IProjectService;
import at.ac.tuwien.ifs.tita.business.service.tasks.ITaskService;
import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;
import at.ac.tuwien.ifs.tita.entity.conv.ProjectStatus;
import at.ac.tuwien.ifs.tita.entity.conv.Role;
import at.ac.tuwien.ifs.tita.ui.importing.effort.csv.EffortImportCSVPage;
import at.ac.tuwien.ifs.tita.ui.login.TitaLoginContext;
import at.ac.tuwien.ifs.tita.ui.login.TitaSession;
import at.ac.tuwien.ifs.tita.ui.utils.TimerCoordinator;

/**
 * Wicket Application for testing Hello World from DB.
 */
public class TiTAApplication extends SwarmWebApplication {

    private static final Long C_ONE = 1L;
    private static final Long C_TWO = 2L;
    private static final Long C_THREE = 3L;

    // the service for DB-Operations
    @SpringBean(name = "titaProjectService")
    private IProjectService titaProjectService;

    @SpringBean(name = "userService")
    private IUserService userService;

    @SpringBean(name = "taskService")
    private ITaskService taskService;

    @SpringBean(name = "timerCoordinator")
    private TimerCoordinator timerCoordinator;

    private Folder uploadFolder = null;

    public TiTAApplication() {
    }

    /** {@inheritDoc} */
    @Override
    protected void init() {
        // THIS LINE IS IMPORTANT - IT INSTALLS THE COMPONENT INJECTOR THAT WILL
        // INJECT NEWLY CREATED COMPONENTS WITH THEIR SPRING DEPENDENCIES
        super.init();
        addComponentInstantiationListener(new SpringComponentInjector(this));
        InjectorHolder.getInjector().inject(this);

        uploadFolder = new Folder(System.getProperty("java.io.tmpdir"), "wicket-uploads");
        // Ensure folder exists
        uploadFolder.mkdirs();

        // try {
        // initDatabase();
        // } catch (NoSuchAlgorithmException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        mountBookmarkablePage("/single", EffortImportCSVPage.class);
        new Thread(timerCoordinator).start();
    }

    /**
     * Init database if the values are not available.
     *
     * @throws NoSuchAlgorithmException
     *             n
     */
    private void initDatabase() throws NoSuchAlgorithmException {

        Role admin = new Role(C_ONE, "Administrator");
        Role timeconsumer = new Role(C_TWO, "Time Consumer");
        Role timecontroller = new Role(C_THREE, "Time Controller");

        IssueTracker issueTracker = new IssueTracker(C_ONE, "Mantis-OpenEngSB",
                "http://localhost/mantisbt-1.1.8");
        ProjectStatus open = new ProjectStatus(C_ONE, "open");
        ProjectStatus closed = new ProjectStatus(C_TWO, "closed");

        TiTAUser user1 = new TiTAUser("admin", TiTASecurity.calcHash("admin"), "Andreas", "Pieber",
                "anpi@gmx.at", false,
                admin, null, null);
        TiTAUser user2 = new TiTAUser("timecontroller", TiTASecurity.calcHash("timecontroller"),
                "Andreas", "Pieber",
                "anpi@gmx.at", false, timecontroller, null, null);
        TiTAUser user3 = new TiTAUser("timeconsumer", TiTASecurity.calcHash("timeconsumer"),
                "Christoph", "Zehetner",
                "christoph.zehetner@gmx.at", false, timeconsumer, null, null);


        IssueTracker i = taskService.getIssueTrackerById(issueTracker.getId());

        if (taskService.getIssueTrackerById(issueTracker.getId()) != null) {
            // taskService.deleteIssueTracker(issueTracker);
        }
        taskService.saveAndFlushIssueTracker(issueTracker);

        List a = titaProjectService.findAllTiTAProjects();

        if (titaProjectService.findAllTiTAProjects().contains(open)) {
            titaProjectService.deleteProjectStatus(open);
        }
        titaProjectService.saveAndFlushProjectStatus(open);

        if (titaProjectService.findAllTiTAProjects().contains(closed)) {
            titaProjectService.deleteProjectStatus(closed);
        }
        titaProjectService.saveAndFlushProjectStatus(closed);

        if (userService.getRoles().contains(admin)) {
            userService.deleteRole(admin);
        }
        userService.saveAndFlushRole(admin);

        if (userService.getRoles().contains(timeconsumer)) {
            userService.deleteRole(timeconsumer);
        }
        userService.saveAndFlushRole(timeconsumer);

        if (userService.getRoles().contains(timecontroller)) {
            userService.deleteRole(timecontroller);
        }
        userService.saveAndFlushRole(timecontroller);

        List u = userService.getAvailableTiTAUser();

        if (userService.getAvailableTiTAUser().contains(user1)) {
            userService.deleteUser(user1);
        }
        userService.saveAndFlushUser(user1);

        if (userService.getAvailableTiTAUser().contains(user2)) {
            userService.deleteUser(user2);
        }
        userService.saveAndFlushUser(user2);

        if (userService.getAvailableTiTAUser().contains(user3)) {
            userService.deleteUser(user3);
        }
        userService.saveAndFlushUser(user3);
    }

    /**
     * Gets current homepage of application.
     *
     * @return homepage of app
     */
    @Override
    public Class<BasePage> getHomePage() {
        return BasePage.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object getHiveKey() {
        return getServletContext().getRealPath(CONTEXTPATH);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUpHive() {
        PolicyFileHiveFactory factory = new SwarmPolicyFileHiveFactory(getActionFactory());

        try {
            factory.addPolicyFile(getServletContext().getResource("/WEB-INF/tita.hive"));
            factory.setAlias("hp", "at.ac.tuwien.ifs.tita.ui.BasePage");

         // Aliases for Admin
            factory.setAlias("userAdministration",
                    "at.ac.tuwien.ifs.tita.ui.administration.user.UserAdministrationPage");
            factory.setAlias("projectAdministration",
                    "at.ac.tuwien.ifs.tita.ui.administration.project.ProjectAdministrationPage");


            // Aliases for timeconsumer
            factory.setAlias("effortsPage", "at.ac.tuwien.ifs.tita.ui.startpages.EffortsPage");
            factory.setAlias("dailyView", "at.ac.tuwien.ifs.tita.ui.evaluation.timeconsumer.DailyViewPage");
            factory.setAlias("monthlyView", "at.ac.tuwien.ifs.tita.ui.evaluation.timeconsumer.MonthlyViewPage");
            factory.setAlias("effortImportCSV", "at.ac.tuwien.ifs.tita.ui.importing.effort.csv.EffortImportCSVPage");

            // Aliases for timecontroller
            factory.setAlias("projectsPage", "at.ac.tuwien.ifs.tita.ui.startpages" + ".ProjectsPage");
            factory.setAlias("multipleProjectsView", "at.ac.tuwien.ifs.tita.ui.evaluation"
                    + ".timecontroller.MultipleProjectsView");
            factory.setAlias("targetActualView", "at.ac.tuwien.ifs.tita.ui.evaluation"
                    + ".timecontroller.TargetActualView");

            factory.setAlias("performanceOfPersonView", "at.ac.tuwien.ifs.tita.ui.evaluation"
                    + ".timecontroller.PerformanceOfPersonView");

            // Aliases for admin
            factory.setAlias("adminPage", "at.ac.tuwien.ifs.tita.ui.startpages" + ".AdminPage");

        } catch (MalformedURLException e) {
            throw new WicketRuntimeException(e);
        }

        // register factory
        HiveMind.registerHive(getHiveKey(), factory);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<HomePage> getLoginPage() {
        return HomePage.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session newSession(Request request, Response response) {
        return new TitaSession(this, request);
    }

    /**
     * {@inheritDoc}
     */
    public LoginContext getLogoffContext() {
        return new TitaLoginContext();
    }

    /**
     * Return the upload folder.
     *
     * @return the folder for uploads
     */
    public Folder getUploadFolder() {
        return uploadFolder;
    }
}

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

import at.ac.tuwien.ifs.tita.ui.importing.effort.csv.EffortImportCSVPage;
import at.ac.tuwien.ifs.tita.ui.login.TitaLoginContext;
import at.ac.tuwien.ifs.tita.ui.login.TitaSession;
import at.ac.tuwien.ifs.tita.ui.utils.TimerCoordinator;

/**
 * Wicket Application for testing Hello World from DB.
 */
public class TiTAApplication extends SwarmWebApplication {
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

        mountBookmarkablePage("/single", EffortImportCSVPage.class);
        new Thread(timerCoordinator).start();
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

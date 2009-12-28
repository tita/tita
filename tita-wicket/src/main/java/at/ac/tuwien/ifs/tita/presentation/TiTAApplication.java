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

import java.net.MalformedURLException;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.security.hive.HiveMind;
import org.apache.wicket.security.hive.config.PolicyFileHiveFactory;
import org.apache.wicket.security.hive.config.SwarmPolicyFileHiveFactory;
import org.apache.wicket.security.swarm.SwarmWebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

import at.ac.tuwien.ifs.tita.presentation.login.TitaLoginContext;

/**
 * Wicket Application for testing Hello World from DB.
 */
public class TiTAApplication extends SwarmWebApplication {
    
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

    }

    /**
     * Gets current homepage of application.
     * 
     * @return homepage of app
     */
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
            factory.setAlias("hp", "at.ac.tuwien.ifs.tita.presentation.BasePage");
            
            //Aliases for Admin
            
            //Aliases for timeconsumer
            factory.setAlias("effortsPage", 
                    "at.ac.tuwien.ifs.tita.presentation.effort.EffortsPage");
            factory.setAlias("dailyView", 
                    "at.ac.tuwien.ifs.tita.presentation.effort.evaluation.timeconsumer.DailyView");
            factory.setAlias("monthlyView", 
                  "at.ac.tuwien.ifs.tita.presentation.effort.evaluation.timeconsumer.MonthlyView");
            
            
            //Aliases for timecontroller
            factory.setAlias("multipleProjectsView", 
                "at.ac.tuwien.ifs.tita.presentation.effort.evaluation" +
                ".timecontroller.MultipleProjectsView");
      
        } catch (MalformedURLException e) {
            throw new WicketRuntimeException(e);
        }
        
        //register factory
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
    public TitaLoginContext getLogoffContext(){
        return new TitaLoginContext();
    }
}

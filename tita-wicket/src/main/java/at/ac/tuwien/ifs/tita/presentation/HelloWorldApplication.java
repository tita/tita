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

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

/**
 * Wicket Application for testing Hello World from DB.
 */
public class HelloWorldApplication extends WebApplication {
    
    public HelloWorldApplication() {
    }

    /** {@inheritDoc} */
    @Override
    protected void init() {
        // THIS LINE IS IMPORTANT - IT INSTALLS THE COMPONENT INJECTOR THAT WILL
        // INJECT NEWLY CREATED COMPONENTS WITH THEIR SPRING DEPENDENCIES
        addComponentInstantiationListener(new SpringComponentInjector(this));
    }

    /**
     * Gets current homepage of application.
     * 
     * @return homepage of app
     */
    public Class<HomePage> getHomePage() {
        return HomePage.class;
    }

}

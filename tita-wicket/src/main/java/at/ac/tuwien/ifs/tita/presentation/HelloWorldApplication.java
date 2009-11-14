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

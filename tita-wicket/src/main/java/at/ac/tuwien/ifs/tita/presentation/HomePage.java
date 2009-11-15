package at.ac.tuwien.ifs.tita.presentation;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import at.ac.tuwien.ifs.tita.datasource.domain.Role;
import at.ac.tuwien.ifs.tita.datasource.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.datasource.service.IUserService;

/**
 * Homepage of Hello World Application.
 */
public class HomePage extends WebPage {

    @SpringBean(name = "userService")
    private IUserService service;

    public HomePage() {
        displayHello();
    }

    /**
     * Puts "Hello World" in DB and reads it afterwards.
     */
    private void displayHello() {
        Role helloWorld = initPage();

        try {
            Role temp = service.saveRole(helloWorld);
            helloWorld = null;
            helloWorld = service.getRoleById(temp.getId());
            add(new Label("message", helloWorld.getDescription()));
        } catch (TitaDAOException e) {
            add(new Label("message", "Couldn't read data from DB."));
        }
    }

    /**
     * Initialise homepage data.
     * 
     * @return Role "Hello World"
     */
    private Role initPage() {
        Role r = new Role();
        r.setDescription("Hello World.");
        return r;
    }
}

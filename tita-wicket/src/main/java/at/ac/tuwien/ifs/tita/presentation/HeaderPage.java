package at.ac.tuwien.ifs.tita.presentation;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.TextField;

/**
 * Class for dynamic header information.
 * 
 * @author rene
 * 
 */
public class HeaderPage extends BasePage {

    public HeaderPage() {
        String username = "User1";
        add(new Label("showUser", "Signed in as " + username));

        final WebMarkupContainer timeConsumergroup = new WebMarkupContainer("timeConsumerGroup");
        final WebMarkupContainer timeControllergroup = new WebMarkupContainer("timeControllerGroup");
        final WebMarkupContainer administratorGroup = new WebMarkupContainer("administratorGroup");
        add(timeConsumergroup);
        add(timeControllergroup);
        add(administratorGroup);

        TextField<String> searchField = new TextField<String>("searchField");
        add(searchField);
        add(new Button("searchButton"));
    }
}

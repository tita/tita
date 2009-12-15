package at.ac.tuwien.ifs.tita.presentation;

import org.apache.wicket.markup.html.basic.Label;

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
    }
}

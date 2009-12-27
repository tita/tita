package at.ac.tuwien.ifs.tita.presentation;

import org.apache.wicket.Application;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.value.ValueMap;

/**
 * Panel to authenticate user.
 * @author Karin
 *
 */
public abstract class SignInPanel extends Panel{

    public SignInPanel(final String id) {
        super(id);
        add(new SignInForm("signInForm").setOutputMarkupId(false));
    }

    /**
     * The actual login process.
     * 
     * @param username - name
     * @param password - pwd
     * @return true, if the login was successful, false otherwise
     */
    public abstract boolean signIn(String username, String password);

    /**
     * Sign in form.
     */
    public final class SignInForm extends StatelessForm {
        private static final long serialVersionUID = 1L;

        /**
         * Constructor.
         * 
         * @param id
         *            id of the form component
         */
        public SignInForm(final String id) {
            // sets a compound model on this form, every component without an
            // explicit model will use this model too
            super(id, new CompoundPropertyModel(new ValueMap()));

            // only remember username, not passwords
            add(new TextField("username").setOutputMarkupId(false));
            add(new PasswordTextField("password").setOutputMarkupId(false));
        }

       
        /**
         * .
         * @see org.apache.wicket.markup.html.form.Form#onSubmit()
         */
        public void onSubmit() {
           
                // delete persistent data
            getPage().removePersistedFormData(SignInForm.class, true);


            ValueMap values = (ValueMap)getModelObject();
            String username = values.getString("username");
            String password = values.getString("password");

            if (signIn(username, password)) {
                // continue or homepage?
                if (!getPage().continueToOriginalDestination()) {
                    setResponsePage(Application.get().getHomePage());
                }
            }  else {
                // Try the component based localizer first. If not found try the
                // application localizer. Else use the default
                error(getLocalizer().getString("exception.login", this,
                        "Illegal username password combo"));
            }
        }

    }
}

package at.ac.tuwien.ifs.tita.ui.uihelper;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

/**
 * 
 * Component ValidationTextField extends the standard wicket component for
 * validation handling.
 * 
 * @author msiedler
 * 
 * @param <T>
 */
public class ValidationTextField<T> extends TextField<T> {

    public ValidationTextField(String id) {
        super(id);
    }

    public ValidationTextField(String id, IModel<T> model) {
        super(id, model);
    }

    /**
     * Set Textfield class attribute to valid.
     */
    public void setTextFieldValid() {
        add(new SimpleAttributeModifier("class", "valid"));
    }

    /**
     * Set Textfield class attribute to invalid.
     */
    public void setTextFieldInvalid() {
        add(new SimpleAttributeModifier("class", "invalid"));
    }
}

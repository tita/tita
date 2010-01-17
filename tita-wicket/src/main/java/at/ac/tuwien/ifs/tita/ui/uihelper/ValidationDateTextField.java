package at.ac.tuwien.ifs.tita.ui.uihelper;

import java.util.Date;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.model.IModel;

/**
 * 
 * Component ValidationDateTextField extends the standard wicket component for
 * validation handling.
 * 
 * @author msiedler
 * 
 */
public class ValidationDateTextField extends DateTextField {

    public ValidationDateTextField(String id, IModel<Date> model,
            DateConverter converter) {
        super(id, model, converter);
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

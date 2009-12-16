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
package at.ac.tuwien.ifs.tita.presentation.uihelper;

import java.util.Date;

import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;

/**
 * 
 * DateTextField class which sets automatic tags.
 * 
 * @author msiedler
 * 
 */
public class LenientDateTextField extends DateTextField {

    public LenientDateTextField(String id, IModel<Date> model,
            DateConverter converter) {
        super(id, model, converter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onComponentTag(final ComponentTag tag) {
        tag.setName("input");
        tag.put("type", "text");
        super.onComponentTag(tag);
    }
}
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

import org.apache.wicket.Component;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.wicketstuff.table.SelectableListItem;
import org.wicketstuff.table.cell.CellEditor;
import org.wicketstuff.table.cell.CellRender;

import at.ac.tuwien.ifs.tita.presentation.utils.GlobalUtils;

/**
 * 
 * Renders effort date column.
 * 
 * @author msiedler
 * 
 */
public class DateTextFieldRenderer implements CellEditor, CellRender {

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Component getEditorComponent(String id, IModel model,
            SelectableListItem parent, int row, int column) {

        LenientDateTextField teDate = new LenientDateTextField(id, model,
                new StyleDateConverter("S-", true)) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("class", "dateCell");
            }
        };
        teDate.add(new DatePicker());
        return teDate;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Component getRenderComponent(String id, IModel model,
            SelectableListItem parent, int row, int column) {
        IModel<String> newModel = (IModel<String>) model;
        newModel.setObject(GlobalUtils.DATEFORMAT.format(model.getObject()));
        return new Label(id, newModel);
    }

}

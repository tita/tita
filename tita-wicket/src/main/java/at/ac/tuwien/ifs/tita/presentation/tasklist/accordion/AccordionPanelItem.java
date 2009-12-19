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
package at.ac.tuwien.ifs.tita.presentation.tasklist.accordion;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * The AccordionPanelItem can be appended to the Accordion and support ether a
 * list of elements of one Panel or a List of Panels.
 * 
 * @author Christoph
 * 
 */
public class AccordionPanelItem extends Panel {

    public static final String ITEM_ID = "item";

    @SuppressWarnings("unchecked")
    public AccordionPanelItem(String title, List<WebMarkupContainer> items) {
        super(ITEM_ID);
        add(new Label("title", title));
        add(new ListView("content", items) {

            @Override
            protected void populateItem(ListItem item) {
                WebMarkupContainer child = (WebMarkupContainer) item
                        .getModelObject();
                item.add(child);

            }
        });
    }

    @SuppressWarnings("unchecked")
    public AccordionPanelItem(String title,
            List<List<WebMarkupContainer>> items, boolean multipleLists) {
        super(ITEM_ID);
        add(new Label("title", title));

        Boolean mulLists = multipleLists;
        for (int i = 0; i < items.size(); i++) {          
            
            add(new ListView("content", items.get(i)) {
                @Override
                protected void populateItem(ListItem item) {
                    WebMarkupContainer child = (WebMarkupContainer) item
                            .getModelObject();
                    item.add(child);
                }
            });
        }

    }

}

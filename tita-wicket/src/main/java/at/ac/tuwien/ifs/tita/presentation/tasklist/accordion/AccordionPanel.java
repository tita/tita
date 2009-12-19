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

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

/**
 * The Accordion Panel defines a special UI Element, that can add Accordion
 * Panel Items.
 * 
 * @author Christoph
 * 
 */

public class AccordionPanel extends Panel implements IHeaderContributor {

    protected List<AccordionPanelItem> accordionMenu = new ArrayList<AccordionPanelItem>();

    private final ResourceReference javascript = new CompressedResourceReference(
            AccordionPanel.class, "accordion-menu-v2.js");
    private ResourceReference style = new CompressedResourceReference(AccordionPanel.class,
            "accordion-menu-v2.css");

    /**
     * Constructor defined from wicket stuff.
     * 
     * @param id
     */
    public AccordionPanel(String id) {
        super(id);
        add(new ListView("accordionMenu", accordionMenu) {
            @Override
            protected void populateItem(ListItem item) {
                AccordionPanelItem accordionPanelItem = (AccordionPanelItem) item.getModelObject();
                item.add(accordionPanelItem);

            }
        });

    }

    /**
     * Constructor for using a different style.
     * 
     * @param id
     * @param style
     */
    public AccordionPanel(String id, ResourceReference style) {
        this(id);
        this.style = style;
    }

    /**
     * Adding a accordion panel item.
     * 
     * @param accordionPanelItem
     *            - from type AccordionPanelItem
     */
    public void addMenu(AccordionPanelItem accordionPanelItem) {
        accordionMenu.add(accordionPanelItem);
    }

    /**
     * Remove all accordion panel items.
     */
    public void removeAllAccordionPanelItems() {
        accordionMenu.removeAll(accordionMenu);
    }

    /** {@inheritDoc} */
    @Override
    public void renderHead(IHeaderResponse response) {

        response.renderJavascriptReference("http://yui.yahooapis.com/2.4.1/build/yahoo/yahoo.js");
        response.renderJavascriptReference("http://yui.yahooapis.com/2.4.1/build/event/event.js");
        response.renderJavascriptReference("http://yui.yahooapis.com/2.4.1/build/dom/dom.js");
        response
                .renderJavascriptReference("http://yui.yahooapis.com/2.4.1/build/animation/animation.js");
        response.renderJavascriptReference(javascript);
        response.renderCSSReference(style);
    };

}

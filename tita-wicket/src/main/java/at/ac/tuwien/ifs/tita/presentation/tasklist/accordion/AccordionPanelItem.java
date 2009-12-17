package at.ac.tuwien.ifs.tita.presentation.tasklist.accordion;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * 
 * @author Christoph
 * 
 */
public class AccordionPanelItem extends Panel {

    public final static String ITEM_ID = "item";

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

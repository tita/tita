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
 * 
 * @author Christoph
 *
 */
public class AccordionPanel extends Panel implements IHeaderContributor {

	protected List<AccordionPanelItem> accordionMenu = new ArrayList<AccordionPanelItem>();

	private final ResourceReference JAVASCRIPT = new CompressedResourceReference(
			AccordionPanel.class, "accordion-menu-v2.js");
	private ResourceReference STYLE = new CompressedResourceReference(
			AccordionPanel.class, "accordion-menu-v2.css");

	/**
	 * minimal constructor
	 * @param id
	 */
	public AccordionPanel(String id) {
		super(id);
		add(new ListView("accordionMenu", accordionMenu) {
			@Override
			protected void populateItem(ListItem item) {
				AccordionPanelItem accordionPanelItem = (AccordionPanelItem) item
						.getModelObject();
				item.add(accordionPanelItem);

			}
		});

	}
	/**
	 * using a different style
	 * @param id
	 * @param style
	 */
	public AccordionPanel(String id, ResourceReference style)
	{
		this(id);
		STYLE=style;
	}

	public void addMenu(AccordionPanelItem accordionPanelItem) {
		accordionMenu.add(accordionPanelItem);
	}
	
	public void removeAllAccordionPanelItems() {
	    accordionMenu.removeAll(accordionMenu);
	}

	public void renderHead(IHeaderResponse response) {

		response
				.renderJavascriptReference("http://yui.yahooapis.com/2.4.1/build/yahoo/yahoo.js");
		response
				.renderJavascriptReference("http://yui.yahooapis.com/2.4.1/build/event/event.js");
		response
				.renderJavascriptReference("http://yui.yahooapis.com/2.4.1/build/dom/dom.js");
		response
				.renderJavascriptReference("http://yui.yahooapis.com/2.4.1/build/animation/animation.js");
		response.renderJavascriptReference(JAVASCRIPT);
		response.renderCSSReference(STYLE);
	};

}

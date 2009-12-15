package at.ac.tuwien.ifs.tita.presentation.effort;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.presentation.HeaderPage;

/**
 * Main Area for Efforts.
 * 
 * @author rene
 * 
 */
public class EffortsPage extends HeaderPage {
    private List<ITab> tabs = new ArrayList<ITab>();
    private List<TiTAProject> projects;

    public EffortsPage() {
        setDefaultModel(new Model<String>("effortsTabbedPanel"));

        projects = new ArrayList<TiTAProject>();

        initTempProjects();
        initTabbedPanels();
        add(new TabbedPanel("effortsTabbedPanel", tabs).add(new AttributeModifier("class", true, EffortsPage.this
                .getDefaultModel())));
    }

    /**
     * Creates temporary Projects. TODO: Remove Later
     */
    private void initTempProjects() {
        TiTAProject p1 = new TiTAProject();
        TiTAProject p2 = new TiTAProject();
        TiTAProject p3 = new TiTAProject();
        p1.setName("Project 1");
        p2.setName("Project 2");
        p3.setName("Project 3");
        projects.add(p1);
        projects.add(p2);
        projects.add(p3);
    }

    /**
     * Inits TabbedPanels.
     */
    private void initTabbedPanels() {
        for (final TiTAProject p : projects) {
            tabs.add(new AbstractTab(new Model<String>(p.getName())) {

                @Override
                public Panel getPanel(String panelId) {
                    return new TimeEffortAdministrationPanel(panelId, p);
                }

            });
        }
    }
}

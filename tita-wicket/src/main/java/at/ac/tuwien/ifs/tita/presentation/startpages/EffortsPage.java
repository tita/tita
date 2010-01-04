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
package at.ac.tuwien.ifs.tita.presentation.startpages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.presentation.BasePage;
import at.ac.tuwien.ifs.tita.presentation.controls.panel.AdministrationPanelEffort;

/**
 * Start Page for Time consumer - Main Area for Efforts.
 * 
 * @author rene
 * 
 */
public class EffortsPage extends BasePage {
    private final List<ITab> tabs = new ArrayList<ITab>();
    private final List<TiTAProject> projects;

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
        TiTAProject p1 = new TiTAProject("Project 1", "Project 1", false, null, null, null);
        TiTAProject p2 = new TiTAProject("Project 2", "Project 1", false, null, null, null);
        TiTAProject p3 = new TiTAProject("Project 3", "Project 1", false, null, null, null);
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
                    return new AdministrationPanelEffort(panelId, p);
                }

            });
        }
    }
}

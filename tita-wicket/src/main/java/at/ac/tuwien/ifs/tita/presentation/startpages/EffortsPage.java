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
import org.apache.wicket.spring.injection.annot.SpringBean;

import at.ac.tuwien.ifs.tita.business.service.project.IProjectService;
import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.presentation.BasePage;
import at.ac.tuwien.ifs.tita.presentation.controls.panel.AdministrationPanelEffort;
import at.ac.tuwien.ifs.tita.presentation.login.TitaSession;
import at.ac.tuwien.ifs.tita.presentation.tasklist.TaskListPanel;

/**
 * Start Page for Time consumer - Main Area for Efforts.
 * 
 * @author rene
 * 
 */
public class EffortsPage extends BasePage {
    @SpringBean (name = "titaProjectService")
    private IProjectService titaProService;
    
    @SpringBean (name = "userService")
    private IUserService userService;
   
    private TiTAUser user;
    private final List<ITab> tabs = new ArrayList<ITab>();
    private final List<TiTAProject> projects;
    private TaskListPanel taskListPanel;
    
    public EffortsPage() {
        setDefaultModel(new Model<String>("effortsTabbedPanel"));
        projects = new ArrayList<TiTAProject>();
        try{
            user = userService.getUserByUsername(TitaSession.getSession().getUsername());
        }catch (TitaDAOException e) {
            throw new RuntimeException("Couldn't find user currently logged in.", e);
        }
        initTempProjects();
        initTabbedPanels();
        add(new TabbedPanel("effortsTabbedPanel", tabs).add(new AttributeModifier("class", true, 
                                                            this.getDefaultModel())));
        taskListPanel =  new TaskListPanel("taskList",projects.get(0));
        add(taskListPanel);
    }

    /**
     * Initialize user's tita projects.
     */
    private void initTempProjects() {
        List<TiTAProject> tips = titaProService.findTiTAProjectsForUser(user);
        
        if (tips != null){
            for(TiTAProject tip : tips){
                projects.add(tip);
            }
        }
    }

    /**
     * Inits TabbedPanels.
     */
    private void initTabbedPanels() {
        for (final TiTAProject p : projects) {
            tabs.add(new AbstractTab(new Model<String>(p.getName())) {

                @Override
                public Panel getPanel(String panelId) {
                    taskListPanel.loadIssueTrackerTasks(p);
                    return new AdministrationPanelEffort(panelId, p);
                }
            });
        }
    }
}

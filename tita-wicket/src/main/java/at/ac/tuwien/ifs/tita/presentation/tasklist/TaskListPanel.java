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
package at.ac.tuwien.ifs.tita.presentation.tasklist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.Model;
import org.apache.wicket.security.components.markup.html.panel.SecurePanel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.business.service.tasks.ITaskService;
import at.ac.tuwien.ifs.tita.business.service.time.IEffortService;
import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueStatus;
import at.ac.tuwien.ifs.tita.issuetracker.exceptions.ProjectNotFoundException;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;
import at.ac.tuwien.ifs.tita.presentation.login.TitaSession;
import at.ac.tuwien.ifs.tita.presentation.tasklist.accordion.AccordionPanel;
import at.ac.tuwien.ifs.tita.presentation.tasklist.accordion.AccordionPanelItem;
import at.ac.tuwien.ifs.tita.presentation.tasklist.stopwatch.TaskTimerPanel;

/**
 * The TaskListPanel defines the position and the user interface design to
 * manage the TaskTimer Panels.
 * 
 * @author Christoph
 * @author herbert
 * 
 */
public class TaskListPanel extends SecurePanel implements IHeaderContributor {
    private final Logger log = LoggerFactory.getLogger(TaskListPanel.class);

    @SpringBean(name = "taskService")
    private ITaskService taskService;
    
    @SpringBean(name = "userService")
    private IUserService userService;
    
    @SpringBean(name = "timeEffortService")
    private IEffortService effortService;
    
    private TiTAUser user;
    private TiTAProject project;
    private WebMarkupContainer containerTaskList = null;
    private final Form tasklistForm;
    private final List<String> groupingList;
    private boolean groupingIssueTracker = true;
    private AccordionPanel accordionPanel = new AccordionPanel("accordionMenu");
    private final ResourceReference style = new CompressedResourceReference(
            TaskListPanel.class, "tasklist.css");
    private DropDownChoice<String> dropDownView;
    
    public TaskListPanel(String id, TiTAProject titaProject) {
        super(id);
        
        groupingList = Arrays.asList(new String[] { "Groups by task state",
                "Group by issuetracker" });
        
        containerTaskList = new WebMarkupContainer("tasklistContainer");
        containerTaskList.setOutputMarkupId(true);
        containerTaskList.setOutputMarkupPlaceholderTag(true);
        add(containerTaskList);

        tasklistForm = new Form("taskListForm");
        add(tasklistForm);
        
        displayHeader();
        try{
            this.project = titaProject;
            user = userService.getUserByUsername(TitaSession.getSession().getUsername());
            createIssueListForTiTAProjectAndUser(project, user);
        }catch (TitaDAOException e) {
            throw new RuntimeException("Couldn't find user currently logged in.", e);
        }
    }
    
    
    
    /**
     * Shows the header and configuration for the tasklist.
     */
    private void displayHeader() {
        
        dropDownView = new DropDownChoice<String>(
                "dropdownGrouping", new Model<String>(), groupingList) {
            
            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }

            
            /**
             * Called when a option is selected of a dropdown list that wants to be
             * notified of this event.
             *
             * @param newSelection The newly selected object of the backing model
             */
            @Override
            protected void onSelectionChanged(final String newSelection) {

            }
        };

        dropDownView.setOutputMarkupId(true);
        dropDownView.setDefaultModelObject(groupingList.get(0));
        dropDownView.setNullValid(false);

        dropDownView.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                createIssueListForTiTAProjectAndUser(project, user);
                target.addComponent(containerTaskList);
            }
        });

        tasklistForm.add(dropDownView);
        tasklistForm.add(new Label("dummy", ""));
        tasklistForm.add(new AjaxButton("updateTaskList") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {

            }
        });

    }

    /**
     * Returns, if order of the issues is by issue state or not.
     * @return true, if order by state
     */
    private Boolean isOrderByState(){
        String o = dropDownView.getModel().getObject();
        if("Groups by task state".equals(o)){
            return true;
        }
        return false;
    }
    
    /**
     * Generates a part of an accordion panel consisting of caption and issues.
     * @param caption String 
     * @param issueMap Map with issues of specified issue state or issue tracker
     *
     */
    private void generateAccordionDisplaySection(String caption,Map<Long, ITaskTrackable> issueMap){
        List<List<WebMarkupContainer>> markupItems = new ArrayList<List<WebMarkupContainer>>();
        markupItems.add(generateTaskPanel(issueMap));
        AccordionPanelItem accordionPanelItem = new AccordionPanelItem(caption, markupItems, true);
        accordionPanel.addMenu(accordionPanelItem);
    }
    
    /**
     * Creates the proper list of issues comming from issue tracker(s) and displays it in tita.
     * @param project TiTAProject of current selected tita project
     * @param titauser TiTAUser of logged in user
     */
    public void createIssueListForTiTAProjectAndUser(TiTAProject titaProject, TiTAUser titaUser){
        Map<Long, ITaskTrackable> issueMap = null;
      
        this.project = titaProject;
        this.user = titaUser;
        
        try {
            taskService.fetchTaskFromIssueTrackerProjects(project.getId(), user.getId());
            accordionPanel.removeAllAccordionPanelItems();
            if(isOrderByState()){
                issueMap = taskService.sortingTasksByIssueStatus(IssueStatus.NEW);
                generateAccordionDisplaySection("New", issueMap);
                issueMap = taskService.sortingTasksByIssueStatus(IssueStatus.ASSIGNED);
                generateAccordionDisplaySection("Assigned", issueMap);
                issueMap = taskService.sortingTasksByIssueStatus(IssueStatus.CLOSED);
                generateAccordionDisplaySection("Closed", issueMap);
            }else{
                issueMap = taskService.sortingTasksByIssueTracker("www.mantis.com");
                generateAccordionDisplaySection("Mantis", issueMap);
            }
            accordionPanel.setOutputMarkupId(true);
            tasklistForm.add(accordionPanel);
            containerTaskList.add(tasklistForm);
        } catch (ProjectNotFoundException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Helper Method for producing dummies.
     * @param projectId Long of current selected tita project
     * @param userId Long of logged in user
     * @return listOfTaskTimer
     */
    private List<WebMarkupContainer> generateTaskPanel(Map<Long, ITaskTrackable> issueMap) {
        List<WebMarkupContainer> listOfTaskTimer = new ArrayList<WebMarkupContainer>();
        Iterator<ITaskTrackable> tasks;
        ITaskTrackable task;
        Long effort = null;
        
        if(issueMap.values() != null){
            tasks = issueMap.values().iterator();
            while(tasks.hasNext()){
                task = tasks.next();
                //TODO: issue tracker id should be fixed and read from db
                effort = effortService.findEffortsForIssueTrackerTask(project, user, 
                                                        task.getProject().getId(), task.getId(),1L);
                listOfTaskTimer.add(new TaskTimerPanel(AccordionPanelItem.ITEM_ID, task, effort));
            }
        }
        return listOfTaskTimer;
    }
    
    /** {@inheritDoc} */
    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference(style);
    }
    
}

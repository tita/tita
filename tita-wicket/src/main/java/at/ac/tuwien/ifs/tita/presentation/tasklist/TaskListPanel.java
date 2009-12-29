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
import java.util.List;

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

import at.ac.tuwien.ifs.tita.presentation.tasklist.accordion.AccordionPanel;
import at.ac.tuwien.ifs.tita.presentation.tasklist.accordion.AccordionPanelItem;
import at.ac.tuwien.ifs.tita.presentation.tasklist.stopwatch.TaskTimerPanel;

/**
 * The TaskListPanel defines the position and the user interface design to
 * manage the TaskTimer Panels.
 * 
 * @author Christoph
 * 
 */
public class TaskListPanel extends SecurePanel implements IHeaderContributor {

    private WebMarkupContainer containerTaskList = null;

    private final Form tasklistForm;
    
    private final List<String> groupingList;
    
    private boolean groupingIssueTracker = true;
    
    private AccordionPanel accordionPanel = new AccordionPanel("accordionMenu");
    
    private final int dummyvalue2 = 2;
    private final int dummyvalue3 = 3;
    private final int dummyvalue4 = 4;

    private final ResourceReference style = new CompressedResourceReference(
            TaskListPanel.class, "tasklist.css");
    
    
    private List<WebMarkupContainer> listOfTaskTimer;

    public TaskListPanel(String id) {
        super(id);
        
        groupingList = Arrays.asList(new String[] { "Gruppen nach Task Status",
                "Gruppen nach Issuetracker" });
        
        containerTaskList = new WebMarkupContainer("tasklistContainer");
        containerTaskList.setOutputMarkupId(true);
        containerTaskList.setOutputMarkupPlaceholderTag(true);
        add(containerTaskList);

        tasklistForm = new Form("taskListForm");
        add(tasklistForm);
        
        displayHeader();
        accordionPanel = displayAccordionOrderByStatus();
    }

    /**
     * Shows the header and configuration for the tasklist.
     */
    private void displayHeader() {
        
        DropDownChoice<String> dropDownView = new DropDownChoice<String>(
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
                              
                if(groupingIssueTracker) {
                    displayPanelOrderByIssueTracker();
                    target.addComponent(containerTaskList);
                    groupingIssueTracker = false;
                } else {
                    displayAccordionOrderByStatus();
                    target.addComponent(containerTaskList);
                    groupingIssueTracker = true;
                }
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
     * Returns the accordion order by status.
     * 
     * @return accordion - the Panel that includes all states of the defined
     *         interface of the issuetracker.
     */
    private AccordionPanel displayAccordionOrderByStatus() {
        
        accordionPanel.removeAllAccordionPanelItems();

        List<List<WebMarkupContainer>> markupItems = new ArrayList<List<WebMarkupContainer>>();
        markupItems.add(getListOfTaskTimer(2));
        AccordionPanelItem accordionPanelItem = new AccordionPanelItem("New", markupItems, true);
        accordionPanel.addMenu(accordionPanelItem);
        
        markupItems = new ArrayList<List<WebMarkupContainer>>();
        markupItems.add(getListOfTaskTimer(2));
        accordionPanelItem = new AccordionPanelItem("Assigned", markupItems, true);
        accordionPanel.addMenu(accordionPanelItem);
        
        markupItems = new ArrayList<List<WebMarkupContainer>>();
        markupItems.add(getListOfTaskTimer(dummyvalue3));
        accordionPanelItem = new AccordionPanelItem("Closed", markupItems, true);
        accordionPanel.addMenu(accordionPanelItem);
        
        accordionPanel.setOutputMarkupId(true);
        tasklistForm.add(accordionPanel);
        containerTaskList.add(tasklistForm);
        
        return accordionPanel;
    }

    /**
     * Helper Method for producing dummies.
     * 
     * @param numberOfTaskTimer
     *            - defines the number of TaskTimer Panels.
     * @return listOfTaskTimer
     */
    private List<WebMarkupContainer> getListOfTaskTimer(int numberOfTaskTimer) {
        
        listOfTaskTimer = new ArrayList<WebMarkupContainer>();
        for(int i=0; i<numberOfTaskTimer; i++) {
            listOfTaskTimer.add(new TaskTimerPanel(AccordionPanelItem.ITEM_ID));
            
        }
    
        return listOfTaskTimer;
    }
    
    /**
     * Returns the accordion order by issuetracker.
     * 
     * @return accordion - the Panel that includes all issuetracker the user has
     *         defined.
     */
    private AccordionPanel displayPanelOrderByIssueTracker() {
        
        accordionPanel.removeAllAccordionPanelItems();

        List<List<WebMarkupContainer>> markupItems = new ArrayList<List<WebMarkupContainer>>();
        markupItems.add(getListOfTaskTimer(dummyvalue4));
        AccordionPanelItem accordionPanelItem = new AccordionPanelItem("Mantis", markupItems, true);
        accordionPanel.addMenu(accordionPanelItem);
        
        markupItems = new ArrayList<List<WebMarkupContainer>>();
        markupItems.add(getListOfTaskTimer(dummyvalue3));
        accordionPanelItem = new AccordionPanelItem("Github", markupItems, true);
        accordionPanel.addMenu(accordionPanelItem);
        
        accordionPanel.setOutputMarkupId(true);
        tasklistForm.add(accordionPanel);
        containerTaskList.add(tasklistForm);
        
        return accordionPanel;
    }
    
    /** {@inheritDoc} */
    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference(style);
    }
    
}

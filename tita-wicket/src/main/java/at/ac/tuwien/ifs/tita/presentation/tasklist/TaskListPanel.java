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
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.Model;

import at.ac.tuwien.ifs.tita.presentation.tasklist.accordion.AccordionPanel;
import at.ac.tuwien.ifs.tita.presentation.tasklist.accordion.AccordionPanelItem;
import at.ac.tuwien.ifs.tita.presentation.tasklist.stopwatch.TaskTimerPanel;

public class TaskListPanel extends Panel implements IHeaderContributor {

    private WebMarkupContainer containerTaskList = null;

    private final Form tasklistForm;
    
    private String selectedGroup;
    private final List<String> groupingList;
    
    private boolean groupingIssueTracker = true;
    
    private AccordionPanel accordionPanel = new AccordionPanel("accordionMenu");
    

    private final ResourceReference style = new CompressedResourceReference(
            TaskListPanel.class, "tasklist.css");
    
    
    private List<WebMarkupContainer> listOfTaskTimer;

    public TaskListPanel(String id) {
        super(id);
        
        groupingList = Arrays.asList(new String[] {
                "Gruppen nach Task Status", "Gruppen nach Issuetracker" });
        
        containerTaskList = new WebMarkupContainer("tasklistContainer");
        containerTaskList.setOutputMarkupId(true);
        containerTaskList.setOutputMarkupPlaceholderTag(true);
        add(containerTaskList);

        tasklistForm = new Form("taskListForm");
        add(tasklistForm);
        
        displayHeader();
        accordionPanel = displayAccordionOrderByStatus();
    }
    
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
                String test = newSelection;
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
                }
                else {
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
        markupItems.add(getListOfTaskTimer(3));
        accordionPanelItem = new AccordionPanelItem("Closed", markupItems, true);
        accordionPanel.addMenu(accordionPanelItem);
        
        accordionPanel.setOutputMarkupId(true);
        tasklistForm.add(accordionPanel);
        containerTaskList.add(tasklistForm);
        
        return accordionPanel;
    }

    private List<WebMarkupContainer> getListOfTaskTimer(int numberOfTaskTimer) {
        
        listOfTaskTimer = new ArrayList<WebMarkupContainer>();
        for(int i=0; i<numberOfTaskTimer; i++) {
            listOfTaskTimer.add(new TaskTimerPanel(AccordionPanelItem.ITEM_ID));
            
        }
    
        return listOfTaskTimer;
    }
    
    private AccordionPanel displayPanelOrderByIssueTracker() {
        
        accordionPanel.removeAllAccordionPanelItems();

        List<List<WebMarkupContainer>> markupItems = new ArrayList<List<WebMarkupContainer>>();
        markupItems.add(getListOfTaskTimer(4));
        AccordionPanelItem accordionPanelItem = new AccordionPanelItem("Mantis", markupItems, true);
        accordionPanel.addMenu(accordionPanelItem);
        
        markupItems = new ArrayList<List<WebMarkupContainer>>();
        markupItems.add(getListOfTaskTimer(3));
        accordionPanelItem = new AccordionPanelItem("Github", markupItems, true);
        accordionPanel.addMenu(accordionPanelItem);
        
        accordionPanel.setOutputMarkupId(true);
        tasklistForm.add(accordionPanel);
        containerTaskList.add(tasklistForm);
        
        return accordionPanel;
    }
    
    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference(style);
    }
    
}

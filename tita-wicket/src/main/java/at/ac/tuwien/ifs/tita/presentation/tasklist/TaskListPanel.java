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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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

import at.ac.tuwien.ifs.tita.business.service.project.IProjectService;
import at.ac.tuwien.ifs.tita.business.service.tasks.ITaskService;
import at.ac.tuwien.ifs.tita.business.service.time.IEffortService;
import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.util.ActiveTaskId;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueStatus;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueTrackingTool;
import at.ac.tuwien.ifs.tita.issuetracker.exceptions.ProjectNotFoundException;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;
import at.ac.tuwien.ifs.tita.presentation.login.TitaSession;
import at.ac.tuwien.ifs.tita.presentation.tasklist.accordion.AccordionPanel;
import at.ac.tuwien.ifs.tita.presentation.tasklist.accordion.AccordionPanelItem;
import at.ac.tuwien.ifs.tita.presentation.tasklist.stopwatch.AssignedTaskTimerPanel;
import at.ac.tuwien.ifs.tita.presentation.tasklist.stopwatch.ClosedTaskTimerPanel;
import at.ac.tuwien.ifs.tita.presentation.tasklist.stopwatch.GeneralTimerPanel;
import at.ac.tuwien.ifs.tita.presentation.tasklist.stopwatch.NewTaskTimerPanel;
import at.ac.tuwien.ifs.tita.presentation.utils.TimerCoordinator;

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
    //TODO: constants should be refactored
    private static final String C_HOST = "http://localhost";
    private static final Long C_ISSUE_TRACKER_ID = 1L;
    @SpringBean(name = "taskService")
    private ITaskService taskService;

    @SpringBean(name = "userService")
    private IUserService userService;

    @SpringBean(name = "titaProjectService")
    private IProjectService projectService;
    
    @SpringBean(name = "timeEffortService")
    private IEffortService effortService;
    
    @SpringBean(name ="timerCoordinator")
    private TimerCoordinator timerCoordinator;

    private TiTAUser user;
    private TiTAProject project;
    private WebMarkupContainer containerTaskList = null;
    private final Form<Object> tasklistForm;
    private final List<String> groupingList;
    private AccordionPanel accordionPanel = new AccordionPanel("accordionMenu");
    private final ResourceReference style = new CompressedResourceReference(
            TaskListPanel.class, "tasklist.css");
    private DropDownChoice<String> dropDownView;
    private Map<Long, NewTaskTimerPanel> newTasks;
    private Map<Long, AssignedTaskTimerPanel> assignedTasks;
    private Map<Long, ClosedTaskTimerPanel> closedTasks;
    private GeneralTimerPanel generalTimer;
    
    public TaskListPanel(String id, TiTAProject titaProject) {
        super(id);
        newTasks = new TreeMap<Long, NewTaskTimerPanel>();
        assignedTasks = new TreeMap<Long, AssignedTaskTimerPanel>();
        closedTasks = new TreeMap<Long, ClosedTaskTimerPanel>();
        groupingList = Arrays.asList(new String[] { "Groups by task state",
                "Group by issuetracker" });

        containerTaskList = new WebMarkupContainer("tasklistContainer");
        containerTaskList.setOutputMarkupId(true);
        containerTaskList.setOutputMarkupPlaceholderTag(true);
        add(containerTaskList);

        tasklistForm = new Form<Object>("taskListForm");
        add(tasklistForm);

        displayHeader();
        try{
            project = titaProject;
            user = userService.getUserByUsername(TitaSession.getSession().getUsername());
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
                loadIssueTrackerTasks(project);
                target.addComponent(containerTaskList);
            }
        });

        tasklistForm.add(dropDownView);
        tasklistForm.add(new Label("dummy", ""));
        tasklistForm.add(new AjaxButton("updateTaskList") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                loadIssueTrackerTasks(project);
                target.addComponent(containerTaskList);
            }
        });
//        general timer panel
        generalTimer = new GeneralTimerPanel("generalTimer", this);
        tasklistForm.add(generalTimer);
    }

    public void resetPanelLists(){
        newTasks.clear();
        assignedTasks.clear();
        closedTasks.clear();
    }
    
    public void loadIssueTrackerTasks(TiTAProject tp){
        this.project = tp;
        
        //clear all accordion items
        accordionPanel.removeAllAccordionPanelItems();
        try{
            taskService.fetchTaskFromIssueTrackerProjects(project.getId(), user.getId());
            if(isOrderByState()){
                generateIssueList(taskService.sortingTasksByIssueStatus(IssueStatus.NEW));
                generateIssueList(taskService.sortingTasksByIssueStatus(IssueStatus.ASSIGNED));
                generateIssueList(taskService.sortingTasksByIssueStatus(IssueStatus.CLOSED));
            }else{
                generateIssueList(taskService.sortingTasksByIssueTracker(C_HOST));
            }
            updateAccordion();
        } catch (ProjectNotFoundException e) {
            log.error(e.getMessage());
        }
    }
    
    private void generateIssueList(Map<Long, ITaskTrackable> issueMap){
        Iterator<Long> keys;
        Long effort = 0L, taskId = null;
        IssueStatus state = null;
                
        if(!issueMap.isEmpty()){
            keys = issueMap.keySet().iterator();
            while(keys.hasNext()){
                Long key = keys.next();
                ITaskTrackable task = issueMap.get(key);
                effort = readEffortFromDb(task.getProject().getId(), task.getId());
                state = task.getStatus();
                taskId = task.getId();
                if(state.equals(IssueStatus.NEW)){
                    if(!newTasks.containsKey(taskId)){
                        newTasks.put(taskId, new NewTaskTimerPanel(
                                AccordionPanelItem.ITEM_ID, task, this));
                    }
                }else if(state.equals(IssueStatus.ASSIGNED)){
                    if(!assignedTasks.containsKey(taskId)){
                        assignedTasks.put(taskId, new AssignedTaskTimerPanel(
                                                  AccordionPanelItem.ITEM_ID, task, effort, this));
                    }
                }else if(state.equals(IssueStatus.CLOSED)){
                    if(!closedTasks.containsKey(taskId)){
                        closedTasks.put(taskId, new ClosedTaskTimerPanel(
                                            AccordionPanelItem.ITEM_ID, task, effort));
                    }
                }
            }
            setRunningTasks();
        }    
    }
    
    private void setRunningTasks(){
        List<ActiveTaskId> active = timerCoordinator.getActiveTasks(user.getId());
        
        for(ActiveTaskId at : active){
            if(assignedTasks.containsKey(at.getIssueId())){
                assignedTasks.get(at.getIssueId()).setTaskStarted();
            }
        }
    }
    
    private Long readEffortFromDb(Long issueTrackerProjectId, Long taskId){
        return effortService.findEffortsForIssueTrackerTask(project, user, 
                issueTrackerProjectId, taskId,C_ISSUE_TRACKER_ID);
    }
        
    public void startTimerForIssue(ITaskTrackable task){
        timerCoordinator.startIssueTimer(user.getId(), 
                                         new ActiveTaskId(project.getId(), C_ISSUE_TRACKER_ID,
                                         task.getProject().getId(), task.getId()));
    }
    
    public void stopTimerForIssue(ITaskTrackable task){
        stopIssueTimer(task);
        Long duration = readEffortFromDb(task.getProject().getId(), task.getId());
        synchronized(assignedTasks){
            AssignedTaskTimerPanel pan = assignedTasks.get(task.getId());
            pan.setEffort(duration);
        }
    }
    
    private void stopIssueTimer(ITaskTrackable task){
        Effort effort = timerCoordinator.stopIssueTimer(user.getId(), 
                new ActiveTaskId(project.getId(), C_ISSUE_TRACKER_ID,
                task.getProject().getId(), task.getId()));
        if(effort != null){
            saveIssueTrackerTaskEfforts(effort, task);
        }
    }
    
    public void startGeneralTimerForTask(){
        timerCoordinator.startTiTATimer(user.getId());
    }
    
    public void stopGeneralTimerForTask(){
        Effort eff = timerCoordinator.stopTiTATimer(user.getId());
        generalTimer.setEffort(eff);
    }
    
    public void saveEffortForTiTATask(Effort effort){
        if(effort != null){
            effort.setDescription(null);
            effort.setUser(user);
            try{
                Set<Effort> effs = new HashSet<Effort>();
                effs.add(effort);
                TiTATask tt = new TiTATask(generalTimer.getDescription(), user, project, effs);
                projectService.saveTiTATask(tt);
                effort.setTitaTask(tt);
                effortService.saveEffort(effort);
            }catch(TitaDAOException ex){
                log.error("couldn't save tita effort with description: " + effort.getDescription());
            }
        }
    }
    
    private void saveIssueTrackerTaskEfforts(Effort effort, ITaskTrackable task){
//      persist issue tracker task anOd effort and read it from db to get actual effort value
        IssueTrackerTask itt = projectService.findIssueTrackerTaskForTiTAProject(project.getId(), 
                                       C_ISSUE_TRACKER_ID, task.getProject().getId(), task.getId());
        effort.setTitaTask(null);
        effort.setDescription(null);
        effort.setUser(user); 
        
        if(itt != null){
            effort.setIssueTTask(itt);
        }else{
            IssueTrackerProject tempProject = projectService.findIssueTrackerProjectForTiTAProject(
                                                    project.getId(), C_ISSUE_TRACKER_ID, 
                                                    task.getProject().getId());
            Set<Effort> eff = new HashSet<Effort>();
            eff.add(effort);
            itt = new IssueTrackerTask(tempProject, task.getId(), task.getDescription(), eff);
            projectService.saveIssueTrackerTask(itt);
            effort.setIssueTTask(itt);
            Set<IssueTrackerTask> tasks = new HashSet<IssueTrackerTask>();
            tasks.add(itt);
            tempProject.setIssueTrackerTasks(tasks);
        }
        try{
            effortService.saveEffort(effort);
        }catch(TitaDAOException ex){
            log.error("Couldn't save effort tita project id: " + project.getId() + 
                      " issuetracker id: " + C_ISSUE_TRACKER_ID + " issue tracker project id: " +
                      task.getProject().getId() + " issue tracker task id : " + task.getId());
        }
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
    
    private void updateAccordion(){
        if(isOrderByState()){
            addToAccordion("NEW", generateNewTaskPanel());
            addToAccordion("ASSIGNED", generateAssignedTaskPanel());
            addToAccordion("CLOSED", generateClosedTaskPanel());
        }else{
            addToAccordion("Mantis",generateIssueTrackerPanels(IssueTrackingTool.MANTIS));
        }
        accordionPanel.setOutputMarkupId(true);
        tasklistForm.setOutputMarkupId(true);
        tasklistForm.setOutputMarkupPlaceholderTag(true);
        tasklistForm.add(accordionPanel);
        containerTaskList.add(tasklistForm);
    }
    
    private List<WebMarkupContainer> generateNewTaskPanel(){
        List<WebMarkupContainer> listOfTaskTimer = new ArrayList<WebMarkupContainer>();
        Iterator<Long> keys = newTasks.keySet().iterator();
        
        while(keys.hasNext()){
            Long key = keys.next();
            listOfTaskTimer.add(newTasks.get(key));
        }
        return listOfTaskTimer;
    }
    
    private List<WebMarkupContainer> generateAssignedTaskPanel(){
        List<WebMarkupContainer> listOfTaskTimer = new ArrayList<WebMarkupContainer>();
        Iterator<Long> keys = assignedTasks.keySet().iterator();
        
        while(keys.hasNext()){
            Long key = keys.next();
            listOfTaskTimer.add(assignedTasks.get(key));
        }
        return listOfTaskTimer;
    }
    
    private List<WebMarkupContainer> generateClosedTaskPanel(){
        List<WebMarkupContainer> listOfTaskTimer = new ArrayList<WebMarkupContainer>();
        Iterator<Long> keys = closedTasks.keySet().iterator();
        
        while(keys.hasNext()){
            Long key = keys.next();
            listOfTaskTimer.add(closedTasks.get(key));
        }
        return listOfTaskTimer;
    }
    
    private void addToAccordion(String caption, List <WebMarkupContainer> panels){
        List<List<WebMarkupContainer>> markupItems = new ArrayList<List<WebMarkupContainer>>();
        markupItems.add(panels);
        AccordionPanelItem accordionPanelItem = new AccordionPanelItem(caption, markupItems, true);
        accordionPanel.addMenu(accordionPanelItem);
    }
    
    private List<WebMarkupContainer> generateIssueTrackerPanels(IssueTrackingTool tool){
        List<WebMarkupContainer> listOfTaskTimer = new ArrayList<WebMarkupContainer>();

        listOfTaskTimer.addAll(iterateThroughNewTasks(tool));
        listOfTaskTimer.addAll(iterateThroughAssignedTasks(tool));
        listOfTaskTimer.addAll(iterateThroughClosedTasks(tool));
        
        return listOfTaskTimer;
    }
    
    private List<WebMarkupContainer> iterateThroughNewTasks(IssueTrackingTool tool){
        List<WebMarkupContainer> listOfTaskTimer = new ArrayList<WebMarkupContainer>();
        Iterator<Long> keys = newTasks.keySet().iterator();
        
        while(keys.hasNext()){
            Long key = keys.next();
            if(newTasks.get(key).getTask().getIssueTrackerType().equals(tool)){
                listOfTaskTimer.add(newTasks.get(key));
            }
        }
        return listOfTaskTimer;
    }

    private List<WebMarkupContainer> iterateThroughAssignedTasks(IssueTrackingTool tool){
        List<WebMarkupContainer> listOfTaskTimer = new ArrayList<WebMarkupContainer>();
        Iterator<Long> keys = assignedTasks.keySet().iterator();
        
        while(keys.hasNext()){
            Long key = keys.next();
            if(assignedTasks.get(key).getTask().getIssueTrackerType().equals(tool)){
                listOfTaskTimer.add(assignedTasks.get(key));
            }
        }
        return listOfTaskTimer;
    }
    
    private List<WebMarkupContainer> iterateThroughClosedTasks(IssueTrackingTool tool){
        List<WebMarkupContainer> listOfTaskTimer = new ArrayList<WebMarkupContainer>();
        Iterator<Long> keys = closedTasks.keySet().iterator();
        
        while(keys.hasNext()){
            Long key = keys.next();
            if(closedTasks.get(key).getTask().getIssueTrackerType().equals(tool)){
                listOfTaskTimer.add(closedTasks.get(key));
            }
        }
        return listOfTaskTimer;
    }

    /** {@inheritDoc} */
    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference(style);
    }

    /**
     * Methode to close a task in tita and mantis.
     * @param task ITaskTrackable
     * @param target AjaxRequestTarget
     */
    public void closeTask(ITaskTrackable task, AjaxRequestTarget target){
        taskService.closeTask(task.getId());
        closedAssignedTask(task);
        target.addComponent(containerTaskList);
    }
    
    private void closedAssignedTask(ITaskTrackable task){
        Long effort = null;
        if(assignedTasks.containsKey(task.getId())){
            assignedTasks.remove(task.getId());
            effort = readEffortFromDb(task.getProject().getId(), task.getId());
            closedTasks.put(task.getId(), new ClosedTaskTimerPanel(
                                                AccordionPanelItem.ITEM_ID, task, effort));
            //very important!! otherwise wicket can't update webcontainer!!!
            accordionPanel.removeAllAccordionPanelItems();
            updateAccordion();
        }
    }

    /**
     * Methode to assign a new task of mantis.
     * @param task ITaskTrackable
     * @param target AjaxRequestTarget
     */
    public void assignTask(ITaskTrackable task, AjaxRequestTarget target){
        taskService.assignTask(task.getId());
        assginNewTaskInAssignPanel(task);
        target.addComponent(containerTaskList);
    }
    
    private void assginNewTaskInAssignPanel(ITaskTrackable task){
        Long effort = null;
        if(newTasks.containsKey(task.getId())){
            newTasks.remove(task.getId());
            effort = readEffortFromDb(task.getProject().getId(), task.getId());
            assignedTasks.put(task.getId(), new AssignedTaskTimerPanel(
                                                AccordionPanelItem.ITEM_ID, task, effort, this));
            //very important!! otherwise wicket can't update webcontainer!!!
            accordionPanel.removeAllAccordionPanelItems();
            updateAccordion();
        }
    }
}

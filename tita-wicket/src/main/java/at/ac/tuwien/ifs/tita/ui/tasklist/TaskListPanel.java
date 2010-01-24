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
package at.ac.tuwien.ifs.tita.ui.tasklist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.PersistenceException;

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
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.util.ActiveTaskId;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueStatus;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueTrackingTool;
import at.ac.tuwien.ifs.tita.issuetracker.exceptions.ProjectNotFoundException;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;
import at.ac.tuwien.ifs.tita.ui.controls.panel.AdministrationPanelEffort;
import at.ac.tuwien.ifs.tita.ui.login.TitaSession;
import at.ac.tuwien.ifs.tita.ui.tasklist.accordion.AccordionPanel;
import at.ac.tuwien.ifs.tita.ui.tasklist.accordion.AccordionPanelItem;
import at.ac.tuwien.ifs.tita.ui.tasklist.stopwatch.AssignedTaskTimerPanel;
import at.ac.tuwien.ifs.tita.ui.tasklist.stopwatch.ClosedTaskTimerPanel;
import at.ac.tuwien.ifs.tita.ui.tasklist.stopwatch.GeneralTimerPanel;
import at.ac.tuwien.ifs.tita.ui.tasklist.stopwatch.NewTaskTimerPanel;
import at.ac.tuwien.ifs.tita.ui.utils.TimerCoordinator;

/**
 * The TaskListPanel defines the position and the user interface design to
 * manage the TaskTimer Panels.
 *
 * @author Christoph
 * @author herbert
 *
 */
public class TaskListPanel extends SecurePanel implements IHeaderContributor {

    //TODO: constants should be refactored
    private static final String C_HOST = "http://localhost";
    private static final Long C_ISSUE_TRACKER_ID = 1L;

    private final Logger log = LoggerFactory.getLogger(TaskListPanel.class);

    @SpringBean(name = "taskService")
    private ITaskService taskService;

    @SpringBean(name = "userService")
    private IUserService userService;

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
    private AdministrationPanelEffort ape;
    
    public TaskListPanel(String id, TiTAProject titaProject) {
        super(id);
        newTasks = new TreeMap<Long, NewTaskTimerPanel>();
        assignedTasks = new TreeMap<Long, AssignedTaskTimerPanel>();
        closedTasks = new TreeMap<Long, ClosedTaskTimerPanel>();
        groupingList = Arrays.asList(
                new String[] { "Groups by task state", "Group by issuetracker" });

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
        } catch (PersistenceException e) {
//            throw new RuntimeException("Couldn't find user currently logged in.", e);
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

    /**
     * Reset all task panels.
     */
    public void resetPanelLists(){
        newTasks.clear();
        assignedTasks.clear();
        closedTasks.clear();
    }
    
    

    public TiTAProject getProject() {
        return project;
    }

    /**
     * Load all tasks from mantis.
     * @param tp TiTAProject
     */
    public void loadIssueTrackerTasks(TiTAProject tp){
        project = tp;

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

    /**
     * Generates from issue tracker map panel lists sorted by status or issue
     * tracker name.
     *
     * @param issueMap
     *            map of trackable tasks
     */
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

    /**
     * Sets the currently running task for user, if he switched project before.
     */
    private void setRunningTasks(){
        List<ActiveTaskId> active = timerCoordinator.getActiveTasks(user.getId());

        for(ActiveTaskId at : active){
            if(assignedTasks.containsKey(at.getIssueId())){
                assignedTasks.get(at.getIssueId()).setTaskStarted();
            }
        }
    }

    /**
     * Read for an issue tracker project and task all efforts from db.
     * @param issueTrackerProjectId Long
     * @param taskId Long
     * @return Effort
     */
    private Long readEffortFromDb(Long issueTrackerProjectId, Long taskId){
        return effortService.findEffortsSumForIssueTrackerTask(project, user,
                issueTrackerProjectId, taskId,C_ISSUE_TRACKER_ID);
    }

    /**
     * Starts timer for issue tracker tasks.
     * @param task ITaskTrackable
     */
    public void startTimerForIssue(ITaskTrackable task){
        timerCoordinator.startIssueTimer(user.getId(),
                                         new ActiveTaskId(project.getId(), C_ISSUE_TRACKER_ID,
                                         task.getProject().getId(), task.getId()));
    }

    /**
     * Stops timer for issue tracker tasks.
     * @param task ITaskTrackable
     * @param target AjaxRequestTarget
     */
    public void stopTimerForIssue(ITaskTrackable task, AjaxRequestTarget target){
        stopIssueTimer(task, target);
        Long duration = readEffortFromDb(task.getProject().getId(), task.getId());
        synchronized(assignedTasks){
            AssignedTaskTimerPanel pan = assignedTasks.get(task.getId());
            pan.setEffort(duration);
        }
    }

    /**
     * Method that stops issue timer and saves its effort in db.
     * @param task ITaskTrackable
     * @param target AjaxRequestTarget
     */
    private void stopIssueTimer(ITaskTrackable task, AjaxRequestTarget target){
        Effort effort = timerCoordinator.stopIssueTimer(user.getId(),
                new ActiveTaskId(project.getId(), C_ISSUE_TRACKER_ID,
                task.getProject().getId(), task.getId()));
        if(effort != null){
            saveIssueTrackerTaskEfforts(effort, task, target);
        }
    }

    /**
     * Starts general timer for tita user task.
     */
    public void startGeneralTimerForTask(){
        timerCoordinator.startTiTATimer(user.getId());
    }

    /**
     * Stops general timer for tita user task.
     */
    public void stopGeneralTimerForTask(){
        Effort eff = timerCoordinator.stopTiTATimer(user.getId());
        generalTimer.setEffort(eff);
    }
    
    

    public GeneralTimerPanel getGeneralTimer() {
        return generalTimer;
    }

    /**
     * Saves an effort for tita task generated by some tita user.
     * @param effort Effort
     * @param target AjaxRequestTarget
     */
    public void saveEffortForTiTATask(Effort effort, AjaxRequestTarget target){
        
        effortService.saveEffortForTiTATask(effort, generalTimer.getDescription(), 
                user, project);
//        effortService.clear();
        ape.reloadTable(target);
//        if(effort != null){
//            effort.setDescription(generalTimer.getDescription());
//            effort.setUser(user);
//            try{
//                Set<Effort> effs = new HashSet<Effort>();
//                effs.add(effort);
//                TiTATask tt = new TiTATask(generalTimer.getDescription(), user, project, effs);
//                projectService.saveTiTATask(tt);
//                effort.setTitaTask(tt);
//                effortService.saveEffort(effort);
//                ape.reloadTable(target);
//            } catch (PersistenceException ex) {
//                log.error("couldn't save tita effort with description: " + effort.getDescription());
//            }
//        }
    }

    /**
     * Saves an effort for a issue tracker task.
     * @param effort Effort
     * @param task ITaskTrackable
     * @param target AjaxRequestTarget
     */
    private void saveIssueTrackerTaskEfforts(Effort effort, ITaskTrackable task, 
                                             AjaxRequestTarget target){
        
        effortService.saveIssueTrackerTaskEfforts(effort, task, user, project);
        ape.reloadTable(target);
        
        
        
////      persist issue tracker task anOd effort and read it from db to get actual effort value
//        IssueTrackerTask itt = projectService.findIssueTrackerTaskForTiTAProject(project.getId(),
//                                       C_ISSUE_TRACKER_ID, task.getProject().getId(), task.getId());
//        effort.setTitaTask(null);
//        effort.setDescription(null);
//        effort.setUser(user);
//
//        if(itt != null){
//            effort.setIssueTTask(itt);
//        }else{
//            IssueTrackerProject tempProject = projectService.findIssueTrackerProjectForTiTAProject(
//                                                    project.getId(), C_ISSUE_TRACKER_ID,
//                                                    task.getProject().getId());
//            Set<Effort> eff = new HashSet<Effort>();
//            eff.add(effort);
//            itt = new IssueTrackerTask(tempProject, task.getId(), task.getDescription(), eff);
//            projectService.saveIssueTrackerTask(itt);
//            effort.setIssueTTask(itt);
//            Set<IssueTrackerTask> tasks = new HashSet<IssueTrackerTask>();
//            tasks.add(itt);
//            tempProject.setIssueTrackerTasks(tasks);
//        }
//        try{
//            effortService.saveEffort(effort);
//            ape.reloadTable(target);
//        } catch (PersistenceException ex) {
//            log.error("Couldn't save effort tita project id: " + project.getId() +
//                      " issuetracker id: " + C_ISSUE_TRACKER_ID + " issue tracker project id: " +
//                      task.getProject().getId() + " issue tracker task id : " + task.getId());
//        }
    }

    
    /**
     * Sets current administration panel for efforts.
     * @param ap AdministrationPanelEffort
     */
    public void setAdministrationPanelEffort(AdministrationPanelEffort ap){
        this.ape = ap;
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
     * Updates accordion panel and generates task lists ordered by status or issue tracker.
     */
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

    /**
     * Generates a list for all new tasks comming from issue tracker.
     * @return List of WebMarkupContainer
     */
    private List<WebMarkupContainer> generateNewTaskPanel(){
        List<WebMarkupContainer> listOfTaskTimer = new ArrayList<WebMarkupContainer>();
        Iterator<Long> keys = newTasks.keySet().iterator();

        while(keys.hasNext()){
            Long key = keys.next();
            listOfTaskTimer.add(newTasks.get(key));
        }
        return listOfTaskTimer;
    }

    /**
     * Generates a list for all assigned tasks comming from issue tracker.
     * @return List of WebMarkupContainer
     */
    private List<WebMarkupContainer> generateAssignedTaskPanel(){
        List<WebMarkupContainer> listOfTaskTimer = new ArrayList<WebMarkupContainer>();
        Iterator<Long> keys = assignedTasks.keySet().iterator();

        while(keys.hasNext()){
            Long key = keys.next();
            listOfTaskTimer.add(assignedTasks.get(key));
        }
        return listOfTaskTimer;
    }

    /**
     * Generates a list for all closed tasks comming from issue tracker.
     * @return List of WebMarkupContainer
     */
    private List<WebMarkupContainer> generateClosedTaskPanel(){
        List<WebMarkupContainer> listOfTaskTimer = new ArrayList<WebMarkupContainer>();
        Iterator<Long> keys = closedTasks.keySet().iterator();

        while(keys.hasNext()){
            Long key = keys.next();
            listOfTaskTimer.add(closedTasks.get(key));
        }
        return listOfTaskTimer;
    }

    /**
     * Adds a list of task panels to accordion.
     * @param caption String
     * @param panels List of WebMarkupContainer
     */
    private void addToAccordion(String caption, List <WebMarkupContainer> panels){
        List<List<WebMarkupContainer>> markupItems = new ArrayList<List<WebMarkupContainer>>();
        markupItems.add(panels);
        AccordionPanelItem accordionPanelItem = new AccordionPanelItem(caption, markupItems, true);
        accordionPanel.addMenu(accordionPanelItem);
    }

    /**
     * Generate all issue tracker task panels for adding them to accordion menu.
     * @param tool IssueTrackingTool
     * @return List of WebMarkupContainer
     */
    private List<WebMarkupContainer> generateIssueTrackerPanels(IssueTrackingTool tool){
        List<WebMarkupContainer> listOfTaskTimer = new ArrayList<WebMarkupContainer>();

        listOfTaskTimer.addAll(iterateThroughNewTasks(tool));
        listOfTaskTimer.addAll(iterateThroughAssignedTasks(tool));
        listOfTaskTimer.addAll(iterateThroughClosedTasks(tool));

        return listOfTaskTimer;
    }

    /**
     * Generate a list of new task panels sorted by issue tracker.
     * @param tool IssueTrackingTool
     * @return  List of WebMarkupContainer
     */
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

    /**
     * Generate a list of assigned task panels sorted by issue tracker.
     * @param tool IssueTrackingTool
     * @return  List of WebMarkupContainer
     */
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

    /**
     * Generate a list of closed task panels sorted by issue tracker.
     * @param tool IssueTrackingTool
     * @return  List of WebMarkupContainer
     */
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

    /**
     * Close an issue tracker task in tita and specific issue tracker.
     * @param task ITaskTrackable
     */
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

    /**
     * Assign a new task in tita comming from issue tracker.
     * @param task ITaskTrackable
     */
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

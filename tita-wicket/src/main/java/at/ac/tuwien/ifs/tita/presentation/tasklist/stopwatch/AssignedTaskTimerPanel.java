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
package at.ac.tuwien.ifs.tita.presentation.tasklist.stopwatch;

import java.util.Calendar;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.util.time.Duration;

import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.util.TiTATimeConverter;
import at.ac.tuwien.ifs.tita.presentation.tasklist.TaskListPanel;

/**
 * The TaskTimerPanel is a ui element to measure the time for a specific task.
 * 
 * @author Christoph
 * 
 */
public class AssignedTaskTimerPanel extends Panel implements IHeaderContributor {
    private static final Integer C_UPDATE_INTERVALL = 10;
    private ResourceReference style = new CompressedResourceReference(
            AssignedTaskTimerPanel.class, "tasktimer.css");
    private Form<Object> taskTimerForm;
    private ITaskTrackable task;
    private Long effort;
    private TaskListPanel owner;
    private Label timer;
    private Boolean started;
    private Clock clock;
    private AjaxSelfUpdatingTimerBehavior ajax;
    
    public AssignedTaskTimerPanel(String id, ITaskTrackable task, Long effort, TaskListPanel owner){
        super(id);
        this.task = task;
        this.effort = effort;
        this.owner = owner;
        this.started = false;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        this.clock = new Clock("clock", cal);
        displayPanel();
    }

    /**
     * Displays the Panel with all wicket elements.
     */
    private void displayPanel() {
        taskTimerForm = new Form<Object>("timerTaskForm");
        add(taskTimerForm);
        
        taskTimerForm.add(new Label("taskId", task.getId().toString()));
        taskTimerForm.add(new Label("taskDescription", task.getDescription()));
        taskTimerForm.add(new AjaxButton("startStopTimer", taskTimerForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                target.addComponent(clock);
                if(!started){
                    // you first must add the component to target event
                    // and then start the timer function, otherwise it wouldn't work
                    ajax = new AjaxSelfUpdatingTimerBehavior(Duration.seconds(C_UPDATE_INTERVALL));
                    clock.add(ajax);
//                    owner.startTaskTimer(task);
                    started = true;
                }else{
//                        getModel().setObject("Start");
                    ajax.stop();
                    clock.
                    started = false;
                }
            }
        });
        taskTimerForm.add(new AjaxButton("closeTask", taskTimerForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                started = false;
                owner.closeTask(task,target);
            }
        });
        
        taskTimerForm.add(new Label("totalEffort", TiTATimeConverter.getDuration2String(
                          effort != null ? effort : 0L)));
//        add clock
        clock.setOutputMarkupId(true);
        taskTimerForm.add(clock);
    }

    /** {@inheritDoc} */
    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference(style);
    }
    
    /**
     * Hits the submit event of the ajax start button.
     */
    public void startTimer(){
        if(!started){
            ((AjaxButton)taskTimerForm.get(2)).onSubmit();
        }
    }
    
    public ITaskTrackable getTask(){
        return task;
    }
    
    public void stopTimer(){
        if(started){
            ajax.stop();
            started = false;
        }
    }
    
    public String getDate(){
        return clock.getDate();
    }
    
    public void setDate(String date){
        clock.setDate(date);
    }
}

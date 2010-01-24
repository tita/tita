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
package at.ac.tuwien.ifs.tita.ui.tasklist.stopwatch;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;

import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.issuetracker.util.TiTATimeConverter;
import at.ac.tuwien.ifs.tita.ui.tasklist.TaskListPanel;

/**
 * The TaskTimerPanel is a ui element to measure the time for a specific task.
 * 
 * @author Christoph
 * 
 */
public class GeneralTimerPanel extends Panel implements IHeaderContributor {
//    private ResourceReference style = new CompressedResourceReference(GeneralTimerPanel.class, "tasktimer.css");
    private Form<Object> taskTimerForm;
    private TaskListPanel owner;
    private Effort effort = null;
    private Boolean started;
    private TextField<String> description, duration;

    public GeneralTimerPanel(String id, TaskListPanel owner) {
        super(id);
        this.owner = owner;
        started = false;
        displayPanel();
    }

    /**
     * Displays the Panel with all wicket elements.
     */
    private void displayPanel() {
        taskTimerForm = new Form<Object>("timerTaskForm");
        add(taskTimerForm);
        taskTimerForm.add(new Label("taskLabel", "Description"));
        description = new TextField<String>("taskDescription", new Model<String>(""));
        description.setType(String.class);

        duration = new TextField<String>("taskDuration", new Model<String>("00:00:00"));
        duration.setType(String.class);
        duration.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(1)));
        duration.setOutputMarkupId(true);
        description.setOutputMarkupId(true);

        taskTimerForm.add(duration);
        taskTimerForm.add(description);

        taskTimerForm.add(new AjaxButton("startStopTimer", new Model<String>(), taskTimerForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                if (!started) {
                    owner.startGeneralTimerForTask();
                    description.getModel().setObject("");
                    started = true;
                    this.getModel().setObject("Stop");
                } else {
                    owner.stopGeneralTimerForTask();
                    started = false;
                    this.getModel().setObject("Start");
                }

                this.setLabel(this.getModel());
                target.addComponent(this);
            }
        });
        taskTimerForm.add(new AjaxButton("saveEffort", taskTimerForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                if (effort != null && !started) {
                    owner.saveEffortForTiTATask(effort,target);
                    description.setModelObject("");
                    duration.setModelObject("");
                }
                target.addComponent(description);
                target.addComponent(duration);
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public void renderHead(IHeaderResponse response) {
//        response.renderCSSReference(style);
    }

    /**
     * Set timer of task started.
     */
    public void setTaskStarted() {
        started = true;
    }

    /**
     * Set the effort of the assigned task.
     * 
     * @param effort Long
     */
    public void setEffort(Effort effort) {
        this.effort = effort;
        duration.getModel().setObject(TiTATimeConverter.getDuration2String(effort != null ? effort.getDuration() : 0L));
    }

    /**
     * Get description of task.
     * 
     * @return String
     */
    public String getDescription() {
        String desc = description.getModelObject();

        if (desc != null) {
            return desc;
        }
        return "";
    }
}

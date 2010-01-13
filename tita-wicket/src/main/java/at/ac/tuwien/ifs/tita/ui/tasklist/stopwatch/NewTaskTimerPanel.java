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

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;
import at.ac.tuwien.ifs.tita.ui.tasklist.TaskListPanel;

/**
 * The TaskTimerPanel is a ui element to measure the time for a specific task.
 *
 * @author Christoph
 *
 */
public class NewTaskTimerPanel extends Panel implements IHeaderContributor {

    private ResourceReference style = new CompressedResourceReference(
            NewTaskTimerPanel.class, "tasktimer.css");
    private Form<Object> taskTimerForm;
    private ITaskTrackable task;
    private TaskListPanel owner;

    public NewTaskTimerPanel(String id, ITaskTrackable task, TaskListPanel owner){
        super(id);
        this.task = task;
        displayPanel();
        this.owner = owner;
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
                owner.assignTask(task, target);
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference(style);
    }

    /**
     * Returns current issue tracker task of panel.
     * @return ITaskTrackable
     */
    public ITaskTrackable getTask(){
        return task;
    }
}

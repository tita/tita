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

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

/**
 * The TaskTimerPanel is a ui element to measure the time for a specific task.
 * 
 * @author Christoph
 * 
 */
public class TaskTimerPanel extends Panel implements IHeaderContributor {

    private ResourceReference style = new CompressedResourceReference(
            TaskTimerPanel.class, "tasktimer.css");
    
    private WebMarkupContainer container = null;
    
    private Form taskTimerForm;

    public TaskTimerPanel(String id) {
        super(id);
        displayPanel();

    }
    
    public TaskTimerPanel(String id, ResourceReference style) {
        this(id);
        this.style=style;
    }

    /**
     * Displays the Panel with all wicket elements.
     */
    private void displayPanel() {
        
        container = new WebMarkupContainer("tasktimer");
        container.setOutputMarkupId(true);
        container.setOutputMarkupPlaceholderTag(true);
        this.add(container);
        
        taskTimerForm = new Form("timerTaskForm");
        add(taskTimerForm);
        
        taskTimerForm.add(new Label("taskId", "ID"));
        taskTimerForm.add(new Label("taskDescription", "Description"));


        taskTimerForm.add(new AjaxButton("startStopTimer", taskTimerForm) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {

            }
        });

        taskTimerForm.add(new AjaxButton("closeTask", taskTimerForm) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {

            }
        });

        taskTimerForm.add(new Label("workingTime", "02:30"));
        taskTimerForm.add(new Label("totalTime", "04:52"));
        
        container.add(taskTimerForm);

    }

    /** {@inheritDoc} */
    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference(style);
    }

}

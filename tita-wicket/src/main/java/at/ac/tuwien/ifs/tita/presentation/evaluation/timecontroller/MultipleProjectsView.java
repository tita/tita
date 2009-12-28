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
package at.ac.tuwien.ifs.tita.presentation.evaluation.timecontroller;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;

import at.ac.tuwien.ifs.tita.presentation.BasePage;
import at.ac.tuwien.ifs.tita.presentation.controls.dropdown.SelectOption;

/**
 * Page for Multiple project evaluation.
 * 
 * @author rene
 * 
 */
public class MultipleProjectsView extends BasePage {
    private final SelectOption selectedTimespan;

    public MultipleProjectsView() {
        Form<Object> form = new Form<Object>("multipleProjectsForm");
        form.setOutputMarkupId(true);
        add(form);

        final List<String> projects = Arrays.asList(new String[] { "Project 1", "Project 2" });
        form.add(new ListMultipleChoice<String>("projectSelection", projects));

        final List<String> timeconsumers = Arrays.asList(new String[] { "TimeConsumer 1", "TimeConsumer 2" });
        form.add(new ListMultipleChoice<String>("timeConsumerSelection", timeconsumers));

        selectedTimespan = new SelectOption("monthly", "Monthly");
        ChoiceRenderer choiceRenderer = new ChoiceRenderer("value", "key");
        SelectOption[] options = new SelectOption[] { new SelectOption("daily", "Daily"),
                new SelectOption("monthly", "Monthly") };
        final DropDownChoice ddTimeSpan = new DropDownChoice("timeSpanSelection", new PropertyModel(this,
                "selectedTimespan"), Arrays.asList(options), choiceRenderer);

        form.add(ddTimeSpan);
        form.add(new AjaxButton("btnShowMultipleProjects", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                // TODO
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form1) {
                // TODO Set border red on textfields which are'nt filled
            }
        });

        final List<String> data = Arrays.asList(new String[] { "ID 1", "Project 1", "Time", "consumer1", "..." });
        ListView<String> dataView = new ListView<String>("multipleProjectList", data) {
            @Override
            protected void populateItem(ListItem<String> item) {
                item.add(new Label("projectID", item.getDefaultModelObjectAsString()));
                item.add(new Label("projectDescription", item.getDefaultModelObjectAsString()));
                item.add(new Label("firstName", item.getDefaultModelObjectAsString()));
                item.add(new Label("lastName", item.getDefaultModelObjectAsString()));
                item.add(new Label("...", item.getDefaultModelObjectAsString()));
            };
        };

        add(dataView);
    }
}

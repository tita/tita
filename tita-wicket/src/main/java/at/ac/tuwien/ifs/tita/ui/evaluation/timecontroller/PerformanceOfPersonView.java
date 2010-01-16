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
package at.ac.tuwien.ifs.tita.ui.evaluation.timecontroller;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

import at.ac.tuwien.ifs.tita.ui.BasePage;

/**
 * Page for Performance of person evaluation.
 * 
 * @author rene
 * 
 */
public class PerformanceOfPersonView extends BasePage {

    private Boolean stop = true;

    public PerformanceOfPersonView() {
        initPage();
    }

    /**
     * Inits Page.
     */
    private void initPage() {
        // final IModel<String> buttonModel = new Model<String>();
        Form form = new Form("timeConsumerEvaluationForm");
        add(form);
        form.setOutputMarkupId(true);

        form.add(new AjaxButton("btnShowEvaluation", new Model<String>(), form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                stop = !stop;

                this.getModel().setObject(stop ? "Stop" : "Start");
                this.setLabel(this.getModel());
                target.addComponent(this);
            }
        });
    }
}

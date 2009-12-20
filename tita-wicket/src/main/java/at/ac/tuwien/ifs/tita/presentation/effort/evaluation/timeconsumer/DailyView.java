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
package at.ac.tuwien.ifs.tita.presentation.effort.evaluation.timeconsumer;

import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import at.ac.tuwien.ifs.tita.business.service.time.IEffortService;
import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.presentation.HeaderPage;
import at.ac.tuwien.ifs.tita.presentation.controls.listview.EffortEvaluationListView;

/**
 * Daily evaluation.
 */
public class DailyView extends HeaderPage {
    @SpringBean(name = "timeEffortService")
    private IEffortService service;

    private final Date date = new Date();

    public DailyView() {
        initPage();
    }

    /**
     * Inits Page.
     */
    private void initPage() {
        Form<Effort> form = new Form<Effort>("dailyviewform", new CompoundPropertyModel<Effort>(new Effort()));
        add(form);
        form.setOutputMarkupId(true);

        final DateTextField dateTextField = new DateTextField("tedate", new PropertyModel<Date>(this, "date"),
                new StyleDateConverter("S-", true));
        dateTextField.add(new DatePicker());
        form.add(dateTextField);

        final EffortEvaluationListView<Effort> listView = new EffortEvaluationListView<Effort>("dailyList",
                getTimeEffortsDailyView(new Date()));
        listView.setOutputMarkupId(true);

        final WebMarkupContainer timeeffortContainer = new WebMarkupContainer("timeeffortContainer");
        timeeffortContainer.setOutputMarkupId(true);
        timeeffortContainer.setOutputMarkupPlaceholderTag(true);
        add(timeeffortContainer);
        timeeffortContainer.add(listView);

        form.add(new AjaxButton("btnShowDaily", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                listView.setList(getTimeEffortsDailyView(dateTextField.getModelObject()));
                target.addComponent(timeeffortContainer);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form1) {
                // TODO Set border red on textfields which are'nt filled
            }
        });
    }

    /**
     * Gets time effort data by date.
     * 
     * @param d date of timeeffort entry
     * @return all timeefforts that match the date
     */
    private List<Effort> getTimeEffortsDailyView(Date d) {
        List<Effort> list = null;
        try {
            list = service.getEffortsDailyView(d);
        } catch (TitaDAOException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Returns date for datePicker.
     * 
     * @return the date
     */
    public Date getDate() {
        return date;
    }
}

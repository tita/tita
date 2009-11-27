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
package at.ac.tuwien.ifs.tita.presentation.timeeffort.evaluation;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import at.ac.tuwien.ifs.tita.datasource.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.timeeffort.domain.TimeEffort;
import at.ac.tuwien.ifs.tita.timeeffort.service.ITimeEffortService;

/**
 * Daily evaluation.
 */
public class DailyView extends WebPage {
    @SpringBean(name = "timeEffortService")
    private ITimeEffortService service;

    private TimeEffortEvaluationListView<TimeEffort> listView = null;

    public DailyView() {
        initForm();
        // addInitialTimeEfforts();
        initTable();
    }

    /**
     * Inits Form.
     */
    private void initForm() {
        Form<TimeEffort> form = new Form<TimeEffort>("dailyviewform", new CompoundPropertyModel<TimeEffort>(
                new TimeEffort()));
        add(form);
        form.setOutputMarkupId(true);

        form.add(new AjaxButton("btnShowDaily", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {

            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form1) {
                // TODO Set border red on textfields which are'nt filled
            }
        });
    }

    /**
     * Inits timeeffort table.
     */
    private void initTable() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(2009, 11, 27);

        listView = new TimeEffortEvaluationListView<TimeEffort>("dailyList", getTimeEffortsDailyView(cal));
        listView.setOutputMarkupId(true);
        add(listView);
    }

    /**
     * Gets time effort data by date.
     * 
     * @param cal date of timeeffort entry
     * @return all timeefforts that match the date
     */
    private List<TimeEffort> getTimeEffortsDailyView(Calendar cal) {
        List<TimeEffort> list = null;
        try {
            list = service.getTimeEffortsDailyView(cal);
            System.out.println(list.size());
        } catch (TitaDAOException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Adds Iniital Time Efforts.
     */
    private void addInitialTimeEfforts() {
        TimeEffort timeEffort = new TimeEffort();
        timeEffort.setDate(new Date());
        timeEffort.setDeleted(false);
        timeEffort.setEndTime(new Date());
        timeEffort.setStartTime(new Date());
        timeEffort.setDescription("Das ist die Test TimeEffort 1");

        TimeEffort timeEffort2 = new TimeEffort();
        timeEffort2.setDate(new Date());
        timeEffort2.setDeleted(false);
        timeEffort2.setEndTime(new Date());
        timeEffort2.setStartTime(new Date());
        timeEffort2.setDescription("Das ist die Test TimeEffort 2");

        try {
            service.saveTimeEffort(timeEffort);
            service.saveTimeEffort(timeEffort2);
        } catch (TitaDAOException e) {
            e.printStackTrace();
        }
    }
}

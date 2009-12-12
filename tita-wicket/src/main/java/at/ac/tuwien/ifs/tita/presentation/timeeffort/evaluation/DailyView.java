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
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import at.ac.tuwien.ifs.tita.datasource.entity.TimeEffort;
import at.ac.tuwien.ifs.tita.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.presentation.utils.GlobalUtils;
import at.ac.tuwien.ifs.tita.service.time.IEffortService;

/**
 * Daily evaluation.
 */
public class DailyView extends WebPage {
    @SpringBean(name = "timeEffortService")
    private IEffortService service;

    private final Date date = new Date();

    public DailyView() {
        // addInitialTimeEfforts();
        initPage();
    }

    /**
     * Inits Page.
     */
    private void initPage() {
        Form<TimeEffort> form = new Form<TimeEffort>("dailyviewform", new CompoundPropertyModel<TimeEffort>(
                new TimeEffort()));
        add(form);
        form.setOutputMarkupId(true);

        final DateTextField dateTextField = new DateTextField("tedate", new PropertyModel<Date>(this, "date"),
                new StyleDateConverter("S-", true));
        dateTextField.add(new DatePicker());
        form.add(dateTextField);

        final TimeEffortEvaluationListView<TimeEffort> listView = new TimeEffortEvaluationListView<TimeEffort>(
                "dailyList", getTimeEffortsDailyView(new Date()));
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
    private List<TimeEffort> getTimeEffortsDailyView(Date d) {
        Calendar cal = GlobalUtils.getCalendarFromDate(d);

        List<TimeEffort> list = null;
        try {
            list = service.getTimeEffortsDailyView(cal);
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

    /**
     * Adds Inital Time Efforts. will be removed later.
     */

    // private void addInitialTimeEfforts() {
    // GregorianCalendar cal1 = new GregorianCalendar();
    // cal1.set(Calendar.AM_PM, Calendar.PM);
    // cal1.set(2009, 11, 28, 14, 33);
    //
    // GregorianCalendar cal2 = new GregorianCalendar();
    // cal2.set(Calendar.AM_PM, Calendar.PM);
    // cal2.set(2009, 11, 28, 15, 46);
    //
    // TimeEffort timeEffort = new TimeEffort();
    // timeEffort.setDate(new Date());
    // timeEffort.setDeleted(false);
    // timeEffort.setEndTime(cal2.getTime());
    // timeEffort.setStartTime(cal1.getTime());
    // timeEffort.setDescription("Das ist die Test TimeEffort 1");
    //
    // cal1 = new GregorianCalendar();
    // cal1.set(Calendar.AM_PM, Calendar.PM);
    // cal1.set(2009, 11, 28, 16, 11);
    //
    // cal2 = new GregorianCalendar();
    // cal2.set(Calendar.AM_PM, Calendar.PM);
    // cal2.set(2009, 11, 28, 17, 1);
    //
    // TimeEffort timeEffort2 = new TimeEffort();
    // timeEffort2.setDate(new Date());
    // timeEffort2.setDeleted(false);
    // timeEffort2.setEndTime(cal2.getTime());
    // timeEffort2.setStartTime(cal1.getTime());
    // timeEffort2.setDescription("Das ist die Test TimeEffort 2");
    //
    // try {
    // service.saveTimeEffort(timeEffort);
    // service.saveTimeEffort(timeEffort2);
    // } catch (TitaDAOException e) {
    // e.printStackTrace();
    // }
    // }

}

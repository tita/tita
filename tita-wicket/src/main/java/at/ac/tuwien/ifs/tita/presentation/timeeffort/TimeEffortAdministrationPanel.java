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
package at.ac.tuwien.ifs.tita.presentation.timeeffort;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.datasource.criteria.IBaseCriteria;
import at.ac.tuwien.ifs.tita.datasource.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.presentation.utils.GlobalUtils;
import at.ac.tuwien.ifs.tita.timeeffort.domain.TimeEffort;
import at.ac.tuwien.ifs.tita.timeeffort.service.ITimeEffortService;

/**
 * 
 * Wicket Panel for time effort administration
 * 
 * @author msiedler
 * 
 */
public class TimeEffortAdministrationPanel extends Panel implements GlobalUtils {

    @SpringBean(name = "timeEffortService")
    private ITimeEffortService service;

    // Actual Date
    private final Date date = new Date();

    // Logger
    final Logger log = LoggerFactory
            .getLogger(TimeEffortAdministrationPanel.class);

    // Actual time effort list
    private final List<TimeEffort> timeeffortList = new ArrayList<TimeEffort>();

    // Wicket Components
    private final Button saveButton = new Button("saveButton",
            new Model<String>("Save"));

    private TextField<String> descriptionTextfield = null;
    private DateTextField dateTextField = null;
    private TextField<Integer> startHourTextfield = null;
    private TextField<Integer> startMinuteTextfield = null;
    private TextField<Integer> endHourTextfield = null;
    private TextField<Integer> endMinuteTextfield = null;
    private TimeEffortListView<TimeEffort> listView = null;

    // private final Button saveButton = new Button("saveButton",
    // new Model<String>("Save"));
    // private final TextField<String> descriptionTextfield = new
    // TextField<String>(
    // "descriptionTextfield", new Model<String>(""));
    // private final DateTextField dateTextField = new DateTextField(
    // "dateTextfield", new PropertyModel<Date>(this, "date"),
    // new StyleDateConverter("S-", true));
    // private final TextField<Integer> startHourTextfield = new
    // TextField<Integer>(
    // "starthourTextfield");
    // private final TextField<Integer> startMinuteTextfield = new
    // TextField<Integer>(
    // "startminuteTextfield");
    // private final TextField<Integer> endHourTextfield = new
    // TextField<Integer>(
    // "endhourTextfield");
    // private final TextField<Integer> endMinuteTextfield = new
    // TextField<Integer>(
    // "endminuteTextfield");
    // private final TimeEffortListView<TimeEffort> listView = new
    // TimeEffortListView<TimeEffort>(
    // "timeEffortList", timeeffortList);

    // private User user;
    // private Project project;

    public TimeEffortAdministrationPanel(String id) {
        super(id);
        displayPanel();
    }

    /**
     * Displays panel
     */
    private void displayPanel() {
        List<TimeEffort> list = getTimeEfforts(10);
        // initData();

        descriptionTextfield = new TextField<String>("descriptionTextfield",
                new Model<String>(""));
        descriptionTextfield.setOutputMarkupId(true);
        dateTextField = new DateTextField("dateTextfield",
                new PropertyModel<Date>(this, "date"), new StyleDateConverter(
                        "S-", true));
        startHourTextfield = new TextField<Integer>("starthourTextfield");
        startMinuteTextfield = new TextField<Integer>("startminuteTextfield");
        endHourTextfield = new TextField<Integer>("endhourTextfield");
        endMinuteTextfield = new TextField<Integer>("endminuteTextfield");
        listView = new TimeEffortListView<TimeEffort>("timeEffortList",
                timeeffortList);

        dateTextField.add(new DatePicker());
        add(dateTextField);
        add(descriptionTextfield);
        add(startHourTextfield);
        add(startMinuteTextfield);
        add(endHourTextfield);
        add(endMinuteTextfield);
        listView.refreshView(timeeffortList);
        add(listView);
        initSaveButton();
    }

    // =========== WICKET METHODS ==============================================

    private void initSaveButton() {
        Form<Button> form = new Form<Button>("saveButtonForm");
        saveButton.add(new AjaxEventBehavior("onclick") {

            @Override
            protected void onEvent(AjaxRequestTarget target) {
                log.info("saveButton.onSubmit executed");
                saveTimeEffort();
                // initData();
                target.addChildren(listView, TimeEffort.class);
            }
        });
        add(form);
        saveButton.setDefaultFormProcessing(false);
        form.add(saveButton);
    }

    // =========== AJAX METHODS ==============================================

    // private void setAjaxTextfields() {
    // dateTextField.add(new AjaxFormComponentUpdatingBehavior("onblur") {
    // @Override
    // protected void onUpdate(AjaxRequestTarget target) {
    // System.out.println("current value of the textfield is: "+getModelObject());
    // }
    // }
    // }

    // =========== DB METHODS ================================================

    private List<TimeEffort> saveTimeEffort() {
        // try {
        TimeEffort timeEffort = new TimeEffort();
        timeEffort.setDate(dateTextField.getModelObject());
        log.info("DATE = " + dateTextField.getModelObject());
        log.info("DESCRIPTION = " + descriptionTextfield.getModelObject());
        log.info("START HOUR = " + startHourTextfield.getModelObject());
        timeEffort.setDescription(descriptionTextfield.getModelObject());
        timeEffort.setStartTime(null);
        timeEffort.setEndTime(null);
        timeEffort.setDeleted(false);

        // service.saveTimeEffort(timeEffort);

        timeeffortList.add(0, timeEffort);
        // } catch (TitaDAOException e) {
        // e.printStackTrace();
        // }
        return timeeffortList;
    }

    private List<TimeEffort> getTimeEfforts(int maxsize) {
        GregorianCalendar cal = new GregorianCalendar();
        TimeEffort te2 = new TimeEffort();
        cal.set(2009, 10, 10);
        te2.setDate(cal.getGregorianChange());
        te2.setDeleted(false);
        te2.setDescription("Testbeschreibung 2");
        (cal = new GregorianCalendar()).set(2009, 10, 10, 10, 10, 10);
        te2.setStartTime(cal.getTime());
        (cal = new GregorianCalendar()).set(2009, 10, 10, 11, 11, 11);
        te2.setEndTime(cal.getTime());

        try {
            te2 = service.saveTimeEffort(te2);
        } catch (TitaDAOException e) {
            e.printStackTrace();
        }

        List<TimeEffort> returnValue = null;
        try {
            TimeEffort timeEffort = new TimeEffort();
            timeEffort.setDate(date);
            IBaseCriteria<TimeEffort> timeefcrit = service
                    .createCriteria(timeEffort);

            timeefcrit.getCriteria().setMaxResults(maxsize);
            // timeefcrit.setOrderAscBy("date");
            returnValue = service.searchTimeEffort(timeefcrit);
        } catch (TitaDAOException e) {
            e.printStackTrace();
        }

        return returnValue;
    }

    // ============= TIME EFFORT LIST VIEW ===========================

    /**
     * Time efforts are displayed in a table list
     * 
     * @author msiedler
     * 
     * @param <T>
     */
    private class TimeEffortListView<T> extends ListView<TimeEffort> {

        TimeEffortListView(String id, List<TimeEffort> list) {
            super(id, list);
        }

        public void refreshView(List<TimeEffort> list) {
            setList(list);
            // render();
        }

        @Override
        protected void populateItem(ListItem<TimeEffort> item) {
            TimeEffort timeEffort = item.getModelObject();
            Label lbDate = new Label("date", DATEFORMAT.format(timeEffort
                    .getDate()));

            Label lbDescription = new Label("description", timeEffort
                    .getDescription());

            Label lbLength = new Label("length", "10");
            // Label lbLength = new Label("length", TIMELENGTHFORMAT
            // .format(timeEffort.getEndTime().compareTo(
            // timeEffort.getStartTime())));

            lbDate.setOutputMarkupId(true);
            lbDescription.setOutputMarkupId(true);
            lbLength.setOutputMarkupId(true);

            item.add(lbDate);
            item.add(lbDescription);
            item.add(lbLength);

            Form<Button> form = new Form<Button>("editdeleteForm");
            form.setOutputMarkupId(true);
            item.add(form);
            addEditButton(form);
            addDeleteButton(form);
        }

        private void addEditButton(Form<Button> form) {
            Button editButton = new Button("edit") {
                @Override
                public void onSubmit() {
                    info("editButton.onSubmit executed");
                }
            };
            editButton.setOutputMarkupId(true);
            form.add(editButton);
        }

        private void addDeleteButton(Form<Button> form) {
            Button deleteButton = new Button("delete") {
                @Override
                public void onSubmit() {
                    info("deleteButton.onSubmit executed");
                }
            };
            deleteButton.setOutputMarkupId(true);
            form.add(deleteButton);
        }
    }

    // temp method
    private void initData() {
        // try {
        // TimeEffort timeEffort2 = service.getTimeEffortById(0L);
        TimeEffort timeEffort2 = new TimeEffort();
        timeEffort2.setDate(new Date());
        timeEffort2.setDescription("Das ist eine Testbeschreibung");
        timeEffort2.setEndTime(new Date());
        timeEffort2.setStartTime(new Date());
        // timeEffort2 = service.saveTimeEffort(timeEffort2);
        timeeffortList.add(timeEffort2);
        /*
         * timeEffort2 = new TimeEffort(); timeEffort2.setDate(new Date());
         * timeEffort2.setDescription(
         * "Das ist eine Testbeschreibung Das ist eine Testbeschreibung");
         * timeEffort2.setEndTime(new Date()); timeEffort2.setStartTime(new
         * Date()); // timeEffort2 = service.saveTimeEffort(timeEffort2);
         * timeeffortList.add(timeEffort2); // } catch (TitaDAOException e) { //
         * }
         */
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }
}

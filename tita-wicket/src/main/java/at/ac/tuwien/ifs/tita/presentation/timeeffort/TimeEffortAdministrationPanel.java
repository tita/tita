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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.datasource.entity.TimeEffort;
import at.ac.tuwien.ifs.tita.datasource.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.datasource.service.time.ITimeEffortService;
import at.ac.tuwien.ifs.tita.datasource.time.ITimer;
import at.ac.tuwien.ifs.tita.datasource.util.TiTATimeConverter;
import at.ac.tuwien.ifs.tita.issuetracker.enums.IssueStatus;
import at.ac.tuwien.ifs.tita.issuetracker.exceptions.ProjectNotFoundException;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IIssueTrackerDao;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IProjectTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;
import at.ac.tuwien.ifs.tita.issuetracker.issue.service.TaskService;
import at.ac.tuwien.ifs.tita.presentation.utils.GlobalUtils;

/**
 * 
 * Wicket Panel for time effort administration
 * 
 * @author msiedler
 * 
 */
public class TimeEffortAdministrationPanel extends Panel {

    @SpringBean(name = "timeEffortService")
    private ITimeEffortService service;
    
    @SpringBean(name = "generalTimer")
    private ITimer generalTimer;
    
    private TimeEffort timeEffort = null;

    // Actual Date
    private final Date date = new Date();

    // Logger
    final Logger log = LoggerFactory.getLogger(TimeEffortAdministrationPanel.class);

    // Actual time effort list
    private List<TimeEffort> timeeffortList = new ArrayList<TimeEffort>();

    // IssueTracker dao
    @SpringBean(name = "mantisDAO")
    IIssueTrackerDao issuetrackerDao;

    // Wicket Components

    private Form form = null;
    private Form timerForm = null;
    
    final FeedbackPanel feedback = new FeedbackPanel("feedbackPanel");

    private TextField<String> descriptionTextfield = null;
    private DateTextField dateTextField = null;
    private TextField<String> txtTimeLength = null;
    private TimeEffortListView<TimeEffort> listView = null;

    private WebMarkupContainer timeeffortContainer = null;

    // private User user;
    // private Project project;

    public TimeEffortAdministrationPanel(String id) {
        super(id);
        displayPanel();

        displayTasks();
    }

    /**
     * Displays panel
     */
    private void displayPanel() {
        // List<TimeEffort> list = getTimeEfforts();
        initData();

        timeEffort = new TimeEffort();

        form = new Form<TimeEffort>("timeeffortForm", 
                                    new CompoundPropertyModel<TimeEffort>(timeEffort));
        add(form);
        form.setOutputMarkupId(true);
//        add(feedback);
//        feedback.setOutputMarkupId(true);
        
        //timer Form
        timerForm = new Form("timerForm");
        add(timerForm);
        form.setOutputMarkupId(true);
        
        // add form components to the form as usual
        timeeffortContainer = new WebMarkupContainer("timeeffortContainer");
        timeeffortContainer.setOutputMarkupId(true);
        timeeffortContainer.setOutputMarkupPlaceholderTag(true);
        add(timeeffortContainer);

        listView = new TimeEffortListView<TimeEffort>("timeEffortList", timeeffortList);
        listView.setOutputMarkupId(true);
        timeeffortContainer.add(listView);

        descriptionTextfield = new TextField<String>("description", new Model<String>(""));
        descriptionTextfield.add(StringValidator.maximumLength(50));
        form.add(descriptionTextfield);

        dateTextField = new DateTextField("tedate", new PropertyModel<Date>(this, "date"), 
                        new StyleDateConverter("S-",true));
        dateTextField.add(new DatePicker());

        form.add(dateTextField);

        txtTimeLength = new TextField<String>("timeLength", new Model<String>(""));
        txtTimeLength.setType(String.class);
        txtTimeLength.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            } 
        });
        
        form.add(txtTimeLength);

        // AjaxFormValidatingBehavior.addToAllFormComponents(form, "onkeyup",
        // Duration.ONE_SECOND);

        form.add(new AjaxButton("saveButton", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                saveTimeEffort();
                listView.setList(timeeffortList);
                target.addComponent(timeeffortContainer);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form1) {
//                target.addComponent(form.getPage().get("feedbackPanel"));
            }
        });
        
        timerForm.add(new AjaxButton("startTimer", timerForm){

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                generalTimer.start();
            }
        });
        
        timerForm.add(new AjaxButton("stopTimer", timerForm){
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                generalTimer.stop();
                //for setting the value of the textfield
                txtTimeLength.setModelObject(TiTATimeConverter.Duration2String(
                                            generalTimer.getDuration()));
                txtTimeLength.setOutputMarkupId(true);
                target.addComponent(txtTimeLength);
            }
        });
    }

    private void displayTasks() {
        List<ITaskTrackable> allTasks = new ArrayList<ITaskTrackable>();
        // TODO: change so just the
        TaskService taskService = new TaskService();

        Map<Long, IProjectTrackable> projects = taskService.getProjects();

        if (projects != null) {
            for (IProjectTrackable p : projects.values()) {

                Map<Long, ITaskTrackable> tasks;
                try {
                    tasks = taskService.getTasks(p, IssueStatus.NEW);

                    for (ITaskTrackable t : tasks.values()) {
                        allTasks.add(t);
                    }
                } catch (ProjectNotFoundException e) {
                    // TODO show user information
                    e.printStackTrace();
                }
            }
        }

        add(new ListView<ITaskTrackable>("tasklist", allTasks) {
            @Override
            protected void populateItem(ListItem<ITaskTrackable> item) {
                ITaskTrackable task = item.getModelObject();
                item.add(new Label("number", task.getId().toString()));
                item.add(new Label("name", task.getDescription()));
                item.add(new Label("creation", GlobalUtils.DATEFORMAT.format(task.getCreationTime())));
                item.add(new Label("lastchange", GlobalUtils.DATEFORMAT.format(task.getLastChange())));
                item.add(new Label("owner", task.getOwner()));
                item.add(new Label("priority", task.getPriority().name()));
                item.add(new Label("resolution", task.getResolution().name()));
                item.add(new Label("severity", task.getSeverity().name()));
                item.add(new Label("status", task.getStatus().name()));
            }
        });
    }

    // =========== DB METHODS ================================================

    private List<TimeEffort> saveTimeEffort() {
        try {
            Calendar cal = Calendar.getInstance();

            timeEffort.setDate(dateTextField.getModelObject());
            timeEffort.setDescription(descriptionTextfield.getModelObject());

//            cal.clear();
//            cal.setTime(date);
//            cal.set(Calendar.HOUR_OF_DAY, startHourTextfield.getModelObject());
//            cal.set(Calendar.MINUTE, startMinuteTextfield.getModelObject());
//            timeEffort.setStartTime(cal.getTime());
//            cal.clear();
//            cal.setTime(date);
//            cal.set(Calendar.HOUR_OF_DAY, endHourTextfield.getModelObject());
//            cal.set(Calendar.MINUTE, endMinuteTextfield.getModelObject());
//            timeEffort.setEndTime(cal.getTime());
            timeEffort.setDuration(generalTimer.getDuration());
            timeEffort.setDeleted(false);

            service.saveTimeEffort(timeEffort);

            timeeffortList.add(0, timeEffort);

            timeEffort = new TimeEffort();
        } catch (TitaDAOException e) {
            e.printStackTrace();
        }

        return timeeffortList;
    }

    // TODO Search implementation needed
//    private List<TimeEffort> getTimeEfforts(int maxsize) {
//        List<TimeEffort> returnValue = null;
//        try {
//            TimeEffort timeEffort2 = new TimeEffort();
//            timeEffort2.setDeleted(false);
//            timeEffort2.setDate(date);
//            IBaseCriteria<TimeEffort> timeefcrit = service.createCriteria(timeEffort2);
//
//            timeefcrit.getCriteria().setMaxResults(maxsize);
//            timeefcrit.setOrderAscBy("date");
//            returnValue = service.searchTimeEffort(timeefcrit);
//        } catch (TitaDAOException e) {
//            e.printStackTrace();
//        }
//
//        return returnValue;
//    }

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

        @Override
        protected void populateItem(ListItem<TimeEffort> item) {
            TimeEffort timeEffort2 = item.getModelObject();
            Label lbDate = new Label("date", GlobalUtils.DATEFORMAT.format(timeEffort2.getDate()));

            Label lbDescription = new Label("description", timeEffort2.getDescription());

            Label lbLength = new Label("length", (timeEffort2.getDuration() == null ? "" : 
                                     TiTATimeConverter.Duration2String(timeEffort2.getDuration())));
            lbDate.setOutputMarkupId(true);
            lbDescription.setOutputMarkupId(true);
            lbLength.setOutputMarkupId(true);

            item.add(lbDate);
            item.add(lbDescription);
            item.add(lbLength);

            addEditButton(item);
            addDeleteButton(item);
        }

        private void addEditButton(ListItem<TimeEffort> item) {
            Button editButton = new Button("edit") {
                @Override
                public void onSubmit() {
                    info("editButton.onSubmit executed");
                }
            };
            editButton.setOutputMarkupId(true);
            item.add(editButton);
        }

        private void addDeleteButton(ListItem<TimeEffort> item) {
            Button deleteButton = new Button("delete") {
                @Override
                public void onSubmit() {
                    info("deleteButton.onSubmit executed");
                }
            };
            deleteButton.setOutputMarkupId(true);
            item.add(deleteButton);
        }
    }

    // temp method
    private void initData() {
        TimeEffort timeEffort2 = new TimeEffort();
        timeEffort2.setDate(new Date());
        timeEffort2.setDescription("Das ist eine Testbeschreibung");
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.set(Calendar.MINUTE, 10);
//        timeEffort2.setStartTime(cal.getTime());
        cal.clear();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 11);
        cal.set(Calendar.MINUTE, 11);
//        timeEffort2.setEndTime(cal.getTime());
        timeeffortList.add(timeEffort2);

    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }
}

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
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
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
import at.ac.tuwien.ifs.tita.presentation.TitaDataProvider;
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

    // Actual Date
    private final Date date = new Date();

    private static final int MAXLISTSIZE = 25;

    private static final String SORT = "date";

    // Logger
    final Logger log = LoggerFactory
            .getLogger(TimeEffortAdministrationPanel.class);

    // Actual time effort list
    private List<TimeEffort> timeeffortList = new ArrayList<TimeEffort>();

    // IssueTracker dao
    @SpringBean(name = "mantisDAO")
    IIssueTrackerDao issuetrackerDao;

    // Wicket Components

    private Form<TimeEffort> form = null;
    private Form timerForm = null;

    private TitaDataProvider<TimeEffort> teProvider = null;

    final FeedbackPanel feedback = new FeedbackPanel("feedbackPanel");

    private TextField<String> descriptionTextfield = null;
    private DateTextField dateTextField = null;
    private TextField<String> txtTimeLength = null;
    private TextField<String> txtStartTime = null;
    private TextField<String> txtEndTime = null;

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

        // get time efforts
        timeeffortList = getTimeEfforts(MAXLISTSIZE);

        // timer Form
        timerForm = new Form("timerForm");
        add(timerForm);

        // Data table
        displayDataTable(timeeffortList);

        // AjaxFormValidatingBehavior.addToAllFormComponents(form, "onkeyup",
        // Duration.ONE_SECOND);

        timerForm.add(new AjaxButton("startTimer", timerForm) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                generalTimer.start();
            }
        });

        timerForm.add(new AjaxButton("stopTimer", timerForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                generalTimer.stop();
                // for setting the value of the textfield
                txtTimeLength.setModelObject(TiTATimeConverter
                        .Duration2String(generalTimer.getDuration()));

                System.out.println(TiTATimeConverter
                        .Duration2String(generalTimer.getDuration()));

                txtTimeLength.setOutputMarkupId(true);
                target.addComponent(txtTimeLength);
            }
        });
    }

    /**
     * Initializes the time effort data table
     * 
     * @param timeEffortList
     * @return
     */
    private void displayDataTable(List<TimeEffort> timeEffortList) {

        // add form components to the form as usual
        timeeffortContainer = new WebMarkupContainer("timeeffortContainer");
        timeeffortContainer.setOutputMarkupId(true);
        timeeffortContainer.setOutputMarkupPlaceholderTag(true);
        add(timeeffortContainer);

        form = new Form<TimeEffort>("timeeffortForm",
                new CompoundPropertyModel<TimeEffort>(new TimeEffort()));
        add(form);
        form.setOutputMarkupId(true);

        teProvider = new TitaDataProvider<TimeEffort>(timeEffortList, SORT,
                false);

        descriptionTextfield = new TextField<String>("tedescription",
                new Model<String>(""));
        descriptionTextfield.add(StringValidator.maximumLength(50));
        form.add(descriptionTextfield);

        dateTextField = new DateTextField("tedate", new PropertyModel<Date>(
                this, "date"), new StyleDateConverter("S-", true));
        dateTextField.add(new DatePicker());

        form.add(dateTextField);

        txtTimeLength = new TextField<String>("tetimelength",
                new Model<String>(""));
        txtTimeLength.setType(String.class);
        txtTimeLength.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            }
        });
        form.add(txtTimeLength);

        txtStartTime = new TextField<String>("testarttime", new Model<String>(
                ""));
        txtStartTime.setType(String.class);
        txtStartTime.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            }
        });
        form.add(txtStartTime);

        txtEndTime = new TextField<String>("teendtime", new Model<String>(""));
        txtEndTime.setType(String.class);
        txtEndTime.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            }
        });
        form.add(txtEndTime);

        // form.add(new OrderByLink("sortName", "name", dataProvider));
        // form.add(new OrderByLink("sortAlter", "alter", dataProvider));
        // form.add(new OrderByLink("sortAlter", "alter", dataProvider));
        // form.add(new OrderByLink("sortAlter", "alter", dataProvider));
        // form.add(new OrderByLink("sortAlter", "alter", dataProvider));

        TimeEffortDataView dataView = new TimeEffortDataView("dataView",
                teProvider);

        dataView.setItemsPerPage(10);

        form.add(new PagingNavigator("navigator", dataView));
        form.add(new NavigatorLabel("label", dataView));

        form.add(dataView);

        form.add(new AjaxButton("saveButton", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                saveTimeEffort();
                teProvider.setList(timeeffortList);
                teProvider.setSort(SORT, false);
                target.addComponent(timeeffortContainer);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form1) {
                // target.addComponent(form.getPage().get("feedbackPanel"));
            }
        });

        timeeffortContainer.add(form);
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
                item.add(new Label("creation", GlobalUtils.DATEFORMAT
                        .format(task.getCreationTime())));
                item.add(new Label("lastchange", GlobalUtils.DATEFORMAT
                        .format(task.getLastChange())));
                item.add(new Label("owner", task.getOwner()));
                item.add(new Label("priority", task.getPriority().name()));
                item.add(new Label("resolution", task.getResolution().name()));
                item.add(new Label("severity", task.getSeverity().name()));
                item.add(new Label("status", task.getStatus().name()));
            }
        });
    }

    // =========== DB METHODS ================================================

    /**
     * Save Time Effort
     */
    private List<TimeEffort> saveTimeEffort() {
        TimeEffort timeEffort = null;
        try {
            timeEffort = new TimeEffort();

            timeEffort.setDate(dateTextField.getModelObject());
            timeEffort.setDescription(descriptionTextfield.getModelObject());

            timeEffort.setDuration(generalTimer.getDuration());
            timeEffort.setDeleted(false);
            timeEffort.setStartTime(generalTimer.getStartTime());
            service.saveTimeEffort(timeEffort);

            timeeffortList = getTimeEfforts(MAXLISTSIZE);

            timeEffort = new TimeEffort();
        } catch (TitaDAOException e) {
            e.printStackTrace();
        }

        return timeeffortList;
    }

    /**
     * Gets actual time efforts
     * 
     * @param maxresults
     *            sets max results
     * @return all actual time efforts
     */
    private List<TimeEffort> getTimeEfforts(int maxresults) {
        List<TimeEffort> list = null;
        try {
            list = service.getActualTimeEfforts(maxresults);
        } catch (TitaDAOException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ============= TIME EFFORT LIST VIEW ===========================

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }
}

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
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.datasource.entity.BaseTimeEffort;
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

    private TextField<String> teDescription = null;
    private DateTextField teDate = null;
    private TextField<String> teTimeLength = null;
    private TextField<String> teStartTime = null;
    private TextField<String> teEndTime = null;

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
                teTimeLength.setModelObject(TiTATimeConverter
                        .Duration2String(generalTimer.getDuration()));
                teTimeLength.setOutputMarkupId(true);
                target.addComponent(teTimeLength);

                teStartTime.setModelObject(GlobalUtils.TIMEFORMAT24HOURS
                        .format(generalTimer.getStartTime()));
                teStartTime.setOutputMarkupId(true);
                target.addComponent(teStartTime);

                teEndTime.setModelObject(GlobalUtils.TIMEFORMAT24HOURS
                        .format(generalTimer.getStartTime()
                                + generalTimer.getDuration()));
                teEndTime.setOutputMarkupId(true);
                target.addComponent(teEndTime);
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

        teDescription = new TextField<String>("tedescription",
                new Model<String>(""));
        teDescription.add(StringValidator.maximumLength(50));
        form.add(teDescription);

        teDate = new DateTextField("tedate", new PropertyModel<Date>(this,
                "date"), new StyleDateConverter("S-", true));
        teDate.add(new DatePicker());
        form.add(teDate);

        teTimeLength = new TextField<String>("tetimelength", new Model<String>(
                ""));
        teTimeLength.setType(String.class);
        teTimeLength.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            }
        });
        form.add(teTimeLength);

        teStartTime = new TextField<String>("testarttime",
                new Model<String>(""));
        teStartTime.setType(String.class);
        teStartTime.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            }
        });
        form.add(teStartTime);

        teEndTime = new TextField<String>("teendtime", new Model<String>(""));
        teEndTime.setType(String.class);
        teEndTime.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            }
        });
        form.add(teEndTime);

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

            timeEffort.setDate(teDate.getModelObject());
            timeEffort.setDescription(teDescription.getModelObject());

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

    private void deleteTimeEffort(TimeEffort timeEffort) {
        try {
            service.deleteTimeEffort(timeEffort);
        } catch (TitaDAOException e) {
            log.error("Deleting TimeEffort with id " + timeEffort.getId()
                    + " failed.");
        }
    }

    // ============= TIME EFFORT DATA VIEW ===========================

    public class TimeEffortDataView extends DataView<TimeEffort> {

        protected TimeEffortDataView(String id,
                IDataProvider<TimeEffort> dataProvider) {
            super(id, dataProvider);
        }

        @Override
        protected void populateItem(Item<TimeEffort> item) {

            if (item.getIndex() % 2 == 0) {
                item.add(new SimpleAttributeModifier("class", "even"));
            } else {
                item.add(new SimpleAttributeModifier("class", "odd"));
            }

            item.setDefaultModel(new CompoundPropertyModel<BaseTimeEffort>(item
                    .getModel()));
            TimeEffort timeEffort = (TimeEffort) item.getModelObject();

            Label lbDate = new Label("date", GlobalUtils.DATEFORMAT
                    .format(timeEffort.getDate()));

            Label lbDescription = new Label("description", timeEffort
                    .getDescription());

            Label lbStarttime = new Label("starttime", (timeEffort
                    .getStartTime() == null ? ""
                    : GlobalUtils.TIMEFORMAT24HOURS.format(timeEffort
                            .getStartTime())));

            Label lbEndtime = new Label("endtime",
                    (timeEffort.getEndTime() == null ? ""
                            : GlobalUtils.TIMEFORMAT24HOURS.format(timeEffort
                                    .getEndTime())));

            Label lbLength = new Label("length",
                    (timeEffort.getDuration() == null ? "" : TiTATimeConverter
                            .Duration2String(timeEffort.getDuration())));
            lbDate.setOutputMarkupId(true);
            lbDescription.setOutputMarkupId(true);
            lbLength.setOutputMarkupId(true);

            item.add(lbDate);
            item.add(lbDescription);
            item.add(lbStarttime);
            item.add(lbEndtime);
            item.add(lbLength);

            item.add(getEditButton(timeEffort));
            item.add(getDeleteButton(timeEffort));
        }

        private Button getEditButton(final TimeEffort timeEffort) {
            AjaxButton editButton = new AjaxButton("edit") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                    teDescription.setModelObject(timeEffort.getDescription());
                    teDescription.setOutputMarkupId(true);
                    target.addComponent(teDescription);

                    teDate.setModelObject(timeEffort.getDate());
                    teDate.setOutputMarkupId(true);
                    target.addComponent(teDate);

                    teTimeLength.setModelObject(TiTATimeConverter
                            .Duration2String(timeEffort.getDuration()));
                    teTimeLength.setOutputMarkupId(true);
                    target.addComponent(teTimeLength);

                    teStartTime.setModelObject(GlobalUtils.TIMEFORMAT24HOURS
                            .format(timeEffort.getStartTime()));
                    teStartTime.setOutputMarkupId(true);
                    target.addComponent(teStartTime);

                    teEndTime.setModelObject(GlobalUtils.TIMEFORMAT24HOURS
                            .format(timeEffort.getStartTime()
                                    + timeEffort.getDuration()));
                    teEndTime.setOutputMarkupId(true);
                    target.addComponent(teEndTime);
                }
            };
            editButton.setOutputMarkupId(true);
            return editButton;
        }

        private Button getDeleteButton(final TimeEffort timeEffort) {
            AjaxButton deleteButton = new AjaxButton("delete") {
                @Override
                public void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                    deleteTimeEffort(timeEffort);
                    teProvider.setList(timeeffortList);
                    teProvider.setSort(SORT, false);
                    target.addComponent(timeeffortContainer);
                }
            };
            deleteButton.setOutputMarkupId(true);
            return deleteButton;
        }
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }
}

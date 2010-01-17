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
package at.ac.tuwien.ifs.tita.ui.controls.panel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.PersistenceException;
import javax.swing.ListSelectionModel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.table.Table;
import org.wicketstuff.table.cell.renders.LenientTextField;

import at.ac.tuwien.ifs.tita.business.service.tasks.ITaskService;
import at.ac.tuwien.ifs.tita.business.service.time.IEffortService;
import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.ui.login.TitaSession;
import at.ac.tuwien.ifs.tita.ui.models.TableModelEffort;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonDelete;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonDeleteRenderer;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonEdit;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonEditRenderer;
import at.ac.tuwien.ifs.tita.ui.uihelper.DateTextFieldRenderer;
import at.ac.tuwien.ifs.tita.ui.uihelper.IAdministrationPanel;
import at.ac.tuwien.ifs.tita.ui.uihelper.LenientDateTextField;
import at.ac.tuwien.ifs.tita.ui.uihelper.LinkToIssueTrackerRenderer;
import at.ac.tuwien.ifs.tita.ui.uihelper.ValidationDateTextField;
import at.ac.tuwien.ifs.tita.ui.uihelper.ValidationTextField;
import at.ac.tuwien.ifs.tita.ui.utils.EffortUtils;
import at.ac.tuwien.ifs.tita.ui.utils.GlobalUtils;
import at.ac.tuwien.ifs.tita.ui.utils.IntegerConstants;

/**
 * 
 * Wicket Panel for time effort administration.
 * 
 * @author msiedler
 * 
 */
public class AdministrationPanelEffort extends Panel implements IAdministrationPanel {

    @SpringBean(name = "titaEffortService")
    private IEffortService service;
    @SpringBean(name = "userService")
    private IUserService userService;
    @SpringBean(name = "taskService")
    private ITaskService taskService;

    // Actual Date
    private Date date = new Date();
    private Date dateFrom = null;
    private Date dateUntil = null;

    private TiTAProject project = null;
    private TiTAUser user = null;

    // Logger
    private final Logger log = LoggerFactory.getLogger(AdministrationPanelEffort.class);

    // Actual time effort list
    private List<Effort> timeeffortList = new ArrayList<Effort>();
    private List<Effort> fullTimeEffortList = new ArrayList<Effort>();

    // Wicket Components
    private Form<Effort> form = null;

    private Table table = null;

    private AjaxPagingNavigator pagingNavigator = null;

    private TableModelEffort tm = null;

    private ValidationTextField<String> teDescription = null;
    private ValidationDateTextField teDate = null;
    private ValidationTextField<String> teTimeLength = null;
    private ValidationTextField<String> teStartTime = null;
    private ValidationTextField<String> teEndTime = null;

    private TextField<String> teFilterDescription = null;
    private DateTextField teFilterDateFrom = null;
    private DateTextField teFilterDateUntil = null;

    private WebMarkupContainer timeeffortContainer = null;

    public AdministrationPanelEffort(String id, TiTAProject project) {
        super(id);

        this.project = project;
        user = userService.getUserByUsername(TitaSession.getSession().getUsername());

        displayPanel();
    }

    /**
     * Displays panel.
     */
    private void displayPanel() {

        // get time efforts
        loadListEntities();

        tm = new TableModelEffort(timeeffortList);

        // add form components to the form as usual
        timeeffortContainer = new WebMarkupContainer("timeeffortContainer");
        timeeffortContainer.setOutputMarkupId(true);
        timeeffortContainer.setOutputMarkupPlaceholderTag(true);
        add(timeeffortContainer);

        form = new Form<Effort>("timeeffortForm");
        add(form);
        form.setOutputMarkupId(true);

        // Data table
        displayDataTable();

        // Text fields
        displayTextFields();

        // Filter text fields
        displayFilterFields();
    }

    /**
     * Initializes the time effort data table.
     */
    private void displayDataTable() {
        table = new Table("tetable", tm) {
            @Override
            protected void onSelection(AjaxRequestTarget target) {
                if (!(table.getSelectedRows()[0] == tm.getSelectedRow())) {
                    tm.setSelectedRow(table.getSelectedRows()[0]);
                    tm.reload();
                    target.addComponent(table);
                }
            }
        };

        table.setWidths(EffortUtils.WIDTHS);

        DateTextFieldRenderer re = new DateTextFieldRenderer();
        table.setDefaultEditor(Date.class, re);
        table.setDefaultRenderer(Date.class, re);

        ButtonEditRenderer btReEdit = new ButtonEditRenderer(this);
        table.setDefaultRenderer(ButtonEdit.class, btReEdit);
        table.setDefaultEditor(ButtonEdit.class, btReEdit);

        ButtonDeleteRenderer btReDelete = new ButtonDeleteRenderer(this);
        table.setDefaultRenderer(ButtonDelete.class, btReDelete);
        table.setDefaultEditor(ButtonDelete.class, btReDelete);

        LinkToIssueTrackerRenderer lkRenderer = new LinkToIssueTrackerRenderer(this);
        table.setDefaultRenderer(ExternalLink.class, lkRenderer);
        table.setDefaultEditor(ExternalLink.class, lkRenderer);

        table.setRowsPerPage(EffortUtils.ROWS_PER_PAGE);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        reloadPagingNavigator();
        form.add(pagingNavigator);

        form.add(table);

        form.add(new AjaxButton("buttonSave", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                saveListEntity(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form1) {
                // target.addComponent(form.getPage().get("feedbackPanel"));
            }
        });

        timeeffortContainer.add(form);
    }

    /**
     * Displays all effort textfields.
     */
    private void displayTextFields() {

        teDescription = new ValidationTextField<String>("tedescription", new Model<String>(""));
        teDescription.add(StringValidator.maximumLength(IntegerConstants.FIFTY));
        teDescription.setOutputMarkupId(true);
        form.add(teDescription);

        teDate = new ValidationDateTextField("tedate", new PropertyModel<Date>(this, "date"), new StyleDateConverter(
                "S-", true));
        teDate.add(new DatePicker());
        teDate.setOutputMarkupId(true);
        form.add(teDate);

        teTimeLength = new ValidationTextField<String>("tetimelength", new Model<String>(""));
        teTimeLength.setType(String.class);
        teTimeLength.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            }
        });
        teTimeLength.setOutputMarkupId(true);
        form.add(teTimeLength);

        teStartTime = new ValidationTextField<String>("testarttime", new Model<String>(""));
        teStartTime.setType(String.class);
        teStartTime.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            }
        });
        teStartTime.setOutputMarkupId(true);
        form.add(teStartTime);

        teEndTime = new ValidationTextField<String>("teendtime", new Model<String>(""));
        teEndTime.setType(String.class);
        teEndTime.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            }
        });
        teEndTime.setOutputMarkupId(true);
        form.add(teEndTime);
    }

    /**
     * Display all FilterFields.
     */
    private void displayFilterFields() {
        teFilterDescription = new TextField<String>("filterdescription", new Model<String>("")) {

        };
        teFilterDescription.add(StringValidator.maximumLength(IntegerConstants.FIFTY));
        teFilterDescription.setType(String.class);
        teFilterDescription.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                filterList(target);
            }
        });
        form.add(teFilterDescription);

        teFilterDateFrom = new DateTextField("filterdatefrom", new PropertyModel<Date>(this, "dateFrom"),
                new StyleDateConverter("S-", true));
        teFilterDateFrom.add(new DatePicker());
        teFilterDateFrom.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                filterList(target);
            }
        });
        form.add(teFilterDateFrom);

        form.add(new AjaxButton("buttonCancelDateFrom", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                dateFrom = null;
                target.addComponent(teFilterDateFrom);
                filterList(target);
            }
        });

        form.add(new AjaxButton("buttonCancelDateUntil", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                dateUntil = null;
                target.addComponent(teFilterDateUntil);
                filterList(target);
            }
        });

        teFilterDateUntil = new DateTextField("filterdateuntil", new PropertyModel<Date>(this, "dateUntil"),
                new StyleDateConverter("S-", true));
        teFilterDateUntil.add(new DatePicker());
        teFilterDateUntil.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                filterList(target);
            }
        });
        form.add(teFilterDateUntil);
    }

    /**
     * Filter effort list.
     * 
     * @param target AjaxRequestTarget
     */
    private void filterList(AjaxRequestTarget target) {
        timeeffortList = new ArrayList<Effort>();
        if (teFilterDescription.getModelObject().trim().compareTo("") == -1 && dateFrom == null && dateUntil == null) {
            timeeffortList = fullTimeEffortList.size() <= EffortUtils.MAXLISTSIZE ? fullTimeEffortList
                    : fullTimeEffortList.subList(0, EffortUtils.MAXLISTSIZE);
        } else {
            for (int i = 0; i < fullTimeEffortList.size(); i++) {
                int match = -1;
                if (teFilterDescription.getModelObject().trim().compareTo("") != -1) {
                    if (fullTimeEffortList.get(i).matchDescription(teFilterDescription.getModelObject())) {
                        match = 1;
                    } else {
                        match = 0;
                    }
                }
                if (dateFrom != null && match != 0) {
                    if (fullTimeEffortList.get(i).matchDateFrom(dateFrom)) {
                        match = 1;
                    } else {
                        match = 0;
                    }
                }
                if (dateUntil != null && match != 0) {
                    if (fullTimeEffortList.get(i).matchDateUntil(dateUntil)) {
                        match = 1;
                    } else {
                        match = 0;
                    }
                }
                if (match == 1) {
                    if (timeeffortList.size() <= EffortUtils.MAXLISTSIZE) {
                        timeeffortList.add(fullTimeEffortList.get(i));
                    }
                }
            }
        }
        tm.reload(timeeffortList);
        target.addComponent(table);
        reloadPagingNavigator();
        target.addComponent(pagingNavigator);
    }

    /**
     * Reloads the paging navigator.
     */
    private void reloadPagingNavigator() {
        pagingNavigator = table.getRowsAjaxPagingNavigator("rowsPaging");
    }

    /**
     * Clear TextFields.
     */
    private void clearFields() {
        teDescription.setModelObject("");
        teStartTime.setModelObject("");
        teEndTime.setModelObject("");
        teTimeLength.setModelObject("");
    }

    /**
     * Sets all Validation text fields to valid state.
     */
    private void setAllTextFieldsValid() {
        teDescription.setTextFieldValid();
        teTimeLength.setTextFieldValid();
        teEndTime.setTextFieldValid();
        teStartTime.setTextFieldValid();
        teDate.setTextFieldValid();
    }

    // =========== DB METHODS ================================================

    /**
     * {@inheritDoc}
     */
    public void loadListEntities() {
        try {
            fullTimeEffortList = service.findEffortsForTiTAProjectAndTiTAUserOrdered(project.getId(), user.getId());

            timeeffortList = fullTimeEffortList.size() <= EffortUtils.MAXLISTSIZE ? fullTimeEffortList
                    : fullTimeEffortList.subList(0, EffortUtils.MAXLISTSIZE);
        } catch (PersistenceException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void saveListEntity(AjaxRequestTarget target) {
        Effort timeEffort = null;
        try {
            timeEffort = new Effort();

            Date newDate = teDate.getModelObject();
            if (newDate != null) {
                timeEffort.setDate(newDate);
                teDate.setTextFieldValid();
                target.addComponent(teDate);
            } else {
                teDate.setTextFieldInvalid();
                target.addComponent(teDate);
            }

            String description = teDescription.getModelObject();
            if (description != null) {
                timeEffort.setDescription(description);
                teDescription.setTextFieldValid();
                target.addComponent(teDescription);
            } else {
                teDescription.setTextFieldInvalid();
                target.addComponent(teDescription);
            }

            Long startTime = null;
            try {
                startTime = GlobalUtils.getTimeFromTextField(teStartTime);

                if (startTime != null) {
                    teStartTime.setTextFieldValid();
                    timeEffort.setStartTime(startTime);
                    target.addComponent(teStartTime);
                } else {
                    teStartTime.setTextFieldInvalid();
                    target.addComponent(teStartTime);
                }
            } catch (ParseException e) {
                teStartTime.setTextFieldInvalid();
                target.addComponent(teStartTime);
            }
            Long endTime = null;
            try {
                endTime = GlobalUtils.getTimeFromTextField(teEndTime);

                if (endTime != null) {
                    teEndTime.setTextFieldValid();
                    timeEffort.setEndTime(endTime);
                    target.addComponent(teEndTime);
                } else {
                    teEndTime.setTextFieldInvalid();
                    target.addComponent(teEndTime);
                }
            } catch (ParseException e) {
                teEndTime.setTextFieldInvalid();
                target.addComponent(teEndTime);
            }
            Long duration = null;
            try {
                duration = GlobalUtils.getDurationFromTextField(teTimeLength);

                if (duration != null) {
                    timeEffort.setDuration(duration);
                    teTimeLength.setTextFieldValid();
                    target.addComponent(teTimeLength);
                } else {
                    teTimeLength.setTextFieldInvalid();
                    target.addComponent(teTimeLength);
                }
            } catch (ParseException e) {
                teTimeLength.setTextFieldInvalid();
                target.addComponent(teTimeLength);
            }

            if (newDate != null && description != null && startTime != null && endTime != null && duration != null) {

                timeEffort.setDeleted(false);
                timeEffort.setUser(user);

                Set<Effort> set = new HashSet<Effort>();
                set.add(timeEffort);
                TiTATask task = new TiTATask(user, set);
                task.setTitaProject(project);
                timeEffort.setTitaTask(task);

                taskService.saveTiTATask(task);
                service.saveEffort(timeEffort);
                setAllTextFieldsValid();

                reloadTable(target);
                clearFields();
            }
        } catch (PersistenceException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void deleteListEntity(AjaxRequestTarget target) {
        Effort timeEffort = null;
        try {
            timeEffort = (Effort) tm.getValueAt(tm.getSelectedRow(), -1);

            timeEffort.setDeleted(true);
            service.saveEffort(timeEffort);

            reloadTable(target);
        } catch (PersistenceException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void updateListEntity(AjaxRequestTarget target) {
        Effort timeEffort = null;
        try {
            timeEffort = (Effort) tm.getValueAt(table.getSelectedRows()[0], -1);

            timeEffort.setDate(((LenientDateTextField) table.getSelectedComponent(IntegerConstants.ZERO))
                    .getModelObject());
            timeEffort.setDescription(((LenientTextField) table.getSelectedComponent(IntegerConstants.ONE))
                    .getModelObject().toString());

            Long startTime = null;
            try {
                startTime = GlobalUtils.getTimeFromTextField((LenientTextField) table
                        .getSelectedComponent(IntegerConstants.TWO));
            } catch (ParseException e) {
                // TODO Error Handling
            }
            Long endTime = null;
            try {
                endTime = GlobalUtils.getTimeFromTextField((LenientTextField) table
                        .getSelectedComponent(IntegerConstants.THREE));
            } catch (ParseException e) {
                // TODO Error Handling
            }
            Long duration = null;
            try {
                duration = GlobalUtils.getDurationFromTextField((LenientTextField) table
                        .getSelectedComponent(IntegerConstants.FOUR));
            } catch (ParseException e) {
                // TODO Error Handling
            }

            if (startTime != null && (endTime != null || duration != null)) {
                if (duration == null && endTime != null) {
                    duration = endTime - startTime - GlobalUtils.HOUR;
                } else if (endTime == null && duration != null) {
                    endTime = startTime + duration + GlobalUtils.HOUR;
                }

                timeEffort.setDuration(duration);
                timeEffort.setEndTime(endTime);
                timeEffort.setDeleted(false);
                timeEffort.setStartTime(startTime);
                service.saveEffort(timeEffort);

                reloadTable(target);
            }
        } catch (PersistenceException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void reloadTable(AjaxRequestTarget target) {
        loadListEntities();

        tm.reload(timeeffortList);
        target.addComponent(timeeffortContainer);
    }

    /**
     * Returns current project.
     * 
     * @return the project
     */
    public TiTAProject getProject() {
        return project;
    }

    /**
     * Returns current date.
     * 
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateUntil(Date dateUntil) {
        this.dateUntil = dateUntil;
    }

    public Date getDateUntil() {
        return dateUntil;
    }

    public List<Effort> getEntityList() {
        return this.timeeffortList;
    }
}

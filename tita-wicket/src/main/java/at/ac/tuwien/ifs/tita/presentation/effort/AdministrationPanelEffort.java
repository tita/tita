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
package at.ac.tuwien.ifs.tita.presentation.effort;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ListSelectionModel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.table.Table;
import org.wicketstuff.table.cell.renders.LenientTextField;

import at.ac.tuwien.ifs.tita.business.service.time.IEffortService;
import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.presentation.uihelper.ButtonDelete;
import at.ac.tuwien.ifs.tita.presentation.uihelper.ButtonDeleteRenderer;
import at.ac.tuwien.ifs.tita.presentation.uihelper.ButtonEdit;
import at.ac.tuwien.ifs.tita.presentation.uihelper.ButtonEditRenderer;
import at.ac.tuwien.ifs.tita.presentation.uihelper.DateTextFieldRenderer;
import at.ac.tuwien.ifs.tita.presentation.uihelper.IAdministrationPanel;
import at.ac.tuwien.ifs.tita.presentation.uihelper.LenientDateTextField;
import at.ac.tuwien.ifs.tita.presentation.utils.EffortUtils;
import at.ac.tuwien.ifs.tita.presentation.utils.GlobalUtils;
import at.ac.tuwien.ifs.tita.presentation.utils.IntegerConstants;

/**
 * 
 * Wicket Panel for time effort administration.
 * 
 * @author msiedler
 * 
 */
public class AdministrationPanelEffort extends Panel implements
        IAdministrationPanel {

    @SpringBean(name = "timeEffortService")
    private IEffortService service;

    // Actual Date
    private final Date date = new Date();

    private TiTAProject project = null;

    // Logger
    private final Logger log = LoggerFactory
            .getLogger(AdministrationPanelEffort.class);

    // Actual time effort list
    private List<Effort> timeeffortList = new ArrayList<Effort>();
    private List<Effort> unfilteredList = new ArrayList<Effort>();

    // Wicket Components
    private Form<Effort> form = null;

    private Table table = null;

    private TableModelEffort tm = null;

    private TextField<String> teDescription = null;
    private DateTextField teDate = null;
    private TextField<String> teTimeLength = null;
    private TextField<String> teStartTime = null;
    private TextField<String> teEndTime = null;

    private TextField<String> teFilterDescription = null;

    private WebMarkupContainer timeeffortContainer = null;

    public AdministrationPanelEffort(String id, TiTAProject project) {
        super(id);

        this.project = project;

        displayPanel();
    }

    /**
     * Displays panel.
     */
    private void displayPanel() {

        // get time efforts
        unfilteredList = getListEntities(EffortUtils.MAXLISTSIZE);
        timeeffortList = unfilteredList;

        // Data table
        displayDataTable(timeeffortList);
    }

    /**
     * Initializes the time effort data table.
     * 
     * @param timeEffortList
     *            the list to displayed in the table
     */
    private void displayDataTable(List<Effort> timeEffortList) {

        // add form components to the form as usual
        timeeffortContainer = new WebMarkupContainer("timeeffortContainer");
        timeeffortContainer.setOutputMarkupId(true);
        timeeffortContainer.setOutputMarkupPlaceholderTag(true);
        add(timeeffortContainer);

        form = new Form<Effort>("timeeffortForm",
                new CompoundPropertyModel<Effort>(new Effort()));
        add(form);
        form.setOutputMarkupId(true);

        teFilterDescription = new TextField<String>("filterdescription",
                new Model<String>("")) {

        };
        teFilterDescription.add(StringValidator
                .maximumLength(IntegerConstants.FIFTY));
        teFilterDescription.setType(String.class);
        teFilterDescription.add(new AjaxFormComponentUpdatingBehavior(
                "onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                timeeffortList = unfilteredList;
                for (int i = 0; i < unfilteredList.size(); i++) {
                    if (!unfilteredList.get(i).matchDescription(
                            teFilterDescription.getModelObject())) {
                        timeeffortList.remove(i);
                    }
                    tm.reload(timeeffortList);
                    target.addComponent(timeeffortContainer);
                }
            }
        });
        form.add(teFilterDescription);

        teDescription = new TextField<String>("tedescription",
                new Model<String>(""));
        teDescription
                .add(StringValidator.maximumLength(IntegerConstants.FIFTY));
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

        tm = new TableModelEffort(timeEffortList);
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

        table.setRowsPerPage(EffortUtils.ROWS_PER_PAGE);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        form.add(table.getRowsAjaxPagingNavigator("rowsPaging"));
        form.add(table);

        form.add(new AjaxButton("buttonSave", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                saveListEntity();
                reloadTable(target);
                clearFields();
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form1) {
                // target.addComponent(form.getPage().get("feedbackPanel"));
            }
        });

        timeeffortContainer.add(form);
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

    // =========== DB METHODS ================================================

    /**
     * {@inheritDoc}
     */
    public List<Effort> getListEntities(int maxresults) {
        List<Effort> list = null;
        try {
            list = service.getActualEfforts(maxresults);
        } catch (TitaDAOException e) {
            log.error(e.getMessage());
        }

        return list;
    }

    /**
     * {@inheritDoc}
     */
    public void saveListEntity() {
        Effort timeEffort = null;
        try {
            timeEffort = new Effort();

            timeEffort.setDate(teDate.getModelObject());
            timeEffort.setDescription(teDescription.getModelObject());

            Long startTime = GlobalUtils.getTimeFromTextField(teStartTime);
            Long endTime = GlobalUtils.getTimeFromTextField(teEndTime);
            Long duration = GlobalUtils.getDurationFromTextField(teTimeLength);

            if (startTime != null && (endTime != null || duration != null)) {
                if (duration == null && endTime != null) {
                    duration = endTime - startTime;
                }

                timeEffort.setDuration(duration);
                timeEffort.setDeleted(false);
                timeEffort.setStartTime(startTime);

                service.saveEffort(timeEffort);
            }
        } catch (TitaDAOException e) {
            log.error(e.getMessage());
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void deleteListEntity() {
        Effort timeEffort = null;
        try {
            timeEffort = (Effort) tm.getValueAt(tm.getSelectedRow(), -1);

            timeEffort.setDeleted(true);
            service.saveEffort(timeEffort);

            unfilteredList = getListEntities(EffortUtils.MAXLISTSIZE);
            timeeffortList = unfilteredList;
        } catch (TitaDAOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void updateListEntity() {
        Effort timeEffort = null;
        try {
            timeEffort = (Effort) tm.getValueAt(table.getSelectedRows()[0], -1);

            timeEffort.setDate(((LenientDateTextField) table
                    .getSelectedComponent(IntegerConstants.ONE))
                    .getModelObject());
            timeEffort.setDescription(((LenientTextField) table
                    .getSelectedComponent(IntegerConstants.TWO))
                    .getModelObject().toString());

            Long startTime = GlobalUtils
                    .getTimeFromTextField((LenientTextField) table
                            .getSelectedComponent(IntegerConstants.THREE));
            Long endTime = GlobalUtils
                    .getTimeFromTextField((LenientTextField) table
                            .getSelectedComponent(IntegerConstants.FOUR));

            if (startTime != null && endTime != null) {
                timeEffort.setDuration(endTime - startTime);
                timeEffort.setDeleted(false);
                timeEffort.setStartTime(startTime);
                service.saveEffort(timeEffort);
            }
        } catch (TitaDAOException e) {
            log.error(e.getMessage());
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void reloadTable(AjaxRequestTarget target) {
        unfilteredList = getListEntities(EffortUtils.MAXLISTSIZE);
        timeeffortList = unfilteredList;

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
}

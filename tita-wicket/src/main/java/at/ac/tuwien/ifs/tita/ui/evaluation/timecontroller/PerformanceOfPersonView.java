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

import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.servlet.ServletContext;
import javax.swing.ListSelectionModel;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.table.Table;

import at.ac.tuwien.ifs.tita.business.service.project.IProjectService;
import at.ac.tuwien.ifs.tita.business.service.tasks.ITaskService;
import at.ac.tuwien.ifs.tita.business.service.time.IEffortService;
import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.util.UserProjectTaskEffort;
import at.ac.tuwien.ifs.tita.issuetracker.exceptions.ProjectNotFoundException;
import at.ac.tuwien.ifs.tita.issuetracker.util.TiTATimeConverter;
import at.ac.tuwien.ifs.tita.reporting.JasperPdfResource;
import at.ac.tuwien.ifs.tita.ui.BasePage;
import at.ac.tuwien.ifs.tita.ui.login.TitaSession;
import at.ac.tuwien.ifs.tita.ui.models.TableModelPerformanceOfPerson;
import at.ac.tuwien.ifs.tita.ui.models.TableModelTiTAProject;
import at.ac.tuwien.ifs.tita.ui.models.TableModelTiTAUser;
import at.ac.tuwien.ifs.tita.ui.utils.GlobalUtils;
import at.ac.tuwien.ifs.tita.ui.utils.IntegerConstants;

/**
 * Page for Performance of person evaluation.
 * 
 * @author rene
 * 
 */
public class PerformanceOfPersonView extends BasePage {

    private final Logger log = LoggerFactory.getLogger(PerformanceOfPersonView.class);

    @SpringBean(name = "titaProjectService")
    private IProjectService titaProjectService;

    @SpringBean(name = "userService")
    private IUserService userService;

    @SpringBean(name = "timeEffortService")
    private IEffortService effortService;

    @SpringBean(name = "taskService")
    private ITaskService taskService;

    @SpringBean(name = "performanceOfPersonReport")
    private JasperPdfResource pdfResource;

    private Form<Object> form;
    private WebMarkupContainer popUserTableContainer;
    private WebMarkupContainer popContainer;

    private List<TiTAUser> titaUserList = new ArrayList<TiTAUser>();
    private List<TiTAProject> titaProjectList = new ArrayList<TiTAProject>();
    private List<UserProjectTaskEffort> upteResult = new ArrayList<UserProjectTaskEffort>();

    private Table tableForTiTAUser;
    private Table tableForTiTAProject;
    private Table tableForResults;

    private TableModelTiTAUser tmForTiTAUser;
    private TableModelTiTAProject tmForTiTAProject;
    private TableModelPerformanceOfPerson tmPop;

    private String[] columnNames;

    private Label lblActualHours;
    private Label lblTargetHours;
    private Label lblDescriptionForActualHours;
    private Label lblDescriptionForTargetHours;
    private Label lblDescriptionPop;
    private Label lblDescriptionPerformancePercent;
    private Label lblPerformancePercent;

    private String messageForlblDescriptionPop = "";
    private String messageForlblActualHours = "";
    private String messageForlblTargetHours = "";
    private String messageForlblPerformancePercent = "";

    private TiTAProject selectedProject;
    private TiTAUser selectedUser;

    private Button btnShowPoPAsPDF;

    public PerformanceOfPersonView() {
        form = new Form<Object>("popeForm");
        form.setOutputMarkupId(true);

        popContainer = new WebMarkupContainer("PoP");
        popContainer.setOutputMarkupId(true);
        popContainer.setOutputMarkupPlaceholderTag(true);

        popUserTableContainer = new WebMarkupContainer("popUserTableContainer");
        popUserTableContainer.setOutputMarkupId(true);
        popUserTableContainer.setOutputMarkupPlaceholderTag(true);

        loadTableValues();

        tmForTiTAUser = new TableModelTiTAUser(titaUserList);
        tmForTiTAProject = new TableModelTiTAProject(titaProjectList);

        columnNames = new String[] { "Id", "description", "duration" };
        tmPop = new TableModelPerformanceOfPerson(upteResult, columnNames);

        displayTables();

        add(tableForTiTAProject);
        tableForTiTAUser.setVisible(false);
        popUserTableContainer.add(tableForTiTAUser);
        popContainer.add(tableForResults);

        displayLabels();
        displayButtons();

        add(form);
        add(popContainer);
        add(popUserTableContainer);
    }

    /**
     * Method.
     */
    private void loadTableValues() {
        try {
            titaUserList = userService.getUndeletedUsers();
            titaProjectList = titaProjectService.findAllTiTAProjects();

            if (tmForTiTAUser != null && tmForTiTAProject != null) {
                tmForTiTAUser.reload(titaUserList);
                tmForTiTAProject.reload(titaProjectList);
            }

        } catch (PersistenceException e) {
            e.getMessage();
        }
    }

    /**
     * Displays the option tables.
     */
    public void displayTables() {
        tableForTiTAProject = new Table("popTableForTiTAProject", tmForTiTAProject) {
            @Override
            protected void onSelection(AjaxRequestTarget target) {
                if (!(tableForTiTAProject.getSelectedRows()[0] == tmForTiTAProject.getSelectedRow())) {
                    tmForTiTAProject.setSelectedRow(tableForTiTAProject.getSelectedRows()[0]);
                    tmForTiTAProject.reload();

                    TiTAProject project = (TiTAProject) tableForTiTAProject.getTableModel().getValueAt(
                            tableForTiTAProject.getSelectedRows()[0], IntegerConstants.FIFTY);

                    selectedProject = project;

                    List<TiTAUser> listOfUser = userService.findAllTiTAUsersForProject(project);
                    tmForTiTAUser.reload(listOfUser);

                    makeVisible(false);
                    tableForTiTAUser.setVisible(true);
                    popUserTableContainer.add(tableForTiTAUser);
                    updateResultElements();

                    target.addComponent(tableForTiTAProject);
                    target.addComponent(popUserTableContainer);
                    target.addComponent(popContainer);
                }
            }
        };

        tableForTiTAProject.setWidths(new String[] { "150", "150", "100" });
        tableForTiTAProject.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableForTiTAUser = new Table("popTableForTiTAUser", tmForTiTAUser) {
            @Override
            protected void onSelection(AjaxRequestTarget target) {
                if (!(tableForTiTAUser.getSelectedRows()[0] == tmForTiTAUser.getSelectedRow())) {
                    tmForTiTAUser.setSelectedRow(tableForTiTAUser.getSelectedRows()[0]);
                    tmForTiTAUser.reload();

                    selectedUser = (TiTAUser) tableForTiTAUser.getTableModel().getValueAt(
                            tableForTiTAUser.getSelectedRows()[0], IntegerConstants.FIFTY);
                }
            }
        };

        tableForTiTAUser.setWidths(new String[] { "100", "100", "100", "150", "100" });
        tableForTiTAUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableForResults = new Table("popTableForTiTAResult", tmPop) {
            @Override
            protected void onSelection(AjaxRequestTarget target) {
                // do nothing
            }
        };

        tableForResults.setWidths(new String[] { "95", "300", "80", "80", "78" });
        tableForResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableForResults.setVisible(false);

    }

    /**
     * Displays the labels for the target/actual comparison.
     */
    public void displayLabels() {
        lblActualHours = new Label("labelActualHours", new PropertyModel<String>(this, "messageForlblActualHours"));
        lblActualHours.setVisible(false);
        lblActualHours.setOutputMarkupId(true);

        lblTargetHours = new Label("labelTargetHours", new PropertyModel<String>(this, "messageForlblTargetHours"));
        lblTargetHours.setVisible(false);
        lblTargetHours.setOutputMarkupId(true);

        lblDescriptionForActualHours = new Label("labelDescriptionActualHours", "ActualHours:");
        lblDescriptionForActualHours.setVisible(false);

        lblDescriptionForTargetHours = new Label("labelDescriptionTargetHours", "TargetHours:");
        lblDescriptionForTargetHours.setVisible(false);

        lblDescriptionPop = new Label("labelDescriptionPop", new PropertyModel<String>(this,
                "messageForlblDescriptionPop"));
        lblDescriptionPop.setVisible(false);
        lblDescriptionPop.setOutputMarkupId(true);

        lblDescriptionPerformancePercent = new Label("labelDescriptionPerformancePercent", "Percentage:");
        lblDescriptionPerformancePercent.setVisible(false);

        lblPerformancePercent = new Label("labelPerformancePercent", new PropertyModel<String>(this,
                "messageForlblPerformancePercent"));
        lblPerformancePercent.setVisible(false);
        lblPerformancePercent.setOutputMarkupId(true);

        popContainer.add(lblActualHours);
        popContainer.add(lblTargetHours);
        popContainer.add(lblDescriptionForActualHours);
        popContainer.add(lblDescriptionForTargetHours);
        popContainer.add(lblDescriptionPop);
        popContainer.add(lblDescriptionPerformancePercent);
        popContainer.add(lblPerformancePercent);
    }

    /**
     * Displays the buttons for the target/actual comparison.
     */
    public void displayButtons() {
        btnShowPoPAsPDF = new Button("btnShowPoPAsPDF") {
            @Override
            public void onSubmit() {
                try {
                    loadReport();
                    ResourceStreamRequestTarget rsrtarget = new ResourceStreamRequestTarget(pdfResource
                            .getResourceStream());
                    rsrtarget.setFileName(pdfResource.getFilename());
                    RequestCycle.get().setRequestTarget(rsrtarget);
                } catch (JRException e) {
                    // TODO: GUI Exception Handling
                    log.error(e.getMessage());
                } catch (PersistenceException e) {
                    // TODO: GUI Exception Handling
                    log.error(e.getMessage());
                }
            }

            @Override
            public boolean isEnabled() {
                return tableForResults.isVisible();
            }
        };

        form.add(btnShowPoPAsPDF);

        form.add(new AjaxButton("btnShowPoP", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                if (loadPerformanceOfPerson()) {
                    updateResultElements();
                    target.addComponent(popContainer);
                }

                updateResultElements();
                target.addComponent(popContainer);
                target.addComponent(btnShowPoPAsPDF);
            }
        });

        form.add(new AjaxButton("btnRefreshTables", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                loadTableValues();

                makeVisible(false);
                tableForTiTAUser.setVisible(false);
                popUserTableContainer.add(tableForTiTAUser);
                updateResultElements();

                target.addComponent(tableForTiTAProject);
                target.addComponent(popUserTableContainer);
                target.addComponent(popContainer);
                target.addComponent(btnShowPoPAsPDF);
            }
        });

    }

    /**
     * Loads the Performance Of Person view.
     * 
     * @return true if a project and a user was found.
     */
    private boolean loadPerformanceOfPerson() {

        if (tableForTiTAUser.getSelectedRows().length > 0 && tableForTiTAProject.getSelectedRows().length > 0) {
            TiTAProject project = (TiTAProject) tableForTiTAProject.getTableModel().getValueAt(
                    tableForTiTAProject.getSelectedRows()[0], IntegerConstants.FIFTY);

            TiTAUser titaUser = (TiTAUser) tableForTiTAUser.getTableModel().getValueAt(
                    tableForTiTAUser.getSelectedRows()[0], IntegerConstants.FIFTY);

            if (project != null && titaUser != null) {
                try {
                    upteResult = taskService.getPerformanceOfPersonView(project, titaUser, TitaSession.getSession()
                            .getUsername());
                } catch (ProjectNotFoundException e) {
                    log.error(e.getMessage());
                }

                if (upteResult != null && upteResult.size() > 0) {
                    tmPop.reload(upteResult);
                    makeVisible(true);

                    Long duration = effortService.totalizeEffortsForTiTAProjectAndTiTAUser(project.getId(), titaUser
                            .getId());
                    setMessageForlblActualHours(GlobalUtils.getDurationAsString(duration));

                    Long targetHours = userService.findTargetHoursForTiTAProjectAndTiTAUser(titaUser.getId(), project
                            .getId());

                    if (targetHours == null) {
                        setMessageForlblTargetHours("No value for the target hours saved.");
                        setMessageForlblPerformancePercent("");
                    } else {
                        setMessageForlblTargetHours(targetHours.toString() + ":00");
                        Double hoursInMillis = Double.valueOf(TiTATimeConverter.getMillisFromHour(targetHours)
                                .toString());
                        // CHECKSTYLE:OFF
                        Double percent = Double.valueOf(duration / hoursInMillis * 100);
                        // CHECKSTYLE:ON
                        setMessageForlblPerformancePercent(new DecimalFormat("#.00").format(percent) + "%");
                    }

                    setMessageForlblDescriptionPop("Performance of Person:");

                    return true;
                } else {
                    lblDescriptionPop.setVisible(true);
                    setMessageForlblDescriptionPop("No efforts found for the " + "chosen user and project.");

                }
            }
        }

        return false;
    }

    /**
     * Shows the elements of the result or set them to visible false.
     * 
     * @param visible if true, the elements are shown otherwise not.
     */
    public void makeVisible(boolean visible) {
        tableForResults.setVisible(visible);
        lblActualHours.setVisible(visible);
        lblTargetHours.setVisible(visible);
        lblDescriptionForActualHours.setVisible(visible);
        lblDescriptionForTargetHours.setVisible(visible);
        lblDescriptionPop.setVisible(visible);
        lblDescriptionPerformancePercent.setVisible(visible);
        lblPerformancePercent.setVisible(visible);
    }

    /**
     * Update elements on the markup container.
     */
    public void updateResultElements() {
        popContainer.add(tableForResults);
        popContainer.add(lblActualHours);
        popContainer.add(lblTargetHours);
        popContainer.add(lblDescriptionForActualHours);
        popContainer.add(lblDescriptionForTargetHours);
        popContainer.add(lblDescriptionPop);
        popContainer.add(lblDescriptionPerformancePercent);
        popContainer.add(lblPerformancePercent);
    }

    /**
     * loads report and sets data source.
     * 
     * @throws JRException JasperReports Exception
     */
    private void loadReport() throws JRException {
        ServletContext context = ((WebApplication) getApplication()).getServletContext();
        pdfResource.loadReport(context.getRealPath(pdfResource.getDesignFilename()));
        pdfResource.addReportParameter("actualHours", getMessageForlblActualHours());
        pdfResource.addReportParameter("targetHours", getMessageForlblTargetHours());
        pdfResource.addReportParameter("percentage", getMessageForlblPerformancePercent());
        pdfResource.addReportParameter("project", selectedProject.getName());
        pdfResource.addReportParameter("user", selectedUser.getFirstName() + " " + selectedUser.getLastName());

        try {
            pdfResource.getReportParameters().put("imageLocation",
                    context.getResource("/images/time-wave.jpg").toString());
        } catch (MalformedURLException e) {
            e.getMessage();
        }
        pdfResource.setReportDataSource(new JRTableModelDataSource(tmPop));
    }

    public void setMessageForlblDescriptionPop(String messageForlblDescriptionPop) {
        this.messageForlblDescriptionPop = messageForlblDescriptionPop;
    }

    public String getMessageForlblDescriptionPop() {
        return messageForlblDescriptionPop;
    }

    public void setMessageForlblActualHours(String messageForlblActualHours) {
        this.messageForlblActualHours = messageForlblActualHours;
    }

    public String getMessageForlblActualHours() {
        return messageForlblActualHours;
    }

    public void setMessageForlblTargetHours(String messageForlblTargetHours) {
        this.messageForlblTargetHours = messageForlblTargetHours;
    }

    public String getMessageForlblTargetHours() {
        return messageForlblTargetHours;
    }

    public void setMessageForlblPerformancePercent(String messageForlblPerformancePercent) {
        this.messageForlblPerformancePercent = messageForlblPerformancePercent;
    }

    public String getMessageForlblPerformancePercent() {
        return messageForlblPerformancePercent;
    }

}

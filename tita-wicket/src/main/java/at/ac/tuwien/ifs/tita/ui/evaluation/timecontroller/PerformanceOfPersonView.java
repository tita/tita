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
import at.ac.tuwien.ifs.tita.business.service.time.IEffortService;
import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.reporting.JasperPdfResource;
import at.ac.tuwien.ifs.tita.ui.BasePage;
import at.ac.tuwien.ifs.tita.ui.models.AbstractTitaTableModel;
import at.ac.tuwien.ifs.tita.ui.models.TableModelTiTAProject;
import at.ac.tuwien.ifs.tita.ui.models.TableModelTiTAUser;
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

    @SpringBean(name = "targetActualComparison")
    private JasperPdfResource pdfResource;

    private Form<Object> form;
    private WebMarkupContainer popContainer;

    private List<TiTAUser> titaUserList = new ArrayList<TiTAUser>();
    private List<TiTAProject> titaProjectList = new ArrayList<TiTAProject>();
    private List<Effort> effortResult = new ArrayList<Effort>();

    private Table tableForTiTAUser;
    private Table tableForTiTAProject;
    private Table tableForResults;

    private TableModelTiTAUser tmForTiTAUser;
    private TableModelTiTAProject tmForTiTAProject;
    private AbstractTitaTableModel tmForTargetActualComparison;

    private Label lblActualHours;
    private Label lblTargetHours;
    private Label lblDescriptionForActualHours;
    private Label lblDescriptionForTargetHours;
    private Label lblDescriptionTargetActualComparison;

    private String messageForlblDescriptionTargetActualComparison = "";
    private String messageForlblActualHours = "";
    private String messageForlblTargetHours = "";

    public PerformanceOfPersonView() {
        form = new Form<Object>("popeForm");
        form.setOutputMarkupId(true);

        popContainer = new WebMarkupContainer("PoP");
        popContainer.setOutputMarkupId(true);
        popContainer.setOutputMarkupPlaceholderTag(true);

        loadTableValues();

        tmForTiTAUser = new TableModelTiTAUser(titaUserList);
        tmForTiTAProject = new TableModelTiTAProject(titaProjectList);
        // tmForTargetActualComparison = new
        // TableModelTargetActualComparison(effortResult);

        displayTables();

        add(tableForTiTAUser);
        add(tableForTiTAProject);
        // targetActualComparison.add(tableForResults);

        // displayLabels();
        displayButtons();

        add(form);
        add(popContainer);
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
        tableForTiTAUser = new Table("popTableForTiTAUser", tmForTiTAUser) {
            @Override
            protected void onSelection(AjaxRequestTarget target) {
                if (!(tableForTiTAUser.getSelectedRows()[0] == tmForTiTAUser.getSelectedRow())) {
                    tmForTiTAUser.setSelectedRow(tableForTiTAUser.getSelectedRows()[0]);
                    tmForTiTAUser.reload();

                    TiTAUser titaUser = (TiTAUser) tableForTiTAUser.getTableModel().getValueAt(
                            tableForTiTAUser.getSelectedRows()[0], IntegerConstants.FIFTY);

                    List<TiTAProject> listOfProjects = titaProjectService.findTiTAProjectsForUser(titaUser);

                    if (listOfProjects != null) {
                        tmForTiTAProject.reload(listOfProjects);
                    }

                    // makeVisible(false);
                    // updateResultElements();

                    target.addComponent(tableForTiTAProject);
                    target.addComponent(tableForTiTAUser);
                    target.addComponent(popContainer);
                }
            }
        };

        tableForTiTAUser.setWidths(new String[] { "100", "100", "100", "150", "100" });
        tableForTiTAUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableForTiTAProject = new Table("popTableForTiTAProject", tmForTiTAProject) {
            @Override
            protected void onSelection(AjaxRequestTarget target) {
                if (!(tableForTiTAProject.getSelectedRows()[0] == tmForTiTAProject.getSelectedRow())) {
                    tmForTiTAProject.setSelectedRow(tableForTiTAProject.getSelectedRows()[0]);
                    tmForTiTAProject.reload();

                    TiTAProject project = (TiTAProject) tableForTiTAProject.getTableModel().getValueAt(
                            tableForTiTAProject.getSelectedRows()[0], IntegerConstants.FIFTY);

                    List<TiTAUser> listOfUser = userService.findAllTiTAUsersForProject(project);
                    tmForTiTAUser.reload(listOfUser);

                    // makeVisible(false);
                    // updateResultElements();

                    target.addComponent(tableForTiTAProject);
                    target.addComponent(tableForTiTAUser);
                    target.addComponent(popContainer);
                }
            }
        };

        tableForTiTAProject.setWidths(new String[] { "150", "150", "100" });
        tableForTiTAProject.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        /*
         * tableForResults = new Table("taTableForTiTAResult",
         * tmForTargetActualComparison) {
         * 
         * @Override protected void onSelection(AjaxRequestTarget target) { if
         * (!(tableForResults.getSelectedRows()[0] ==
         * tmForTargetActualComparison .getSelectedRow())) {
         * tmForTargetActualComparison
         * .setSelectedRow(tableForResults.getSelectedRows()[0]);
         * tmForTargetActualComparison.reload();
         * 
         * target.addComponent(tableForResults); } } };
         * 
         * tableForResults.setWidths(new String[] { "95", "300", "80", "80",
         * "78" });
         * tableForResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION
         * ); tableForResults.setVisible(false);
         */
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

        lblDescriptionTargetActualComparison = new Label("labelDescriptionTargetActualComparison",
                new PropertyModel<String>(this, "messageForlblDescriptionTargetActualComparison"));
        lblDescriptionTargetActualComparison.setVisible(false);
        lblDescriptionTargetActualComparison.setOutputMarkupId(true);

        popContainer.add(lblActualHours);
        popContainer.add(lblTargetHours);
        popContainer.add(lblDescriptionForActualHours);
        popContainer.add(lblDescriptionForTargetHours);
        popContainer.add(lblDescriptionTargetActualComparison);
    }

    /**
     * Displays the buttons for the target/actual comparison.
     */
    public void displayButtons() {
        form.add(new AjaxButton("btnRefreshTables", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                loadTableValues();

                // makeVisible(false);
                // updateResultElements();

                target.addComponent(tableForTiTAProject);
                target.addComponent(tableForTiTAUser);
                // target.addComponent(targetActualComparison);
            }
        });

        form.add(new AjaxButton("btnShowPoP", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                /*
                 * if (loadTargetActualComparison()) { updateResultElements();
                 * target.addComponent(targetActualComparison); }
                 * 
                 * updateResultElements();
                 * target.addComponent(targetActualComparison);
                 */
            }
        });

        form.add(new Button("btnShowPoPAsPDF") {
            @Override
            public void onSubmit() {
                try {
                    loadReport();
                    ResourceStreamRequestTarget rsrtarget = new ResourceStreamRequestTarget(pdfResource
                            .getResourceStream());
                    rsrtarget.setFileName(pdfResource.getFilename());
                    RequestCycle.get().setRequestTarget(rsrtarget);
                } catch (JRException e) {
                    log.debug(e.getMessage());
                }
            }
        });
    }

    /**
     * loads report and sets data source.
     * 
     * @throws JRException JasperReports Exception
     */
    private void loadReport() throws JRException {
        ServletContext context = ((WebApplication) getApplication()).getServletContext();
        pdfResource.loadReport(context.getRealPath(pdfResource.getDesignFilename()));
        // pdfResource.getReportParameters().put("actualHours",
        // getMessageForlblActualHours());
        // pdfResource.getReportParameters().put("targetHours",
        // getMessageForlblTargetHours());

        try {
            pdfResource.getReportParameters().put("imageLocation",
                    context.getResource("/images/time-wave.jpg").toString());
        } catch (MalformedURLException e) {
            e.getMessage();
        }
        pdfResource.setReportDataSource(new JRTableModelDataSource(tmForTargetActualComparison));
    }
}

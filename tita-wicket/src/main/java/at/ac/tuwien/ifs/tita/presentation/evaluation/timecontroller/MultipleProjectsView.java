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
package at.ac.tuwien.ifs.tita.presentation.evaluation.timecontroller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.swing.ListSelectionModel;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.LoadableDetachableModel;
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
import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.util.UserProjectEffort;
import at.ac.tuwien.ifs.tita.issuetracker.util.TiTATimeConverter;
import at.ac.tuwien.ifs.tita.presentation.BasePage;
import at.ac.tuwien.ifs.tita.presentation.controls.dropdown.SelectOption;
import at.ac.tuwien.ifs.tita.presentation.models.MultipleProjectsEvaluationModel;
import at.ac.tuwien.ifs.tita.presentation.utils.EffortUtils;
import at.ac.tuwien.ifs.tita.reporting.JasperPdfResource;

/**
 * Page for Multiple project evaluation.
 * 
 * @author rene
 * 
 */
public class MultipleProjectsView extends BasePage {
    private final Logger log = LoggerFactory.getLogger(MultipleProjectsView.class);

    @SpringBean(name = "titaProjectService")
    private IProjectService titaProjectService;

    @SpringBean(name = "userService")
    private IUserService userService;

    @SpringBean(name = "timeEffortService")
    private IEffortService effortService;

    @SpringBean(name = "multipleProjectsReport")
    private JasperPdfResource pdfResource;

    private ListMultipleChoice<String> projectList, tcList;
    private DropDownChoice<SelectOption> ddTimeSpan;
    private Form<Object> form;
    private List<String> selectedProjects, selectedUsers;
    private Table table;
    private MultipleProjectsEvaluationModel mpem;
    private SelectOption selectedTimespan;

    @SuppressWarnings("unchecked")
    public MultipleProjectsView() {
        // add form to page
        form = new Form<Object>("multipleProjectsForm");
        form.setOutputMarkupId(true);
        add(form);

        // load projects from db
        projectList = new ListMultipleChoice("projectSelection", new PropertyModel(this, "selectedProjects"),
                new LoadableDetachableModel() {
                    @Override
                    protected Object load() {
                        return new ArrayList<String>();
                    }
                });
        projectList.setOutputMarkupId(true);

        // load users from db
        tcList = new ListMultipleChoice("timeConsumerSelection", new PropertyModel(this, "selectedUsers"),
                new LoadableDetachableModel() {
                    @Override
                    protected Object load() {
                        return new ArrayList<String>();
                    }
                });
        tcList.setOutputMarkupId(true);

        loadTiTAProjects();
        form.add(tcList);
        form.add(projectList);

        // set select options
        selectedTimespan = new SelectOption("overall", "Overall");
        ChoiceRenderer<SelectOption> choiceRenderer = new ChoiceRenderer<SelectOption>("value", "key");
        SelectOption[] options = new SelectOption[] { new SelectOption("day", "Daily"),
                new SelectOption("month", "Monthly"), new SelectOption("overall", "Overall") };

        ddTimeSpan = new DropDownChoice<SelectOption>("timeSpanSelection", new PropertyModel<SelectOption>(this,
                "selectedTimespan"), Arrays.asList(options), choiceRenderer);

        ddTimeSpan.setOutputMarkupId(true);
        form.add(ddTimeSpan);

        // add pdf button
        final Button btnShowAsPDF = new Button("btnShowPDF") {
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
                } catch (TitaDAOException e) {
                    // TODO: GUI Exception Handling
                    log.error(e.getMessage());
                }
            }

            @Override
            public boolean isEnabled() {
                return (mpem.getRowCount() == 0) ? false : true;
            }
        };

        form.add(btnShowAsPDF);

        // add ajax buttons
        form.add(new AjaxButton("btnShowEfforts", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                loadMultipleProjectEvaluation();
                target.addComponent(table);
                target.addComponent(btnShowAsPDF);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form1) {
            }
        });

        form.add(new AjaxButton("btnResetLists", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                projectList.getModel().setObject(new ArrayList<String>());
                tcList.getModel().setObject(new ArrayList<String>());
                ddTimeSpan.getModel().setObject(new SelectOption("overall", "Overall"));
                mpem.setColumnNames(new String[] {});
                mpem.reload(new ArrayList<UserProjectEffort>());
                target.addComponent(table);
                target.addComponent(projectList);
                target.addComponent(tcList);
                target.addComponent(ddTimeSpan);
                target.addComponent(btnShowAsPDF);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form1) {
            }
        });

        form.add(new AjaxButton("btnLoadUsers", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                loadTiTAUsers();
                target.addComponent(tcList);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form1) {
            }
        });

        form.add(new AjaxButton("btnRefreshProjects", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                loadTiTAProjects();
                target.addComponent(projectList);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form1) {
            }
        });

        mpem = new MultipleProjectsEvaluationModel(new ArrayList<UserProjectEffort>(), new String[] {});

        table = new Table("evaluationTable", mpem);
        table.setRowsPerPage(EffortUtils.ROWS_PER_PAGE);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        table.setWidths(new String[] { "100", "100", "100", "100", "100", "100" });

        form.add(table.getRowsAjaxPagingNavigator("rowsPaging"));
        form.add(table);
    }

    
    /**
     * Loads all tita users from db and displays them in a list.
     */
    private void loadTiTAProjects() {
        List<TiTAProject> titaProjects = titaProjectService.findAllTiTAProjects();
        List<String> tiP = new ArrayList<String>();

        for (TiTAProject ti : titaProjects) {
            tiP.add(ti.getName());
        }
        projectList.removeAll();
        projectList.setChoices(tiP);
    }

    /**
     * Loads an effort summary from db for every user and project and displays it in a list.
     */
    private void loadMultipleProjectEvaluation() {
        Boolean userProject = null;
        List<UserProjectEffort> upe = null;

        if (selectedProjects.size() > 0) {
            if (selectedUsers.size() > 0) {
                upe = effortService.getEffortsSummaryForProjectAndUserNames(selectedProjects, selectedUsers, ddTimeSpan
                        .getModel().getObject().getKey());
                userProject = true;
            } else {
                upe = effortService.getEffortsSummaryForProjectNames(selectedProjects, ddTimeSpan.getModel()
                        .getObject().getKey());
                userProject = false;
            }

            setReportDataSource(upe);
        }
        if (userProject != null) {
            createUserProjectEffortTable(ddTimeSpan.getModel().getObject().getKey(), userProject);
            if (upe != null) {
                if (upe.size() > 0) {
                    mpem.reload(upe);
                } else {
                    mpem.setColumnNames(new String[] {});
                    mpem.reload(new ArrayList<UserProjectEffort>());
                }
            }
        }
    }

   
    /**
     * Creats the list view for viewing all effort per user and tita project.
     * @param grouping String
     * @param userProject Boolean
     */
    private void createUserProjectEffortTable(String grouping, Boolean userProject) {
        if (!userProject) {
            if (grouping.equals("overall")) {
                mpem.setColumnNames(new String[] { "PROJECT", "DURATION" });
            } else if (grouping.equals("month")) {
                mpem.setColumnNames(new String[] { "PROJECT", "YEAR", "MONTH", "DURATION" });
            } else if (grouping.equals("day")) {
                mpem.setColumnNames(new String[] { "PROJECT", "YEAR", "MONTH", "DAY", "DURATION" });
            }
        } else {
            if (grouping.equals("overall")) {
                mpem.setColumnNames(new String[] { "PROJECT", "DURATION", "USERNAME" });
            } else if (grouping.equals("month")) {
                mpem.setColumnNames(new String[] { "PROJECT", "YEAR", "MONTH", "DURATION", "USERNAME" });
            } else if (grouping.equals("day")) {
                mpem.setColumnNames(new String[] { "PROJECT", "YEAR", "MONTH", "DAY", "DURATION", "USERNAME" });
            }
        }
    }

    /**
     * Load all tita users for a choosen tita project.
     */
    private void loadTiTAUsers() {
        List<TiTAUser> titaUsers = userService.findAllTiTAUsersForProjects(selectedProjects);
        List<String> tuS = new ArrayList<String>();

        for (TiTAUser tu : titaUsers) {
            tuS.add(tu.getUserName());
        }
        tcList.removeAll();
        tcList.setChoices(tuS);
    }

    /**
     * loads report and sets data source.
     * 
     * @throws JRException JasperReports Exception
     * @throws TitaDAOException if user cannot be found
     */
    private void loadReport() throws JRException, TitaDAOException {
        ServletContext context = ((WebApplication) getApplication()).getServletContext();
        pdfResource.loadReport(context.getRealPath(pdfResource.getDesignFilename()));
    }

    /**
     * Sets report data source for pdf.
     * 
     * @param upelist list of user pojects displayed in table.
     */
    private void setReportDataSource(List<UserProjectEffort> upelist) {
        List<Map<String, String>> reportRows = new ArrayList<Map<String, String>>();

        // for loop is faster than foreach with lists
        for (int i = 0; i < upelist.size(); i++) {
            Map<String, String> map = new HashMap<String, String>();
            UserProjectEffort upe = upelist.get(i);
            map.put("PROJECT", upe.getProject());
            map.put("DURATION", TiTATimeConverter.getDuration2String(upe.getDuration()));
            map.put("YEAR", (upe.getYear() != null) ? upe.getYear().toString() : null);
            map.put("MONTH", (upe.getMonth() != null) ? upe.getMonth().toString() : null);
            map.put("DAY", (upe.getDay() != null) ? upe.getDay().toString() : null);
            map.put("USERNAME", (upe.getUsername() != null) ? upe.getUsername().toString() : null);
            reportRows.add(map);
        }

        pdfResource.setReportDataSource(new JRMapCollectionDataSource(reportRows));
    }
}

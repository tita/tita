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
import java.util.List;

import javax.swing.ListSelectionModel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.table.Table;

import at.ac.tuwien.ifs.tita.business.service.project.IProjectService;
import at.ac.tuwien.ifs.tita.business.service.time.IEffortService;
import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.presentation.BasePage;
import at.ac.tuwien.ifs.tita.presentation.models.AbstractTitaTableModel;
import at.ac.tuwien.ifs.tita.presentation.models.TableModelTargetActualComparison;
import at.ac.tuwien.ifs.tita.presentation.models.TableModelTiTAProject;
import at.ac.tuwien.ifs.tita.presentation.models.TableModelTiTAUser;
import at.ac.tuwien.ifs.tita.presentation.utils.IntegerConstants;

/**
 * Page for target/actual comparison.
 *
 * @author Christoph
 *
 */
public class TargetActualView extends BasePage {

    @SpringBean(name = "titaProjectService")
    private IProjectService titaProjectService;

    @SpringBean(name = "userService")
    private IUserService userService;

    @SpringBean(name = "timeEffortService")
    private IEffortService effortService;

    private Form<Object> form;

    private List<TiTAUser> titaUserList = new ArrayList<TiTAUser>();
    private List<TiTAProject> titaProjectList = new ArrayList<TiTAProject>();
    private List<Effort> effortResult = new ArrayList<Effort>();

    private Table tableForTiTAUser;
    private Table tableForTiTAProject;
    private Table tableForResults;

    private TableModelTiTAUser tmForTiTAUser;
    private TableModelTiTAProject tmForTiTAProject;
    private AbstractTitaTableModel tmForTargetActualComparison;

    public TargetActualView() {
        // add form to page
        form = new Form<Object>("targetActualComparisonForm");
        form.setOutputMarkupId(true);

        final WebMarkupContainer targetActualComparison = new WebMarkupContainer(
                "targetActualComparison");
        targetActualComparison.setOutputMarkupId(true);
        targetActualComparison.setOutputMarkupPlaceholderTag(true);


        loadTableValues();

        tmForTiTAUser = new TableModelTiTAUser(titaUserList);
        tmForTiTAProject = new TableModelTiTAProject(titaProjectList);
        tmForTargetActualComparison = new TableModelTargetActualComparison(effortResult);

        displayTables();

        form.add(tableForTiTAUser);
        form.add(tableForTiTAProject);
        targetActualComparison.add(tableForResults);

        form.add(new AjaxButton("btnRefreshTables", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                loadTableValues();

                tableForResults.setVisible(false);

                target.addComponent(tableForTiTAProject);
                target.addComponent(tableForTiTAUser);
                target.addComponent(tableForResults);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form1) {

            }
        });

        form.add(new AjaxButton("btnShowEfforts", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                if (loadEfforts()) {
                    targetActualComparison.add(tableForResults);
                    target.addComponent(targetActualComparison);
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form1) {

            }
        });

        add(form);
        add(targetActualComparison);
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

        } catch (TitaDAOException e) {
            e.getMessage();
        }

    }

    /**
     * Displays the option tables.
     */
    public void displayTables() {
        tableForTiTAUser = new Table("taTableForTiTAUser", tmForTiTAUser) {
            @Override
            protected void onSelection(AjaxRequestTarget target) {
                if (!(tableForTiTAUser.getSelectedRows()[0] == tmForTiTAUser.getSelectedRow())) {
                    tmForTiTAUser.setSelectedRow(tableForTiTAUser.getSelectedRows()[0]);
                    tmForTiTAUser.reload();

                    TiTAUser titaUser = (TiTAUser) tableForTiTAUser.getTableModel().getValueAt(
                            tableForTiTAUser.getSelectedRows()[0], IntegerConstants.FIFTY);

                    List<TiTAProject> listOfProjects = titaProjectService
                            .findTiTAProjectsForUser(titaUser);

                    if (listOfProjects != null) {
                        tmForTiTAProject.reload(listOfProjects);
                    }

                    tableForResults.setVisible(false);

                    target.addComponent(tableForTiTAProject);
                    target.addComponent(tableForTiTAUser);
                    target.addComponent(tableForResults);
                }
            }
        };

        tableForTiTAUser.setWidths(new String[] { "150", "150", "150", "150", "150" });
        tableForTiTAUser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableForTiTAProject = new Table("taTableForTiTAProject", tmForTiTAProject) {
            @Override
            protected void onSelection(AjaxRequestTarget target) {
                if (!(tableForTiTAProject.getSelectedRows()[0] == tmForTiTAProject.getSelectedRow())) {
                    tmForTiTAProject.setSelectedRow(tableForTiTAProject.getSelectedRows()[0]);
                    tmForTiTAProject.reload();

                    TiTAProject project = (TiTAProject) tableForTiTAProject.getTableModel()
                            .getValueAt(tableForTiTAProject.getSelectedRows()[0],
                                    IntegerConstants.FIFTY);

                    List<TiTAUser> listOfUser = userService.findAllTiTAUsersForProject(project);
                    tmForTiTAUser.reload(listOfUser);

                    tableForResults.setVisible(false);

                    target.addComponent(tableForTiTAProject);
                    target.addComponent(tableForTiTAUser);
                    target.addComponent(tableForResults);
                }
            }
        };

        tableForTiTAProject.setWidths(new String[] { "150", "150", "150" });
        tableForTiTAProject.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableForResults = new Table("taTableForTiTAResult", tmForTargetActualComparison) {
            @Override
            protected void onSelection(AjaxRequestTarget target) {
                if (!(tableForResults.getSelectedRows()[0] == tmForTargetActualComparison
                        .getSelectedRow())) {
                    tmForTargetActualComparison
                            .setSelectedRow(tableForResults.getSelectedRows()[0]);
                    tmForTargetActualComparison.reload();

                    target.addComponent(tableForResults);
                }
            }
        };

        tableForResults.setWidths(new String[] { "95", "300", "80", "80", "78" });
        tableForResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableForResults.setVisible(false);
    }

    /**
     * Method.
     *
     * @return asd
     */
    private boolean loadEfforts() {

        TiTAProject project = (TiTAProject) tableForTiTAProject.getTableModel().getValueAt(
                tableForTiTAProject.getSelectedRows()[0], IntegerConstants.FIFTY);

        TiTAUser titaUser = (TiTAUser) tableForTiTAUser.getTableModel().getValueAt(
                tableForTiTAUser.getSelectedRows()[0], IntegerConstants.FIFTY);

        if (project != null && titaUser != null) {
            effortResult = effortService.findEffortsForTiTAProjectAndTiTAUser(project.getId(),
                    titaUser.getId());
            tmForTargetActualComparison.reload(effortResult);
            tableForResults.setVisible(true);
            return true;
        }

        return false;
    }

}

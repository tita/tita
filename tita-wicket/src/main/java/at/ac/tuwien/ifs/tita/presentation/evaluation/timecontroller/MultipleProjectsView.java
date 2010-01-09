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
import java.util.List;

import javax.swing.ListSelectionModel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.table.Table;

import at.ac.tuwien.ifs.tita.business.service.project.IProjectService;
import at.ac.tuwien.ifs.tita.business.service.time.IEffortService;
import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.util.UserProjectEffort;
import at.ac.tuwien.ifs.tita.presentation.BasePage;
import at.ac.tuwien.ifs.tita.presentation.controls.dropdown.SelectOption;
import at.ac.tuwien.ifs.tita.presentation.models.MultipleProjectsEvaluationModel;
import at.ac.tuwien.ifs.tita.presentation.utils.EffortUtils;

/**
 * Page for Multiple project evaluation.
 *
 * @author rene
 *
 */
public class MultipleProjectsView extends BasePage {

    @SpringBean(name = "titaProjectService")
    private IProjectService titaProjectService;

    @SpringBean(name = "userService")
    private IUserService userService;

    @SpringBean(name = "timeEffortService")
    private IEffortService effortService;

    private ListMultipleChoice<String> projectList, tcList;
    private DropDownChoice<SelectOption> ddTimeSpan;
    private Form<Object> form;
    private List<String> selectedProjects, selectedUsers;
    private Table table;
    private MultipleProjectsEvaluationModel mpem;
    private SelectOption selectedTimespan;

    @SuppressWarnings("unchecked")
    public MultipleProjectsView() {
        //add form to page
        form = new Form<Object>("multipleProjectsForm");
        form.setOutputMarkupId(true);
        add(form);

        //load projects from db
        projectList = new ListMultipleChoice("projectSelection",
                        new PropertyModel(this, "selectedProjects"), new LoadableDetachableModel(){
            @Override
            protected Object load() {
              return new ArrayList<String>();
            }
        });
        projectList.setOutputMarkupId(true);

        //load users from db
        tcList = new ListMultipleChoice("timeConsumerSelection",
                        new PropertyModel(this, "selectedUsers"), new LoadableDetachableModel(){
            @Override
            protected Object load() {
              return new ArrayList<String>();
            }
        });
        tcList.setOutputMarkupId(true);

        loadTiTAProjects();
        form.add(tcList);
        form.add(projectList);

        //set select options
        selectedTimespan = new SelectOption("overall", "Overall");
        ChoiceRenderer<SelectOption> choiceRenderer = new ChoiceRenderer<SelectOption>(
                                                                                    "value", "key");
        SelectOption[] options = new SelectOption[] {
                new SelectOption("day", "Daily"),
                new SelectOption("month", "Monthly"),
                new SelectOption("overall", "Overall")};

        ddTimeSpan = new DropDownChoice<SelectOption>(
                "timeSpanSelection", new PropertyModel<SelectOption>(this,"selectedTimespan"),
                Arrays.asList(options), choiceRenderer);

        ddTimeSpan.setOutputMarkupId(true);
        form.add(ddTimeSpan);

        //add ajax buttons
        form.add(new AjaxButton("btnShowEfforts", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                loadMultipleProjectEvaluation();
                target.addComponent(table);
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

        mpem = new MultipleProjectsEvaluationModel(new ArrayList<UserProjectEffort>(),
                                                   new String [] {});

        table = new Table("evaluationTable", mpem);
        table.setRowsPerPage(EffortUtils.ROWS_PER_PAGE);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        table.setWidths(new String [] {"100","100","100","100","100","100"});

        form.add(table.getRowsAjaxPagingNavigator("rowsPaging"));
        form.add(table);
    }

    /**
     * TODO - Write Javadoc.
     */
    private void loadTiTAProjects(){
        List<TiTAProject> titaProjects = titaProjectService.findAllTiTAProjects();
        List<String> tiP = new ArrayList<String>();

        for(TiTAProject ti : titaProjects){
            tiP.add(ti.getName());
        }
        projectList.removeAll();
        projectList.setChoices(tiP);
    }

    /**
     * TODO - Write Javadoc.
     */
    private void loadMultipleProjectEvaluation() {
        Boolean userProject = null;
        List<UserProjectEffort> upe = null;

        if(selectedProjects.size() > 0 ){
            if(selectedUsers.size() > 0){
                upe = effortService.getEffortsSummaryForProjectAndUserNames(selectedProjects,
                                       selectedUsers, ddTimeSpan.getModel().getObject().getKey());
                userProject = true;
            }else{
                upe = effortService.getEffortsSummaryForProjectNames(selectedProjects,
                                                      ddTimeSpan.getModel().getObject().getKey());
                userProject = false;
            }
        }
        if(userProject != null){
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
     * TODO - Write Javadoc.
     *
     * @param grouping
     *            - TODO - Write Javadoc.
     * @param userProject
     *            - TODO - Write Javadoc.
     */
    private void createUserProjectEffortTable(String grouping, Boolean userProject) {
        if(!userProject){
            if(grouping.equals("overall")){
                mpem.setColumnNames(new String [] {"PROJECT", "DURATION"});
            }else if(grouping.equals("month")){
                mpem.setColumnNames(new String [] {"PROJECT", "YEAR", "MONTH", "DURATION"});
            }else if(grouping.equals("day")){
                mpem.setColumnNames(new String [] {"PROJECT", "YEAR", "MONTH", "DAY", "DURATION"});
            }
        }else{
            if(grouping.equals("overall")){
                mpem.setColumnNames(new String [] {"PROJECT", "DURATION", "USERNAME"});
            }else if(grouping.equals("month")){
                mpem.setColumnNames(new String [] {"PROJECT", "YEAR", "MONTH", "DURATION",
                                                   "USERNAME"});
            }else if(grouping.equals("day")){
                mpem.setColumnNames(new String [] {"PROJECT", "YEAR", "MONTH", "DAY",
                                                   "DURATION", "USERNAME"});
            }
        }
    }

    /**
     * TODO - Write Javadoc.
     */
    private void loadTiTAUsers(){
        List<TiTAUser> titaUsers = userService.findAllTiTAUsersForProjects(selectedProjects);
        List<String> tuS = new ArrayList<String>();

        for(TiTAUser tu : titaUsers){
            tuS.add(tu.getUserName());
        }
        tcList.removeAll();
        tcList.setChoices(tuS);
    }
}

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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import at.ac.tuwien.ifs.tita.business.service.project.IProjectService;
import at.ac.tuwien.ifs.tita.business.service.time.IEffortService;
import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.presentation.BasePage;
import at.ac.tuwien.ifs.tita.presentation.controls.dropdown.SelectOption;

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
    
    private SelectOption selectedTimespan;
    private ListMultipleChoice<String> projectList, tcList;   
    private Form<Object> form;
    private List<String> selectedProjects, selectedUsers;
    
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
        
        selectedTimespan = new SelectOption("overall", "Overall");
        ChoiceRenderer<SelectOption> choiceRenderer = new ChoiceRenderer<SelectOption>(
                                                                                    "value", "key");
        SelectOption[] options = new SelectOption[] {
                new SelectOption("daily", "Daily"),
                new SelectOption("monthly", "Monthly"),
                new SelectOption("overall", "Overall")};
        
        DropDownChoice<SelectOption> ddTimeSpan = new DropDownChoice<SelectOption>(
                "timeSpanSelection", new PropertyModel<SelectOption>(this,"selectedTimespan"), 
                Arrays.asList(options), choiceRenderer);
        
        form.add(ddTimeSpan);
        
        form.add(new AjaxButton("btnShowProjectEfforts", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                // TODO
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form1) {
            }
        });
        
        form.add(new AjaxButton("btnShowUserEfforts", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                // TODO
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
    }
    
    private void loadTiTAProjects(){
        List<TiTAProject> titaProjects = titaProjectService.findAllTiTAProjects();
        List<String> tiP = new ArrayList<String>();
        
        for(TiTAProject ti : titaProjects){
            tiP.add(ti.getName());
        }
        projectList.removeAll();
        projectList.setChoices(tiP);
    }
    
    private void loadTiTAUsers(){
        List<TiTAUser> titaUsers = userService.findAllTiTAUsersForProjects(selectedProjects);
        List<String> tuS = new ArrayList<String>();
        
        for(TiTAUser tu : titaUsers){
            tuS.add(tu.getUserName());
        }
        tcList.removeAll();
        tcList.setChoices(tuS);
    }
    
    /**
     * Displays a list of effort per project per timeconsumer.
     */
    private void displayEffortList() {
//        final List<String> data = Arrays.asList(new String[] { "ID 1", "Project 1", "Time", "consumer1", "..." });
//        
//        ListView<String> dataView = new ListView<String>("multipleProjectList", data) {
//            @Override
//            protected void populateItem(ListItem<String> item) {
//                item.add(new Label("projectID", item.getDefaultModelObjectAsString()));
//                item.add(new Label("projectDescription", item.getDefaultModelObjectAsString()));
//                item.add(new Label("firstName", item.getDefaultModelObjectAsString()));
//                item.add(new Label("lastName", item.getDefaultModelObjectAsString()));
//                item.add(new Label("...", item.getDefaultModelObjectAsString()));
//            };
//        };
//
//       
//        add(dataView);
    }
}

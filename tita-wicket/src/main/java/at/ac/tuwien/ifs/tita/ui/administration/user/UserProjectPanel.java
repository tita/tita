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
package at.ac.tuwien.ifs.tita.ui.administration.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.PersistenceException;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.table.Table;

import at.ac.tuwien.ifs.tita.business.service.project.IProjectService;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.TiTAUserProject;
import at.ac.tuwien.ifs.tita.entity.conv.ProjectStatus;
import at.ac.tuwien.ifs.tita.ui.models.AbstractTitaTableModel;
import at.ac.tuwien.ifs.tita.ui.models.TableModelProject;
import at.ac.tuwien.ifs.tita.ui.utils.EffortUtils;
import at.ac.tuwien.ifs.tita.ui.utils.IntegerConstants;

/**
 * UserProjectPanel allows to select an Existing Project from a Table to add it
 * to a User.
 * 
 * @author ASE Group 10
 */
public class UserProjectPanel extends Panel {
    @SpringBean(name = "titaProjectService")
    private IProjectService service;

    // WebMarkupContainer
    private final WebMarkupContainer container;

    // Entity List
    private List<TiTAProject> list;

    // the Table Model and Table
    private final Table table;
    private final TableModelProjectWithoutButton tm;

    // the Table to Add Projects
    private final List<TiTAProject> actualProjects;
    private final Table actualProjectsTable;
    private final TableModelProjectWithoutButton actualProjectsTM;

    // the Parent Panel
    private final UserAdministrationPanel parent;

    // the user
    private final TiTAUser user;

    // Logger
    private final Logger log = LoggerFactory.getLogger(UserProjectPanel.class);

    /**
     * public constructor.
     * 
     * @param id
     *            the unique ID that is declared in the HTML-File for this
     *            Panel.
     * @param user
     *            the User to add the Projects
     * @param parent
     *            the Parent Panel for Returning a User on Close.
     */
    public UserProjectPanel(String id, TiTAUser user,
            UserAdministrationPanel parent) {
        super(id);

        this.parent = parent;
        if (user == null) {
            this.user = new TiTAUser();
        } else {
            this.user = user;
        }

        try {
            list = service.getOrderedProjects(0, "name");
        } catch (PersistenceException e) {
            log
                    .error("could not load List of TiTAProjects, empty List will be created");
            list = new ArrayList<TiTAProject>();
        }

        tm = new TableModelProjectWithoutButton(list);

        table = new Table("userProjectTable", tm) {
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

        ArrayList<TiTAProject> projects = new ArrayList<TiTAProject>();
        if (this.user.getTitaUserProjects() != null) {
            for (TiTAUserProject temp : this.user.getTitaUserProjects()) {
                projects.add(temp.getProject());
            }
        }
        // now initialize the Table that shows the actual Projects
        this.actualProjects = projects;

        actualProjectsTM = new TableModelProjectWithoutButton(actualProjects);
        actualProjectsTable = new Table("actualUserProjectTable",
                actualProjectsTM);
        actualProjectsTable.setWidths(EffortUtils.WIDTHS);
        container = new WebMarkupContainer("userProjectContainer");
        container.setOutputMarkupId(true);
        container.setOutputMarkupPlaceholderTag(true);

        addOrReplace(container);

        displayTables();
        addButtons();
    }

    /**
     * displays the two Tables to add Projects.
     */
    private void displayTables() {
        container.add(table);
        container.add(actualProjectsTable);
    }

    /**
     * inits the tables for showing and adds a clickhandler.
     */
    private void addButtons() {
        Form<String> form = new Form<String>("dummyForm");

        form.addOrReplace(new AjaxButton("buttonDelete") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                int selection = -1;
                try {
                    selection = actualProjectsTable.getSelectedRows()[0];
                } catch (IndexOutOfBoundsException e) {
                    log.debug("nothing selected");
                }
                log.debug("deleting Project from user " + user.getUserName()
                        + ", Selected Row was: " + selection);
                if (selection > -1) {
                    actualProjectsTM.removeProjectAtIndex(selection);
                    actualProjectsTM.reload();

                    target.addComponent(container);
                }
            }
        });

        form.addOrReplace(new AjaxButton("buttonAdd") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form1) {
                int selection = -1;
                try {
                    selection = table.getSelectedRows()[0];
                } catch (IndexOutOfBoundsException e) {
                    log.debug("nothing selected");
                }
                log.debug("adding Project to Table ");
                log.debug("Selected Row was: " + selection);
                if (selection > -1) {
                    TiTAProject project = tm.getProjectAt(selection);
                    if (!actualProjectsTM.containsProject(project)) {
                        actualProjectsTM.addEntity(project);
                        actualProjectsTM.reload();

                        target.addComponent(container);
                    }
                }
            }
        });

        form.addOrReplace(new Button("buttonOk") {
            @Override
            public void onSubmit() {
                Set<TiTAUserProject> userProjectsList = new HashSet<TiTAUserProject>();
                for (TiTAProject p : actualProjectsTM.getProjects()) {
                    TiTAUserProject up = new TiTAUserProject();
                    up.setUser(user);
                    up.setProject(p);

                    userProjectsList.add(up);
                }
                TiTAUser u = new TiTAUser(getUser().getUserName(), getUser()
                        .getPassword(), getUser().getFirstName(), getUser()
                        .getLastName(), getUser().getEmail(), getUser()
                        .isDeleted(), getUser().getRole(), userProjectsList,
                        getUser().getIssueTrackerLogins());

                parent.displayDetailsPage(u);
            }
        });

        container.addOrReplace(form);
    }

    /**
     * returns the current user.
     * 
     * @return the current user.
     */
    public TiTAUser getUser() {
        return this.user;
    }

    /*********************** INNER CLASS FOR HANDLING CUSTOM TM. ***************/
    public class TableModelProjectWithoutButton extends AbstractTitaTableModel {

        // Logger
        private final Logger log = LoggerFactory
                .getLogger(TableModelProject.class);

        public TableModelProjectWithoutButton(List<TiTAProject> list) {
            super(list);

            columnNames = new String[] { "Name", "Description", "Status" };
        }

        /** {@inheritDoc} **/
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == IntegerConstants.TWO) {
                return ProjectStatus.class;
            } else {
                return super.getColumnClass(columnIndex);
            }
        }

        /** {@inheritDoc} **/
        @Override
        public Object getValueAt(int row, int col) {
            TiTAProject project = null;

            try {
                project = (TiTAProject) list.get(row);

                if (col == IntegerConstants.ZERO) {
                    return project.getName();
                } else if (col == IntegerConstants.ONE) {
                    return project.getDescription();
                } else if (col == IntegerConstants.TWO) {
                    if (project.getProjectStatus() != null) {
                        return project.getProjectStatus().getDescription();
                    } else {
                        return null;
                    }
                } else {
                    return project;
                }
            } catch (IndexOutOfBoundsException e) {
                log.error(e.getMessage());
            } catch (ClassCastException e) {
                log.error(e.getMessage());
            } catch (NullPointerException e) {
                log.error(e.getMessage());
            }
            return null;
        }

        /** {@inheritDoc} **/
        @Override
        public void setValueAt(Object aValue, int row, int col) {
            TiTAProject project = null;

            try {
                project = (TiTAProject) list.get(row);

                if (col == IntegerConstants.ZERO) {
                    project.setName(aValue.toString());
                } else if (col == IntegerConstants.ONE) {
                    project.setDescription(aValue.toString());
                } else if (col == IntegerConstants.TWO) {
                    project.setProjectStatus((ProjectStatus) aValue);
                }
            } catch (IndexOutOfBoundsException e) {
                log.error(e.getMessage());
            } catch (ClassCastException e) {
                log.error(e.getMessage());
            }

        }

        /** {@inheritDoc} **/
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        /**
         * Adds a Project to the TableModel.
         * 
         * @param project
         *            the Project to Add.
         */
        @SuppressWarnings("unchecked")
        public void addProject(TiTAProject project) {
            try {
                ArrayList<TiTAProject> liste = (ArrayList<TiTAProject>) list;
                liste.add(project);
                this.reload(liste);
            } catch (ClassCastException e) {
                log
                        .error("Could not add Project to TableModel. ClassCast failed");
            }
        }

        /**
         * Removes a Project from the TableModel.
         * 
         * @param project
         *            the Project to remove.
         */
        public void removeProject(TiTAProject project) {
            if (list.contains(project)) {
                list.remove(project);
            }
        }

        /**
         * Removes a Project from the TableModel.
         * 
         * @param index
         *            of the Project to remove.
         */
        public void removeProjectAtIndex(int index) {
            if (list.size() > index) {
                list.remove(index);
            }
        }

        /**
         * Returns all Projects, that this TableModel currently holds.
         * 
         * @return the Projects.
         */
        @SuppressWarnings("unchecked")
        public List<TiTAProject> getProjects() {
            try {
                ArrayList<TiTAProject> liste = (ArrayList<TiTAProject>) list;
                return liste;
            } catch (ClassCastException e) {
                log
                        .error("Could not return the Current Projects. ClassCast of List failed");
                return null;
            }
        }

        /**
         * Returns the Project of a specific row index.
         * 
         * @param row
         *            the row index
         * @return the Project of the selected Row.
         */
        public TiTAProject getProjectAt(int row) {
            TiTAProject project = null;

            try {
                project = (TiTAProject) list.get(row);
                return project;
            } catch (IndexOutOfBoundsException e) {
                log.error(e.getMessage());
            } catch (ClassCastException e) {
                log.error(e.getMessage());
            } catch (NullPointerException e) {
                log.error(e.getMessage());
            }
            return null;
        }

        /**
         * Returns if a specific Project is currently in this Tablemodel.
         * 
         * @param project
         *            to check for availability.
         * @return true if project already in this Model.
         */
        public boolean containsProject(TiTAProject project) {
            return list.contains(project);
        }

        /**
         * Method for adding a Project to the TableModel.
         * 
         * @param project
         *            the Project to be displayed
         */
        @SuppressWarnings("unchecked")
        public void addEntity(TiTAProject project) {
            if (project != null) {
                ((List<TiTAProject>) list).add(project);
            }
        }

    }

}

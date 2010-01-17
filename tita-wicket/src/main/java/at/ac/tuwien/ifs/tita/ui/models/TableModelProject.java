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
package at.ac.tuwien.ifs.tita.ui.models;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.conv.ProjectStatus;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonDelete;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonEdit;
import at.ac.tuwien.ifs.tita.ui.utils.IntegerConstants;

/**
 * Provides all necessary table model methods for entity Project.
 * 
 * @author ASE Group 10
 */
public class TableModelProject extends AbstractTitaTableModel {

    // Logger
    private final Logger log = LoggerFactory.getLogger(TableModelProject.class);

    public TableModelProject(List<TiTAProject> list) {
        super(list);

        columnNames = new String[] { "Name", "Description", "Status", "", "" };
    }

    /** {@inheritDoc} **/
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == IntegerConstants.TWO) {
            return ProjectStatus.class;
        }
        if (columnIndex == IntegerConstants.THREE) {
            return ButtonEdit.class;
        }
        if (columnIndex == IntegerConstants.FOUR) {
            return ButtonDelete.class;
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
            } else if (col == IntegerConstants.THREE) {
                return null;
            } else if (col == IntegerConstants.FOUR) {
                return null;
            } else {
                return project;
            }
        } catch (IndexOutOfBoundsException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (ClassCastException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            e.printStackTrace();
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
            e.printStackTrace();
        } catch (ClassCastException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

    }

    /** {@inheritDoc} **/
    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == IntegerConstants.THREE || column == IntegerConstants.FOUR) {
            return true;
        }
        return false;
    }

    /**
     * Adds a Project to the TableModel.
     * 
     * @param project the Project to Add.
     */
    @SuppressWarnings("unchecked")
    public void addProject(TiTAProject project) {
        try {
            ArrayList<TiTAProject> liste = (ArrayList<TiTAProject>) list;
            liste.add(project);
            this.reload(liste);
        } catch (ClassCastException e) {
            log.error("Could not add Project to TableModel. ClassCast failed");
        }
    }

    /**
     * Removes a Project from the TableModel.
     * 
     * @param project the Project to remove.
     */
    public void removeProject(TiTAProject project) {
        if (list.contains(project)) {
            list.remove(project);
        }
    }

    /**
     * Removes a Project from the TableModel.
     * 
     * @param index of the Project to remove.
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
            log.error("Could not return the Current Projects. ClassCast of List failed");
            return null;
        }
    }

    /**
     * Returns the Project of a specific row index.
     * 
     * @param row the row index
     * @return the Project of the selected Row.
     */
    public TiTAProject getProjectAt(int row) {
        TiTAProject project = null;

        try {
            project = (TiTAProject) list.get(row);
            return project;
        } catch (IndexOutOfBoundsException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (ClassCastException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns if a specific Project is currently in this Tablemodel.
     * 
     * @param project to check for availability.
     * @return true if project already in this Model.
     */
    public boolean containsProject(TiTAProject project) {
        return list.contains(project);
    }

    /**
     * Method for adding a Project to the TableModel.
     * 
     * @param project the Project to be displayed
     */
    @SuppressWarnings("unchecked")
    public void addEntity(TiTAProject project) {
        if (project != null) {
            ((List<TiTAProject>) list).add(project);
        }
    }

}

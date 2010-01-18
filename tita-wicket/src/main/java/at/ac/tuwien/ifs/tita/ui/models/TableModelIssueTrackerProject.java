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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonDelete;
import at.ac.tuwien.ifs.tita.ui.utils.IntegerConstants;

/**
 * Provides all necessary table model methods for entity IssueTrackerProject.
 * 
 * @author ASE Group 10
 */
public class TableModelIssueTrackerProject extends AbstractTitaTableModel {

    // Logger
    private final Logger log = LoggerFactory.getLogger(TableModelIssueTrackerProject.class);

    public TableModelIssueTrackerProject(List<IssueTrackerProject> list) {
        super(list);
        columnNames = new String[] { "Project Name", "Issue Tracker", "" };
    }

    /** {@inheritDoc} **/
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == IntegerConstants.TWO) {
            return ButtonDelete.class;
        } else {
            return super.getColumnClass(columnIndex);
        }
    }

    /** {@inheritDoc} **/
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        IssueTrackerProject project = null;
        try {
            project = (IssueTrackerProject) list.get(rowIndex);

            if (columnIndex == IntegerConstants.ZERO) {
                return project.getProjectName();
            } else if (columnIndex == IntegerConstants.ONE) {
                return project.getIssueTracker().getDescription();
            } else if (columnIndex == IntegerConstants.TWO) {
                return null;
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
    public boolean isCellEditable(int row, int column) {
        if (column == IntegerConstants.TWO) {
            return true;
        }
        return false;
    }

    /**
     * removes a IssueTrackerProject at a specific Index.
     * 
     * @param index the index.
     */
    public void deleteAt(int index) {
        if (index > -1 || index < list.size()) {
            list.remove(index);
        }
    }

    /**
     * adds a new Project to the model and refreshes itself.
     * 
     * @param project the project to add.
     */
    @SuppressWarnings("unchecked")
    public void add(IssueTrackerProject project) {
        ((List<IssueTrackerProject>) list).add(project);
        reload(list);
    }
}

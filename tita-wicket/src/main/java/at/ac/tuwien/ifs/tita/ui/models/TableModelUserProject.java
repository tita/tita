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

import at.ac.tuwien.ifs.tita.entity.TiTAUserProject;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonDelete;
import at.ac.tuwien.ifs.tita.ui.utils.IntegerConstants;

/**
 * Provides all necessary table model methods for entity TiTAUserProject.
 * 
 * @author ASE Group 10
 * 
 */
public class TableModelUserProject extends AbstractTitaTableModel {

    // Logger
    private final Logger log = LoggerFactory.getLogger(TableModelUserProject.class);

    public TableModelUserProject(List<TiTAUserProject> list) {
        super(list);

        columnNames = new String[] { "Project Name", "" };
    }

    /** {@inheritDoc} **/
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == IntegerConstants.ONE) {
            return ButtonDelete.class;
        } else {
            return super.getColumnClass(columnIndex);
        }
    }

    /** {@inheritDoc} **/
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TiTAUserProject userProject = null;
        try {
            userProject = (TiTAUserProject) list.get(rowIndex);

            if (columnIndex == IntegerConstants.ZERO) {
                if (userProject.getProject() != null) {
                    return userProject.getProject().getName();
                } else {
                    return null;
                }
            } else if (columnIndex == IntegerConstants.ONE) {
                return null;
            } else {
                return userProject;
            }

        } catch (IndexOutOfBoundsException e) {
            log.error(e.getMessage() + " indexoutofbounds test");
        } catch (ClassCastException e) {
            log.error(e.getMessage() + " classcast test");
        } catch (NullPointerException e) {
            log.error(e.getMessage() + " nullpointer test");
        }
        return null;
    }

    /** {@inheritDoc} **/
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /**
     * Gets the Current List of Entities in the Table.
     * 
     * @return the list of entites showed in the table.
     */
    @SuppressWarnings("unchecked")
    public List<TiTAUserProject> getList() {
        return (List<TiTAUserProject>) list;
    }

}

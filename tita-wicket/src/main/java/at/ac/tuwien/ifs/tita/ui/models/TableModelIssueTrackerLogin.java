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

import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonDelete;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonEdit;
import at.ac.tuwien.ifs.tita.ui.utils.IntegerConstants;

/**
 * Provides all necessary table model methods for entity IssueTrackerLogin.
 * 
 * @author ASE Group 10
 */
public class TableModelIssueTrackerLogin extends AbstractTitaTableModel {

    // Logger
    private final Logger log = LoggerFactory.getLogger(TableModelIssueTrackerLogin.class);

    public TableModelIssueTrackerLogin(List<IssueTrackerLogin> list) {
        super(list);
        columnNames = new String[] { "IssueTracker", "Username", "", "" };
    }

    /** {@inheritDoc} **/
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == IntegerConstants.TWO) {
            return ButtonEdit.class;
        } else if (columnIndex == IntegerConstants.THREE) {
            return ButtonDelete.class;
        } else {
            return super.getColumnClass(columnIndex);
        }
    }

    /** {@inheritDoc} **/
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        IssueTrackerLogin login = null;
        try {
            login = (IssueTrackerLogin) list.get(rowIndex);

            if (columnIndex == IntegerConstants.ZERO) {
                if (login.getIssueTracker() != null) {
                    return login.getIssueTracker().getDescription();
                } else {
                    return null;
                }
            } else if (columnIndex == IntegerConstants.ONE) {
                return login.getUserName();
            } else if (columnIndex == IntegerConstants.TWO) {
                return null;
            } else if (columnIndex == IntegerConstants.THREE) {
                return null;
            } else {
                return login;
            }

        } catch (IndexOutOfBoundsException e) {
            log.error("Error IndexOutOfBoundsException occured: " + e.getMessage());
        } catch (ClassCastException e) {
            log.error("Error ClassCastException occured: " + e.getMessage());
        } catch (NullPointerException e) {
            log.error("Error NullPointerException occured: " + e.getMessage());
        }
        return null;
    }

    /** {@inheritDoc} **/
    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == IntegerConstants.TWO || column == IntegerConstants.THREE) {
            return true;
        }
        return false;
    }

    /**
     * Gets the Current List of Entities in the Table.
     * 
     * @return the list of entites showed in the table.
     */
    @SuppressWarnings("unchecked")
    public List<IssueTrackerLogin> getList() {
        return (List<IssueTrackerLogin>) list;
    }
}

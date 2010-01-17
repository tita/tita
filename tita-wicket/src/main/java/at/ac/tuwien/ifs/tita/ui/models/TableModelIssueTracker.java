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

import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonDelete;
import at.ac.tuwien.ifs.tita.ui.utils.IntegerConstants;

/**
 * Provides all necessary table model methods for entity IssueTracker.
 * 
 * @author ASE Group 10
 */
public class TableModelIssueTracker extends AbstractTitaTableModel {

    // Logger
    private final Logger log = LoggerFactory.getLogger(TableModelIssueTracker.class);

    public TableModelIssueTracker(final List<IssueTracker> list) {
        super(list);

        columnNames = new String[] { "Description", "URL", "" };
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
    public Object getValueAt(int row, int col) {
        IssueTracker issueTracker = null;

        try {
            issueTracker = (IssueTracker) list.get(row);

            if (col == IntegerConstants.ZERO) {
                return issueTracker.getDescription();
            } else if (col == IntegerConstants.ONE) {
                return issueTracker.getUrl();
            } else if (col == IntegerConstants.TWO) {
                return null;
            } else {
                return issueTracker;
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

}

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

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonDelete;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonEdit;
import at.ac.tuwien.ifs.tita.ui.utils.IntegerConstants;

/**
 * Provides all necessary table model methods for entity Effort.
 *
 * @author msiedler
 *
 */
public class TableModelTiTAUser extends AbstractTitaTableModel {

    // Logger
    private final Logger log = LoggerFactory.getLogger(TableModelTiTAUser.class);

    public TableModelTiTAUser(List<TiTAUser> list) {
        super(list);

        columnNames = new String[] { "Username", "Firstname", "Lastname", "Email", "Role" };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == IntegerConstants.ZERO) {
            return Date.class;
        } else if (columnIndex == IntegerConstants.FIVE) {
            return ButtonEdit.class;
        } else if (columnIndex == IntegerConstants.SIX) {
            return ButtonDelete.class;
        } else {
            return super.getColumnClass(columnIndex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAt(int row, int col) {
        TiTAUser titaUser = null;

        try {
            titaUser = (TiTAUser) list.get(row);

            if (col == IntegerConstants.ZERO) {
                return titaUser.getUserName();
            } else if (col == IntegerConstants.ONE) {
                return titaUser.getFirstName();
            } else if (col == IntegerConstants.TWO) {
                return titaUser.getLastName();
            } else if (col == IntegerConstants.THREE) {
                return titaUser.getEmail();
            } else if (col == IntegerConstants.FOUR) {
                return titaUser.getRole().getDescription();
            } else {
                return titaUser;
            }

        } catch (IndexOutOfBoundsException e) {
            log.error(e.getMessage());
        } catch (ClassCastException e) {
            log.error(e.getMessage());
        }
        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

}

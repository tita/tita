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
package at.ac.tuwien.ifs.tita.presentation.uihelper;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import at.ac.tuwien.ifs.tita.entity.interfaces.BaseEntity;

/**
 * 
 * Abstract class for all tita table models. Provides common table model
 * methods.
 * 
 * @author msiedler
 * 
 */
public abstract class AbstractTitaTableModel extends AbstractTableModel {

    protected List<? extends BaseEntity<Long>> list = null;
    protected String[] columnNames;
    protected int selectedRow = -1;

    protected AbstractTitaTableModel(List<? extends BaseEntity<Long>> list1) {
        super();
        columnNames = new String[] {};
        this.list = list1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRowCount() {
        return list.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getColumnName(int c) {
        return columnNames[c];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        if (column != 0) {
            if (selectedRow != -1) {
                if (selectedRow == row) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Marks the selected row.
     * 
     * @param selectedRow
     *            the selected row
     */
    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }

    /**
     * Get the selected row.
     * 
     * @return the selected row
     */
    public int getSelectedRow() {
        return this.selectedRow;
    }

    /**
     * Sets a new list and reloads the table.
     * 
     * @param list1
     *            the list to set
     */
    public void reload(List<? extends BaseEntity<Long>> list1) {
        this.list = list1;
        reload();
    };

    /**
     * Reloads the table.
     */
    public void reload() {
        fireTableDataChanged();
    }
}

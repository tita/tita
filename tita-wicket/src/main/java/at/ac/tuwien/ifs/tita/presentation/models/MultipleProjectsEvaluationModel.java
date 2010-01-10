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
package at.ac.tuwien.ifs.tita.presentation.models;

import java.lang.reflect.Field;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.entity.util.UserProjectEffort;
import at.ac.tuwien.ifs.tita.issuetracker.util.TiTATimeConverter;

/**
 * Implementation of AbstractTableModel for displaying multiple view on tita projects.
 * @author herbert
 *
 */
public class MultipleProjectsEvaluationModel extends AbstractTableModel {

    protected String[] columnNames;

    // Logger
    private final Logger log = LoggerFactory.getLogger(MultipleProjectsEvaluationModel.class);
    private List<UserProjectEffort> list;


    public MultipleProjectsEvaluationModel(List<UserProjectEffort> list,
                                           String [] colNames) {
        this.list = list;
        columnNames = colNames;
    }

    /** {@inheritDoc} */
    @Override
    public Object getValueAt(int row, int col) {
        Object obj = list.get(row);
        return extractFieldValueFromClass(obj.getClass(), obj, col);
    }

    /**
     * TODO - Write Javadoc.
     * 
     * @param clazz
     *            - TODO - Write Javadoc.
     * @param obj
     *            - TODO - Write Javadoc.
     * @param col
     *            - TODO - Write Javadoc.
     * @return - TODO - Write Javadoc.
     */
    private Object extractFieldValueFromClass(Class<?> clazz, Object obj, Integer col){
        Field fi;
        String fieldName = columnNames[col].toLowerCase();
        Object objField;

        try {
            fi = clazz.getDeclaredField(fieldName);
            fi.setAccessible(true);
            objField = fi.get(obj);
            if(fieldName.equals("duration")){
                String dur = TiTATimeConverter.getDuration2String((Long) objField);
                return dur;
            }
            return objField;
        }catch (Exception e) {
            return null;
        }
    }

    public void setColumnNames (String [] colNames){
        columnNames = colNames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getColumnName(int c) {
        return columnNames[c];
    }

    /** {@inheritDoc} */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /** {@inheritDoc} */
    @Override
    public int getRowCount() {
        return list.size();
    }

    /**
     * Sets a new list and reloads the table.
     *
     * @param list1 the list to set
     */
    public void reload(List<UserProjectEffort> list1) {
        list = list1;
        reload();
    };

    /**
     * Reloads the table.
     */
    private void reload() {
        fireTableDataChanged();
    }
}

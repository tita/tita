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

import at.ac.tuwien.ifs.tita.entity.util.ProjectEffort;

/**
 * Implementation of AbstractTableModel for displaying multiple view on tita projects.
 * @author herbert
 *
 */
public class MultipleProjectsEvaluationModel extends AbstractTableModel {
    // Logger
    private final Logger log = LoggerFactory.getLogger(MultipleProjectsEvaluationModel.class);
    
    private List<? extends ProjectEffort> list;
    protected String[] columnNames;
    
    public MultipleProjectsEvaluationModel(List<? extends ProjectEffort> list,
                                           String [] colNames) {
        this.list = list;
        this.columnNames = colNames;
    }

    /** {@inheritDoc} */
    @Override
    public Object getValueAt(int row, int col) {
        Object pEff = list.get(row);
        
//        if (pEff instanceof ProjectEffort){
//            ProjectEffort pe = (ProjectEffort) pEff;
//            
//            Class<?> clazz = pe.getClass();
//            try {
//                Field fi = clazz.getField(columnNames[col].toLowerCase());
//                
//                return fi.get(pe);
//            }catch(Exception e){
//                return null;
//            }
//        }else if(pEff instanceof UserProjectEffort){
//            UserProjectEffort spe = (UserProjectEffort) pEff;
//            
            Class<?> clazz = pEff.getClass();
            try {
                Field fi = clazz.getField(columnNames[col].toLowerCase());
                
                return fi.get(pEff);
            }catch(Exception e){
                return null;
            }
//        }else{
//            return null;
//        }        
    }
    
    public void setColumnNames (String [] colNames){
        columnNames = colNames;
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
    public void reload(List<? extends ProjectEffort> list1) {
        this.list = list1;
        reload();
    };

    /**
     * Reloads the table.
     */
    private void reload() {
        fireTableDataChanged();
    }
}

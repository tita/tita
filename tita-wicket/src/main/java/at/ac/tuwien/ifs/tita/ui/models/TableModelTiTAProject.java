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

import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.ui.utils.IntegerConstants;

/**
 * Provides all necessary table model methods for entity Effort.
 *
 * @author msiedler
 *
 */
public class TableModelTiTAProject extends AbstractTitaTableModel {

    // Logger
    private final Logger log = LoggerFactory.getLogger(TableModelTiTAProject.class);

    public TableModelTiTAProject(List<TiTAProject> list) {
        super(list);

        columnNames = new String[] { "Project Name", "Description", "Project Status" };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAt(int row, int col) {
        TiTAProject titaProject = null;

        try {
            titaProject = (TiTAProject) list.get(row);

            if (col == IntegerConstants.ZERO) {
                return titaProject.getName();
            } else if (col == IntegerConstants.ONE) {
                return titaProject.getDescription();
            } else if (col == IntegerConstants.TWO) {
                return titaProject.getProjectStatus().getDescription();
            } else {
                return titaProject;
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

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

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.ui.utils.GlobalUtils;
import at.ac.tuwien.ifs.tita.ui.utils.IntegerConstants;

/**
 * Provides all necessary table model methods for entity Effort.
 * 
 * @author Christoph
 * 
 */
public class TableModelEffortImport extends AbstractTitaTableModel {

    // Logger
    private final Logger log = LoggerFactory.getLogger(TableModelEffortImport.class);

    public TableModelEffortImport(List<Effort> list) {
        super(list);

        columnNames = new String[] { "Date", "Description", "Start Time", "End Time", "Duration" };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAt(int row, int col) {
        Effort effort = null;

        try {
            effort = (Effort) list.get(row);
            Calendar cal = GlobalUtils.getCalendarFromDate(effort.getDate());

            if (col == IntegerConstants.ZERO) {
                return GlobalUtils.DATEFORMAT.format(effort.getDate());
            } else if (col == IntegerConstants.ONE) {
                return effort.getDescription();
            } else if (col == IntegerConstants.TWO) {
                if (effort.getStartTime() == null) {
                    return "";
                } else {
                    return GlobalUtils.TIMEFORMAT24HOURS.format(effort.getStartTime());
                }
            } else if (col == IntegerConstants.THREE) {
                if (effort.getEndTime() == null) {
                    return "";
                } else {
                    return GlobalUtils.TIMEFORMAT24HOURS.format(effort.getEndTime());
                }
            } else if (col == IntegerConstants.FOUR) {
                if (effort.getDuration() == null) {
                    return "";
                } else {
                    return GlobalUtils.TIMEFORMAT24HOURS.format(GlobalUtils.getDateFromLong(effort
                            .getDuration()));
                }
            } else {
                return effort;
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

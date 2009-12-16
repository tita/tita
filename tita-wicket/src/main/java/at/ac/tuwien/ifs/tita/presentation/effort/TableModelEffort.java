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
package at.ac.tuwien.ifs.tita.presentation.effort;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.presentation.uihelper.AbstractTitaTableModel;
import at.ac.tuwien.ifs.tita.presentation.uihelper.ButtonDelete;
import at.ac.tuwien.ifs.tita.presentation.uihelper.ButtonEdit;
import at.ac.tuwien.ifs.tita.presentation.utils.GlobalUtils;
import at.ac.tuwien.ifs.tita.presentation.utils.IntegerConstants;

/**
 * Provides all necessary table model methods for entity Effort.
 * 
 * @author msiedler
 * 
 */
public class TableModelEffort extends AbstractTitaTableModel {

    // Logger
    private final Logger log = LoggerFactory.getLogger(TableModelEffort.class);

    public TableModelEffort(List<Effort> list) {
        super(list);

        columnNames = new String[] { "ID", "Date", "Description", "Start Time",
                "End Time", "Duration", "", "" };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == IntegerConstants.ONE) {
            return Date.class;
        } else if (columnIndex == IntegerConstants.SIX) {
            return ButtonEdit.class;
        } else if (columnIndex == IntegerConstants.SEVEN) {
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
        Effort te = null;

        try {
            te = (Effort) list.get(row);

            if (col == IntegerConstants.ZERO) {
                return te.getId();
            } else if (col == IntegerConstants.ONE) {
                return te.getDate();
            } else if (col == IntegerConstants.TWO) {
                return te.getDescription();
            } else if (col == IntegerConstants.THREE) {
                return GlobalUtils.TIMEFORMAT24HOURS.format(te.getStartTime());
            } else if (col == IntegerConstants.FOUR) {
                return GlobalUtils.TIMEFORMAT24HOURS.format(te.getEndTime());
            } else if (col == IntegerConstants.FIVE) {
                return GlobalUtils.TIMELENGTHFORMAT.format(te.getDuration());
            } else if (col == IntegerConstants.SIX) {
                return null;
            } else if (col == IntegerConstants.SEVEN) {
                return null;
            } else {
                return te;
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
    public void setValueAt(Object aValue, int row, int col) {
        Effort te = null;

        try {
            te = (Effort) list.get(row);

            if (col == IntegerConstants.ONE) {
                Date date = null;
                if (aValue.getClass() == Date.class) {
                    date = (Date) aValue;
                } else {
                    date = GlobalUtils.getDateFromObject(aValue);
                }
                if (date != null) {
                    te.setDate(date);
                }
            } else if (col == IntegerConstants.TWO) {
                te.setDescription(aValue.toString());
            } else if (col == IntegerConstants.THREE) {
                Long startTime = GlobalUtils.getTimeFromObject(aValue);
                if (startTime != null) {
                    te.setStartTime(startTime);
                }
            } else if (col == IntegerConstants.FOUR) {
                Long endTime = GlobalUtils.getTimeFromObject(aValue);
                if (endTime != null) {
                    te.setDuration(endTime - te.getStartTime());
                }
            } else if (col == IntegerConstants.FIVE) {
                Long duration = GlobalUtils.getDurationFromObject(aValue);
                if (duration != null) {
                    te.setDuration(duration);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            log.error(e.getMessage());
        } catch (ParseException e) {
            log.error(e.getMessage());
        } catch (ClassCastException e) {
            log.error(e.getMessage());
        }

    }

}

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

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonDelete;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonEdit;
import at.ac.tuwien.ifs.tita.ui.utils.GlobalUtils;
import at.ac.tuwien.ifs.tita.ui.utils.IntegerConstants;

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

        columnNames = new String[] { "Date", "Description", "Start Time",
                "End Time", "Duration", "", "", "Link" };
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
        Effort te = null;

        try {
            te = (Effort) list.get(row);

            if (col == IntegerConstants.ZERO) {
                return te.getDate();
            } else if (col == IntegerConstants.ONE) {
                return te.getDescription();
            } else if (col == IntegerConstants.TWO) {
                if(te.getStartTime() != null){
                    return GlobalUtils.TIMEFORMAT24HOURS.format(te.getStartTime());
                }else{
                    return 0;
                }
            } else if (col == IntegerConstants.THREE) {
                if(te.getEndTime() != null){
                    return GlobalUtils.TIMEFORMAT24HOURS.format(te.getEndTime());
                }else{
                    return 0;
                }
            } else if (col == IntegerConstants.FOUR) {
                if(te.getDuration() != null){
                    return GlobalUtils.TIMELENGTHFORMAT.format(te.getDuration());
                }else{
                    return 0;
                }
            } else if (col == IntegerConstants.FIVE) {
                return null;
            } else if (col == IntegerConstants.SIX) {
                return null;
            } else if (col == IntegerConstants.SEVEN) {
                return te.getUrlToIssueTrackerTask();
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

            if (col == IntegerConstants.ZERO) {
                Date date = null;
                if (aValue.getClass() == Date.class) {
                    date = (Date) aValue;
                } else {
                    date = GlobalUtils.getDateFromObject(aValue);
                }
                if (date != null) {
                    te.setDate(date);
                }
            } else if (col == IntegerConstants.ONE) {
                te.setDescription(aValue.toString());
            } else if (col == IntegerConstants.TWO) {
                Long startTime = GlobalUtils.getTimeFromObject(aValue);
                if (startTime != null) {
                    te.setStartTime(startTime);
                }
            } else if (col == IntegerConstants.THREE) {
                Long endTime = GlobalUtils.getTimeFromObject(aValue);
                if (endTime != null) {
                    te.setEndTime(endTime);
                }
            } else if (col == IntegerConstants.FOUR) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        if (column != IntegerConstants.FOUR) {
            if (selectedRow != -1) {
                if (selectedRow == row) {
                    return true;
                }
            }
        }
        return false;
    }

}

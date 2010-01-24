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
import at.ac.tuwien.ifs.tita.issuetracker.util.TiTATimeConverter;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonDelete;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonEdit;
import at.ac.tuwien.ifs.tita.ui.uihelper.LinkToIssueTracker;
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
        } else if (columnIndex == IntegerConstants.SEVEN) {
            return LinkToIssueTracker.class;
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
                if (te.getStartTime() != null) {
                    return GlobalUtils.TIMEFORMAT24HOURS.format(te
                            .getStartTime());
                } else {
                    return 0;
                }
            } else if (col == IntegerConstants.THREE) {
                if (te.getEndTime() != null) {
                    return GlobalUtils.TIMEFORMAT24HOURS
                            .format(te.getEndTime());
                } else {
                    return 0;
                }
            } else if (col == IntegerConstants.FOUR) {
                if (te.getDuration() != null) {
                    return TiTATimeConverter.getDuration2String(te
                            .getDuration());
                } else {
                    return 0;
                }
            } else if (col == IntegerConstants.FIVE) {
                return null;
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

            if (col == IntegerConstants.ZERO) {
                if (aValue != null) {
                    Date date = null;
                    if (aValue.getClass() == Date.class) {
                        date = (Date) aValue;
                    } else {
                        try {
                            date = GlobalUtils.getDateFromObject(aValue);
                        } catch (ParseException e) {
                            // Do nothing
                        }
                    }
                    if (date != null) {
                        te.setDate(date);
                    }
                }
            } else if (col == IntegerConstants.ONE) {
                if (aValue != null) {
                    te.setDescription(aValue.toString());
                }
            } else if (col == IntegerConstants.TWO) {
                try {
                    Long startTime = GlobalUtils.getTimeFromObject(aValue);
                    if (startTime != null) {
                        te.setStartTime(startTime);
                    }
                } catch (ParseException e) {
                    // Do nothing
                }
            } else if (col == IntegerConstants.THREE) {
                Long endTime = null;
                try {
                    endTime = GlobalUtils.getTimeFromObject(aValue);
                } catch (ParseException e) {
                    // Do nothing
                }
                te.setEndTime(endTime != null ? endTime : te.getStartTime()
                        + te.getDuration());
            } else if (col == IntegerConstants.FOUR) {
                Long duration = null;
                try {
                    duration = GlobalUtils.getDurationFromObject(aValue);
                } catch (ParseException e) {
                    // Do nothing
                }
                te.setDuration(duration != null ? duration : te.getEndTime()
                        - te.getStartTime());
            }
        } catch (IndexOutOfBoundsException e) {
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
        if (column != IntegerConstants.SEVEN) {
            if (selectedRow != -1) {
                if (selectedRow == row) {
                    return true;
                }
            }
        }
        return false;
    }

}

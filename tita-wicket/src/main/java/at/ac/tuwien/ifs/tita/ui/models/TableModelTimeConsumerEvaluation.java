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

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.issuetracker.util.TiTATimeConverter;
import at.ac.tuwien.ifs.tita.ui.utils.GlobalUtils;
import at.ac.tuwien.ifs.tita.ui.utils.IntegerConstants;

/**
 * TableModel for Time consumer evaluations.
 * 
 * @author rene
 * 
 */
public class TableModelTimeConsumerEvaluation extends AbstractTitaTableModel {

    private final Logger log = LoggerFactory.getLogger(TableModelTimeConsumerEvaluation.class);
    private String[] weekdays = new DateFormatSymbols().getShortWeekdays();

    public TableModelTimeConsumerEvaluation(List<Effort> list) {
        super(list);

        columnNames = new String[] { "Date", "WD", "Description", "Start Time", "End Time", "Duration" };
        GlobalUtils.TIMEFORMAT24HOURS.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return super.getColumnClass(columnIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAt(int row, int col) {
        Effort te = null;

        try {
            te = (Effort) list.get(row);
            Calendar cal = GlobalUtils.getCalendarFromDate(te.getDate());

            if (col == IntegerConstants.ZERO) {
                return GlobalUtils.DATEFORMAT.format(te.getDate());
            } else if (col == IntegerConstants.ONE) {
                return weekdays[cal.get(Calendar.DAY_OF_WEEK)];
            } else if (col == IntegerConstants.TWO) {
                return te.getDescription();
            } else if (col == IntegerConstants.THREE) {
                return te.getStartTime() != null ? GlobalUtils.removeSecondsFromDateString(GlobalUtils.TIMELENGTHFORMAT
                        .format(te.getStartTime())) : "0";
            } else if (col == IntegerConstants.FOUR) {
                return te.getEndTime() != null ? GlobalUtils.removeSecondsFromDateString(GlobalUtils.TIMELENGTHFORMAT
                        .format(te.getEndTime())) : "0";
            } else if (col == IntegerConstants.FIVE) {
                return TiTATimeConverter.getDuration2String(te.getDuration());
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
}

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
package at.ac.tuwien.ifs.tita.presentation.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.wicket.markup.html.form.TextField;

/**
 * GlobalUtils.
 * 
 * @author msiedler
 * @author rene
 * 
 */
public final class GlobalUtils {

    public static final int LISTSIZE = 10;

    public static final DateFormat DATEFORMAT = new SimpleDateFormat(
            "dd.MM.yyyy");
    public static final DateFormat TIMELENGTHFORMAT = new SimpleDateFormat(
            "HH:mm:ss");
    public static final DateFormat TIMEFORMAT24HOURS = new SimpleDateFormat(
            "HH:mm");

    /**
     * Converts date into Calendar.
     * 
     * @param d
     *            date to convert
     * @return calendar with date
     */
    public static Calendar getCalendarFromDate(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal;
    }

    /**
     * Calculates the difference between to dates.
     * 
     * @param d1
     *            startdate
     * @param d2
     *            enddate
     * @return difference as date
     */
    public static Date getTimeDifference(Date d1, Date d2) {
        return new Date(d2.getTime() - d1.getTime());
    }

    /**
     * Converts milliseconds to Long.
     * 
     * @param t1
     *            time in milliseconds
     * @return converted date
     */
    public static Date getDateFromLong(Long t1) {
        return new Date(t1);
    }

    /**
     * Parse object to time with type long.
     * 
     * @param o
     *            object to parse
     * @return long object
     * @throws ParseException
     *             parse exception
     */
    public static Long getTimeFromObject(Object o) throws ParseException {
        if (o != null) {
            if (o.toString().compareTo("") != 0) {
                return GlobalUtils.TIMEFORMAT24HOURS.parse(o.toString())
                        .getTime();
            }
        }
        return null;
    }

    /**
     * Parse object to date object.
     * 
     * @param o
     *            object to parse
     * @return date object
     * @throws ParseException
     *             parse exception
     */
    public static Date getDateFromObject(Object o) throws ParseException {
        if (o != null) {
            if (o.toString().compareTo("") != 0) {
                return GlobalUtils.DATEFORMAT.parse(o.toString());
            }
        }
        return null;
    }

    /**
     * Parse object to duration with type long.
     * 
     * @param o
     *            object to parse
     * @return duration with type long
     * @throws ParseException
     *             parse exception
     */
    public static Long getDurationFromObject(Object o) throws ParseException {
        if (o != null) {
            if (o.toString().compareTo("") != 0) {
                return GlobalUtils.TIMELENGTHFORMAT.parse(o.toString())
                        .getTime();
            }
        }
        return null;
    }

    /**
     * Get time data from textfield.
     * 
     * @param field
     *            to read
     * @return time as long
     * @throws ParseException
     *             parse exception
     */
    public static Long getTimeFromTextField(TextField<String> field)
            throws ParseException {
        if (field != null) {
            if (field.getModelObject().compareTo("") != 0) {
                return GlobalUtils.TIMEFORMAT24HOURS.parse(
                        field.getModelObject()).getTime();
            }
        }
        return null;
    }

    /**
     * Get duration from textfield.
     * 
     * @param field
     *            to read
     * @return duration as long
     * @throws ParseException
     *             parse exception
     */
    public static Long getDurationFromTextField(TextField<String> field)
            throws ParseException {
        if (field != null) {
            if (field.getModelObject().compareTo("") != 0) {
                return GlobalUtils.TIMELENGTHFORMAT.parse(
                        field.getModelObject()).getTime();
            }
        }
        return null;
    }
}

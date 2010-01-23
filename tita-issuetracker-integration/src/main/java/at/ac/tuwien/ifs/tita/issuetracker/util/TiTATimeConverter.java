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

package at.ac.tuwien.ifs.tita.issuetracker.util;

import java.text.ParseException;

/**
 * Class for converting Long times to hours, minutes, seconds.
 * 
 * @author herbert
 * @author markus
 * 
 */
public class TiTATimeConverter {
    private static final int C_THOUSAND = 1000;
    private static final int C_SIXTY = 60;
    private static final int C_THREETHOUSANDSIXHUNDRET = 3600;
    private static final int C_THREE = 3;

    /**
     * Converts a time duration to hours and minutes.
     * 
     * @param time
     *            Long
     * @return String value of time
     */
    public static String getDuration2String(Long time) {
        String format = String.format("%%0%dd", 2);
        Long eltime = time / C_THOUSAND;
        String seconds = String.format(format, eltime % C_SIXTY);
        String minutes = String.format(format, eltime
                % C_THREETHOUSANDSIXHUNDRET / C_SIXTY);
        String hours = String
                .format(format, eltime / C_THREETHOUSANDSIXHUNDRET);
        return hours + ":" + minutes + ":" + seconds;
    }

    /**
     * Converts a string to long time.
     * 
     * @param time
     *            - String to convert
     * @return converted long time
     * @throws ParseException
     *             - if string isn't in valid time format
     */
    public static Long getString2Duration(String time) throws ParseException {
        String[] timeArray = time.split(":");
        if (timeArray.length != C_THREE) {
            throw new ParseException("", 0);
        }
        try {
            long millis;
            millis = Integer.parseInt(timeArray[0]) * C_THREETHOUSANDSIXHUNDRET;
            millis += Integer.parseInt(timeArray[1]) * C_SIXTY;
            millis += Integer.parseInt(timeArray[2]);
            millis *= C_THOUSAND;
            return millis;
        } catch (NumberFormatException e) {
            throw new ParseException("", 1);
        }
    }
}

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

package at.ac.tuwien.ifs.tita.business.util;


/**
 * Class for converting Long times to hours, minutes, seconds.
 * 
 * @author herbert
 * 
 */
public class TiTATimeConverter {

    private static final Integer C_THOUSAND = 1000;
    private static final Integer C_SIXTY = 60;

    public TiTATimeConverter() {

    }

    /**
     * Return hours for a given time.
     * 
     * @param time
     *            Long
     * @return Integer hours
     */
    public static Integer getHours(Long time) {
        return (time.intValue() / (C_THOUSAND * C_SIXTY * C_SIXTY));
    }

    /**
     * Return minutes for a given time.
     * 
     * @param time
     *            Long
     * @return Integer minutes
     */
    public static Integer getMinutes(Long time) {
        return (time.intValue() / (C_THOUSAND * C_SIXTY));
    }

    /**
     * Return seconds for a given time.
     * 
     * @param time
     *            Long
     * @return Integer seconds
     */
    public static Integer getSeconds(Long time) {
        return (time.intValue() / C_THOUSAND);
    }

    /**
     * Converts a time duration to hours and minutes.
     * 
     * @param time
     *            Long
     * @return String value of time
     */
    public static String getDuration2String(Long time) {
        String hours = String.valueOf(TiTATimeConverter.getHours(time));
        String minutes = String.valueOf(TiTATimeConverter.getMinutes(time));
        String secondes = String.valueOf(TiTATimeConverter.getSeconds(time));

        if (hours.length() < 2) {
            hours = "0" + hours;
        }
        if (minutes.length() < 2) {
            minutes = "0" + minutes;
        }
        if (secondes.length() < 2) {
            secondes = "0" + secondes;
        }

        return hours + ":" + minutes + ":" + secondes;
    }
}

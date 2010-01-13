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



/**
 * Class for converting Long times to hours, minutes, seconds.
 *
 * @author herbert
 *
 */
public class TiTATimeConverter {
    private static final int C_THOUSAND = 1000;
    private static final int C_SIXTY = 60;
    private static final int C_THREETHOUSANDSIXHUNDRET = 3600;

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
        String minutes = String.format(format, eltime % C_THREETHOUSANDSIXHUNDRET / C_SIXTY);
        String hours = String.format(format, eltime / C_THREETHOUSANDSIXHUNDRET);
        return  hours + ":" + minutes + ":" + seconds;
    }
}

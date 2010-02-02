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

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.GregorianCalendar;

import org.junit.Test;

/**
 * Class for testing the TiTATimeConverter.
 *
 * @author Karin
 *
 */
public class TiTATimeConverterTest {

    /**
     * Tests the convertMethods.
     *
     * @throws ParseException - ex
     */
    @Test
    public void testGetString2DurationAndBack() throws ParseException {
        Long l = TiTATimeConverter.getString2Duration("22:34:01");
        String s = TiTATimeConverter.getDuration2String(l);
        assertEquals("22:34:01", s);
    }

    /**
     * Tests the convertMethods.
     *
     * @throws ParseException
     *             - ex
     */
    @Test
    public void testGetString2Duration() throws ParseException {
        // CHECKSTYLE:OFF
        GregorianCalendar c1 = new GregorianCalendar();
        GregorianCalendar c2 = new GregorianCalendar();
        c1.set(2009, GregorianCalendar.JANUARY, 10, 10, 23, 15);
        c2.set(2009, GregorianCalendar.JANUARY, 10, 0, 0, 0);

        Long l = c1.getTimeInMillis() - c2.getTimeInMillis();
        // CHECKSTYLE:ON
        String s = TiTATimeConverter.getDuration2String(l);
        assertEquals("10:23:15", s);
    }
}

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

package at.ac.tuwien.ifs.tita.common.test.util.time;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import at.ac.tuwien.ifs.tita.util.TiTATimeConverter;

/**
 * Tests for class TiTATimeConverter.
 * 
 * @author herbert
 * 
 */
public class TiTATimeConverterTest {

    /**
     * Test hours for given time.
     */
    @Test
    public void testHours() {
        // CHECKSTYLE:OFF
        assertEquals(new Integer(3), TiTATimeConverter.getHours(10800000L));
        System.out.println(TiTATimeConverter.getHours(10800000L));
        // CHECKSTYLE:ON
    }

    /**
     * Test minutes for given time.
     */
    @Test
    public void testMinutes() {
        // CHECKSTYLE:OFF
        assertEquals(new Integer(180), TiTATimeConverter.getMinutes(10800000L));
        System.out.println(TiTATimeConverter.getMinutes(10800000L));
        // CHECKSTYLE:ON
    }

    /**
     * Test seconds for given time.
     */
    @Test
    public void testSeconds() {
        // CHECKSTYLE:OFF
        assertEquals(new Integer(10800), TiTATimeConverter
                .getSeconds(10800000L));
        System.out.println(TiTATimeConverter.getSeconds(10800000L));
        // CHECKSTYLE:ON
    }
}

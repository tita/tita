package at.ac.tuwien.ifs.tita.issuetracker.mantis.time;

import static org.junit.Assert.*;

import org.junit.Test;

import at.ac.tuwien.ifs.tita.issuetracker.time.TiTATimeConverter;


/**
 * Tests for class TiTATimeConverter.
 * @author herbert
 *
 */
public class TiTATimeConverterTest {

    /**
     * Test hours for given time.
     */
    @Test
    public void testHours(){
        //CHECKSTYLE:OFF
        assertEquals(new Integer(3), TiTATimeConverter.getHours(10800000L));
        System.out.println(TiTATimeConverter.getHours(10800000L));
        //CHECKSTYLE:ON
    }
    

    /**
     * Test minutes for given time.
     */
    @Test
    public void testMinutes(){
        //CHECKSTYLE:OFF
        assertEquals(new Integer(180), TiTATimeConverter.getMinutes(10800000L));
        System.out.println(TiTATimeConverter.getMinutes(10800000L));
        //CHECKSTYLE:ON
    }
    
    /**
     * Test seconds for given time.
     */
    @Test
    public void testSeconds(){
        //CHECKSTYLE:OFF
        assertEquals(new Integer(10800), TiTATimeConverter.getSeconds(10800000L));
        System.out.println(TiTATimeConverter.getSeconds(10800000L));
        //CHECKSTYLE:ON
    }
}

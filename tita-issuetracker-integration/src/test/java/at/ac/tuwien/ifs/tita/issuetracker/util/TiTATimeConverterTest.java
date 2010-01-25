package at.ac.tuwien.ifs.tita.issuetracker.util;

import java.text.ParseException;

import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Class for testing the TiTATimeConverter.
 * @author Karin
 *
 */
public class TiTATimeConverterTest {

    /**
     * Tests the convertMethods.
     * @throws ParseException - ex
     */
    @Test
    public void testGetString2DurationAndBack() throws ParseException{
        Long l = TiTATimeConverter.getString2Duration("22:34:01");
        String s = TiTATimeConverter.getDuration2String(l);
        assertEquals("22:34:01", s);
    }
}

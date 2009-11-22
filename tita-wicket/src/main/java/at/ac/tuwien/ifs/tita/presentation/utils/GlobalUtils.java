package at.ac.tuwien.ifs.tita.presentation.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 
 * @author msiedler
 * 
 */
public interface GlobalUtils {

    public final static DateFormat DATEFORMAT = new SimpleDateFormat(
            "dd.MM.yyyy");
    public final static DateFormat TIMELENGTHFORMAT = new SimpleDateFormat(
            "hh:mm");
}

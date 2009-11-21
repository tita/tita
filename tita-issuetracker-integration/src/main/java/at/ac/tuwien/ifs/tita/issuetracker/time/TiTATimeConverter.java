package at.ac.tuwien.ifs.tita.issuetracker.time;


/**
 * Class for converting Long times to hours, minutes, seconds.
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
     * @param time Long
     * @return Integer hours
     */
    public static Integer getHours(Long time){
        return (time.intValue() / (C_THOUSAND * C_SIXTY * C_SIXTY));
    }
    
    /**
     * Return minutes for a given time.
     * @param time Long
     * @return Integer minutes
     */
    public static Integer getMinutes(Long time){
        return (time.intValue() / (C_THOUSAND * C_SIXTY));
    }
    
    /**
     * Return seconds for a given time.
     * @param time Long
     * @return Integer seconds
     */
    public static Integer getSeconds(Long time){
        return (time.intValue() / C_THOUSAND);
    }
}

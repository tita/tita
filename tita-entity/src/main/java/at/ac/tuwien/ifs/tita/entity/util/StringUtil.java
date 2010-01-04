package at.ac.tuwien.ifs.tita.entity.util;

import java.util.List;

public class StringUtil {
    /**
     * Generates a list of strings of project or time consumer ids.
     * @param projectIds List of Long
     * @return String
     */
    public static String generateIdStringFromLongList(List<Long> ids) {
        String pidList = "";
        
        for(Long id : ids){
            pidList += id + ",";
        }
        
        return pidList.substring(0, pidList.lastIndexOf(","));
    }
    
    /**
     * Generates a list of strings of project or time consumer names.
     * @param  names List of Long
     * @return String
     */
    public static String generateIdStringFromStringList(List<String> names) {
        String pidList = "";
        
        for(String id : names){
            pidList += ("'" + id + "',");
        }
        
        return pidList.substring(0, pidList.lastIndexOf(","));
    }
}

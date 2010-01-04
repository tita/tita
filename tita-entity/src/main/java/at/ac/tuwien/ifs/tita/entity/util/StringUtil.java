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
        
        if(names.size() == 0){
            pidList = "'',";
        }else{
            for(String id : names){
                pidList += ("'" + id + "',");
            }
        }
        
        return pidList.substring(0, pidList.lastIndexOf(","));
    }
}

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
package at.ac.tuwien.ifs.tita.entity.conv;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity for storing IssueTrackers like Mantis.
 * @author karin
 *
 */
@Entity
@Table(name="CONV_ISSUE_TRACKER")
public class IssueTracker extends ConvBaseEntity{
    
    @Column(name = "URL")
    private String url;

    public IssueTracker(Long id, String description, String url) {
        super(id, description);
        this.url = url;
    }

    public IssueTracker(){
        super();
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;  
    }

}

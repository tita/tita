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

import at.ac.tuwien.ifs.tita.entity.Effort;

/**
 * Container class for effort per issue tracker task and user.
 * 
 * @author herbert
 * 
 */
public class ActiveTaskEffort {

    private Long userId;
    private Effort effort;

    public ActiveTaskEffort(Long userId, Effort effort) {
        super();
        this.userId = userId;
        this.effort = effort;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Effort getEffort() {
        return effort;
    }

    public void addEffort(Long duration) {
        effort.addDuration(duration);
    }
}

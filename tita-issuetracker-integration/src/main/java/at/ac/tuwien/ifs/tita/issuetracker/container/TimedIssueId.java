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
package at.ac.tuwien.ifs.tita.issuetracker.container;

/**
 * For temporary encapsulating an id to identify an effort in coordinators map.
 * @author herbert
 *
 */
public class TimedIssueId implements Comparable<TimedIssueId>{
    private final Long projectId;
    private final Long issueId;
    
    public TimedIssueId(Long projectId, Long issueId){
        this.projectId = projectId;
        this.issueId = issueId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public Long getIssueId() {
        return issueId;
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(TimedIssueId o) {
        return (projectId.equals(o.getProjectId()) && issueId.equals(o.getIssueId()) ? 0 : 1);
    }
}
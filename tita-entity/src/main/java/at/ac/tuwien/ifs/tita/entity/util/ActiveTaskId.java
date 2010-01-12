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

/**
 * Container class for active tasks that consists of tita project id and issue
 * tracker task id.
 * 
 * @author herbert
 * 
 */
public class ActiveTaskId implements Comparable<ActiveTaskId> {
    private Long titaProjectId;
    private Long issueId;
    private Long issueTrackerId;
    private Long issueTProjetId;

    public ActiveTaskId(Long titaProjectId, Long issueTrackerId, Long issueTProjetId, Long issueId){
        this.titaProjectId = titaProjectId;
        this.issueTrackerId = issueTrackerId;
        this.issueId = issueId;
        this.issueTProjetId = issueTProjetId;
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(ActiveTaskId o) {
        return (((titaProjectId.equals(o.getTitaProjectId())) && 
                (issueTrackerId.equals(o.getIssueTrackerId()))
                && (issueTProjetId.equals(o.getIssueTProjetId())) &&
                (issueId.equals(o.getIssueId()))) ? 0 : -1);
    }

    public Long getTitaProjectId() {
        return titaProjectId;
    }

    public Long getIssueId() {
        return issueId;
    }

    public Long getIssueTrackerId() {
        return issueTrackerId;
    }

    public Long getIssueTProjetId() {
        return issueTProjetId;
    }
}

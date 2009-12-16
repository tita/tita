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
package at.ac.tuwien.ifs.tita.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import at.ac.tuwien.ifs.tita.entity.interfaces.IBaseEntity;

/**
 * Entity for storing projects comming from different issue trackers.
 * @author herbert
 *
 */
@Entity
@Table(name="ISSUE_TRACKER_PROJECT")
@SequenceGenerator(name = "seq_issue_project", sequenceName = "ISSUE_PROJECT_ID_SEQ", 
                   allocationSize = 1)
public class IssueTrackerProject implements IBaseEntity<Long> {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq_issue_project")
    private Long id;   
    
    @Column(name="PROJECT_ID")
    private Long projectId;
    
    @Column(name="ISST_PROJECT_ID")
    private Long isstProjectId;
    
//    @Column(name="DESCRIPTION")
//    private String description;
        
    
    @OneToMany
    @JoinColumn(name="ISST_PROJECT_ID")
    private Set<IssueTrackerTask> issueTrackerTasks;
    
    @SuppressWarnings("unused")
    @Column(name="MODIFICATION_VERSION")
    @Version
    private Long modificationVersion;
    
    public IssueTrackerProject() {
    }
    
    public IssueTrackerProject(Long id, Long projectId, Long isstProjectId) {
        super();
        this.id = id;
        this.projectId = projectId;
        this.isstProjectId = isstProjectId;
    }

    @Override
    public Long getId() {
        return id;
    }


    public Long getProjectId() {
        return projectId;
    }

    public Long getIsstProjectId() {
        return isstProjectId;
    }

    public Set<IssueTrackerTask> getIssueTrackerTasks() {
        return issueTrackerTasks;
    }
}

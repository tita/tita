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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;

/**
 * Entity for storing projects comming from different issue trackers.
 *
 * @author herbert
 *
 */
@Entity
@Table(name = "ISSUE_TRACKER_PROJECT")
@SequenceGenerator(name = "seq_issue_project", sequenceName = "ISSUE_PROJECT_ID_SEQ", allocationSize = 1)
public class IssueTrackerProject extends BaseEntity<Long> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_issue_project")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TITA_PROJECT_ID")
    private TiTAProject titaProject;

    @Column(name = "PROJECT_NAME")
    private String projectName;

    @ManyToOne
    @JoinColumn(name = "ISST_ID")
    private IssueTracker issueTracker;
    
    @Column(name = "ISST_PROJECT_ID")
    private Long isstProjectId;

    @Column(name = "DELETED")
    private boolean deleted;

    // @Column(name="DESCRIPTION")
    // private String description;

    @OneToMany(mappedBy="isstProject")
    private Set<IssueTrackerTask> issueTrackerTasks;

    @SuppressWarnings("unused")
    @Column(name = "MODIFICATION_VERSION")
    @Version
    private Long modificationVersion;

    public IssueTrackerProject() {
    }

    public IssueTrackerProject(Long id, TiTAProject titaProject, 
                               IssueTracker issTracker, Long isstProjectId, 
                               Set<IssueTrackerTask> issueTrackerTasks){
        super();
        this.id = id;
        this.titaProject = titaProject;
        this.issueTracker = issTracker;
        this.isstProjectId = isstProjectId;
        this.issueTrackerTasks = issueTrackerTasks;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    public TiTAProject getProjectId() {
        return titaProject;
    }
    
    public IssueTracker getIssueTrackerId() {
        return issueTracker;
    }

    public Long getIsstProjectId() {
        return this.isstProjectId;
    }

    public Set<IssueTrackerTask> getIssueTrackerTasks() {
        return this.issueTrackerTasks;
    }

    public void setIssueTracker(IssueTracker issueTracker) {
        this.issueTracker = issueTracker;
    }

    public IssueTracker getIssueTracker() {
        return this.issueTracker;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return this.projectName;
    }
    
    public void setTitaProject(TiTAProject titaProject){
        this.titaProject = titaProject;
    }
}

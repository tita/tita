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

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
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


/**
 * Entity for storing tasks coming from different issue trackers.
 *
 * @author herbert
 *
 */
@Entity
@Table(name = "ISSUE_TRACKER_TASK")
@SequenceGenerator(name = "seq_issue_task", sequenceName = "ISSUE_TASK_ID_SEQ", allocationSize = 1)
public class IssueTrackerTask extends BaseEntity<Long> implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_issue_task")
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "ISSUE_TRACKER_PROJECT_ID") //, referencedColumnName = "ID")
    private IssueTrackerProject isstProject;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "issueTTask",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Set<Effort> issueTEfforts;

    @SuppressWarnings("unused")
    @Column(name = "MODIFICATION_VERSION")
    @Version
    private Long modificationVersion;

    public IssueTrackerTask() {
    }

    public IssueTrackerTask(Set<Effort> efforts) {
        this.issueTEfforts = efforts;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    public Set<Effort> getIssueTEfforts() {
        return this.issueTEfforts;
    }

    public IssueTrackerProject getIsstProject(){
        return this.isstProject;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setIsstProject(IssueTrackerProject isstProject) {
        this.isstProject = isstProject;
    }
}
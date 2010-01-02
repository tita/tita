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

import at.ac.tuwien.ifs.tita.entity.conv.ProjectStatus;

/**
 * Entity for storing projects that are associated with an issue tracker.
 * 
 * @author herbert
 * 
 */
@Entity
@Table(name = "TITA_PROJECT")
@SequenceGenerator(name = "seq_project", sequenceName = "PROJECT_ID_SEQ", allocationSize = 1)
public class TiTAProject extends BaseEntity<Long> implements Serializable {

    @Id
    @Column(name = "ID", insertable=false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_project")
    private Long id;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DELETED")
    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "STATUS_ID")
    private ProjectStatus projectStatus;

    @OneToMany(mappedBy = "titaProject", 
               cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Set<TiTATask> titaTasks;

    @OneToMany(mappedBy = "titaProject", 
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Set<IssueTrackerProject> issueTrackerProjects;

    @OneToMany(mappedBy = "project")
    private Set<TiTAUserProject> users;


    @SuppressWarnings("unused")
    @Column(name = "MODIFICATION_VERSION")
    @Version
    private Long modificationVersion; 
    
    public TiTAProject(String description, String name,
            Boolean deleted, ProjectStatus projectStatus,
            Set<TiTATask> titaTasks,
            Set<IssueTrackerProject> issueTrackerProjects) {
        super();
        this.description = description;
        this.name = name;
        this.deleted = deleted;
        this.projectStatus = projectStatus;
        this.titaTasks = titaTasks;
        this.issueTrackerProjects = issueTrackerProjects;
    }

    public TiTAProject(){    
    }
    
    @Override
    public Long getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public Set<IssueTrackerProject> getIssueTrackerProjects() {
        return this.issueTrackerProjects;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitaTasks(Set<TiTATask> titaTasks) {
        this.titaTasks = titaTasks;
    }

    public void setIssueTrackerProjects(Set<IssueTrackerProject> issueTrackerProjects) {
        this.issueTrackerProjects = issueTrackerProjects;
    }

    public void setUsers(Set<TiTAUserProject> users) {
        this.users = users;
    }

    public Set<TiTAUserProject> getUsers() {
        return users;

    }

    public ProjectStatus getProjectStatus() {
        return this.projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Set<TiTATask> getTitaTasks() {
        return this.titaTasks;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}

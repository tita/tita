package at.ac.tuwien.ifs.tita.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import at.ac.tuwien.ifs.tita.entity.conv.ProjectStatus;
import at.ac.tuwien.ifs.tita.entity.interfaces.IBaseEntity;

/**
 * Entity for storing projects that are associated with an issue tracker.
 * @author herbert
 *
 */
@Entity
@Table(name="PROJECT")
@SequenceGenerator(name = "seq_project", sequenceName = "PROJECT_ID_SEQ", allocationSize = 1)
public class TiTAProject implements IBaseEntity<Long> {
    
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq_project")
    private Long id;
    
    @Column(name="DESCRIPTION")
    private String description;
    
    @Column(name="NAME")
    private String name;
    
    @Column(name="DELETED")
    private Boolean deleted;
    
    @ManyToOne
    @JoinColumn(name="STATUS_ID", referencedColumnName="ID")
    private ProjectStatus projectStatus;
    
    @OneToMany
    @JoinColumn(name="PROJECT_ID")
    private Set<TiTATask> titaTasks;
    
    @OneToMany
    @JoinColumn(name="PROJECT_ID")
    private Set<IssueTrackerProject> issueTrackerProjects;
    
    @ManyToMany
    @JoinTable(name = "USER_PROJECT",    
            joinColumns = { @JoinColumn(name = "PROJECT_ID")},  
            inverseJoinColumns={@JoinColumn(name="USER_ID")}) 
    private Set<User> users;
    
    @SuppressWarnings("unused")
    @Column(name="MODIFICATION_VERSION")
    @Version
    private Long modificationVersion;
   
    public TiTAProject() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
    
    public Set<TiTATask> getTiTATasks(){
        return titaTasks;
    }

    public Set<IssueTrackerProject> getIssueTrackerProjects() {
        return issueTrackerProjects;
    }

    public Set<User> getUsers() {
        return users;
    }

    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Set<TiTATask> getTitaTasks() {
        return titaTasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}

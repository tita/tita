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
    
    @OneToMany
    @JoinColumn(name="PROJECT_ID")
    private Set<TiTATask> titaTasks;
    
    @OneToMany
    @JoinColumn(name="PROJECT_ID")
    private Set<IssueTrackerProject> issueTrackerProjects;
    
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
}

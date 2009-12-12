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
        
    
    @OneToMany(mappedBy="issue_tracker_project")
    @JoinColumn(name="ISST_PROJECT_ID")
    private Set<IssueTrackerTask> issueTrackerTasks;
    
    @SuppressWarnings("unused")
    @Column(name="MODIFICATION_VERSION")
    @Version
    private Long modificationVersion;
    
    public IssueTrackerProject() {
    }

    @Override
    public Long getId() {
        return id;
    }

//    public String getDescription() {
//        return description;
//    }

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

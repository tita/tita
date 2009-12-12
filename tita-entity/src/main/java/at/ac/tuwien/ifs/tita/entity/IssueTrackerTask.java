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
 * Entity for storing tasks comming from different issue trackers.
 * @author herbert
 *
 */
@Entity
@Table(name="ISSUE_TRACKER_TASK")
@SequenceGenerator(name = "seq_issue_task", sequenceName = "ISSUE_TASK_ID_SEQ", allocationSize = 1)
public class IssueTrackerTask implements IBaseEntity<Long> {
    
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq_issue_task")
    private Long id;   
        
    @OneToMany(mappedBy="issue_tracker_task")
    @JoinColumn(name="ISSUET_TASK_ID")
    private Set<Effort> issueTEfforts;
    
    @SuppressWarnings("unused")
    @Column(name="MODIFICATION_VERSION")
    @Version
    private Long modificationVersion;
    
    public IssueTrackerTask() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public Set<Effort> getIssueTEfforts() {
        return issueTEfforts;
    }
}

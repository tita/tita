package at.ac.tuwien.ifs.tita.issuetracker.container;

/**
 * For temporary encapsulating an id to identify an effort in coordinators map.
 * @author herbert
 *
 */
public class TimedIssueId implements Comparable<TimedIssueId>{
    private Long projectId;
    private Long issueId;
    
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
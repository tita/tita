package at.ac.tuwien.ifs.tita.presentation.tasklist.stopwatch;

/**
 * Container class for active tasks that consists of tita project id and issue tracker task id
 * @author herbert
 *
 */
public class ActiveTaskId implements Comparable<ActiveTaskId>{
    private Long titaProjectId;
    private Long issueId;
    private Long issueTrackerId;
    private Long issueTProjetId;
//    private Long userId;
    
    public ActiveTaskId(Long titaProjectId, /*Long userId,*/ Long issueTrackerId, 
                        Long issueTProjetId, Long issueId){
        this.titaProjectId = titaProjectId;
        this.issueTrackerId = issueTrackerId;
        this.issueId = issueId;
//        this.userId = userId;
        this.issueTProjetId = issueTProjetId;
    }

    @Override
    public int compareTo(ActiveTaskId o) {
        return (((titaProjectId.equals(o.getTitaProjectId())) &&
               (issueTrackerId.equals(o.getIssueTrackerId())) &&
               (issueTProjetId.equals(o.getIssueTProjetId())) &&
//               (userId.equals(o.getUserId())) &&
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

//    public Long getUserId() {
//        return userId;
//    }
}

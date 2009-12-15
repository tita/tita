package at.ac.tuwien.ifs.tita.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;
import at.ac.tuwien.ifs.tita.entity.interfaces.IBaseEntity;


/**
 * Entity for storing login-Data for Users to login at IssueTrackers.
 * @author karin
 *
 */
@Entity
@Table(name="ISST_LOGIN")
@SequenceGenerator(name = "seq_isst_login", sequenceName = "ISST_LOGIN_ID_SEQ", allocationSize = 1)
public class IssueTrackerLogin  implements IBaseEntity<Long>{
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq_isst_login")
    private Long id;
    
    @Column(name="NAME")
    private String userName;
    
    @Column(name="PASSWORD")
    private String password;
    
    @ManyToOne
    @JoinColumn(name="ISSUETRACKER_ID", referencedColumnName="ID")
    private IssueTracker issueTracker;
        
    @SuppressWarnings("unused")
    @Column(name="MODIFICATION_VERSION")
    @Version
    private Long modificationVersion;
    
    public IssueTrackerLogin(){
        super();
    }

    public IssueTrackerLogin(Long id, String userName, String password,
            IssueTracker issueTracker) {
        super();
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.issueTracker = issueTracker;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public IssueTracker getIssueTracker() {
        return issueTracker;
    }

    public void setIssueTracker(IssueTracker issueTracker) {
        this.issueTracker = issueTracker;
    }
    
    
}

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

import at.ac.tuwien.ifs.inv.conv.Role;


/**
 * Entity for storing TiTA user.
 * @author karin
 *
 */
@Entity
@Table(name="USER")
@SequenceGenerator(name = "seq_user", sequenceName = "USER_ID_SEQ", allocationSize = 1)

public class User implements IBaseEntity<Long>{

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq_user")
    private Long id;   
    
    @Column(name="USERNAME")
    private String userName;
    
    @Column(name="PASSWORD")
    private String password;
    
    @Column(name="FIRSTNAME")
    private String firstName;
    
    @Column(name="LASTNAME")
    private String lastName;
    
    @Column(name="EMAIL")
    private String email;
    
    @Column(name="DELETED")
    private boolean deleted;
    
    @ManyToOne
    @JoinColumn(name="ROLE", referencedColumnName="ID")
    private Role role;
    
    @ManyToMany
    @JoinTable(name = "USER_PROJECT",    
            joinColumns = { @JoinColumn(name = "USER_ID")},  
            inverseJoinColumns={@JoinColumn(name="PROJECT_ID")}) 
    private Set<TiTAProject> titaProjects;
    
    @OneToMany
    @JoinColumn(name="ID")
    private Set<IssueTrackerLogin> issueTrackerLogins;
    
    @SuppressWarnings("unused")
    @Column(name="MODIFICATION_VERSION")
    @Version
    private Long modificationVersion;
    
    public User(){
        
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<TiTAProject> getTitaProjects() {
        return titaProjects;
    }

    public Set<IssueTrackerLogin> getIssueTrackerLogins() {
        return issueTrackerLogins;
    }
    
    
}

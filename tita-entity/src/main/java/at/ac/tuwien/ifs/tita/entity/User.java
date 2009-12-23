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

import java.util.Set;

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

import at.ac.tuwien.ifs.tita.entity.conv.Role;

/**
 * Entity for storing TiTA user.
 * 
 * @author karin
 * 
 */
@Entity
@Table(name = "USER")
@SequenceGenerator(name = "seq_user", sequenceName = "USER_ID_SEQ", allocationSize = 1)
public class User extends BaseEntity<Long> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user")
    private Long id;

    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "FIRSTNAME")
    private String firstName;

    @Column(name = "LASTNAME")
    private String lastName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "DELETED")
    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID") //,insertable=false) //, referencedColumnName = "ID")
    private Role role;

    @OneToMany(mappedBy = "user")
//    @JoinColumn(name="ID") //, referencedColumnName="USER1_ID")
    private Set<UserTitaProject> userTitaProject;

    @OneToMany(mappedBy = "user")
    private Set<IssueTrackerLogin> issueTrackerLogins;

    @SuppressWarnings("unused")
    @Column(name = "MODIFICATION_VERSION")
    @Version
    private Long modificationVersion;

    public User() {

    }
    
    public User(String userName, String password, String firstName,
            String lastName, String email, Boolean deleted, Role role,
            Set<UserTitaProject> userTitaProject, Set<IssueTrackerLogin> issueTrackerLogins) {
        super();
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.deleted = deleted;
        this.role = role;
        this.userTitaProject = userTitaProject;
        this.issueTrackerLogins = issueTrackerLogins;
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

    @Override
    public Long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<UserTitaProject> getUserTitaProject() {
        return userTitaProject;
    }

    public Set<IssueTrackerLogin> getIssueTrackerLogins() {
        return issueTrackerLogins;
    }

}

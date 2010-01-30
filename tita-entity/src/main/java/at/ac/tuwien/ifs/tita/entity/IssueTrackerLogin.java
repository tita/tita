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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import at.ac.tuwien.ifs.tita.entity.conv.IssueTracker;

/**
 * Entity for storing login-Data for Users to login at IssueTrackers.
 * 
 * @author karin
 * 
 */
@Entity
@Table(name = "ISST_LOGIN")
//@SequenceGenerator(name = "seq_isst_login", sequenceName = "ISST_LOGIN_ID_SEQ", allocationSize = 1)
public class IssueTrackerLogin extends BaseEntity<Long> {

    private static final int C_MAX_ENCRYPTED_PWD_LENGTH = 2100;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String userName;

    @Column(name = "PASSWORD", length = C_MAX_ENCRYPTED_PWD_LENGTH)
    private String password;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    // , referencedColumnName = "ID")
    private TiTAUser user;

    @ManyToOne
    @JoinColumn(name = "ISST_ID")
    // , referencedColumnName = "ID")
    private IssueTracker issueTracker;

    @SuppressWarnings("unused")
    @Column(name = "MODIFICATION_VERSION")
    @Version
    private Long modificationVersion;

    public IssueTrackerLogin() {
        super();
    }

    public IssueTrackerLogin(String userName, String password, IssueTracker issueTracker, TiTAUser user) {
        super();
        this.userName = userName;
        this.password = password;
        this.issueTracker = issueTracker;
        this.user = user;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    public IssueTracker getIssueTracker() {
        return this.issueTracker;
    }

    public void setIssueTracker(IssueTracker issueTracker) {
        this.issueTracker = issueTracker;
    }

    public TiTAUser getUser() {
        return this.user;
    }

    public void setUser(TiTAUser user) {
        this.user = user;
    }
}

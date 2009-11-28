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
package at.ac.tuwien.ifs.tita.issuetracker.container;

import java.util.List;

import javax.persistence.OneToMany;

import at.ac.tuwien.ifs.tita.datasource.domain.BaseEntity;

public class User extends BaseEntity {

    private Long id;
    private String username;
    private String password;

    @OneToMany
    private List<LoginIssueTracker> logins;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setLogins(List<LoginIssueTracker> logins) {
        this.logins = logins;
    }

    public List<LoginIssueTracker> getLogins() {
        return logins;
    }

}

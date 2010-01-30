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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * TiTAUserProject represents the join column of TiTAUser and TiTAProject. Extra
 * Class is necessary for storing information about the target hours that should
 * be consumed by a Time-Consumer.
 *
 * @author ASE Group 10
 */
@Entity
@Table(name = "USER_PROJECT")
//@SequenceGenerator(name = "seq_tita_user_project", sequenceName = "TITA_USER_PROJECT_ID_SEQ", allocationSize = 1)
public class TiTAUserProject extends BaseEntity<Long> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TARGET_HOURS")
    private Long targetHours;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private TiTAUser user;

    @ManyToOne
    @JoinColumn(name = "TITA_PROJECT_ID", referencedColumnName = "ID")
    private TiTAProject project;

    @SuppressWarnings("unused")
    @Column(name = "MODIFICATION_VERSION")
    @Version
    private Long modificationVersion;

    public TiTAUserProject() {

    }

    public TiTAUserProject(TiTAUser user, TiTAProject project, Long targetHours) {
        this.user = user;
        this.project = project;
        this.targetHours = targetHours;
    }

    /**
     * Method for getting the targetHours.
     *
     * @return the targetHours
     */
    public Long getTargetHours() {
        return targetHours;
    }

    /**
     * Method for setting the targetHours.
     *
     * @param targetHours the targetHours to set
     */
    public void setTargetHours(Long targetHours) {
        this.targetHours = targetHours;
    }

    /**
     * Method for getting the user.
     *
     * @return the user
     */
    public TiTAUser getUser() {
        return user;
    }

    /**
     * Method for setting the user.
     *
     * @param user the user to set
     */
    public void setUser(TiTAUser user) {
        this.user = user;
    }

    /**
     * Method for getting the project.
     *
     * @return the project
     */
    public TiTAProject getProject() {
        return project;
    }

    /**
     * Method for setting the project.
     *
     * @param project the project to set
     */
    public void setProject(TiTAProject project) {
        this.project = project;
    }

    @Override
    public Long getId() {
        return id;
    }
}
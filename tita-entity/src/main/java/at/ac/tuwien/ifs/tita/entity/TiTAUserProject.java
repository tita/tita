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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * TiTAUserProject represents the join column of TiTAUser and TiTAProject. Extra
 * Class is necessary for storing information about the target hours that should
 * be consumed by a Time-Consumer.
 * 
 * @author ASE Group 10
 */
@Entity
@Table(name = "USER_PROJECT")
@IdClass(TiTAUserProject.TiTAUserProjectId.class)
public class TiTAUserProject {

    @Id
    // @Column(name = "USER_ID")
    private long userId;

    @Id
    // @Column(name = "PROJECT_ID")
    private long projectId;

    @Column(name = "TARGET_HOURS")
    private Long targetHours;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "USERID", referencedColumnName = "ID")
    private User user;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "PROJECTID", referencedColumnName = "ID")
    private TiTAProject project;

    /**
     * Method for getting the userId.
     * 
     * @return the userId
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Method for setting the userId.
     * 
     * @param userId the userId to set
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * Method for getting the projectId.
     * 
     * @return the projectId
     */
    public long getProjectId() {
        return projectId;
    }

    /**
     * Method for setting the projectId.
     * 
     * @param projectId the projectId to set
     */
    public void setProjectId(long projectId) {
        this.projectId = projectId;
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
    public User getUser() {
        return user;
    }

    /**
     * Method for setting the user.
     * 
     * @param user the user to set
     */
    public void setUser(User user) {
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

    /**
     * TiTAUserProjectId is an inner class of TiTAUserProject representing the
     * Primary Key for the Database.
     * 
     * @author ASE Group 10
     */
    public class TiTAUserProjectId implements Serializable {
        private long userId;
        private long projectId;

        /**
         * Method for retrieving the UserId.
         * 
         * @return the userId
         */
        public long getUserId() {
            return userId;
        }

        /**
         * Method for setting the UserId.
         * 
         * @param userId the userId to set
         */
        public void setUserId(long userId) {
            this.userId = userId;
        }

        /**
         * Method for retrieving the ProjectId.
         * 
         * @return the projectId
         */
        public long getProjectId() {
            return projectId;
        }

        /**
         * Method for setting the ProjectId.
         * 
         * @param projectId the projectId to set
         */
        public void setProjectId(long projectId) {
            this.projectId = projectId;
        }
    }
}
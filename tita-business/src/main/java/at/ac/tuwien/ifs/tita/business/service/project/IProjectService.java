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
package at.ac.tuwien.ifs.tita.business.service.project;

import java.util.List;

import javax.persistence.PersistenceException;

import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;

/**
 * IProjectService encapsulates all tita project based db operations.
 *
 * @author herbert
 *
 */
@Transactional
public interface IProjectService {
    
    /**
     * Saves an issue tracker task.
     * 
     * @param itt IssueTrackerTask
     * @return IssueTrackerTask
     * @throws PersistenceException pe
     */
    IssueTrackerTask saveIssueTrackerTask(IssueTrackerTask itt) throws PersistenceException;

    /**
     * deletes an existing project.
     *
     * @param project
     *            the project to be deleted
     * @throws PersistenceException
     *             if Parameter is null or another Exception is thrown
     */
    void deleteProject(TiTAProject project) throws PersistenceException;

    /**
     * returns a specific Project found to the id given.
     *
     * @param id
     *            the unique identifier of an project
     * @throws PersistenceException
     *             if no project was found or another Exception is thrown
     * @return the specified Project, if found.
     */
    TiTAProject getProjectById(Long id) throws PersistenceException;

    /**
     * Returns a list of all existing tita projects.
     * @return List of TiTAProjects
     */
    List<TiTAProject> findAllTiTAProjects();

    /**
     * Returns a list of all tita projects for a given user.
     * 
     * @param user
     *            - the specific tita user
     * @return List of TiTAProjects
     */
    List<TiTAProject> findTiTAProjectsForUser(TiTAUser user);
    
    /** Finds an issue tracker task for a given tita project.
     * @param tp Long
     * @param it Long
     * @param itp Long
     * @param itt Long
     * @return IssueTrackerTask
     */
    IssueTrackerTask findIssueTrackerTaskForTiTAProject(Long tp, Long it, Long itp, Long itt);

    /**
     * Finds an issue tracker project for a given tita project.
     * @param tp Long
     * @param issueTrackerId Long
     * @param itp Long
     * @return IssueTrackerProject
     */
    IssueTrackerProject findIssueTrackerProjectForTiTAProject(Long tp, Long issueTrackerId, 
                                                              Long itp);
}

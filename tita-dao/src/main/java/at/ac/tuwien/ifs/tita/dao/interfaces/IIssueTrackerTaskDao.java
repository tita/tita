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
package at.ac.tuwien.ifs.tita.dao.interfaces;

import java.util.List;

import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;

/**
 * Interface for IssueTrackerTaskDao.
 * 
 * @author herbert
 * 
 */
public interface IIssueTrackerTaskDao extends IGenericHibernateDao<IssueTrackerTask, Long> {
    /**
     * Find IssueTrackerTasks for given tita project.
     * 
     * @param tp Long tita project
     * @param it Long issue tracker
     * @param itp Long issue tracker project
     * @param itt Long issue tracker task
     * @return IssueTrackerTask
     */
    IssueTrackerTask findIssueTrackerTask(Long tp, Long it, Long itp, Long itt);

    /**
     * Find IssueTrackerTasks for given tita project.
     * 
     * @param projectId projectId as Long
     * @param userId userId as Long
     * 
     * @return List of IssueTrackerTasks
     */
    List<IssueTrackerTask> findIssueTrackerTasksforUserProject(Long projectId, Long userId);
}

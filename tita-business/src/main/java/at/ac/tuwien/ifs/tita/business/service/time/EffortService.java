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

package at.ac.tuwien.ifs.tita.business.service.time;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.PersistenceException;

import at.ac.tuwien.ifs.tita.business.service.project.IProjectService;
import at.ac.tuwien.ifs.tita.dao.interfaces.IEffortDao;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerProject;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.util.UserProjectEffort;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.ITaskTrackable;

/**
 * Service for manipulating (insert, update, delete, search... ) efforts in
 * TiTA.
 * 
 * @author herbert
 * 
 */
public class EffortService implements IEffortService {

    // TODO: constants should be refactored
    private static final Long C_ISSUE_TRACKER_ID = 1L;

    private IProjectService projectService;

    private IEffortDao timeEffortDao;

    public EffortService() {

    }

    public EffortService(IEffortDao timeEffortDao, IProjectService projectService) {
        this.timeEffortDao = timeEffortDao;
        this.projectService = projectService;
    }

    public void setTimeEffortDao(IEffortDao timeEffortDao) {
        this.timeEffortDao = timeEffortDao;
    }

    /** {@inheritDoc} */
    @Override
    public void deleteEffort(Effort effort) throws PersistenceException {
        timeEffortDao.delete(effort);
    }

    /** {@inheritDoc} */
    @Override
    public Effort getEffortById(Long id) throws PersistenceException {
        return timeEffortDao.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public Effort saveEffort(Effort effort) throws PersistenceException {
        return timeEffortDao.save(effort);
//        timeEffortDao.flushnClear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Effort> getEffortsDailyView(Date date) throws PersistenceException {
        return timeEffortDao.getTimeEffortsDailyView(date);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Effort> getEffortsMonthlyView(Integer year, Integer month)
        throws PersistenceException {
        return timeEffortDao.getTimeEffortsMonthlyView(year, month);
    }

    /**
     * {@inheritDoc}
     */
    public List<Integer> getEffortsYears() {
        return timeEffortDao.getTimeEffortsYears();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserProjectEffort> getEffortsSummaryForProjectAndUserNames(List<String> projects,
            List<String> usernames, String grouping) {
        return timeEffortDao.findEffortsForTiTAProjectAndTimeConsumerId(projects, 
                usernames, grouping);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserProjectEffort> getEffortsSummaryForProjectNames(List<String> projects,
            String grouping) {
        return timeEffortDao.findEffortsForTiTAProjectId(projects, grouping);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long findEffortsSumForIssueTrackerTask(TiTAProject tp, TiTAUser tu, 
            Long issTProjectId, Long isstTTaskId,
            Long isstId) {
        return timeEffortDao.findEffortsSumForIssueTrackerTask(tp.getId(), 
                tu.getUserName(), issTProjectId,
                isstTTaskId, isstId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Effort> findEffortsForTiTAProjectAndTiTAUser(Long projectId, Long userId) {
        return timeEffortDao.findEffortsForTiTAProjectAndTiTAUser(projectId, userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long totalizeEffortsForTiTAProjectAndTiTAUser(Long projectId, Long userId) {
        return timeEffortDao.totalizeEffortsForTiTAProjectAndTiTAUser(projectId, userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Effort> findEffortsForTiTAProjectAndTiTAUserOrdered(Long projectId, Long userId) {
        return timeEffortDao.findEffortsForTiTAProjectAndTiTAUserOrdered(projectId, userId);
    }

    /**
     * Saves an effort for a issue tracker task.
     * 
     * @param effort Effort
     * @param task ITaskTrackable
     * @param user - user
     * @param project - TiTaproject
     * @return saved effort
     */
    public Effort saveIssueTrackerTaskEfforts(Effort effort, ITaskTrackable task,
            TiTAUser user, TiTAProject project) {

        return saveIssueTrackerTaskEfforts(effort, task.getId(), task.getProject().getId(),
                task.getDescription(), user, project.getId());
    }

    /**
     * Saves an effort for a issue tracker task.
     * 
     * @param effort - effort
     * @param isstTaskId - issuetracker Task id
     * @param isstProjectId - issuetracker Project id
     * @param isstTaskDescription - task description
     * @param user - user
     * @param titaProjectId - tita project id
     * @return saved effort
     */
    public Effort saveIssueTrackerTaskEfforts(Effort effort, Long isstTaskId, Long isstProjectId,
            String isstTaskDescription, TiTAUser user, Long titaProjectId) {

        // persist issue tracker task anOd effort and read it from db to get
        // actual effort value
        IssueTrackerTask itt = 
            projectService.findIssueTrackerTaskForTiTAProject(titaProjectId, C_ISSUE_TRACKER_ID,
                isstProjectId, isstTaskId);
        effort.setTitaTask(null);
        effort.setDescription(isstTaskDescription);
        effort.setUser(user);

        if (itt != null) {
            effort.setIssueTTask(itt);
        } else {
            IssueTrackerProject tempProject = 
                projectService.findIssueTrackerProjectForTiTAProject(titaProjectId,
                    C_ISSUE_TRACKER_ID, isstProjectId);
            Set<Effort> eff = new HashSet<Effort>();
            eff.add(effort);
            itt = new IssueTrackerTask(tempProject, isstTaskId, isstTaskDescription, eff);
            projectService.saveIssueTrackerTask(itt);
            effort.setIssueTTask(itt);
            Set<IssueTrackerTask> tasks = new HashSet<IssueTrackerTask>();
            tasks.add(itt);
            tempProject.setIssueTrackerTasks(tasks);
        }
        return saveEffort(effort);
        
        // timeEffortDao.flushnClear();
    }

    /**
     * Saves an effort for tita task generated by some tita user.
     * 
     * @param effort Effort
     * @param description effortdescription
     * @param user user
     * @param project TiTaProject
     * @return saved effort
     */
    public Effort saveEffortForTiTATask(Effort effort, String description, 
            TiTAUser user, TiTAProject project) {
        if (effort != null) {
            effort.setDescription(description);
            effort.setUser(user);

            TiTATask tt = new TiTATask(description, user, project, new HashSet<Effort>());
            tt.getTitaEfforts().add(effort);
            projectService.saveTiTATask(tt);
            
            effort.setTitaTask(tt);
            return saveEffort(effort);
            
        }
        return null;
    }
}

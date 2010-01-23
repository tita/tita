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
import java.util.List;

import javax.persistence.PersistenceException;

import at.ac.tuwien.ifs.tita.dao.interfaces.IEffortDao;
import at.ac.tuwien.ifs.tita.entity.Effort;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.util.UserProjectEffort;

/**
 * Service for manipulating (insert, update, delete, search... ) efforts in
 * TiTA.
 * 
 * @author herbert
 * 
 */
public class EffortService implements IEffortService {

    private IEffortDao timeEffortDao;

    public EffortService() {

    }

    public EffortService(IEffortDao timeEffortDao) {
        this.timeEffortDao = timeEffortDao;
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
    public void saveEffort(Effort effort) throws PersistenceException {
        timeEffortDao.save(effort);
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
    public List<Effort> getEffortsMonthlyView(Integer year, Integer month) throws PersistenceException {
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
        return timeEffortDao.findEffortsForTiTAProjectAndTimeConsumerId(projects, usernames, grouping);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserProjectEffort> getEffortsSummaryForProjectNames(List<String> projects, String grouping) {
        return timeEffortDao.findEffortsForTiTAProjectId(projects, grouping);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long findEffortsSumForIssueTrackerTask(TiTAProject tp, TiTAUser tu, Long issTProjectId, Long isstTTaskId,
            Long isstId) {
        return timeEffortDao.findEffortsSumForIssueTrackerTask(tp.getId(), tu.getUserName(), issTProjectId,
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
}

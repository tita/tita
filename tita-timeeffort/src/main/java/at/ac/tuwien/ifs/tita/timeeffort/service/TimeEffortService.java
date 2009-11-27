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

package at.ac.tuwien.ifs.tita.timeeffort.service;

import java.util.Calendar;
import java.util.List;

import at.ac.tuwien.ifs.tita.datasource.criteria.IBaseCriteria;
import at.ac.tuwien.ifs.tita.datasource.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.timeeffort.dao.TimeEffortDAO;
import at.ac.tuwien.ifs.tita.timeeffort.domain.TimeEffort;

public class TimeEffortService implements ITimeEffortService {

    private TimeEffortDAO timeEffortDAO;

    public void setTimeEffortDAO(TimeEffortDAO timeEffortDAO) {
        this.timeEffortDAO = timeEffortDAO;
    }

    @Override
    public void deleteTimeEffort(TimeEffort timeEffort) throws TitaDAOException {
        timeEffortDAO.delete(timeEffort);
    }

    @Override
    public TimeEffort getTimeEffortById(Long id) throws TitaDAOException {
        return timeEffortDAO.getById(TimeEffort.class, id);
    }

    @Override
    public TimeEffort saveTimeEffort(TimeEffort timeEffort) throws TitaDAOException {
        return timeEffortDAO.save(timeEffort);
    }

    @Override
    public void updateTimeEffort(TimeEffort timeEffort) throws TitaDAOException {
        timeEffortDAO.update(timeEffort);
    }

    @Override
    public List<TimeEffort> searchTimeEffort(IBaseCriteria<TimeEffort> criteria) throws TitaDAOException {
        return timeEffortDAO.search(criteria);
    }

    @Override
    public IBaseCriteria<TimeEffort> createCriteria(TimeEffort timeEffort) throws TitaDAOException {
        return timeEffortDAO.createCriteria(timeEffort);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TimeEffort> getTimeEffortsDailyView(Calendar cal) throws TitaDAOException {
        return timeEffortDAO.getTimeEffortsDailyView(cal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TimeEffort> getTimeEffortsMonthlyView(Calendar cal) throws TitaDAOException {
        return timeEffortDAO.getTimeEffortsMonthlyView(cal);
    }
}

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

package at.ac.tuwien.ifs.tita.datasource.service.time;

import java.util.Calendar;
import java.util.List;

import at.ac.tuwien.ifs.tita.datasource.dao.TimeEffortDao;
import at.ac.tuwien.ifs.tita.datasource.entity.TimeEffort;
import at.ac.tuwien.ifs.tita.datasource.exception.TitaDAOException;

public class TimeEffortService implements ITimeEffortService {

    private TimeEffortDao timeEffortDao;

    public void setTimeEffortDao(TimeEffortDao timeEffortDao) {
        this.timeEffortDao = timeEffortDao;
    }

    @Override
    public void deleteTimeEffort(TimeEffort timeEffort) throws TitaDAOException {
        timeEffortDao.delete(timeEffort);
    }

    @Override
    public TimeEffort getTimeEffortById(Long id) throws TitaDAOException {
        return timeEffortDao.findById(id);
    }

    @Override
    public void saveTimeEffort(TimeEffort timeEffort) throws TitaDAOException {
        timeEffortDao.persist(timeEffort);
    }

    @Override
    public TimeEffort updateTimeEffort(TimeEffort timeEffort) throws TitaDAOException {
        return timeEffortDao.merge(timeEffort);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TimeEffort> getTimeEffortsDailyView(Calendar cal) throws TitaDAOException {
        return timeEffortDao.getTimeEffortsDailyView(cal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TimeEffort> getTimeEffortsMonthlyView(Calendar cal) throws TitaDAOException {
        return timeEffortDao.getTimeEffortsMonthlyView(cal);
    }
}

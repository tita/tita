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

package at.ac.tuwien.ifs.tita.common.service.time;

import java.util.Calendar;
import java.util.List;

import at.ac.tuwien.ifs.tita.common.dao.time.TimeEffortDao;
import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.entity.Effort;

/**
 * Service for manipulating (insert, update, delete, search... ) efforts in TiTA.
 * @author herbert
 *
 */
public class EffortService implements IEffortService {

    private TimeEffortDao timeEffortDao;

    public void setTimeEffortDao(TimeEffortDao timeEffortDao) {
        this.timeEffortDao = timeEffortDao;
    }

    /** {@inheritDoc} */
    @Override
    public void deleteEffort(Effort effort) throws TitaDAOException {
        timeEffortDao.delete(effort);
    }

    /** {@inheritDoc} */
    @Override
    public Effort getEffortById(Long id) throws TitaDAOException {
        return timeEffortDao.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public void saveEffort(Effort effort) throws TitaDAOException {
        timeEffortDao.save(effort);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Effort> getEffortsDailyView(Calendar cal) throws TitaDAOException {
        return timeEffortDao.getTimeEffortsDailyView(cal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Effort> getEffortsMonthlyView(Calendar cal) throws TitaDAOException {
        return timeEffortDao.getTimeEffortsMonthlyView(cal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Effort> getActualEfforts(int maxresults) throws TitaDAOException {
        return timeEffortDao.getActualTimeEfforts(maxresults);
    }
}

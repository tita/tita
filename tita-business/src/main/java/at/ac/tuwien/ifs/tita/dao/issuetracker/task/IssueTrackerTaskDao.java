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
package at.ac.tuwien.ifs.tita.dao.issuetracker.task;

import java.util.Map;

import org.springframework.stereotype.Repository;

import at.ac.tuwien.ifs.tita.dao.GenericHibernateDao;
import at.ac.tuwien.ifs.tita.entity.IssueTrackerTask;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;

/**
 * IssueTrackerTask Data Access Object.
 * 
 * @author Christoph
 * 
 */
@Repository
public class IssueTrackerTaskDao extends GenericHibernateDao<IssueTrackerTask, Long> {

    public IssueTrackerTaskDao() {
        super(IssueTrackerTask.class);
    }

    /**
     * dfsg.
     * 
     * @param project
     *            sdfg
     * @return asdf
     */
    public Map<Long, TiTATask> getTiTATasksForTitaProject(TiTAProject project) {

        return null;

    }

}
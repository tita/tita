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

import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTATask;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;

/**
 * Interface for TitaTaskDao implementation.
 * 
 * @author rene
 * 
 */
public interface ITiTATaskDao extends IGenericHibernateDao<TiTATask, Long> {
    /**
     * Find TiTATask for given tita project.
     * 
     * @param project TiTAProject tita project
     * @param user TiTAUser issue tracker
     * 
     * @return TiTATask
     */
    List<TiTATask> findTiTATasksforUserProject(TiTAProject project, TiTAUser user);
}

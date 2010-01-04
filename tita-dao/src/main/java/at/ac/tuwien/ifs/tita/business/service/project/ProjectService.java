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

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;

import at.ac.tuwien.ifs.tita.dao.IGenericHibernateDao;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;

/**
 * Service for manipulating (insert, update, delete, search... ) tita projects
 * in TiTA.
 * 
 * @author herbert
 * 
 */
public class ProjectService implements IProjectService {

    private IGenericHibernateDao<TiTAProject, Long> titaProjectDao;

    public void setTitaProjectDao(IGenericHibernateDao<TiTAProject, Long> titaPDao){
        this.titaProjectDao = titaPDao;
    }
    
    @Override
    public List<TiTAProject> findAllTiTAProjects() {
        return titaProjectDao.findAllOrdered(new Order[] { Property.forName("name").asc() });
    }    
}

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

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;

import at.ac.tuwien.ifs.tita.dao.project.TiTAProjectDao;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;

/**
 * Service for manipulating (insert, update, delete, search... ) tita projects
 * in TiTA.
 * 
 * @author herbert
 * 
 */
public class ProjectService implements IProjectService {

    private TiTAProjectDao titaProjectDao;

    public void setTitaProjectDao(TiTAProjectDao titaProjectDao) {
        this.titaProjectDao = titaProjectDao;
    }

    /** {@inheritDoc} */
    @Override
    public void deleteProject(TiTAProject project) throws PersistenceException {
        this.titaProjectDao.delete(project);
    }

    /** {@inheritDoc} */
    @Override
    public TiTAProject getProjectById(Long id) throws PersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public TiTAProject saveProject(TiTAProject project) throws PersistenceException {
        // TODO Auto-generated method stub
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public List<TiTAProject> findAllTiTAProjects() {
        return titaProjectDao.findAllOrdered(new Order[] { Property.forName("name").asc() });
    }
    
    /** {@inheritDoc} */
    @Override
    public List<TiTAProject> findTiTAProjectsForUser(TiTAUser user) {
        return titaProjectDao.findTiTAProjectsForUsername(user.getUserName());
    }  
}

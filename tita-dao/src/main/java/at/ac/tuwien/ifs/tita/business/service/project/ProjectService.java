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

import javax.persistence.PersistenceException;

import at.ac.tuwien.ifs.tita.dao.IGenericHibernateDao;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;

/**
 * Testclass.
 *
 * @author Christoph
 *
 */
public class ProjectService implements IProjectService {


    private IGenericHibernateDao<TiTAProject, Long> titaProjectDao;

    public void setTitaProjectDao(IGenericHibernateDao<TiTAProject, Long> titaProjectDao) {
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

}

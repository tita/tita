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

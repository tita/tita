package at.ac.tuwien.ifs.tita.datasource.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.hibernate.Session;


/**
 * The top class of all daos. 
 *
 */
public class PersistenceContextProvider implements IPersistenceContextProvider {

    @PersistenceContext
    protected EntityManager em;

    public PersistenceContextProvider() {
        super();
    }     
    
    /**
     * Returns the actual (normally with the thread associated) HibernateSession.
     * @return actual session
     */
    protected Session getSession() {
        Session session = (Session) em.getDelegate();
        return session;
    }
    
    /**
     * Returns the Entity Manager.
     * @return Entity Manager from the PersistenceContext.
     */
    protected EntityManager getEntityManager() {
        return em;
    }
    
    /** {@inheritDoc} */ 
    public boolean isDataChanged() {
        Session s = getSession();
        if (s.isOpen()) {
            return s.isDirty();
        }
        return false;
    }
    
    /** {@inheritDoc} */ 
    public void clear() {
        try {
            getSession().clear();
        } catch (Exception e) {
            throw new PersistenceException("Error during clearing the database session.\n"+
                    e.getMessage(), e);
        }
    }
    
    /** {@inheritDoc} */ 
    public void flush() {
        try {
            getSession().flush();
        } catch (Exception e) {
            throw new PersistenceException("Error during flushing the database session.\n"+
                    e.getMessage(), e);
        }
    }

    /** {@inheritDoc} */ 
    public void flushnClear() {
        try {
            getSession().flush();
            getSession().clear();
        } catch (Exception e) {
            throw new PersistenceException(
                             "Error during flushing and clearing the database session.\n"+
                             e.getMessage(), 
                             e);
        }
 
    }

}

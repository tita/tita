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
package at.ac.tuwien.ifs.tita.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.hibernate.Session;

import at.ac.tuwien.ifs.tita.dao.interfaces.IPersistenceContextProvider;


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

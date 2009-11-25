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

package at.ac.tuwien.ifs.tita.datasource.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.ifs.tita.datasource.criteria.IBaseCriteria;
import at.ac.tuwien.ifs.tita.datasource.domain.BaseEntity;
import at.ac.tuwien.ifs.tita.datasource.exception.TitaDAOException;

/**
 * 
 * @author ASE Group 10 - TiTA
 * 
 * @param <DomainClass>
 *            Specifies a Wild-card, that allows all sub-types of the BaseEntity
 * 
 *            BaseDAO is a fully functionally Class for persisting any sub-types
 *            of the BaseEntity Object. If additional functionality is needed
 *            simply extend this class and implement the needed Methods.
 * 
 */
@Repository
public class BaseDAO<DomainClass extends BaseEntity> implements
        IBaseDAO<DomainClass> {

    protected EntityManager entityManager;

    final Logger log = LoggerFactory.getLogger(BaseDAO.class);

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Method for persisting a Domain Object. If Id of Object is null it will be
     * persisted in the Database, else it will be updated
     * 
     * @param entity
     *            - A sub-type of BaseEntity
     */
    public DomainClass save(DomainClass entity) throws TitaDAOException {
        if (entity == null) {
            log.debug("Entity is null, Exception will be thrown");
            throw new TitaDAOException(
                    "Entity with value null cannot be persisted");
        }

        log.debug("Trying to persist Domain with Id: " + entity.getId() + ".");

        try {
            if (entity.getId() == null) {
                DomainClass retDomain = entityManager.merge(entity);
                entityManager.persist(retDomain);
                log.debug("Entity with Id: " + entity.getId()
                        + " successfuly  saved!");
                return retDomain;
            } else {
                log.debug("Entity seems to exist, trying to update");
                DomainClass retDomain = entityManager.merge(entity);
                log.debug("Entity with Id: " + retDomain.getId()
                        + " successfuly updated!");
                return entity;
            }
        } catch (Exception e) {
            log.debug("Exception catched while trying to save Entity with Id: "
                    + entity.getId());
            throw new TitaDAOException("Exception was thrown: \n" + "Cause: "
                    + e.getCause() + "\n" + "Error: " + e.getMessage());
        }
    }

    /**
     * Method for updating a Domain Object.
     * 
     * @param entity
     *            - A sub-type of BaseEntity
     */
    public void update(DomainClass entity) throws TitaDAOException {
        if (entity == null || entity.getId() == null) {
            log.debug("Entity is null, Exception will be thrown");
            throw new TitaDAOException(
                    "Entity cannot be updated if its ID or itself is null");
        }

        try {
            log.debug("Trying to update Entity with Id: " + entity.getId());
            entityManager.merge(entity);
            log.debug("Entity with Id: " + entity.getId()
                    + " successfuly updated!");
        } catch (Exception e) {
            log
                    .debug("Exception catched while trying to update Entity with Id: "
                            + entity.getId());
            throw new TitaDAOException("Exception was thrown: \n" + "Cause: "
                    + e.getCause() + "\n" + "Error: " + e.getMessage());
        }
    }

    /**
     * Method for deleting a Domain Object
     * 
     * @param entity
     *            - A sub-type of BaseEntity
     */
    public void delete(DomainClass entity) throws TitaDAOException {
        if (entity == null || entity.getId() == null) {
            log.debug("Entity or its Id is null, Exception will be thrown");
            throw new TitaDAOException(
                    "Entity cannot be deleted if its ID or itself is null");
        }

        try {
            log.debug("trying to remove Entity with Id: " + entity.getId());
            entityManager.remove(entityManager.merge(entity));
            log.debug("Entity with Id: " + entity.getId()
                    + " successfuly removed");
        } catch (Exception e) {
            log
                    .debug("Exception catched while trying to delete Entity with Id: "
                            + entity.getId());
            throw new TitaDAOException("Exception was thrown: \n" + "Cause: "
                    + e.getCause() + "\n" + "Error: " + e.getMessage());
        }
    }

    /**
     * Method for finding an existing Domain object.
     * 
     * @param the
     *            id of the existing object
     * @return the object
     */
    public DomainClass getById(Class<? extends DomainClass> persistentClass,
            Long id) throws TitaDAOException {

        if (id == null) {
            log.debug("Id of Entity is null, Exception will be thrown");
            throw new TitaDAOException("Entity with id: null cannot be found");
        }

        DomainClass entity = null;
        try {
            log.debug("trying to find Entity with Id: " + id);
            entity = entityManager.find(persistentClass, id);
            log.debug("Entity with Id: " + id + " was found.");
        } catch (Exception e) {
            log.debug("Exception catched while trying to get Entity with Id: "
                    + id);
            throw new TitaDAOException("Exception was thrown: \n" + "Cause: "
                    + e.getCause() + "\n" + "Error: " + e.getMessage());
        }

        if (entity == null) {
            throw new TitaDAOException("Entity with id: " + id
                    + " cannot be found");
        }

        return entity;
    }

    /**
     * Method for search for Domain object with criteria.
     * 
     * @param the
     *            criteria for searching Domain objects
     */
    @SuppressWarnings("unchecked")
    public List<DomainClass> search(IBaseCriteria criteria)
            throws TitaDAOException {
        if (criteria == null) {
            log.debug("Criteria is null, Exception will be thrown");
            throw new TitaDAOException(
                    "Entities cannot be search, because criteria is null");
        }
        List<DomainClass> returnValue = null;

        try {
            returnValue = criteria.getCriteria().list();
        } catch (Exception e) {
            log.debug("Exception catched while trying to search for Enties");
            throw new TitaDAOException("Exception was thrown: \n" + "Cause: "
                    + e.getCause() + "\n" + "Error: " + e.getMessage());
        }
        return returnValue;
    }
}

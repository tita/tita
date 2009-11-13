package at.ac.tuwien.ifs.tita.datasource.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

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
            throw new TitaDAOException(
                    "Entity with value null cannot be persisted");
        }

        try {
            if (entity.getId() == null) {
                DomainClass retDomain = entityManager.merge(entity);
                entityManager.persist(retDomain);
                return retDomain;
            } else {
                return entityManager.merge(entity);
            }
        } catch (Exception e) {
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
            throw new TitaDAOException(
                    "Entity cannot be updated if its ID or itself is null");
        }

        try {
            entityManager.merge(entity);
        } catch (Exception e) {
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
            throw new TitaDAOException(
                    "Entity cannot be deleted if its ID or itself is null");
        }

        try {
            entityManager.remove(entityManager.merge(entity));
        } catch (Exception e) {
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
            throw new TitaDAOException("Entity with id: null cannot be found");
        }

        DomainClass entity = null;
        try {
            entity = entityManager.find(persistentClass, id);
        } catch (Exception e) {
            throw new TitaDAOException("Exception was thrown: \n" + "Cause: "
                    + e.getCause() + "\n" + "Error: " + e.getMessage());
        }

        if (entity == null) {
            throw new TitaDAOException("Entity with id: " + id
                    + " cannot be found");
        }

        return entity;
    }
}

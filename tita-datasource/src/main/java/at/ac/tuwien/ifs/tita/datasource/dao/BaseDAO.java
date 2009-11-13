package at.ac.tuwien.ifs.tita.datasource.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import at.ac.tuwien.ifs.tita.datasource.domain.BaseEntity;

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
    public void save(DomainClass entity) {
        if (entity.getId() != null) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
    }

    /**
     * Method for updating a Domain Object.
     * 
     * @param entity
     *            - A sub-type of BaseEntity
     */
    public void update(DomainClass entity) {
        entityManager.merge(entity);
    }

    /**
     * Method for deleting a Domain Object
     * 
     * @param entity
     *            - A sub-type of BaseEntity
     */
    public void delete(DomainClass entity) {
        entityManager.remove(entityManager.merge(entity));
    }

    /**
     * Method for finding an existing Domain object.
     * 
     * @param the
     *            id of the existing object
     * @return the object
     */
    public DomainClass getById(Class<? extends DomainClass> persistentClass,
            Long id) {

        return entityManager.find(persistentClass, id);
    }
}

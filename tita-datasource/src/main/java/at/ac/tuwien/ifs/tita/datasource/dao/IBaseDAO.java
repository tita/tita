package at.ac.tuwien.ifs.tita.datasource.dao;

import at.ac.tuwien.ifs.tita.datasource.domain.BaseEntity;
import at.ac.tuwien.ifs.tita.datasource.exception.TitaDAOException;

public interface IBaseDAO<DomainClass extends BaseEntity> {

    /**
     * Method for persisting a Domain Object.
     * 
     * @param entity
     *            - A sub-type of BaseEntity
     */
    DomainClass save(DomainClass entity) throws TitaDAOException;

    /**
     * Method for updating a Domain Object.
     * 
     * @param entity
     *            - A sub-type of BaseEntity
     */
    void update(DomainClass entity) throws TitaDAOException;

    /**
     * Method for deleting a Domain Object
     * 
     * @param entity
     *            - A sub-type of BaseEntity
     */
    void delete(DomainClass entity) throws TitaDAOException;

    /**
     * Method for finding an existing Domain object.
     * 
     * @param the
     *            id of the existing object
     */
    DomainClass getById(Class<? extends DomainClass> persistentClass, Long id)
            throws TitaDAOException;
}

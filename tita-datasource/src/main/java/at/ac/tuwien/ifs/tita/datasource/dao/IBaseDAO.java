package at.ac.tuwien.ifs.tita.datasource.dao;

import at.ac.tuwien.ifs.tita.datasource.domain.BaseEntity;

public interface IBaseDAO<DomainClass extends BaseEntity> {

    /**
     * Method for persisting a Domain Object.
     * 
     * @param entity
     *            - A sub-type of BaseEntity
     */
    void save(DomainClass entity);

    /**
     * Method for updating a Domain Object.
     * 
     * @param entity
     *            - A sub-type of BaseEntity
     */
    void update(DomainClass entity);

    /**
     * Method for deleting a Domain Object
     * 
     * @param entity
     *            - A sub-type of BaseEntity
     */
    void delete(DomainClass entity);

    /**
     * Method for finding an existing Domain object.
     * 
     * @param the
     *            id of the existing object
     */
    DomainClass getById(Class<? extends DomainClass> persistentClass, Long id);
}

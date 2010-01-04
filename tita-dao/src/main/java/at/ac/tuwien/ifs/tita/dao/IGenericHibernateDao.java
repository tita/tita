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

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.Order;

/**
 * This DAO has multiple access methods for all common dao functions for every
 * entity type.
 * 
 * @param <T> class of entity
 * @param <ID> class of associated key (Long, Integer, ...)
 */
public interface IGenericHibernateDao<T, ID extends Serializable> extends IPersistenceContextProvider {

    /**
     * Find an entity by ID.
     * 
     * @param id id of entity
     * @param joinProps describe what many-associations should be read too.
     * @return entity of type T or null
     */
    T findById(ID id, String... joinProps);

    /**
     * Find an entity by ID and lock this while reading it.
     * 
     * @param id Id of entity
     * @param lock optional you can allocate a pessimistic lock on entity/table.
     * @return entity of type T or null
     */
    T findById(ID id, boolean lock);

    /**
     * Find all existing entities of class T.
     * 
     * @return List of entities found or null
     */
    List<T> findAll();

    /**
     * FindAll method, that allows specification of orderclauses.
     * 
     * @param orders all orderclauses
     * @return list of entities
     */
    List<T> findAllOrdered(Order... orders);

    /**
     * Find all existing entities of class T.
     * 
     * @return Scrollable result (cursor) with all records
     */
    Iterator<T> findAllScrollable();

    /**
     * Find all entities which are like example instance.
     * 
     * @param exampleInstance T
     * @param excludeProps all properties(attributes of exampleInstance) that
     *        will be excluded from search
     * @return (empty) list of entities
     */
    List<T> findByExample(T exampleInstance, String... excludeProps);

    /**
     * Find all entities which are like example instance.
     * 
     * @param exampleInstance T
     * @param excludeProps all properties(attributes of exampleInstance) that
     *        will be excluded from search
     * @return a scrollable result (cursor)
     */
    Iterator<T> findByExampleScrollable(T exampleInstance, String... excludeProps);

    /**
     * Stores an entity of type T the first time or merges an entity if it is
     * allready in database.
     * 
     * @param entity to persist.
     * @return new merged entity in session
     */
    T save(T entity);

    /**
     * Deletes an entity.
     * 
     * @param entity to delete
     */
    void delete(T entity);

    /**
     * Finds an entity by id and deletes it.
     * 
     * @param id of entity to delete
     */
    void delete(ID id);

    /**
     * Deletes an entity from session cache without storing information in Db.
     * 
     * @param entity to delete
     */
    void evict(T entity);

    /**
     * Refresh the specified entity's content from the database.
     * 
     * @param entity - the entity to refresh
     */
    void refresh(T entity);
}

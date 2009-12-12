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

package at.ac.tuwien.ifs.tita.common.dao;
/**
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
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;

import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.entity.IBaseEntity;

/**
 * Concrete implementation of IGenericHibernateDao Interface.
 * 
 * @author herbert
 *
 * @param <T> class of entity
 * @param <ID> class of associated key (Long, Integer, ...)
 */
public class GenericHibernateDao<T,ID extends Serializable> extends PersistenceContextProvider
                                         implements IGenericHibernateDao<T,ID> {
    
    private   Class<T> persistenceClass;    

    public GenericHibernateDao() {
        super();
    }

    public GenericHibernateDao(Class<T> persistenceClass) {
        this.persistenceClass = persistenceClass;
    }
    
    /** {@inheritDoc} */ 
    @SuppressWarnings("unchecked")
    public T findById(ID id, String... joinProps) {
        T myEntity = null;
        Criteria criteria = null;
        
        try {
            criteria = getSession().createCriteria(persistenceClass);
            criteria.add( Restrictions.idEq( id ) );
            for ( String prop : joinProps ) {
                criteria.setFetchMode( prop, FetchMode.JOIN );
            }
            criteria.setResultTransformer( CriteriaSpecification.DISTINCT_ROOT_ENTITY );
            myEntity = ( T ) criteria.uniqueResult(  );
        } catch (Exception e) {
            throw new PersistenceException("Fehler beim lesen eines Entities: Class=" + 
                          persistenceClass.getSimpleName() + " Key=" + id.toString()+"\n"+
                          e.getMessage(), e);
        }
        
        return myEntity;
        
    }

    /** {@inheritDoc} */ 
    @SuppressWarnings("unchecked")
    public T findById(ID id, boolean lock) {
        T entity;
        try {
            if (lock) {
                entity = (T) getSession().load(persistenceClass, id, LockMode.UPGRADE);
            } else {
                entity = (T) getSession().load(persistenceClass, id);
            }
        } catch (Exception e) {
            throw new PersistenceException("Failure during reading entity. Class=" + 
                                            persistenceClass.getSimpleName()+"\n"+
                                            e.getMessage(), e);
        }

        return entity;
    }
    
    /** {@inheritDoc} */ 
    @SuppressWarnings("unchecked")
    public List<T> findAll() {

        List<T> myList = null;

        try {
            myList = getSession().createCriteria(persistenceClass)
                                 .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                                 .list();
        } catch (Exception e) {
            throw new PersistenceException(
                    "Failure during reading entities. Class="
                    + persistenceClass.getSimpleName() + "\n"
                    + e.getMessage(), e);
        }

        return myList;
    }

    /** {@inheritDoc} */ 
    @SuppressWarnings("unchecked")
    public List<T> findAllOrdered(Order... orders) {

        List<T> myList = null;
        
        try {
            Criteria crit = getSession().createCriteria(persistenceClass);
            for (Order order : orders) {
                crit.addOrder(order);
            }
            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            myList = crit.list();
        } catch (Exception e) {
            throw new PersistenceException("Failure during reading entities. Class=" + 
                                            persistenceClass.getSimpleName()+"\n"+
                                            e.getMessage(), e);
        }
        
        return myList;
    }
    
    
    /** {@inheritDoc} */ 
    @SuppressWarnings("unchecked")
    public List<T> findByExample(T exampleInstance, String... excludeProps) {

        List<T> myList = null;
        
        try {
            Criteria crit = getSession().createCriteria(persistenceClass);
            Example example = Example.create(exampleInstance);
            for (String exclude : excludeProps) {
                example.excludeProperty(exclude);
            }
            crit.add(example);
            // Tell Hibernate to remove duplicates from the result set if there is a
            // OneToMany relation in the exampleInstance entity.
            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            myList =  crit.list();
        } catch (Exception e) {
            throw new PersistenceException("Failure during reading entities (by example). Class=" + 
                                            persistenceClass.getSimpleName()+"\n"+
                                            e.getMessage(), e);
        }
        
        return myList;
    }

    /** {@inheritDoc} */ 
    public Iterator<T> findAllScrollable() {

        ScrollableResults myList = null;
        
        try {
            Criteria crit = getSession().createCriteria(persistenceClass);
            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            myList = crit.scroll(ScrollMode.FORWARD_ONLY);
        } catch (Exception e) {
            throw new PersistenceException("Failure during reading entities. Class=" + 
                                            persistenceClass.getSimpleName()+"\n"+
                                            e.getMessage(), e);
        }
        
        return new ScrollableWrapper<T>(myList);
    }
    
    /** {@inheritDoc} */ 
    public Iterator<T> findByExampleScrollable(T exampleInstance, String... excludeProps) {

        ScrollableResults myList = null;
        
        try {
            Criteria crit = getSession().createCriteria(persistenceClass);
            Example example = Example.create(exampleInstance);
            for (String exclude : excludeProps) {
                example.excludeProperty(exclude);
            }
            crit.add(example);
            // Tell Hibernate to remove duplicates from the result set if there is a
            // OneToMany relation in the exampleInstance entity.
            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            myList =  crit.scroll(ScrollMode.FORWARD_ONLY);
        } catch (Exception e) {
            throw new PersistenceException("Failure during reading entities (by example). Class=" + 
                                            persistenceClass.getSimpleName()+"\n"+
                                            e.getMessage(), e);
        }
        
        return new ScrollableWrapper<T>(myList);
    }

    /** {@inheritDoc} */ 
    @SuppressWarnings("unchecked")
    public T save(T entity){
        T persistedEntity = null;
        
        persistedEntity = findById(((IBaseEntity<ID>)entity).getId());
        if(persistedEntity != null){
            return merge(entity);
        }else{
            persist(entity);
            return entity;
        }
    }
    
    /**
     * Stores an entity of type T the first time.
     * @param entity to persist.
     */
    private void persist(T entity) {
        try {
            getSession().persist(entity);
        } catch (Exception e) {
            throw new PersistenceException("Failure during persisting entity. Class=" + 
                                            persistenceClass.getSimpleName()+"\n"+
                                            e.getMessage(), e);
        }
    }

    /** {@inheritDoc} */ 
    public void delete(T entity) {
        try {
            getSession().delete(entity);
        } catch (Exception e) {
            throw new PersistenceException("Failure during deleting entity. Class=" + 
                                            persistenceClass.getSimpleName()+"\n"+
                                            e.getMessage(), e);
        }
    }

    /** {@inheritDoc} */ 
    public void delete(ID id) {
        try {
            getSession().delete(findById(id));
        } catch (Exception e) {
            throw new PersistenceException("Failure during deleting entity: Id="+id.toString()+"\n"+
                                            e.getMessage(), e);
        }
    }
  
    /**
     * Merges an entity into session and returns an instance of this entity, which is 
     * different from passed entity.
     * @param entity to merge
     * @return new merged entity in session
     */
    private T merge(T entity) {
        T mergedEntity = null;
        try {
            mergedEntity =  (T)getSession().merge(entity);
        } catch (Exception e) {
            throw new PersistenceException("Failure during merging entity: " + entity+"\n"+
                    e.getMessage(), e);
        }
        return mergedEntity;
    }

    /** {@inheritDoc} */ 
    public void evict(T entity) {
        try {
            getSession().evict(entity);
        } catch (Exception e) {
            throw new PersistenceException("Failure during flushing session.\n"+
                    e.getMessage(), e);
        }
    }
    
    /**
     * Find several entities via criterion.
     * @param criterion beliebige Kriterien
     * @return (leere) Liste von Entities
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(Criterion... criterion) {
        Criteria crit = getSession().createCriteria(persistenceClass);
        for (Criterion c : criterion) {
            crit.add(c);
        }
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return crit.list();
    }

    /**
     * Search by use of the specified search criterions, order by specified order criterions. 
     * @param criterions - search criterions
     * @param orders - order criterions
     * @param aliases list of aliasnames
     * @return List of result records
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteriaOrdered(Criterion criterions[], Order orders[],
        String aliases[]) {
        Criteria crit = getSession().createCriteria(persistenceClass);
        if (aliases != null) {
            for (String alias : aliases) {
                crit.createAlias(alias, alias);
            }
        }

        for (int i = 0; i < criterions.length; i++) {
            crit.add(criterions[i]);
        }
        for (int j = 0; j < orders.length; j++) {
            crit.addOrder(orders[j]);
        }
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return crit.list();
    }

    /**
     * Find several entities via criterion.
     * @param exceptionKeyString for clear exception handling
     * @param criterion random criterias
     * @return entitiy or null
     * @exception TitaDAOException e
     */
    protected T findRecordByUniqueKeyCriteria(String exceptionKeyString, Criterion... criterion) 
        throws TitaDAOException{
        
        List<T> resultList = findByCriteria(criterion);
        
        if (resultList.size() == 1) {
            return resultList.get(0);
        }
        
        if (resultList.size() == 0) {
            return null;
        }
        
        /*
         * No unique ResultList leads to a TitaDAOException.
         */
        throw new TitaDAOException("Dao found more than one Record for UniqueKey (" +
                exceptionKeyString + ").");
    }

    
    /**
     * Find entities for given criterions.
     * @param criterion random criterias
     * @return (empty) list of entities
     */
    protected Iterator<T> findScrollListByCriteria(Criterion... criterion) {
        Criteria crit = getSession().createCriteria(persistenceClass);
        for (Criterion c : criterion) {
            crit.add(c);
        }
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return new ScrollableWrapper<T>(crit.scroll(ScrollMode.FORWARD_ONLY));
    }
    
    /**
     * HQL query returning a List of objects of type persistenceClass.
     * Note that aggregate functions like sum,count in the query will return
     * a Java Long, i.e. in the statement "sum(Entity.items) as items", the alias
     * items must map to a Java Long in the result entity "public Long getItems()".
     * @param query a HQL query String
     * @return a List of type persistenceClass
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByHqlQuery(String query) {
        Query q = getSession().createQuery(query);
        List<T> result = null;
        try {
            q.setResultTransformer(new AliasToBeanResultTransformer(persistenceClass));
            result = (List<T>)q.list();
        } catch (HibernateException e) {
            throw new PersistenceException("Failure during HQL-query.\n"+
                    e.getMessage(), e);
        }
        return result;
    }
    
    /** {@inheritDoc} */
    public void refresh(T entity) {
        Session s = getSession();
        s.refresh(entity);
    }
}

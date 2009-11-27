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

package at.ac.tuwien.ifs.tita.datasource.criteria;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import at.ac.tuwien.ifs.tita.datasource.domain.BaseEntity;
import at.ac.tuwien.ifs.tita.datasource.exception.TitaDAOException;

/**
 * 
 * BaseCriteria
 * 
 * @author ASE Group 10 - TiTA
 * 
 * @param <DomainClass> Specifies a Wild-card, that allows all sub-types of the
 *        BaseEntity
 * 
 *        BaseCriteria is the base class to setting criteria for searching for
 *        entities with superclass BaseEntity. If additional functionality is
 *        needed simply extend this class and implement the needed Methods.
 */
@Repository
public class BaseCriteria<DomainClass extends BaseEntity> implements IBaseCriteria<DomainClass> {

    // private EntityManager entityManager;
    protected Criteria criteria;
    protected Example example;

    final Logger log = LoggerFactory.getLogger(BaseCriteria.class);

    // @PersistenceContext
    // public void setEntityManager(EntityManager entityManager) {
    // this.entityManager = entityManager;
    // }

    public BaseCriteria(EntityManager entityManager, DomainClass exampleEntity) throws TitaDAOException {
        if (exampleEntity == null) {
            log.debug("Example is null, Exception will be thrown");
            throw new TitaDAOException("Entity with value null cannot be persisted");
        }
        System.out.println("EntityManager = " + entityManager);
        Session session = (Session) entityManager.getDelegate();
        System.out.println("Session = " + session);
        criteria = session.createCriteria(exampleEntity.getClass());
        criteria.add(this.example = Example.create(exampleEntity).ignoreCase().enableLike());
    }

    @Override
    public void setOrderAscBy(String attribute) {
        criteria.addOrder(Property.forName(attribute).asc());
    }

    @Override
    public void setOrderDescBy(String attribute) {
        criteria.addOrder(Property.forName(attribute).asc());
    }

    @Override
    public Criteria getCriteria() {
        return criteria;
    }

}

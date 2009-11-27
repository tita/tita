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

import at.ac.tuwien.ifs.tita.datasource.criteria.IBaseCriteria;
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

    /**
     * Method for search for Domain object with criteria.
     * 
     * @param the
     *            criteria for searching Domain objects
     */
    List<DomainClass> search(IBaseCriteria<DomainClass> criteria)
            throws TitaDAOException;

    public IBaseCriteria<DomainClass> createCriteria(DomainClass domain)
            throws TitaDAOException;
}

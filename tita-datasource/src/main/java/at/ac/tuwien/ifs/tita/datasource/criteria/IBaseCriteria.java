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

import org.hibernate.Criteria;

import at.ac.tuwien.ifs.tita.datasource.domain.BaseEntity;

/**
 * IBaseCriteria
 * 
 * @author ASE Group 10 - TiTA
 * 
 */
public interface IBaseCriteria<DomainClass extends BaseEntity> {

    /**
     * Returns criteria object which search for data entries with list method.
     * 
     * @return criteria
     */
    Criteria getCriteria();

    /**
     * Set order by attribute asc
     * 
     * @param attribute
     */
    void setOrderAscBy(String attribute);

    /**
     * Set order by attribue desc
     * 
     * @param attribute
     */
    void setOrderDescBy(String attribute);
}

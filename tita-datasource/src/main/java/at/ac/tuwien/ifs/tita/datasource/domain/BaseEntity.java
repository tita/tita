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

package at.ac.tuwien.ifs.tita.datasource.domain;

import java.io.Serializable;

/**
 * BaseEntity is the Superclass of all Entities used in the TiTA Project.
 * 
 * NOTE: Setter- and GetterMethod for ID are abstract so that subclasses can add
 * specific Annotations to the ID.
 * 
 * @author ASE Group 10 - TiTA
 * 
 */
@SuppressWarnings("serial")
@javax.persistence.MappedSuperclass
public abstract class BaseEntity implements Serializable {

    /**
     * @return the id
     */
    public abstract Long getId();

    /**
     * @param id
     *            the id to set
     */
    public abstract void setId(Long id);
}

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
package at.ac.tuwien.ifs.tita.entity.conv;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import at.ac.tuwien.ifs.tita.entity.interfaces.IBaseEntity;

/**
 * Base-Entity for Conv-Tables.
 * @author Karin
 *
 */
@MappedSuperclass
public class ConvBaseEntity implements IBaseEntity<Long>{
    @Id
    @Column(name="ID")
    private Long id;
    
    @Column(name="DESCRIPTION")
    private String description;

    public ConvBaseEntity() {
        super();
    }

    public ConvBaseEntity(Long id, String description) {
        super();
        this.id = id;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }
    
    public String getDescription() {
        return description;
    }
}

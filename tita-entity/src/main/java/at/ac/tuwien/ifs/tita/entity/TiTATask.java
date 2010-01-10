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
package at.ac.tuwien.ifs.tita.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * Entity for storing tasks that a time producer has generated in TiTA.
 *
 * @author herbert
 *
 */
@Entity
@Table(name = "TITA_TASK")
@SequenceGenerator(name = "seq_tita_task", sequenceName = "TITA_TASK_ID_SEQ", allocationSize = 1)
public class TiTATask extends BaseEntity<Long> implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tita_task")
    private Long id;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "USER_ID")
    private TiTAUser user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "TITA_PROJECT_ID")
    private TiTAProject titaProject;

    @OneToMany(mappedBy ="titaTask",
               cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Set<Effort> titaEfforts;

    @SuppressWarnings("unused")
    @Column(name = "MODIFICATION_VERSION")
    @Version
    private Long modificationVersion;

    public TiTATask(TiTAUser user, Set<Effort> efforts) {
        this.user = user;
        this.titaEfforts = efforts;
    }
    
     public TiTATask(String description, TiTAUser user, TiTAProject titaProject,
            Set<Effort> titaEfforts) {
        super();
        this.description = description;
        this.user = user;
        this.titaProject = titaProject;
        this.titaEfforts = titaEfforts;
    }



    public TiTATask() {
    }

    @Override
    public Long getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public TiTAProject getProject() {
        return this.titaProject;
    }

    public Set<Effort> getTitaEfforts() {
        return this.titaEfforts;
    }

    public TiTAUser getUser(){
        return this.user;
    }

    public void setTitaProject(TiTAProject titaProject) {
        this.titaProject = titaProject;
    }
}

package at.ac.tuwien.ifs.tita.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import at.ac.tuwien.ifs.tita.entity.interfaces.IBaseEntity;

/**
 * Entity for storing tasks that a time producer has generated in TiTA.
 * @author herbert
 *
 */
@Entity
@Table(name="TITA_TASK")
@SequenceGenerator(name = "seq_tita_task", sequenceName = "TITA_TASK_ID_SEQ", allocationSize = 1)
public class TiTATask implements IBaseEntity<Long> {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq_tita_task")
    private Long id;
    
    @Column(name="DESCRIPTION")
    private String description;
    
    @Column(name="PROJECT_ID")
    private Long projectId;
    
    @OneToMany
    @JoinColumn(name="TITA_TASK_ID")
    private Set<Effort> titaEfforts;
    
    @SuppressWarnings("unused")
    @Column(name="MODIFICATION_VERSION")
    @Version
    private Long modificationVersion;
    
    public TiTATask() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
    
    public Long getProjectId(){
        return projectId;
    }

    public Set<Effort> getTitaEfforts() {
        return titaEfforts;
    }
}

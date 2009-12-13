package at.ac.tuwien.ifs.inv.conv;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import at.ac.tuwien.ifs.tita.entity.IBaseEntity;

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

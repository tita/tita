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

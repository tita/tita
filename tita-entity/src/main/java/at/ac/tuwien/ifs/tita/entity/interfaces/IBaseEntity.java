package at.ac.tuwien.ifs.tita.entity.interfaces;

import java.io.Serializable;


/**
 * Interface for defining the base methods of an entity.
 * @author herbert
 *
 * @param <ID> (Long, Integer,....)
 */
public interface IBaseEntity<ID extends Serializable> {
    
    /**
     * Returns the Id of an specific entity.
     * @return Long
     */
    ID getId();
}

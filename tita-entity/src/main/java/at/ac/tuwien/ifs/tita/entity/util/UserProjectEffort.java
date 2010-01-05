package at.ac.tuwien.ifs.tita.entity.util;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Container class extending ProjectEffort class.
 * @author herbert
 *
 */
@Table(name="DUMMY2")
@Entity
public class UserProjectEffort extends ProjectEffort{
   
    @Column(name = "USERNAME", insertable=false, updatable=false)
    private String username;
    
    public UserProjectEffort() {
    }

    public String getUsername() {
        return username;
    }
}

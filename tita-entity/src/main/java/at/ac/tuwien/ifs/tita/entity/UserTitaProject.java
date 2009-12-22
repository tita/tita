package at.ac.tuwien.ifs.tita.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Container class for representing which user belongs to which tita project.
 * @author herbert
 *
 */
@Entity
@Table(name = "USER_TITA_PROJECT")
@SequenceGenerator(name = "seq_user_tita", sequenceName = "USER_TITA_ID_SEQ", allocationSize = 1)
public class UserTitaProject extends BaseEntity<Long> {
    
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user_tita")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "USER_ID") //, referencedColumnName = "ID")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "TITA_PROJECT_ID") //, referencedColumnName = "ID")
    private TiTAProject titaProject;
    
    public UserTitaProject(User user, TiTAProject titaProject) {
        super();
        this.user = user;
        this.titaProject = titaProject;
    }

    public User getUser() {
        return user;
    }

    public TiTAProject getTitaProject() {
        return titaProject;
    }

    @Override
    public Long getId() {
        return id;
    }

}

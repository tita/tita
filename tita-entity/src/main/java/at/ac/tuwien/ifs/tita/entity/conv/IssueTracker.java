package at.ac.tuwien.ifs.tita.entity.conv;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity for storing IssueTrackers like Mantis.
 * @author karin
 *
 */
@Entity
@Table(name="ISSUETRACKER")
public class IssueTracker extends ConvBaseEntity{
   
}

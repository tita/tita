package at.ac.tuwien.ifs.inv.conv;

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

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
package at.ac.tuwien.ifs.tita.ui.models;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.conv.Role;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonDelete;
import at.ac.tuwien.ifs.tita.ui.uihelper.ButtonEdit;
import at.ac.tuwien.ifs.tita.ui.utils.IntegerConstants;

/**
 * Provides all necessary table model methods for entity User.
 * 
 * @author ASE Group 10
 */
public class TableModelUser extends AbstractTitaTableModel {

	// Logger
	private final Logger log = LoggerFactory.getLogger(TableModelUser.class);

	public TableModelUser(List<TiTAUser> list) {
		super(list);

		columnNames = new String[] { "Username", "First Name", "Last Name",
				"Email", "Deleted", "Role", "", "" };
	}

	/** {@inheritDoc} **/
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == IntegerConstants.FOUR) {
			return Boolean.class;
		} else if (columnIndex == IntegerConstants.FIVE) {
			return Role.class;
		} else if (columnIndex == IntegerConstants.SIX) {
			return ButtonEdit.class;
		} else if (columnIndex == IntegerConstants.SEVEN) {
			return ButtonDelete.class;
		} else {
			return super.getColumnClass(columnIndex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValueAt(int row, int col) {
		TiTAUser user = null;

		try {
			user = (TiTAUser) list.get(row);

			if (col == IntegerConstants.ZERO) {
				return user.getUserName();
			} else if (col == IntegerConstants.ONE) {
				return user.getFirstName();
			} else if (col == IntegerConstants.TWO) {
				return user.getLastName();
			} else if (col == IntegerConstants.THREE) {
				return user.getEmail();
			} else if (col == IntegerConstants.FOUR) {
				return user.isDeleted();
			} else if (col == IntegerConstants.FIVE) {
				if (user.getRole() != null) {
					return user.getRole().getDescription();
				} else {
					return null;
				}
			} else if (col == IntegerConstants.SIX) {
				return null;
			} else if (col == IntegerConstants.SEVEN) {
				return null;
			} else {
				return user;
			}

		} catch (IndexOutOfBoundsException e) {
			log.error(e.getMessage());
		} catch (ClassCastException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/** {@inheritDoc} **/
	@Override
	public void setValueAt(Object aValue, int row, int col) {
		TiTAUser user = null;

		try {
			user = (TiTAUser) list.get(row);

			if (col == IntegerConstants.ZERO) {
				user.setUserName(aValue.toString());
			} else if (col == IntegerConstants.ONE) {
				user.setFirstName(aValue.toString());
			} else if (col == IntegerConstants.TWO) {
				user.setLastName(aValue.toString());
			} else if (col == IntegerConstants.THREE) {
				// email, deleted, role
				user.setEmail(aValue.toString());
			} else if (col == IntegerConstants.FOUR) {
				if (aValue.toString().trim().equalsIgnoreCase("true")) {
					user.setDeleted(true);
				} else {
					user.setDeleted(false);
				}
			} else if (col == IntegerConstants.FIVE) {
				user.setRole((Role) aValue);
			}
		} catch (IndexOutOfBoundsException e) {
			log.error(e.getMessage());
		} catch (ClassCastException e) {
			log.error(e.getMessage());
		}

	}

	/** {@inheritDoc} **/
	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == IntegerConstants.SIX || column == IntegerConstants.SEVEN) {
			return true;
		}

		return false;
	}

	/**
	 * Method for adding a User to the TableModel.
	 * 
	 * @param user
	 *            the user to be displayed
	 */
	@SuppressWarnings("unchecked")
	public void addEntity(TiTAUser user) {
		if (user != null) {
			((List<TiTAUser>) list).add(user);
		}
	}

}

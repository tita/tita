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
package at.ac.tuwien.ifs.tita.ui.uihelper;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Interface ITitaAdministrationPanel provides access to basic administration
 * methods to administrate an entity and the table.
 * 
 * @author msiedler
 * 
 */
public interface IAdministrationPanel {

    /**
     * Deletes a table list entry.
     */
    void deleteListEntity();

    /**
     * Loads list entities.
     */
    void loadListEntities();

    /**
     * Saves list entity to database and table.
     */
    void saveListEntity();

    /**
     * Updates list entity in database and table.
     */
    void updateListEntity();

    /**
     * Reloads table with ajax request.
     * 
     * @param target AjaxRequestTarget
     */
    void reloadTable(AjaxRequestTarget target);
}

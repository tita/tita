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
package at.ac.tuwien.ifs.tita.ui.controls.panel;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.table.Table;

import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.entity.TiTAProject;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.ui.models.TableModelTiTAUser;

/**
 * 
 * Wicket Panel for project information.
 * 
 * @author rene
 * 
 */
public class ProjectInformationPanel extends Panel {

    @SpringBean(name = "userService")
    private IUserService userService;

    public ProjectInformationPanel(String id, TiTAProject project) {
        super(id);

        List<TiTAUser> listOfUser = userService.findAllTiTAUsersForProject(project);
        TableModelTiTAUser tmForTiTAUser = new TableModelTiTAUser(listOfUser);

        Table tableForTiTAUser = new Table("piTableForTitaUser", tmForTiTAUser) {
            @Override
            protected void onSelection(AjaxRequestTarget target) {
                // do nothing
            }
        };
        add(tableForTiTAUser);
    }
}

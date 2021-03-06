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
package at.ac.tuwien.ifs.tita.ui.administration.project;

import at.ac.tuwien.ifs.tita.ui.BasePage;

/**
 * ProjectAdministration Page which allows to manage Projects.
 * 
 * @author ASE Group 10
 */
public class ProjectAdministrationPage extends BasePage {

    /**
     * public constructor.
     */
    public ProjectAdministrationPage() {
        add(new ProjectAdministrationPanel("projectAdministrationPanel"));
    }
}

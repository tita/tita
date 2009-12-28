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
package at.ac.tuwien.ifs.tita.presentation.permissions;

import org.apache.wicket.security.hive.authentication.Subject;
import org.apache.wicket.security.hive.authorization.Principal;

/**
 * Principals for Tita.
 * @author Karin
 *
 */
public class TitaPrincipal implements Principal{

    private String name;


    public TitaPrincipal(String name){
        super();
        if(name != null){

            this.name = name;
        } else{
            throw new IllegalArgumentException("Name must be specified");
        }
    }

    @Override
    public String getName() {
        return name;
    }

    /**{@inheritDoc}*/
    @Override
    public boolean implies(Subject user) {
        // no inheritance structure in these principals.
        return false;
    }

}

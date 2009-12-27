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
package at.ac.tuwien.ifs.tita.presentation;

import org.apache.wicket.security.authentication.LoginException;
import org.apache.wicket.security.hive.authentication.DefaultSubject;
import org.apache.wicket.security.hive.authentication.LoginContext;
import org.apache.wicket.security.hive.authentication.Subject;
import org.apache.wicket.util.lang.Objects;

/**
 * Login Context for Tita - to authenticate Users and grant principals.
 * @author Karin
 *
 */
public class TitaLoginContext extends LoginContext {
    private final String username;
    private final String password;
    
    /**
     * 
     * Constructor for loging off.
     */
    public TitaLoginContext(){
        username = null;
        password = null;
    }
    
    public TitaLoginContext(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    /**{@inheritDoc}*/
    public Subject login() throws LoginException {
        //TODO: check username und pwd from db
        if (username != null && Objects.equal(username, password)) {
            DefaultSubject user = new DefaultSubject();
            //TODO: get role from db and check
            if(username.equals("admin")){
                System.out.println("admin logged in!!!!!!!!!!!!!!");
                user.addPrincipal(new TitaPrincipal("admin"));
            } else if(username.equals("timecontroller")){
                System.out.println("timecontroller logged in!!!!!!!!!!!!!!");
                user.addPrincipal(new TitaPrincipal("timecontroller"));
            }else if(username.equals("timeconsumer")){
                System.out.println("TimeConsumer logged in!!!!!!!!!!!!!!");
                user.addPrincipal(new TitaPrincipal("timeconsumer"));
            }
            
            return user;
        }
        throw new LoginException("Login of user " + username + " failed.");
    }
}

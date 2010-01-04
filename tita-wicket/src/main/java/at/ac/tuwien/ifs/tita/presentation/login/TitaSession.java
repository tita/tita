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
package at.ac.tuwien.ifs.tita.presentation.login;

import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.security.WaspApplication;
import org.apache.wicket.security.WaspSession;

/**
 * Session for storing user data.
 * 
 * @author rene
 * 
 */
public class TitaSession extends WaspSession {
    private String username;
    private String role;

    public TitaSession(WaspApplication application, Request request) {
        super(application, request);
    }

    /**
     * Checks if user is signed in.
     * 
     * @return True if user is signed in
     */
    public boolean isSignedIn() {
        return username != null;
    }

    /**
     * Returns current user name.
     * 
     * @return the user name
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets current user name.
     * 
     * @param username the user name to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the role of the current user.
     * 
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the current User.
     * 
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Return the session.
     * 
     * @return the session
     */
    public static final TitaSession getSession() {
        return (TitaSession) Session.get();
    }
}

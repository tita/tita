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
package at.ac.tuwien.ifs.tita.ui.login;

import java.security.NoSuchAlgorithmException;

import javax.persistence.PersistenceException;

import org.apache.wicket.security.authentication.LoginException;
import org.apache.wicket.security.hive.authentication.DefaultSubject;
import org.apache.wicket.security.hive.authentication.Subject;
import org.apache.wicket.security.hive.authentication.UsernamePasswordContext;
import org.apache.wicket.security.hive.authorization.SimplePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.business.security.TiTASecurity;
import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;

/**
 * Login Context for Tita - to authenticate Users and grant principals.
 * 
 * @author Karin
 * @author rene
 * 
 */
public class TitaLoginContext extends UsernamePasswordContext {
    private final Logger log = LoggerFactory.getLogger(TitaLoginContext.class);
    private IUserService service;

    /**
     * Constructor for logging off.
     */
    public TitaLoginContext() {
    }

    public TitaLoginContext(String username, String password, IUserService service) {
        super(username, password);
        this.service = service;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Subject getSubject(String username, String password) throws LoginException {

        if (username != null) {
            DefaultSubject user = new DefaultSubject();

            try {
                TiTAUser u = service.getUserByUsername(username);

                if (TiTASecurity.calcHash(password).equals(u.getPassword())) {
                    if (u.getRole().getDescription().equals("Administrator")) {
                        user.addPrincipal(new SimplePrincipal("admin"));
                        TitaSession.getSession().setRole("admin");
                    } else if (u.getRole().getDescription().equals("Time controller")) {
                        user.addPrincipal(new SimplePrincipal("timecontroller"));
                        TitaSession.getSession().setRole("timecontroller");
                    } else if (u.getRole().getDescription().equals("Time consumer")) {
                        user.addPrincipal(new SimplePrincipal("timeconsumer"));
                        TitaSession.getSession().setRole("timeconsumer");
                    } else {
                        throw new LoginException("Login of user " + username + " failed.");
                    }
                } else {
                    throw new LoginException("Login of user " + username + " failed.");
                }
            } catch (PersistenceException e) {
                throw new LoginException("Login of user " + username + " failed.");

            } catch (NoSuchAlgorithmException e) {
                log.error("Hash Algorithm not found!");
            }

            return user;
        }
        throw new LoginException("Login of user " + username + " failed.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preventsAdditionalLogins() {
        return true;
    }
}

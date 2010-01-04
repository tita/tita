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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.wicket.security.authentication.LoginException;
import org.apache.wicket.security.hive.authentication.DefaultSubject;
import org.apache.wicket.security.hive.authentication.Subject;
import org.apache.wicket.security.hive.authentication.UsernamePasswordContext;
import org.apache.wicket.security.hive.authorization.SimplePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
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
                String hashedPass = "";
                try {
                    hashedPass = getHashedPassword(password);
                } catch (NoSuchAlgorithmException e) {
                    log.error("Hash Algorithm not found!");
                }

                if (hashedPass.equals(u.getPassword())) {
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
            } catch (TitaDAOException e) {
                throw new LoginException("Login of user " + username + " failed.");
            }

            return user;
        }
        throw new LoginException("Login of user " + username + " failed.");
    }

    /**
     * Hashes the password with SHA-1 Algorithm.
     * 
     * @param pwd password to hash.
     * @return hashed password
     * @throws NoSuchAlgorithmException if algorithm wasn't found.
     */
    private String getHashedPassword(String pwd) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] encryptMsg = md.digest(pwd.getBytes());
        return bytes2String(encryptMsg);
    }

    /**
     * Converts byte array in readable String.
     * 
     * @param bytes digest to convert.
     * @return digest as readable String.
     */
    private String bytes2String(byte[] bytes) {
        StringBuilder string = new StringBuilder();
        for (byte b : bytes) {
            // CHECKSTYLE:OFF
            String hexString = Integer.toHexString(0x00FF & b);
            // CHKECKSTYLE:ON
            string.append(hexString.length() == 1 ? "0" + hexString : hexString);
        }
        return string.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preventsAdditionalLogins() {
        return true;
    }
}

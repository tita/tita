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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.apache.wicket.security.authentication.LoginException;
import org.apache.wicket.security.hive.authentication.DefaultSubject;
import org.apache.wicket.security.hive.authentication.Subject;
import org.apache.wicket.security.hive.authentication.UsernamePasswordContext;
import org.apache.wicket.security.hive.authorization.SimplePrincipal;

import at.ac.tuwien.ifs.tita.business.service.user.IUserService;
import at.ac.tuwien.ifs.tita.dao.exception.TitaDAOException;
import at.ac.tuwien.ifs.tita.entity.TiTAUser;
import at.ac.tuwien.ifs.tita.entity.conv.Role;

/**
 * Login Context for Tita - to authenticate Users and grant principals.
 * 
 * @author Karin
 * @author rene
 * 
 */
public class TitaLoginContext extends UsernamePasswordContext {
    // TODO: doesn't work - always null
    /*
     * @SpringBean(name = "userService") private IUserService userService;
     */

    private IUserService service;

    /**
     * 
     * Constructor for loging off.
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

        // List<User> users = null;
        // try {
        // users = service.getUndeletedUsers();
        //
        // for (User us : users) {
        // us.setDeleted(true);
        // service.saveUser(us);
        // }
        // } catch (TitaDAOException e2) { // TODO Auto-generated catch block
        // e2.printStackTrace();
        // }
        // TODO: DELETE LATER
        insertTempRoles();
        insertTempUsers();
        // TODO: DELETE LATER

        if (username != null) {
            DefaultSubject user = new DefaultSubject();

            try {
                TiTAUser u = service.getUserByUsername(username);
                String hashedPass = "";
                try {
                    hashedPass = getHashedPassword(password);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                if (hashedPass.equals(u.getPassword())) {
                    if (u.getRole().getDescription().equals("Administrator")) {
                        user.addPrincipal(new SimplePrincipal("admin"));
                    } else if (u.getRole().getDescription().equals("Time controller")) {
                        user.addPrincipal(new SimplePrincipal("timecontroller"));
                    } else if (u.getRole().getDescription().equals("Time consumer")) {
                        user.addPrincipal(new SimplePrincipal("timeconsumer"));
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
     * Insert temporary roles for testing. TODO: REMOVE LATER.
     */
    private void insertTempRoles() {
        // CHECKSTYLE:OFF
        List<Role> tempRolesList = new ArrayList<Role>(3);

        tempRolesList.add(new Role(1L, "Administrator"));
        tempRolesList.add(new Role(2L, "Time consumer"));
        tempRolesList.add(new Role(3L, "Time controller"));
        // CHECKSTYLE:ON

        try {
            for (Role r : tempRolesList) {
                service.saveRole(r);
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert temporary users for testing. TODO: REMOVE LATER.
     */
    private void insertTempUsers() {
        // CHECKSTYLE:OFF
        List<TiTAUser> tempUserList = new ArrayList<TiTAUser>(3);
        // CHECKSTYLE:ON
        TiTAUser user1 = new TiTAUser();
        TiTAUser user2 = new TiTAUser();
        TiTAUser user3 = new TiTAUser();

        user1.setDeleted(false);
        user2.setDeleted(false);
        user3.setDeleted(false);

        user1.setUserName("admin");
        user2.setUserName("timeconsumer");
        user3.setUserName("timecontroller");

        try {
            user1.setPassword(getHashedPassword("admin"));
            user2.setPassword(getHashedPassword("timeconsumer"));
            user3.setPassword(getHashedPassword("timecontroller"));
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
        }

        try {
            List<Role> rlist = service.getRoles();
            user1.setRole(rlist.get(0));
            user2.setRole(rlist.get(1));
            user3.setRole(rlist.get(2));
        } catch (TitaDAOException e1) {
            e1.printStackTrace();
        }

        tempUserList.add(user1);
        tempUserList.add(user2);
        tempUserList.add(user3);

        try {
            for (TiTAUser u : tempUserList) {
                service.saveUser(u);
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hashes the password with SHA-1 Algorithm.
     * 
     * @param pwd password to hash.
     * @return hashed password
     * @throws NoSuchAlgorithmException if algorithm wasn't found.
     */
    private String getHashedPassword(String pwd) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] encryptMsg = md.digest(pwd.getBytes());
        return new String(encryptMsg);
    }

    /**
     * No Additional Logins allowed.
     * 
     * @see org.apache.wicket.security.hive.authentication.LoginContext#preventsAdditionalLogins()
     * @return true if additonal logins allowed false otherwise
     */
    /*
     * @Override public boolean preventsAdditionalLogins() { return true; }
     */
}

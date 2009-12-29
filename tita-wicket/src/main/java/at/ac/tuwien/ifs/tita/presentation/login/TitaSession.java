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
     * Return the session.
     * 
     * @return the session
     */
    public static final TitaSession getSession() {
        return (TitaSession) Session.get();
    }
}

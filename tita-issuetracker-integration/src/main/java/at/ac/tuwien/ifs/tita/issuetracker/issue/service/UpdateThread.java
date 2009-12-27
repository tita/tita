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
package at.ac.tuwien.ifs.tita.issuetracker.issue.service;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.entity.IssueTrackerLogin;
import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IProjectTrackable;

/**
 * The UpdateThread starts after a log in from a user and manage the automatic
 * update. After a successful update a timeout between the next update is set.
 * 
 * @see DispatcherThread
 * @author Christoph
 * 
 */
public class UpdateThread extends Thread {

    private final Logger log = LoggerFactory.getLogger(UpdateThread.class);

    private int timeout;
    private boolean applicationIsRunning = true;
    private final Map<Long, IProjectTrackable> projects;
    private final IssueTrackerLogin login;

    /**
     * Constructor for the automatic update thread.
     * 
     * @param projects - map of projects that are accessbile from the issue
     *        trackers.
     */
    public UpdateThread(Map<Long, IProjectTrackable> projects, IssueTrackerLogin login) {
        super("automatic update");
        this.projects = projects;
        this.login = login;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

        try {
            Properties prop = new Properties();
            try {
                prop.load(ClassLoader.getSystemClassLoader().getResourceAsStream("system.properties"));
                timeout = Integer.valueOf(prop.getProperty("timeout"));
                log.debug("Timeout: " + timeout + "minutes.");

            } catch (IOException e) {
                log.error("Get timeout from properties failed.");
                e.getStackTrace();
            }

            while (applicationIsRunning) {

                log.debug("Starting automatic update.");
                new DispatcherThread(projects, login).start();
                log.debug("Automatic update was succesful.");
                log.debug("Waiting until timeout is over.");
                // CHECKSTYLE:OFF
                Thread.sleep(timeout * 60 * 1000);
                // CHECKSTYLE:ON
            }

        } catch (InterruptedException e) {
            log.error("Automatic update failed.");
            throw new RuntimeException("Automatic update failed.");
        }
    }

    /**
     * Shutdown the automatic update thread.
     */
    @SuppressWarnings("deprecation")
    public void shutdown() {
        applicationIsRunning = false;
        this.stop();
    }
}

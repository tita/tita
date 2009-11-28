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

    Logger log = LoggerFactory.getLogger(UpdateThread.class);

    private int timeout;
    private boolean applicationIsRunning = true;
    private Map<Long, IProjectTrackable> projects;

    public UpdateThread(Map<Long, IProjectTrackable> projects) {
        super("automatic update");
        this.projects = projects;
    }

    @Override
    public void run() {

        try {
            Properties prop = new Properties();
            try {
                prop.load(ClassLoader.getSystemClassLoader()
                        .getResourceAsStream("system.properties"));
                this.timeout = Integer.valueOf(prop.getProperty("timeout"));
                this.log.debug("Timeout: " + this.timeout + "minutes.");

            } catch (IOException e) {
                this.log.error("Get timeout from properties failed.");
                e.getStackTrace();
            }

            while (this.applicationIsRunning) {

                this.log.debug("Starting automatic update.");
                new DispatcherThread(this.projects).start();
                this.log.debug("Automatic update was succesful.");
                this.log.debug("Waiting until timeout is over.");
                Thread.sleep(this.timeout * 60 * 1000);
            }

        } catch (InterruptedException e) {
            this.log.error("Automatic update failed.");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public void shutdown() {
        this.applicationIsRunning = false;
        this.stop();
    }
}

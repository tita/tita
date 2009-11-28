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

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.issuetracker.interfaces.IProjectTrackable;

/**
 * The DispatcherThread fetches all projects from a single user and starts for
 * each project a worker thread, that manage the update of the data.
 * 
 * @author Christoph
 * 
 */
public class DispatcherThread extends Thread {
    private ExecutorService pool;
    private boolean listening = true;
    private Map<Long, IProjectTrackable> projects;

    Logger log = LoggerFactory.getLogger(DispatcherThread.class);

    public DispatcherThread(Map<Long, IProjectTrackable> projects) {
        super("Dispatcher");
        this.projects = projects;
    }

    @Override
    public void run() {

        try {
            this.log.info("Connection opened for update.");
            this.pool = Executors.newCachedThreadPool();
            for (IProjectTrackable project : this.projects.values()) {

                while (this.listening) {
                    this.pool.execute(new WorkerThread(project));
                }
            }

        } catch (Exception ex) {
            this.log.error("Connection error while data update.");
            this.pool.shutdown();
        }
    }

    /**
     * Close the thread pool for the worker threads and the running loop.
     */
    public void close() {
        this.listening = false;

        if (this.pool != null) {
            this.pool.shutdown();
        }
    }
}

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
package at.ac.tuwien.ifs.tita.dao.interfaces;

/**
 * Interface to allow Spring Injection of PersistenceContext.
 */
public interface IPersistenceContextProvider {

    /**
     * Return 'true' if data has been changed and should be saved into the database.
     * @return true/false
     */
    boolean isDataChanged();

    /**
     * Clears the session completely. 
     * Evict all loaded instances and cancel all pending saves, updates and deletions.
     */
    void clear();

    /**
     * Flushes the session, executes all necessary SQL-Statements immediately.
     */
    void flush();
    
    
    /**
     * Flushes the session and clears the session completely.
     * Evict all loaded instances and cancel all pending saves, updates and deletions.
     * 
     */
    void flushnClear();
}